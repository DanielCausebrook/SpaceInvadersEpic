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
                
                count++;
                if(count >= healthTime && armour<maxHealth-healthGain){
                    armour += healthGain;
                    count=0;
                }
    }
}