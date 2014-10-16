package spaceInvaders;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class TitleScreen extends JPanel{
	private JFrame mainFrame;
	private LinkedList<Button> buttons = new LinkedList<>();

	public TitleScreen() {
		this.setSize(800, 600);
		mainFrame=new JFrame("SpaceInvadersEPIC");
		JPanel panel = (JPanel) mainFrame.getContentPane();
		panel.setPreferredSize(new Dimension(800,600));
		panel.setLayout(null);
		panel.add(this);
		mainFrame.pack();
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);
		this.addMouseListener(new MouseClickHandler());
		this.addMouseMotionListener(new MouseMoveHandler());
		initButtons();
		repaint();
		}

	public void initButtons() {
		//Game g,int x,int y, int width, int height,String text,Color outlineColor,Color innerColor,Color textColor,int type
		buttons.add(new Button(400,300,75,20,"start",Color.DARK_GRAY,Color.GREEN,Color.RED,1));//add buttons here
	}
	public void paint(Graphics g) {
		//Draw your epic title screen stuff here
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		System.out.println("painted");
		//Draw all buttons
		for(int i = 0;i<buttons.size();i++){
			buttons.get(i).draw((Graphics2D)g);
		}
	}
	private class MouseMoveHandler implements MouseMotionListener{

		@Override
		public void mouseDragged(MouseEvent e) {
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			try{
				if(buttons.get(0).isInside(e.getX(), e.getY())&&!buttons.get(0).hasMouseOver()){
					buttons.get(0).changeMouseOver(true);
					buttons.get(0).changeColors(Color.DARK_GRAY, Color.BLUE, Color.LIGHT_GRAY);
				}
				if(!buttons.get(0).isInside(e.getX(), e.getY())&&buttons.get(0).hasMouseOver()){
					buttons.get(0).changeMouseOver(false);
					buttons.get(0).changeColors(Color.DARK_GRAY,Color.LIGHT_GRAY,Color.BLACK);
				}
			} catch(NullPointerException ex){

			} catch(IndexOutOfBoundsException ex){
				
			}
		}

	}
	private class MouseClickHandler implements MouseListener{
		public void mouseClicked(MouseEvent e) {
			if(buttons.get(0).isInside(e.getX(), e.getY())){
				 Game g =new Game();
				 TitleScreen.this.mainFrame.setVisible(false);
				 g.errorCatch();
			}
		}
		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}
		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}
		@Override
		public void mousePressed(MouseEvent arg0) {

		}
		@Override
		public void mouseReleased(MouseEvent arg0) {
		}
	}
	
	 public static void main(String args[]) {
		 TitleScreen t = new TitleScreen();
	 }
}
