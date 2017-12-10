import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * 
 * Enttiy superclass which allows all entties to be classified under one category
 *
 */
public abstract class Entity extends Rectangle{
	
	protected int lives,health, size;
	protected boolean isDead;
	protected EntityType t;
	protected Sprite sprite;
	
	public int getHealth() {
		return health;
	}
	

	public EntityType getT() {
		return t;
	}

	/**
	 * @param x, x position of entity
	 * @param y, y position of entity
	 * @param lives, lives of the entity
	 * @param health, health of the entity
	 * @param size, size of the entity (assuming square)
	 * @param t, type of entity
	 * @param filePath, image corresponding to entity
	 */
	public Entity(int x, int y, int lives, int health, int size, EntityType t, String filePath){
		isDead = false;
		sprite = new Sprite(filePath,size,size);
		this.setBounds(x, y, size, size);
		this.lives = lives;
		this.size = size;
		this.health = health;
		this.t =t;
	}
	
	//Abstract methods for subclasses to override
	public abstract void move(int x, int y);
	public abstract void draw(Graphics g);
	public abstract void collision();

	/**
	 * @param a is teh entity being check for a collision with
	 * @return return true if a collision has occured
	 */
	public boolean collides(Entity a){
		if(this.intersects(a))
			return true;
		return false;
	}
}
