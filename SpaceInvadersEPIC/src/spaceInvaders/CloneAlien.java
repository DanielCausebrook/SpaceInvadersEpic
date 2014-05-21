/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceInvaders;

/**
 *
 * @author Daniel
 */
public class CloneAlien extends AlienEntity{
	private static final int maxTime = 500;
    private int timeToClone = maxTime;
	
	
    public CloneAlien(Game game,String ref,int x,int y, int row, int col){
        super(game,ref,x,y,row,col);
        armour=150;
        maxHealth = 150;
    }
    
    @Override
    public void move(long delta) {
            // if we have reached the left hand side of the screen and
            // are moving left then request a logic update 
           if ((dx < 0) && (x < 10)) {
                    game.updateLogic();
            }
            // and vice versa, if we have reached the right hand side of 
           // the screen and are moving right, request a logic update
            if ((dx > 0) && (x > 750)) {
                    game.updateLogic();
            }

            // proceed with normal move
            super.move(delta);
            try{
                if(game.getAlienGrid()[rowNum+1][colNum]==null){
                	timeToClone-=delta;
                	if(timeToClone<=0){
                		AlienEntity alien;
                		alien = new RegularAlien(game,"sprites/alienShield.png",(int) x,(int)y+30,rowNum+1,colNum);
                		game.addEntity(alien);
                		alien.setHorizontalMovement(dx);
	                    Entity[][] grid = game.getAlienGrid();
	                    grid[rowNum+1][colNum]=alien;
	                    game.setAlienGrid(grid);
	                    game.notifyAlienCreated();
	                    timeToClone=maxTime;
                	}
                }
            }catch(Exception e){

            }
    }
}
