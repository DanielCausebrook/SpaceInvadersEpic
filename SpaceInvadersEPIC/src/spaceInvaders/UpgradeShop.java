/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceInvaders;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Daniel
 */
public class UpgradeShop extends JPanel{
    private int upgradePoints = 0;
    private int shotPower = 0;
    private int xShotPower = 0;
    private int bonusPower = 0;
    private int xBonusPower = 0;
    private Game game;
    private JFrame upgradeFrame;
    private int mode=0;
    private boolean change=false;
    private int moveStep;
    private int currentLevel;
    private boolean reset=false;
    private LinkedList<UpgradeItem> items = new LinkedList<>();

    public UpgradeShop(Game g,JFrame frame) {
    	game=g;
        upgradeFrame=frame;
        currentLevel = game.getLevel();
        initUpgrades();
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(mode==1) {
                    for(int i=0;i<items.size();i++){
                    	if(items.get(i).isInside(e.getX(), e.getY())){
                    		upgradePoints=items.get(i).buy(upgradePoints);
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
    
    private void initUpgrades() {
        items.add(new UpgradeItem(game,2,30,"Shot power",5,30));
        items.add(new UpgradeItem(game,2,70,"Bonus power",10,10));
    }
    
    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0,0,240,400);
        if(mode==1) {
            g.setColor(Color.WHITE);
            g.drawString("You have " + upgradePoints + " upgrade points!",10,20);
            for(int i=0;i<items.size();i++){
            	items.get(i).draw((Graphics2D) g);
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
    
    public int getLevel(int item) {
        return items.get(item).getLevel();
    }
    
    public int getxBonusPower() {
    	return xBonusPower;
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
