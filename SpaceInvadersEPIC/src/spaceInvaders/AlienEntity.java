package spaceInvaders;

import java.awt.Color;
import java.util.Random;

/**
 * An entity which represents one of our space invader aliens.
 * 
* @author Kevin Glass
 */
public abstract class AlienEntity extends Entity {
        public int maxHealth = 100;
    /** The speed at which the alien moves horizontally */
    protected final double moveSpeed = 75;
    /** The game in which the entity exists */
    protected final Game game;
    
    protected boolean alreadyKilled = false;
    
    protected double armour=100;

    protected int rowNum;

    protected int colNum;
    
    protected int XPBonus = 10;
    /**
     * Create a new alien entity
     * 
     * @param game The game in which this entity is being created
     * @param ref The sprite which should be displayed for this alien
     * @param x The initial x location of this alien
     * @param y The initial y location of this alien
     */
    public AlienEntity(Game game,String ref,int x,int y, int row, int col) {
        super(ref,x,y);
                rowNum = row;
                colNum = col;
        this.game = game;
        dx = -moveSpeed;
    }

    /**
     * Request that this alien moved based on time elapsed
     * 
     * @param delta The time that has elapsed since last move
     */
    @Override
    public void move(long delta) {
        // if we have reached the left hand side of the screen and
        // are moving left then request a logic update 
        if ((dx < 0) && (x < 10)) {
            game.updateLogic();
        }
        // and vice vesa, if we have reached the right hand side of 
        // the screen and are moving right, request a logic update
        if ((dx > 0) && ((x+sprite.getWidth()) > 790)) {
            game.updateLogic();
        }
        
        // proceed with normal move
        super.move(delta);
    }
    
    
    /**
     * Update the game logic related to aliens
     */
    @Override
    public void doLogic() {
        // swap over horizontal movement and move down the
        // screen a bit
        dx = -dx;
        y += 10;
        
        // if we've reached the bottom of the screen then the player
        // dies
        if (y > 570) {
            game.notifyDeath();
        }
    }
    
    /**
     * Notification that this alien has collided with another entity
     * 
     * @param other The other entity
     */
        
        public void alienKilled(double dmg) {
        if(!(armour<=dmg)){
            armour-=dmg;
        } else if(!alreadyKilled){
            alreadyKilled=true;
                        Entity[][] grid = game.getAlienGrid();
                        grid[rowNum][colNum]=null;
                        game.setAlienGrid(grid);
            game.removeEntity(this);
            game.notifyAlienKilled((int)y,(int)x);
            Random r = new Random();
            Color c;

            int numSparks;
            if(Spark.glowEnabled){
            	numSparks=Spark.glowSparks;
            } else {
            	numSparks=Spark.normalSparks;
            }
            for(int i = 0; i<numSparks;i++){
            	switch(r.nextInt(3)){
            	case 0:c=Color.RED;
            	break;
            	case 1:c=Color.ORANGE;
            	break;
            	case 2:c=Color.RED;
            	break;
            	default:c=Color.RED;
            }
            	double angle = r.nextInt(360);
            	int distance = r.nextInt(200);
            	int xPos = (int) (x+(Math.cos(angle)*distance));
            	int yPos = (int) (y+(Math.sin(angle)*distance));
            	game.addSpark((int) (x+(sprite.getWidth()/2)),(int) (y+(sprite.getHeight()/2)), 10,xPos,yPos,20, c,Spark.glowEnabled);
            }
            for(int i = 0; i<XPBonus;i++){
            	double angle = r.nextInt(360);
            	int distance = r.nextInt(50);
            	int xPos = (int) (x+(Math.cos(angle)*distance));
            	int yPos = (int) (y+(Math.sin(angle)*distance));
            	game.addSpark(xPos,yPos, 60,1,false,100, Color.WHITE,true);
            }
        }
        }
        
        public int getXPBonus() {
            return XPBonus;
        }
        
    @Override
    public void collidedWith(Entity other) {
        // collisions with aliens are handled elsewhere
    }
}