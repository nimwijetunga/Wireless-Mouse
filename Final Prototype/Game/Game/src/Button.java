import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Class for all buttons in the game
 *
 */
public class Button extends Rectangle{
	
	int width, height, x, y;
	Sprite background;
	String text;
	
	public String getText() {
		return text;
	}
	
	/**
	 *@param filePath is filePath of the image
	 *@param text is the name of the button
	 *@params width, height,x,y are the dimesions of the button
	 */
	public Button(String filePath, String text, int width, int height, int x, int y){
		background = new Sprite(filePath, width,  height);
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
		this.text = text;
		this.setBounds(x , y, width, height);
	}
	
	//Check if the button has been clicked
	public boolean clicked(){
		//If mouse pressed and hovering over button, return true
		if(this.contains(MouseMotion.x + 50, MouseMotion.y + 50) && MouseMotion.clicked) {
			MouseMotion.clicked = false;
			return true;
		}
		return false;
	}
	
	//Draw button
	public void paintComponent(Graphics g){
		g.setColor(Color.GREEN);
		background.paintComponent(g, this.x, this.y);
	}

}
