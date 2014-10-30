package spaceInvaders;

import java.awt.Color;
import java.awt.Graphics2D;

//PLEASE DO NOT USE!!!
public class Glow implements Runnable{
	Graphics2D graphics;
	Game game;
	
	public Glow(Graphics2D g,Game game){
		this.game=game;
		graphics=g;
	}
	
	@Override
	public void run() {
		while(game.gameRunning){
	        for(int x=0;x<game.gameDimensions.x;x++){
	        	for(int y=0;y<game.gameDimensions.y;y++){
					if(game.glowColor[x][y]!=null){
						graphics.setColor(game.glowColor[x][y]);
						graphics.drawRect(x,y, 1, 1);
					} else {
						graphics.setColor(Color.black);
					}
					game.glowColor[x][y]=Color.black;
	        	}
	        }
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
