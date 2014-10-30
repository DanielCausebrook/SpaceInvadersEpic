/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceInvaders;

/**
 *
 * @author Daniel
 */
public class StrongAlien extends AlienEntity{
    
    public StrongAlien(Game game,String ref,int x,int y, int row, int col) {
            super(game,ref,x,y,row,col);
            armour=300;
            maxHealth = 300;
            init();
    }
    
}