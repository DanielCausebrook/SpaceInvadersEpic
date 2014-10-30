/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceInvaders;
import java.awt.Color;
import java.awt.Graphics;
/**
 *
 * @author Daniel
 */
public class PowerBar {
    private final int maxPower = 1000;
    private int power = 0;
    private double pixPerPwr;
    private final Game game;
    private int nextXP=0;
    
    public PowerBar(Game g) {
        game=g;
    }
    
    public int[] getBarPos(){
        int[] returnVal = new int[2];
        returnVal[1] = 587;
        pixPerPwr =(double) 780/maxPower;
        returnVal[0]=(int) (pixPerPwr * power)+10;
        return returnVal;
    }
    
    public int getPower() {
        return power;
    }
    
    public int getMaxPower() {
        return maxPower;
    }
    
    public double getPixPerPower(){
        return pixPerPwr;
    }
    
    public void addPower(int amount) {
    	power+=amount;
    	if(power>maxPower){
    		power=maxPower;
                nextXP++;
                if(nextXP>40){
                    game.addSpark(getBarPos()[0],getBarPos()[1], 1,1,false,100, Color.WHITE,false);
                    nextXP=0;
                }
    	}
    }
    
    public void draw(Graphics g){
        g.setColor(Color.DARK_GRAY);
        g.drawRect(10, 585, 780, 4);
        g.setColor(Color.BLACK);
        g.fillRect(11, 586, 779, 3);
        g.setColor(Color.orange);
        g.fillRect(11, 586, (int) (pixPerPwr * power)  , 3);
    }
}
