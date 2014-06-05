package spaceInvaders;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Random;

/**
 * An entity representing a shot fired by the player's ship
 * 
* @author Kevin Glass
 */
public class ShotEntity extends Entity {
    Toolkit t = Toolkit.getDefaultToolkit();
	Dimension d = t.getScreenSize();
	private int sX = (int)d.getWidth();
	private int sY = (int)d.getHeight();
    /** The vertical speed at which the players shot moves */
    private final double moveSpeed = ((int)((double)(-0.694)*sX));
    /** The game in which this entity exists */
    private final Game game;
    /** True if this shot has been "used", i.e. its hit something */
    private boolean used = false;
    
    private int shotType;
    
    private double shotDmg;
    
    /**
     * Create a new shot from the player
     * 
     * @param game The game in which the shot has been created
     * @param sprite The sprite representing this shot
     * @param x The initial x location of the shot
     * @param y The initial y location of the shot
     * @param shotType 0: Player's shot | 1: Alien's shot
     */
    public ShotEntity(Game game,String sprite,int x,int y,int dmg,int shotType) {
        super(sprite,x,y);
        
        this.game = game;
        
        this.shotType = shotType;
        
        dy = moveSpeed;
        
        if(shotType==0){
        	shotDmg=((double) (dmg)/80)+1;
        } else if(shotType==1){
        	shotDmg=dmg;
        }
        
    }
    
    /**
     * Request that this shot moved based on time elapsed
     * 
     * @param delta The time that has elapsed since last move
     */
    @Override
    public void move(long delta) {
        // proceed with normal move
    	if(shotType==0){
    		super.move(delta);
    	} else if(shotType==1){
    		super.move(-(delta/5));
    	}
        
        // if we shot off the screen, remove ourselves
        if (y <= 0) {
            game.removeEntity(this);
        }
    }
    
    /**
     * Notification that this shot has collided with another
     * entity
     * 
     * @parma other The other entity with which we've collided
     */
    @Override
    public void collidedWith(Entity other) {
        // prevents double kills, if we've already hit something,
        // don't collide
        if (used) {
            return;
        }
        
        // if we've hit an alien, kill it!
        if (other instanceof AlienEntity && shotType==0) {
            // remove the affected entities
            try{
            ((AlienEntity) other).alienKilled(shotDmg);
            } catch(Exception e) {
                game.removeEntity(other);
            }
            game.removeEntity(this);
            
            // notify the game that the alien has been killed
            used = true;
        } else if (other instanceof ShipEntity && shotType==1){
        	((ShipEntity)game.getShip()).tookDamage(shotDmg);
            game.removeEntity(this);
        }
    }
}