// File Name: DueReciever
// SE Group: 18
// Description: Sketch to upload to Arduino Due which recieves data transmitted from Arduino Nano which it analyzes and
// executes specific functions based on the data. Functions include moving the mouse up/down/left/right at variable speeds
// and left/right clicking mouse keys

#include <RH_ASK.h> // Utilizing Radiohead's ASK transmission library
#include <SPI.h> // Not actualy used but needed to compile
#include<Mouse.h> // Mouse library to use Mouse functionality


//RH_ASK driver for a RF reciever on port 4 with speed of 2000 bits/s
RH_ASK driver(2000, 4);

bool lClicked = false; // Boolean for if left clicked
bool rClicked = false; // Boolean for if right clicked

// Raw data values for x y and z recieved from
int x = 0;
int y = 0;
int z = 0;

// Values of acceleration of x y z axis in m/s^2
double xVal = 0;
double yVal = 0;
double zVal = 0;

// Value of xy vector
double xyVal;

// Low and high thresholds of acceleration activation
double lBound = 1.7;
double hBound = 13.5;

// Speed Scaler
double lowSpeed = 0.0;
double hiSpeed = 9.0;

//Scaled x and y values to use on mouse
double init_x = 0;
double init_y = 0;

void setup()
{
  driver.init(); // Initializes radio reciever driver
}

void loop()
{

  String dataStr = getData(); // Creates string to hold data from transmission and calls getData() to read transmission
  if (!dataStr.equals("NULL")) { // Only reads the data string if it is not NULL

    if (dataStr.equals("1") && !lClicked)  { // If data string is just 1 and if it is not already pressed

      lClicked = true; // Sets lClicking to true

      Mouse.press(MOUSE_LEFT); // Presses down Left Mouse key

    } else if (dataStr.equals("2") && !rClicked) { // If data string is just 2 and if it is not already pressed

      rClicked = true; // Sets rClicking to true

      Mouse.press(MOUSE_RIGHT); // Presses down Right Mouse key

    } else { // Else, when mouse is not pressed and the mouse is being pressed, (When transmitting x y z values of accelerometer)

      if (lClicked)  { // If the mouse was left clicking, release key and set lClicked to false

        lClicked = false;
        Mouse.release(MOUSE_LEFT);

      }

      if (rClicked)  { // If the mouse was right clicking, release key and set rClicked to false

        rClicked = false;
        Mouse.release(MOUSE_RIGHT);

      }

      // Parse x, y, and z values from the data string
      x = dataStr.substring(0, 3).toInt(); // First three digits of string to x
      y = dataStr.substring(3, 6).toInt(); // Second three digits of string to y
      z = dataStr.substring(6, 9).toInt(); // Third three digits of string to z

    }
  }

  /****************Value Conversions**********************/

  //Conversion of raw voltage values to g
  xVal = -(x - 350.0) * 0.014286 * 9.8;
  yVal = -(y - 350.0) * 0.014286 * 9.8;
  zVal = (z - 360.0) * 0.014286 * 9.8;

  // Scales xVal and yVal to init_x and init_y respectively if they are between the bounds, else sets init_x and init_y to 0
  if (xVal > -hBound && xVal < -lBound)  {

    init_x = -scale(-hBound, -lBound, xVal, 1.0, 10); // init_x is inverted due to orientation of accelerometer

  } else if (xVal > lBound && xVal < hBound) {

    init_x = scale(lBound, hBound, xVal, 1.0, 10); // init_x is inverted due to orientation of accelerometer

  } else init_x = 0;

  if (yVal > -hBound && yVal < -lBound)  {

    init_y = scale(-hBound, -lBound, yVal, 1.0, 10); // init_y is inverted due to orientation of accelerometer

  } else if (yVal > lBound && yVal < hBound) {

    init_y = -scale(lBound, hBound, yVal, 1.0, 10); // init_y is inverted due to orientation of accelerometer

  } else init_y = 0;

  // Adds init_x and int_y together as vectors compenents to make xyVal
  xyVal = sqrt(pow(init_x, 2) + pow(init_y, 2));

  // Converts init_x, init_y into a unit vector then scales it to desired value (If the xyVal is not almost 0)
  if (fabs(xyVal) >= 0.000001) {

    // Converts init_x, init_y to unit vector compenents
    init_x /= xyVal;
    init_y /= xyVal;

    // Scales xyVal to from original bound to desired speed bound
    xyVal = scale(0.0, 8.0, xyVal, lowSpeed, hiSpeed);

    // Multiplies init_x and init_y by this value
    init_x *= xyVal;
    init_y *= xyVal;
  }

  // Moves mouse (init_x and init_y are swapped due to orientation of accelerometer)
  Mouse.move(init_y, init_x, 0);

  delay(10); // Reponse delay of 10ms

}

double scale(double iLow, double iHigh, double num, double fLow, double fHigh) { // Scales a number between an initial range to final range

  double iMid = (iLow + iHigh) / 2; // Initial midpoint

  double deviation = (num - iMid) / fabs(iMid - iLow); // Finds how far the number deviates from the midpoint as a ratio of this to the maximum deviation

  double fMid = (fLow + fHigh) / 2; // Final Midpoint

  if (iMid >= 0)  {

    return  (fMid + deviation * (fabs(fMid - fLow))); // Returns the new number as the final midpoint times the deviation

  } else return (fMid - deviation * (fabs(fMid - fLow))); // Returns the new number as the final midpoint times the deviation

}

String getData() {

  uint8_t buf[RH_ASK_MAX_MESSAGE_LEN]; // Creates array which holds message from reciever
  uint8_t buflen = sizeof(buf); // Size of array

  if (driver.recv(buf, &buflen)) // Recieves message from reciever and executes if statement if message is recieved
  {

    String str = ""; // Creates string to hold message

    for (int i = 0; i < buflen; i++) { // For loop that iterates for the length of buf, incrementing by 1 each time
      str += (char) buf[i]; // Adds char value corresponding to value of buf[i] to str
    }

    return str; // Returns str

  }

  return "NULL"; // Returns null if no message is recieved

}

