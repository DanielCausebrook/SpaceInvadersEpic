/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceInvaders;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

/**
 *
 * @author Daniel
 */
public class Spark{
	public static final boolean glowEnabled = true;
	public static final int glowSparks = 70;
	public static final int normalSparks = 300;
    double x;
    double y;
    double stepX;
    double stepY;
    int stepsLeft;
    int finalPosX;
    int finalPosY;
    int startX;
    int startY;
    PowerBar transferPowerTo;
    XPBar transferXPTo;
    int amountToTransfer;
    boolean isPowerBar = true;
    boolean glow =false;
    int pause;
    Game game;
    Color color;
    
    public Spark(Game g, PowerBar finalPwrBar, XPBar finalXPBar,boolean isPwrBar, int startX, int startY, int maxPause, int transfer,int speed, Color c, boolean glow){
        if(isPwrBar){
            transferPowerTo = finalPwrBar;
            isPowerBar=true;
            finalPosX = transferPowerTo.getBarPos()[0]+10;
            finalPosY = transferPowerTo.getBarPos()[1];
        } else {
            transferXPTo = finalXPBar;
            isPowerBar = false;
            finalPosX = transferXPTo.getBarPos()[0]+10;
            finalPosY = transferXPTo.getBarPos()[1];
        }
        amountToTransfer = transfer;
        x = startX;
        y = startY;
        stepX = (finalPosX-x)/speed;
        stepY = (finalPosY-y)/speed;
        Random r = new Random();
        pause=r.nextInt()%maxPause;
        stepsLeft=speed;
        game = g;
        color = c;
    }
    
    public Spark(Game g, int startX, int startY, int endX, int endY, int maxPause,int speed, Color c,boolean glow){
        finalPosX = endX;
        finalPosY = endY;
        amountToTransfer = 0;
        x = startX;
        y = startY;
        stepX = (finalPosX-x)/speed;
        stepY = (finalPosY-y)/speed;
        Random r = new Random();
        pause=r.nextInt(maxPause);
        stepsLeft=speed;
        game = g;
        color = c;
        this.startX=startX;
        this.startY=startY;
        this.glow=glow;
    }
    
    public void move() {
        if(pause<=0){
            x += stepX;
            y += stepY;
            stepsLeft--;
            if(amountToTransfer!=0){
                if(isPowerBar) {
                finalPosX = transferPowerTo.getBarPos()[0];
                } else {
                    finalPosX = transferXPTo.getBarPos()[0];
                }
                stepX = (finalPosX-x)/stepsLeft;
                stepY = (finalPosY-y)/stepsLeft;
            }
        } else {
            pause--;
        }
        if(stepsLeft<=0){
            if(amountToTransfer>0){
                if(isPowerBar) {
                    transferPowerTo.addPower(amountToTransfer);
                } else {
                    transferXPTo.addXP(amountToTransfer);
                }
            }
            game.removeSpark(this);
        }
        if(glow){
	        //for(int x=-5;x<=5;x++){
		    //    for(int y=-5;y<=5;y++){
	        //		game.addGlow((int)(Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null)[0]),(int) (Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null)[1]), 100-(Math.abs(y+x)*20), (int)(this.x+x), (int)(this.y+y));
		    //    }
	        //}
        }
    }
    
    public void draw(Graphics g) {
        g.setColor(color);
        g.drawRect((int) x-1,(int) y-1, 1, 1);
        move();
    }

    public void drawGlow(Graphics g) {
        if(glow){
        	int dist = (int)(Math.sqrt(Math.pow(x-startX,2)+Math.pow(y-startY,2)));
        	if(dist<50){
	        	for(int i=0;i<dist+10;i=i+10){
			        g.setColor(new Color(color.getRed(),color.getGreen(),color.getBlue(),(50/(dist+10))*i));
			        g.fillOval((int) x-i, (int) y-i, 2*i, 2*i);
	        	}
        	} else {

	        	for(int i=0;i<50;i=i+10){
			        g.setColor(new Color(color.getRed(),color.getGreen(),color.getBlue(),i));
			        g.fillOval((int) x-i, (int) y-i, 2*i, 2*i);
	        	}
        	}
        	
        	/*if(dist>50){
		        g.setColor(new Color(color.getRed(),color.getGreen(),color.getBlue(),30));
		        g.fillOval((int) x-50, (int) y-50, 99, 99);
		        g.setColor(new Color(color.getRed(),color.getGreen(),color.getBlue(),70));
		        if(Math.pow(x-startX,2)+(Math.pow(y-startY,2))>20){
			        g.fillOval((int) x-20, (int) y-20, 39, 39);
	        	} else{
			        g.fillOval((int)(x-dist), (int)(y-dist), dist*2, dist*2);
	        	}
        	} else{
        		g.fillOval((int)(x-dist), (int)(y-dist), dist*2, dist*2);
        	}*/
        }
        
    }

   

    
}