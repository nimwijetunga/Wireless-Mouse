import java.awt.Graphics;

/**
 * 
 * Collectible entity type
 *
 */
public class Collectible extends Entity{
	
	private Player player;
	
    /**
     *@param Player takes in the player object
     *@param filePath is the sprite for collectible
     */
	public Collectible(int x, int y, int lives, int health, int size, Player player, String filePath) {
		super(x, y, lives, health, size,EntityType.COLLECT, filePath);
		this.x = x;
		this.y = y;
		this.player = player;
	}

	@Override
	public void move(int x, int y) {
		this.x +=x;
		this.y += y;
	}
	
	@Override
	public void collision(){
		if(this.collides(player)){
			this.player.setScore(this.player.getScore() + 1);
			this.isDead = true;
		}
	}
	
	@Override
	public void draw(Graphics g) {
		sprite.paintComponent(g, this.x, this.y);
	}

}
