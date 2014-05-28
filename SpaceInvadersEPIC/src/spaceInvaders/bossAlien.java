package spaceInvaders;

import java.awt.Color;
import java.util.Random;

/**
 * An entity which represents one of our space invader aliens.
 * 
* @author Kevin Glass
 */
public class bossAlien extends AlienEntity {
    
    public bossAlien(Game game,String ref,int x,int y, int row, int col) {
        super(game,ref,x,y,row,col);
        armour=300;
        maxHealth = 300;
        XPBonus = 25;
        nextShot = (int) Math.floor(Math.random()*1000);
        isFiring=true;
        init();
    }
}
