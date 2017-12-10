import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class MouseMotion implements Runnable{
public static int x,y;
private static Draw canvas;
public static final int WIDTH = 1000, HEIGHT = 800;
public static JFrame frame;
public static Thread main;
public static AL al = new AL();
public static boolean clicked = false, restart = false;

//main method of the game, sets the size of the image, and adds mouse listener, and sets the frame
// to visible
public static void main(String[] args){
	
	//Set up the JFrame
	
	frame = new JFrame("Mouse");
	canvas = new Draw();
	frame.setVisible(true);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setSize(WIDTH, HEIGHT);
	Toolkit toolkit = Toolkit.getDefaultToolkit();
	BufferedImage image = new BufferedImage(1, 1, BufferedImage.TRANSLUCENT);
	Cursor c = toolkit.createCustomCursor(image , new Point(frame.getX(), 
	           frame.getY()), "img");
	frame.add(canvas);
	canvas.addMouseListener(al);
	canvas.addMouseMotionListener(al);
	main = new Thread(new MouseMotion());
	main.start();
	frame.setCursor (c);
	
}

/**
 * 
 * Implement the mouse responsiveness
 *
 */
static class AL extends MouseAdapter{
	public void mouseMoved(MouseEvent e){
		x = e.getX();
		y = e.getY();
	}
	public void mousePressed(MouseEvent e){
		clicked = true;
	}
}

@Override
public void run() {
	while(true){
		//Draw objects to the canvas
		canvas.drawing(x, y);//Calls drawing method in Draw Class
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {e.printStackTrace();}
		//If the canvas is visible, draw it!
		if(canvas.isDispose()){
			frame.setVisible(false);
			frame.dispose();
		}
		//If user wants to restart then re-initialize the canvas 
		if(restart){
			canvas.init();
			restart = false;
		}
	}
}
}
