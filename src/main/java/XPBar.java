package spaceInvaders;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class XPBar {
    private int maxXP = 150;
    private int XP = 0;
    private double pixPerXP;
    private final Game game;
    private int shipLevel=1;
    
    public XPBar(Game g) {
        game=g;
    }
    
    public int[] getBarPos(){
        int[] returnVal = new int[2];
        returnVal[1] = 10;
        pixPerXP =(double) 780/maxXP;
        returnVal[0]=(int) (pixPerXP * XP)+10;
        return returnVal;
    }
    
    public int getXP() {
        return XP;
    }
    
    public int getMaxXP() {
        return XP;
    }
    
    public double getPixPerXP(){
        return XP;
    }
    
    public void addXP(int amount) {
    	XP+=amount;
    	if(XP>=maxXP){
    		XP=0;
                game.levelUp();
                for(int i =0;i<500;i++){
                    Random r = new Random();
                    game.addSpark((int) ((r.nextInt()%(maxXP*pixPerXP))+10), (int) ((r.nextInt()%4)+10), 30, (int)(r.nextInt()%game.getWidth()), (int) ((r.nextInt()%500)+30), 50, Color.BLUE,Spark.glowEnabled);
                }
                maxXP+=15;
                shipLevel++;
    	}
    }
    
    public void draw(Graphics g){
        g.setColor(Color.DARK_GRAY);
        g.drawRect(10, 10, 780, 4);
        g.setColor(Color.WHITE);
        g.fillRect(10, 10, (int) (pixPerXP * XP)  , 4);
        g.drawString("Ship Level: "+shipLevel, 720, 30);
    }
}
