package spaceInvaders;
/**
 * The entity that represents the players ship
 * 
* @author Kevin Glass
 */
public class ShipEntity extends Entity {
    /** The game in which the ship exists */
    private final Game game;
    
    private double armour = 100;
    
    private int maxHealth = 100;
    
    /**
     * Create a new entity to represent the players ship
     *  
     * @param game The game in which the ship is being created
     * @param ref The reference to the sprite to show for the ship
     * @param x The initial x location of the player's ship
     * @param y The initial y location of the player's ship
     */
    public ShipEntity(Game game,String ref,int x,int y) {
        super(ref,x,y);
        
        this.game = game;
    }
    
    /**
     * Request that the ship move itself based on an elapsed ammount of
     * time
     * 
     * @param delta The time that has elapsed since last move (ms)
     */
    @Override
    public void move(long delta) {
        // if we're moving left and have reached the left hand side
        // of the screen, don't move
        if ((dx < 0) && (x < 10)) {
            return;
        }
        // if we're moving right and have reached the right hand side
        // of the screen, don't move
        if ((dx > 0) && (x > 750)) {
           return;

        }
        
        super.move(delta);
    }
    
    public void resetShip() {
    	armour = maxHealth;
    }
    
    public void tookDamage(double damageTaken) {
    	armour-=damageTaken;
    	if(armour<=0){
            game.notifyDeath();
    	}
    }
    
    /**
     * Notification that the player's ship has collided with something
     * 
     * @param other The entity with which the ship has collided
     */
    @Override
    public void collidedWith(Entity other) {

        // if its an alien, notify the game that the player
        // is dead
        if (other instanceof AlienEntity) {
            game.notifyDeath();
        }
    }
}