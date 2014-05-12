package spaceInvaders;

public class FiringAlien extends AlienEntity{
    protected int nextShot;
	
    public FiringAlien(Game game,String ref,int x,int y, int row, int col) {
            super(game,ref,x,y,row,col);
    }

    @Override
    public void move(long delta) {
        // if we have reached the left hand side of the screen and
        // are moving left then request a logic update 
        if ((dx < 0) && (x < 10)) {
            game.updateLogic();
        }
        // and vice vesa, if we have reached the right hand side of 
        // the screen and are moving right, request a logic update
        if ((dx > 0) && (x > 750)) {
            game.updateLogic();
        }
        
        // proceed with normal move
        super.move(delta);
        
        if(nextShot<=0){
        	ShotEntity shot = new ShotEntity(game,"sprites/AlienShot.png",(int)x+14,(int)y-5,10,1);
            game.entities.add(shot);
        	nextShot = (int) Math.floor(Math.random()*300);
        } else {
        	nextShot--;
        }
    }
}
