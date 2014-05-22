package spaceInvaders;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

public class Button {
	private Rectangle rect;
	private String text;
	private int strX;
	private int strY;
	private Game game;
	private Color[] colors= new Color[3];
	private boolean isActive = true;
	private boolean mouseOver = false;
	
	public Button(Game g,int x,int y, int width, int height,String text){
		game=g;
		rect = new Rectangle(x,y,width,height);
		this.text=text;
	    Graphics2D g2D = (Graphics2D) game.getStrategy().getDrawGraphics();
	}
	
	public Button(Game g,int x,int y, int width, int height,String text,Color outlineColor,Color innerColor,Color textColor){
		game=g;
		rect = new Rectangle(x,y,width,height);
		this.text=text;
		colors[0]=outlineColor;
		colors[1]=innerColor;
		colors[2]=textColor;
	}
	
	public boolean isInside(int x, int y){
		if(x>rect.x&&y>rect.y&&x<(rect.x+rect.width)&&y<(rect.y+rect.height)){
			return true;
		}
		return false;
	}
	
	public void changePos(int newX, int newY){
		rect.setLocation(newX, newY);
	}
	
	public void changeSize(int newW, int newH){
		rect.setSize(newW, newH);
	    Graphics2D g2D = (Graphics2D) game.getStrategy().getDrawGraphics();
	}
	
	public void changeColors(Color outlineColor,Color innerColor,Color textColor){
		colors[0]=outlineColor;
		colors[1]=innerColor;
		colors[2]=textColor;
	}
	
	public void changeText(String newText) {
		text=newText;
	}
	
	public void changeAll(int x,int y, int width, int height,String text,Color outlineColor,Color innerColor,Color textColor){
		rect = new Rectangle(x,y,width,height);
		this.text=text;
		colors[0]=outlineColor;
		colors[1]=innerColor;
		colors[2]=textColor;
	}
	
	public Rectangle getRect() {
		return rect;
	}
	
	public Color[] getColors() {
		return colors;
	}
	
	public void changeActive(boolean newS){
		isActive=newS;
	}
	
	public boolean isActive() {
		return isActive;
	}
	
	public void changeMouseOver(boolean newS){
		mouseOver=newS;
	}
	
	public boolean hasMouseOver(){
		return mouseOver;
	}
	
	public void draw(Graphics2D g,Color outlineColor,Color innerColor,Color textColor) {
		if(isActive){
			strX=(int)(rect.x+(((double)(rect.width-g.getFontMetrics().stringWidth(this.text)))/2))+2;
			strY=(int)(rect.height+rect.y+(((double)(rect.height-g.getFontMetrics().getHeight()))/2))-2;
			g.setColor(outlineColor);
			g.drawRect(rect.x, rect.y, rect.width, rect.height);
			g.setColor(innerColor);
			g.fillRect(rect.x+1, rect.y+1, rect.width-1, rect.height-1);
			g.setColor(textColor);
			g.drawString(text, strX, strY);
		}
	}
	
	public void draw(Graphics2D g) {
		if(isActive){
			strX=(int)(rect.x+(((double)(rect.width-g.getFontMetrics().stringWidth(this.text)))/2))+2;
			strY=(int)(rect.height+rect.y+(((double)(rect.height-g.getFontMetrics().getHeight()))/2))-2;
			g.setColor(colors[0]);
			g.drawRect(rect.x, rect.y, rect.width, rect.height);
			g.setColor(colors[1]);
			g.fillRect(rect.x+1, rect.y+1, rect.width-1, rect.height-1);
			g.setColor(colors[2]);
			g.drawString(text, strX, strY);
		}
			
	}
	
}
