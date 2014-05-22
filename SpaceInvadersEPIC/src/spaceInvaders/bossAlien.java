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
        armour=1000;
        maxHealth = 1000;
    }
}
