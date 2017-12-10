import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class MouseMotion implements Runnable{
private static int x,y;
private static Draw canvas;
public static final int WIDTH = 1000, HEIGHT = 800;
public static JFrame frame;
public static Thread main;

public static void main(String[] args){
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
	canvas.addMouseMotionListener(new AL());
	main = new Thread(new MouseMotion());
	main.start();
	frame.setCursor (c);
	
}

static class AL extends MouseAdapter{
	public void mouseMoved(MouseEvent e){
		x = e.getX();
		y = e.getY();
	}
}

@Override
public void run() {
	while(true){
		canvas.drawing(x, y);
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {e.printStackTrace();}
		if(canvas.isDispose()){
			frame.setVisible(false);
			frame.dispose();
		}
	}
}
}
