package spaceInvaders;

import java.util.Random;

/**
 * An entity representing a bomb fired by the player's ship
 * 
* @author Kevin Glass
 */
public class Bomb extends Entity {
    /** The vertical speed at which the players bomb moves */
    private final double moveSpeed = -50;
    /** The game in which this entity exists */
    private final Game game;
    /** True if this bomb has been "used", i.e. its hit something */
    private boolean used = false;
    
    private int bombType;
    
    private double bombDmg;
    
    /**
     * Create a new bomb from the player
     * 
     * @param game The game in which the bomb has been created
     * @param sprite The sprite representing this bomb
     * @param x The initial x location of the bomb
     * @param y The initial y location of the bomb
     * @param bombType 0: Player's bomb | 1: Alien's bomb
     */
    public Bomb(Game game,String sprite,int x,int y,int dmg,int bombType) {
        super(sprite,x,y);
        
        this.game = game;
        
        this.bombType = bombType;
        
        dy = moveSpeed;
        
        if(bombType==0){
        	bombDmg=((double) (dmg)/80)+1;
        } else if(bombType==1){
        	bombDmg=dmg;
        }
        
    }
    
    /**
     * Request that this bomb moved based on time elapsed
     * 
     * @param delta The time that has elapsed since last move
     */
    @Override
    public void move(long delta) {
        // proceed with normal move
    	if(bombType==0){
    		super.move(delta);
    	} else if(bombType==1){
    		super.move(-(delta/5));
    	}
        
        // if we bomb off the screen, remove ourselves
        if (y < -100) {
            game.removeEntity(this);
        }
        if (y > 700) {
            game.removeEntity(this);
        }
    }
    
    /**
     * Notification that this bomb has collided with another
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
        if (other instanceof AlienEntity && bombType==0) {
            // remove the affected entities
            try{
            ((AlienEntity) other).alienKilled(bombDmg);
            } catch(Exception e) {
                game.removeEntity(other);
            }
            game.removeEntity(this);
            
            // notify the game that the alien has been killed
            used = true;
        } else if (other instanceof ShipEntity && bombType==1){
        	((ShipEntity)game.getShip()).tookDamage(bombDmg);
            game.removeEntity(this);
        }
    }
}