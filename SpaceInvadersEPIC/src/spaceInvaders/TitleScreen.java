package spaceInvaders;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class TitleScreen extends JPanel{
	private JFrame mainFrame;
    private LinkedList<Button> buttons = new LinkedList<>();
	
	public TitleScreen() {
		mainFrame=new JFrame("SpaceInvadersEPIC");
		//1:init mainFrame
		
		//2:add this to mainFrame
		
	}
	
	public void initButtons() {
		buttons.add(new Button(/*etc...*/,1));//add buttons here
	}
	
	public void repaint(Graphics g) {
		//Draw your epic title screen stuff here
		
		 //Draw all buttons
		 for(int i = 0;i<buttons.size();i++){
			 buttons.get(i).draw((Graphics2D)g);
		 }
	}
}
