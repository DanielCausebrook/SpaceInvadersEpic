package spaceInvaders;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class UpgradeItem {
	private int x;
	private int y;
	private String text;
	private Game game;
	private int cost;
	private int numUpgradeLevels;
	private int upgradeLevel=0;
	private boolean mouseOver = false;

	public UpgradeItem(Game g, int x, int y, String text,int cost,int numUpgLev) {
		game=g;
		this.x=x;
		this.y=y;
		this.text=text;
		this.cost=cost;
		numUpgradeLevels=numUpgLev;
	}
	
	public int buy(int currPoints){
		if(currPoints>=cost&&upgradeLevel<numUpgradeLevels){
			upgradeLevel++;
		}
		return currPoints;
	}
	
	public void isInside(int mouseX,int mouseY){
		
	}
	
	public void mouseOver() {
		
	}
	
	public void draw(Graphics2D g){
		g.setColor(Color.BLACK);
		g.fillRect(x, y, 200, 70);
		g.setColor(Color.WHITE);
	}
}