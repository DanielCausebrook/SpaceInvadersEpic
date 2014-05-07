/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceInvaders;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Daniel
 */
public class UpgradeShop extends JPanel{
    private int upgradePoints = 0;
    private int shotPower = 0;
    private int bonusPower = 0;
    private Game game;
    private JFrame upgradeFrame;
    private int mode=0;
    private boolean change=false;
    private int moveStep;

    public UpgradeShop(Game g,JFrame frame) {
        game=g;
        upgradeFrame=frame;
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(mode==1) {
                    if(e.getX()>130&&e.getX()<230&&e.getY()>35&&e.getY()<55) {
                        if(upgradePoints>=5&&shotPower<30){
                            upgradePoints-=5;
                            shotPower++;
                            repaint();
                        }
                    }
                    if(e.getX()>130&&e.getX()<230&&e.getY()>75&&e.getY()<95) {
                        if(upgradePoints>=10&&bonusPower<10){
                            upgradePoints-=10;
                            bonusPower++;
                            repaint();
                        }
                    }
                }
                
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if(mode==0) {
                    mode=1;
                    change=true;
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if(mode==1) {
                    mode=0;
                    change=true;
                }
            }
            
        });;
        
        
    }
    
    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0,0,240,400);
        if(mode==1) {
            g.setColor(Color.WHITE);
            g.drawString("You have " + upgradePoints + " upgrade points!",10,20);
            g.setColor(Color.ORANGE);
            g.drawString("Shot power:",10,50);
            g.drawString("Super shot power:",10,90);
            g.setColor(Color.DARK_GRAY);
            for(int i=0;i<30;i++){
                g.drawRect((i*6)+2, 60, 5, 10);
                if(shotPower-1>=i){
                    g.setColor(Color.BLUE);
                    g.fillRect((i*6)+3, 61, 4, 9);
                    g.setColor(Color.DARK_GRAY);
                }
            }
            for(int i=0;i<10;i++){
                g.drawRect((i*16)+2, 100, 15, 10);
                if(bonusPower-1>=i){
                    g.setColor(Color.BLUE);
                    g.fillRect((i*16)+3, 101, 14, 9);
                    g.setColor(Color.DARK_GRAY);
                }
            }
            if(shotPower<30) {
                g.fillRect(130, 35, 100, 20);
            }
            if(bonusPower<10) {
                g.fillRect(130, 75, 100, 20);
            }
            g.setColor(Color.RED);
            if(shotPower<30) {
                g.drawString("Upgrade   5 pts", 140, 50);
            }
            if(bonusPower<10) {
                g.drawString("Upgrade  10 pts", 140, 90);
            }
        } else if (mode==0) {
            g.setColor(Color.WHITE);
            g.drawString("U", 10, 13);
            g.drawString("p", 10, 25);
            g.drawString("g", 10, 37);
            g.drawString("r", 10, 49);
            g.drawString("a", 10, 61);
            g.drawString("d", 10, 73);
            g.drawString("e", 10, 85);
            g.drawString("s", 10, 97);
        }
    }   
    
    public int getUpgradePoints() {
        return upgradePoints;
    }
    
    public int getShotPower() {
        return shotPower;
    }
    
    public int getBonusPower() {
        return bonusPower;
    }
    public void levelUp() {
        upgradePoints+=10;
        repaint();
    }
    public void updateWindow() {
        if(change){
            if(mode==0) {
                if(moveStep>0) {
                    upgradeFrame.setLocation(upgradeFrame.getX(), upgradeFrame.getY()+5);
                    upgradeFrame.setSize(upgradeFrame.getWidth()-7, upgradeFrame.getHeight()-10);
                    moveStep--;
                    requestFocus();
                } else {
                    moveStep=0;
                    change=false;
                }
            } else if(mode==1) {
                if(moveStep<30) {
                    upgradeFrame.setLocation(upgradeFrame.getX(), upgradeFrame.getY()-5);
                    upgradeFrame.setSize(upgradeFrame.getWidth()+7, upgradeFrame.getHeight()+10);
                    moveStep++;
                    requestFocus();
                } else {
                    moveStep=30;
                    change=false;
                }
            }
        }
    }
}
