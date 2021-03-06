import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 *Creates the buffered images for all sprites used in the game
 *
 */
public class Sprite {
	
	BufferedImage image;
	
	/**
	 * @param filePath of the image
	 * @param width of image
	 * @param height of image
	 */
	public Sprite(String filePath, int width, int height){
		try {
			image = ImageIO.read(new File(filePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		//resize the image to desired dimesions
		image = resize(image, width,height);
	}
	
	//Draw the sprite
	public void paintComponent(Graphics g, int x, int y){
		g.drawImage(image, x, y, null);
	}
	
	/**
	 * @param img to be resized
	 * @param newW, new width
	 * @param newH, new height
	 * @return
	 */
	public BufferedImage resize(BufferedImage img, int newW, int newH) { 
	    Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
	    BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

	    Graphics2D g2d = dimg.createGraphics();
	    g2d.drawImage(tmp, 0, 0, null);
	    g2d.dispose();

	    return dimg;
	}

}
