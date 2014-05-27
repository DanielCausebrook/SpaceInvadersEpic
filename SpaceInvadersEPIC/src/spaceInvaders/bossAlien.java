package spaceInvaders;

import java.awt.Color;
import java.util.Random;

/**
 * An entity which represents one of our space invader aliens.
 * 
* @author Kevin Glass
 */
public class bossAlien extends AlienEntity {
	private static final int maxTime = 5000;
    private int timeToClone = maxTime;

    protected int nextShot;
    
    public bossAlien(Game game,String ref,int x,int y, int row, int col) {
        super(game,ref,x,y,row,col);
        armour=10000;
        maxHealth = 10000;
        XPBonus = 500;
        nextShot = (int) Math.floor(Math.random()*1000);
    }
    
    @Override
    public void move(long delta) {
        // if we have reached the left hand side of the screen and
        // are moving left then request a logic update 
        if ((dx < 0) && (x < 10)) {
            game.updateLogic();
        }
        // and vice vesa, if we have reached the right hand side of 
        // the screen and are moving right, request a logic update
        if ((dx > 0) && (x > 750)) {
            game.updateLogic();
        }
        
        // proceed with normal move
        super.move(delta);
        
        if(nextShot<=0){
        	ShotEntity shot = new ShotEntity(game,"sprites/AlienShot.png",(int)x,(int)y,10,1);
            game.entities.add(shot);
        	nextShot = 80;
        } else {
        	nextShot--;
        }
    }
}
