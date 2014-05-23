package spaceInvaders;

import java.awt.Color;
import java.awt.Rectangle;

public class UpgradeItem {
	private int x;
	private int y;
	private String text;
	private Game game;
	private int cost;
	private int numUpgradeLevels;
	private int upgradeLevel;
	private boolean mouseOver = false;
	
	public UpgradeItem(Game g, int x, int y, String text,int cost,int numUpgLev) {
		game=g;
		this.x=x;
		this.y=y;
		this.text=text;
		this.cost=cost;
		numUpgradeLevels=numUpgLev;
	}
}
