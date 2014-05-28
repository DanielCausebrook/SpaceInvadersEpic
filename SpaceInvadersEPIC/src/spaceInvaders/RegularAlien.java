/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceInvaders;
/**
 *
 * @author Daniel
 */
public class RegularAlien extends AlienEntity{
    
    public RegularAlien(Game game,String ref,int x,int y, int row, int col) {
        super(game,ref,x,y,row,col);
        XPBonus=5;
        maxHealth = 100;
        init();
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */