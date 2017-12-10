// File Name: NanoTransmitter
// SE Group: 18
// Description: Sketch to upload to Arduino Nano which transmits data to be recieved on Arduino Due. Voltage values
// from accelerometer are concatenated into one string to be transmitted to Arduino Due. Also sends 1 if a left click
// button is pressed or 2 if right click button is pressed

#include <RH_ASK.h> // Utilizing Radiohead's ASK transmission library
#include <SPI.h> // Not actualy used but needed to compile

// Stores voltage values recieved from accelerometer for respective axes

int xVal = 0;
int yVal = 0;
int zVal = 0;

// Digital ports for clicking
int leftClick = 8;
int rightClick = 7;

RH_ASK driver(2000, ' ', 4); // RH_ASK driver for RF transmitter on port 4 with speed of 2000bits/s (the ' ' is a placeholder for where the reciever port is)

void setup()
{
  // Sets pinMode of left and right click digital ports to input
  pinMode(leftClick, INPUT);
  pinMode(leftClick, INPUT);

  driver.init(); // Initializes driver
}

void loop()
{

  // Gets voltage value for axes from repesctive analog ports

  xVal = (int)(analogRead(A1));
  yVal = (int)(analogRead(A2));
  zVal = (int)(analogRead(A3));

  if (digitalRead(leftClick))  { // If the left click button is pressed

    driver.send((uint8_t *)"1", 1); // Sends string "1" of length 1
    driver.waitPacketSent(); // Waits until message is sent before continuing

  } else if (digitalRead(rightClick)) {

    driver.send((uint8_t *)"2", 1); // Sends string "2" of length 1
    driver.waitPacketSent(); // Waits until message is sent before continuing

  }

  char final_data[10]; // Creates char array which holds the accelerometer data

  char *data1 = getData(xVal); // Creates char pointer which holds xVal as 3 digit string
  strcpy(final_data, data1); // Copies data1 to first 3 letters of final_data
  
  free(data1); // Frees memory that data1 points to
  data1 = NULL; // Guards against potential memory conflicts

  data1 = getData(yVal); // Sets data1 to yVal as a 3 digit string
  strcpy(final_data + 3, data1); // Copies data1 to second 3 letters of final_data
  
  free(data1); // Frees memory that data1 points to
  data1 = NULL; // Guards against potential memory conflicts

  data1 = getData(zVal); // Sets data1 to zVal as a 3 digit string
  strcpy(final_data + 6, data1); // Copies data1 to third 3 letters of final_data
  
  free(data1); // Frees memory that data1 points to
  data1 = NULL; // Guards against potential memory conflicts

  final_data[10] = '\0'; // Adds NULL character to end of final_data

  driver.send((uint8_t *)final_data, strlen(final_data)); // Sends final_data which is now a concatenation of the three accelerometer values which has a string length of 9
  driver.waitPacketSent(); // Waits until message is sent before continuing

  delay(1); // Delay of 1ms
}

char *getData(int n) { // Converts n to a string and returns it

  char* tmp_str = (char *)malloc(4 * sizeof(int)); // Allocates a temporary string on the heap

  sprintf(tmp_str, "%d", n); // Converts an integer n to a string and assigns it to the the memory that tmp_str points to

  tmp_str = pad(tmp_str); // Pads the temporary string with extra leading zeros if needed
  tmp_str[3] = '\0'; // Adds null

  return tmp_str; // Returns tmp_str

}

char *pad(char *s) { // Function which adds leading zeros to a inputted string (s) if needed (desired number of digits is 3)

  char * n = (char *)malloc(4 * sizeof(char)); // Temporary string which will be padded if needed

  if (strlen(s) == 3) { // If s is already a 3 digit string, just return s

    s[3] = '\0'; // Makes sure last character is a null character

    free(n); // Frees n
    n = NULL; // Guards against potential memory conflicts

    return s;

  }
  else if (strlen(s) == 2) { // If s is a 2 digit string

    strcpy(n + 1, s); // Copies s to n+1 position in memory
    
    n[0] = '0'; // Sets first character of n to 0
    n[3] = '\0'; // Sets last character of n to null character

  }
  else if (strlen(s) == 1) { // If s is a 1 digit string

    strcpy(n + 2, s); // Copies s to n+2 position in memory
    
    n[0] = '0'; // Sets first character of n to 0
    n[1] = '0'; // Sets second character of n to 0
    n[3] = '\0'; // Sets last character of n to null character

  }

  free(s); // Frees s
  s = NULL; // Guards against potential memory conflicts

  return n;
}

