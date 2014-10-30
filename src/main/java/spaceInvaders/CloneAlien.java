/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceInvaders;

/**
 *
 * @author Daniel
 */
public class CloneAlien extends AlienEntity{
	
	
    public CloneAlien(Game game,String ref,int x,int y, int row, int col){
        super(game,ref,x,y,row,col);
        armour=150;
        maxHealth = 150;
        isShield=true;
        init();
    }
}
