import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * 
 * Canvas of the game 
 * Responsible for drawing and updating
 */
public class Draw extends JPanel{
	private static int x,y;
	private Player player;
	private Level level;
	private boolean dispose;
	private Sprite background;
	private ArrayList<Button> buttons;

	//Constructor for the draw class
	public Draw(){
		init();
	}
	
	//Intitalize all variables (needed for when the game is reset)
	public void init(){
		buttons = new ArrayList<Button>();
		dispose = false;
		background =  new Sprite("res/background.jpg",MouseMotion.WIDTH, MouseMotion.HEIGHT);
		player = new Player(10,10,3,10,100);
		level = new Level(2, player,3);
		addButtons();
	}
	
	//Add buttons (currently has only onen button)
	public void addButtons(){
		buttons.add(new Button("res/retry.png","Retry", 125,50,15,50));
	}
	
	public void update(){
		player.move(x, y);
		
		//Check for Collision with Entities
		for(Entity i : level.getEntities())
			i.collision();
		//Removes Entites
		for(int i = 0; i < level.getEntities().size(); i++){
			if(level.getEntities().get(i).isDead)
				level.getEntities().remove(i);
		}
		
		//Move Entities
		for(int i = 0; i < level.getEntities().size(); i++){
			if(level.getEntities().get(i).y > MouseMotion.HEIGHT)
				level.getEntities().remove(i);
			else
				level.getEntities().get(i).move(0, 5);
		}
		
		//Respawn Entites
		if(level.respawn(EntityType.ENEMY))
			level.spawn(EntityType.ENEMY);
		else if(level.respawn(EntityType.COLLECT))
			level.spawn(EntityType.COLLECT);
		
		for(Button b : buttons){
		    if(b.clicked()){
		    	if(b.getText().equals("Retry")) MouseMotion.restart = true;
		    }
		}
		
		//Close the screen (exit game)
		if(player.health <= 0) dispose = true;
			
	}
	
	public boolean isDispose() {
		return dispose;
	}
	
	//Updates and draws objects in the canvas
	public void drawing(int xx, int yy){
		x = xx;
		y = yy;
		update();
		repaint();
	}
	
	//Draws different components displayed to the screen
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		background.paintComponent(g, 0, 0);
		//Draw Entites and Buttons
		for(Entity i : level.getEntities()){
			i.draw(g);
		}
		for(Button b : buttons)
			b.paintComponent(g);
		player.draw(g);
		//Display text to the screen
		g.setFont(new Font("Impact", Font.BOLD,30));
		String score = Integer.toString(player.getScore());
		String lives = Integer.toString(player.getHealth());
		g.setColor(Color.YELLOW);
		g.drawString("Lives: " + lives, 20, 30);
		g.drawString("Score: " + score, MouseMotion.WIDTH - 170, 30);
	}

}