/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceInvaders;

/**
 *
 * @author Daniel
 */
public class RegenAlien extends AlienEntity{
    public final int healthGain = 1;
    public final int healthTime = 5;
    private int count = 0;
    
    public RegenAlien(Game game,String ref,int x,int y, int row, int col){
        super(game,ref,x,y,row,col);
        maxHealth = 100;
        isRegen=true;
        init();
    }
}