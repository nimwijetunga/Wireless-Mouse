import java.awt.Color;
import java.awt.Graphics;

/**
 * 
 * The player which the user controls
 *
 */
public class Player extends Entity{
	
	private int score;

	public void setScore(int score) {
		this.score = score;
	}

	public int getScore() {
		return score;
	}

	//Constructor for the player
	public Player(int x, int y, int lives, int health, int size) {
		super(x, y, lives, health, size, EntityType.PLAYER, "res/basket.png");
		this.score = 0;
		this.x = x;
		this.y = y;
	}
	
	@Override
	public void move(int x, int y){
		//Moves the player within the boundary contraints of the screen
		if(x < MouseMotion.WIDTH - 60 && x > 0)
			this.x = x;
		if(y < MouseMotion.HEIGHT - 100 && y > 0)
			this.y = y;
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.BLACK);
		sprite.paintComponent(g, this.x, this.y);
		//g.fillRect(this.x, this.y, size, size);
	}

	@Override
	public void collision() {
		//Does nothing but must be implemented
	}

}