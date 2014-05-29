package spaceInvaders;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class TitleScreen extends JPanel{
	private JFrame mainFrame;
    private LinkedList<Button> buttons = new LinkedList<>();
	
	public TitleScreen() {
		this.setSize(800, 600);
		mainFrame=new JFrame("SpaceInvadersEPIC");
		mainFrame.add(this,0,0);
		mainFrame.pack();
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);
		//1:init mainFrame
		
		//2:add this to mainFramelstop
	}
	
	public void initButtons() {
		//Game g,int x,int y, int width, int height,String text,Color outlineColor,Color innerColor,Color textColor,int type
		buttons.add(new Button(400,300,75,10,"start",Color.DARK_GRAY,Color.GREEN,Color.RED,1));//add buttons here
	}
	public void repaint(Graphics g) {
		//Draw your epic title screen stuff here
		
		 //Draw all buttons
		 for(int i = 0;i<buttons.size();i++){
			 buttons.get(i).draw((Graphics2D)g);
		 }
	}
}
