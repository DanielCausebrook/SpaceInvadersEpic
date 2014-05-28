package spaceInvaders;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
/**
 * The main hook of our game. This class with both act as a manager
 * for the display and central mediator for the game logic. 
 * 
 * Display management will consist of a loop that cycles round all
 * entities in the game asking them to move and then drawing them
 * in the appropriate place. With the help of an inner class it
 * will also allow the player to control the main ship.
 * 
 * As a mediator it will be informed when entities within our game
 * detect events (e.g. alien killed, played died) and will take
 * appropriate game actions.
 * 
 * @author Kevin Glass
 */
public class Game extends Canvas {
	final Point gameDimensions = new Point(800,600);
    /** The strategy that allows us to use accelerate page flipping */
    private final BufferStrategy strategy;
    /** True if the game is currently "running", i.e. the game loop is looping */
    final boolean gameRunning = true;
    /** The list of all the entities that exist in our game */
    final ArrayList entities = new ArrayList();
    /** The list of entities that need to be removed from the game this loop */
    private final ArrayList removeList = new ArrayList();
    /** The entity representing the player */
    private Entity ship;
    /** The speed at which the player's ship should move (pixels/second) */
    private final double moveSpeed = 300;
    /** The time at which last fired a shot */
    private long lastFire = 0;
    /** The interval between our players shot (ms) */
    private final long firingInterval = 0;
    /** The number of aliens left on the screen */
    private int alienCount;
    
    /** The message to display which waiting for a key press */
    private String message = "";
    /** True if we're holding up game play until a key has been pressed */
    private boolean waitingForKeyPress = true;
    /** True if the left cursor key is currently pressed */
    private boolean leftPressed = false;
    /** True if the right cursor key is currently pressed */
    private boolean rightPressed = false;
    /** True if we are firing */
    private boolean firePressed = false;
    /** True if game logic needs to be applied this loop, normally as a result of a game event */
    private boolean logicRequiredThisLoop = false;
        
    private boolean epicPressed = false;
    
    private final int gridRows = 5;
        
    private final int gridCols = 12;
        
    private PowerBar power;
        
    private XPBar XP;
        
    private Entity[][] alienGrid = new AlienEntity[gridRows][gridCols];
        
    private final ArrayList sparks = new ArrayList();
        
    private int sparkCount=0;
    
    private int level = 14;
    
    private UpgradeShop upgradePanel;
    
    private JFrame upgradeFrame;
    
    private boolean paused=false;//0=not paused, 1=paused, 2=focus lost
    
    private boolean upgradesShown = false;
    
    private int damaged;
    
    private int mouseX;
    
    private int mouseY;
    
    private boolean mouseControls = false;
    
    private boolean mousePressed = false;
    
    private boolean autoFire = false;
    
    private LinkedList<Button> buttons = new LinkedList<>();
    
    Color[][] glowColor;
    int[][] numGlows;
    Glow glow;
    //hue: 0-50:red-yellow
    //sat:100
    //brightness: 0-100:black-colour
    /**
     * Construct our game and set it running.
     */
    public Game() {
        // create a frame to contain our game
        JFrame container = new JFrame("Space Invaders 101");
        //Window frame?
        container.setUndecorated(true);
        // get hold the content of the frame and set up the resolution of the game
        JPanel panel = (JPanel) container.getContentPane();
        panel.setPreferredSize(new Dimension(800,600));
        panel.setLayout(null);
        
        // setup our canvas size and put it into the content of the frame
        setBounds(0,0,gameDimensions.x,gameDimensions.y);
        panel.add(this);
        
        // Tell AWT not to bother repainting our canvas since we're
        // going to do that our self in accelerated mode
        setIgnoreRepaint(true);
        
        // finally make the window visible 
        container.pack();
        container.setResizable(false);
        container.setVisible(true);
        
        glowColor = new Color[gameDimensions.x][gameDimensions.y];
        numGlows = new int[gameDimensions.x][gameDimensions.y];
        
        // add a listener to respond to the user closing the window. If they
        // do we'd like to exit the game
        container.addWindowListener(new WindowAdapter() {
            @Override
            public void windowDeactivated(WindowEvent e) {
                if(!waitingForKeyPress) {
                    paused=true;
                    buttons.get(0).changeAll(365, 350, 70, 15, "Unpause(P)", Color.DARK_GRAY,Color.LIGHT_GRAY,Color.BLACK);
    	        	buttons.get(1).changeMouseOver(false);
        	        buttons.get(1).changeActive(true);
                }
                upgradesShown=false;
            }
            @Override
            public void windowActivated(WindowEvent e) {
                if(!upgradesShown) {
                    upgradesShown=true;
                    requestFocus();
                } else {
                    upgradesShown=false;
                    upgradeFrame.requestFocus();
                }
                
            }
            
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
            
            @Override
            public void windowIconified(WindowEvent e) {
                upgradeFrame.setVisible(false);
                upgradesShown=false;
            }
            
            @Override
            public void windowDeiconified(WindowEvent e) {
                upgradeFrame.setVisible(true);
                upgradesShown=true;
                requestFocus();
                
            }
        });
        
        initButtons();
        
        // add a key input system (defined below) to our canvas
        // so we can respond to key pressed
        addKeyListener(new KeyInputHandler());
        this.addMouseListener(new MouseClickHandler());
        this.addMouseMotionListener(new MouseMoveHandler());
        
        initUpgrades();
        
        // request the focus so key events come to us
        requestFocus();

        // create the buffering strategy which will allow AWT
        // to manage our accelerated graphics
        createBufferStrategy(2);
        strategy = getBufferStrategy();
        
        //glow=new Glow((Graphics2D)strategy.getDrawGraphics(),this);
        
        // initialise the entities in our game so there's something
        // to see at startup
        initEntities();
        XP = new XPBar(this);
    }
    
    /**
     * Start a fresh game, this should clear out any old data and
     * create a new set.
     */
    private void startGame() {
        // clear out any existing entities and initialise a new set
        entities.clear();
        initEntities();
        // blank out any keyboard settings we might currently have
        leftPressed = false;
        rightPressed = false;
        firePressed = false;
        new Thread(glow).start();
    }
    
    private void initButtons(){
        buttons.add(new Button(this,720,40,70,15,"Pause (P)",Color.DARK_GRAY,Color.LIGHT_GRAY,Color.BLACK));
        buttons.add(new Button(this,355,330,90,15,"Mouse Controls",Color.DARK_GRAY,Color.LIGHT_GRAY,Color.BLACK));
        buttons.get(1).changeActive(false);
    }
    
    private void initUpgrades() {
        upgradeFrame = new JFrame("Upgrades");
        upgradePanel = new UpgradeShop(this,upgradeFrame);
        upgradePanel.setBounds(0,0,240,400);
        upgradeFrame.setLayout(null);
        upgradeFrame.add(upgradePanel);
        upgradeFrame.setUndecorated(true);
        upgradeFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        upgradeFrame.setSize(30, 100);
        upgradeFrame.setLocation(800, 300);
        upgradeFrame.setVisible(true);
    }
    
    /**
     * Initialise the starting state of the entities (ship and aliens). Each
     * entity will be added to the overall list of entities in the game.
     */
    private void initEntities() {
        // create the player ship and place it roughly in the centre of the screen
    	double prevArm;
    	try{
    		prevArm = ((ShipEntity)ship).armour;
    	} catch(NullPointerException e){
    		prevArm=0;
    	}
        ship = new ShipEntity(this,"sprites/ship.png",370,550);
        if(prevArm>0){
        	((ShipEntity)ship).armour = prevArm;
        }
        entities.add(ship);
        
        // create a boss alien at level 15
        if(level==14){
        	alienCount = 0;
        	upgradePanel.getUpgradeItem(0).setNumUpgradeLevels(60);
        	upgradePanel.getUpgradeItem(1).setNumUpgradeLevels(20);
        	Entity alien;
        	alien = new bossAlien(this,"sprites/alienBoss.png",100,50,0,0);
        	entities.add(alien);
        	alienGrid[0][0]=alien;
        	alienCount++;
        } else{

			// create a block of aliens (5 rows, by 12 aliens, spaced evenly)
			alienCount = 0;
			for (int row=0;row<gridRows;row++) {
				for (int x=0;x<gridCols;x++) {
					Entity alien;
					double randDec = Math.random();
					if(randDec<=(level)*0.01) {
						alien = new StrongAlien(this,"sprites/alienStrong.png",100+(x*50),(50)+row*30,row,x);
					} else if(randDec<=(level)*0.02) {
						alien = new RegenAlien(this,"sprites/alienRegen.png",100+(x*50),(50)+row*30,row,x);
					} else if(randDec<=(level)*0.03) {
						alien = new CloneAlien(this,"sprites/alienClone.png",100+(x*50),(50)+row*30,row,x);
					} else if(randDec<=(level)*0.04) {
						alien = new FiringAlien(this,"sprites/FiringAlien.png",100+(x*50),(50)+row*30,row,x);
					} else {
						alien = new RegularAlien(this,"sprites/alien.png",100+(x*50),(50)+row*30,row,x);
					}
					entities.add(alien);
					alienGrid[row][x]=alien;
					alienCount++;
				}
			}
		}
		power = new PowerBar(this);
	}

	/**
	 * Notification from a game entity that the logic of the game
	 * should be run at the next opportunity (normally as a result of some
	 * game event)
	 */
	public void updateLogic() {
		logicRequiredThisLoop = true;
	}

	/**
	 * Remove an entity from the game. The entity removed will
	 * no longer move or be drawn.
	 * 
	 * @param entity The entity that should be removed
	 */
	public void removeEntity(Entity entity) {
		removeList.add(entity);
	}

	/**
	 * Notification that the player has died. 
	 */
	public void notifyDeath() {
		((ShipEntity) ship).resetShip();
		message = "Oh no! They got you, try again?";
		waitingForKeyPress = true; 
	}

	/**
	 * Notification that the player has won since all the aliens
	 * are dead.
	 */
	public void notifyWin() {
		message = "Well done! You Win!";
		Random r = new Random();
		for(int i=0;i<100;i++){
			double angle = r.nextInt(360);
			int distance = r.nextInt(100);
			int xPos = (int) (400+(Math.cos(angle)*distance));
			int yPos = (int) (300+(Math.sin(angle)*distance));
			addSpark(xPos,yPos, 200,1,false,100, Color.WHITE,false);
		}
		level++;
		waitingForKeyPress = true;
	}
	
	public int getLevel() {
		return level;
	}

	public ArrayList getEntities(){
		return entities;
	}

	public Entity[][] getAlienGrid(){
		return alienGrid;
	}

	public void setAlienGrid(Entity[][] grid){
		alienGrid=grid;
	}

	public void addEntity(Entity ent){
		entities.add(ent);
	}

	public void addSpark(int startX, int startY, int maxPause, int transfer, boolean isPowerBar,int speed, Color c,boolean glow) {
		if(isPowerBar){
			Spark spk= new Spark(this,power,null,true,startX,startY,maxPause,transfer,speed,c,glow);
			sparks.add(spk);
		} else {
			Spark spk= new Spark(this,null,XP,false,startX,startY,maxPause,transfer,speed,c,glow);
			sparks.add(spk);
		}
	}

	public void addSpark(int startX, int startY, int maxPause, int endX, int endY,int speed, Color c,boolean glow) {
		Spark spk= new Spark(this,startX,startY,endX,endY,maxPause,speed,c,glow);
		sparks.add(spk);
	}

	public void removeSpark(Spark s){
		sparks.remove(s);
	}

	public void levelUp() {
		upgradePanel.levelUp();
	}

	public void addGlow(int h, int s, int b, int x, int y) {
		int[] hsb = new int[3];
		hsb[0]=(int)(((numGlows[x][y]*Color.RGBtoHSB(glowColor[x][y].getBlue(),glowColor[x][y].getGreen(),glowColor[x][y].getRed(), null)[0])+h)/numGlows[x][y]+1);
		hsb[1]=(int)(((numGlows[x][y]*Color.RGBtoHSB(glowColor[x][y].getBlue(),glowColor[x][y].getGreen(),glowColor[x][y].getRed(), null)[1])+s)/numGlows[x][y]+1);
		hsb[2]=(int)(((numGlows[x][y]*Color.RGBtoHSB(glowColor[x][y].getBlue(),glowColor[x][y].getGreen(),glowColor[x][y].getRed(), null)[2])+b)/numGlows[x][y]+1);
		glowColor[x][y]=Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
		numGlows[x][y]++;
	}

	public Entity getShip() {
		return ship;
	}

	public BufferStrategy getStrategy(){
		return strategy;
	}

	public void isDamaged() {
		damaged=100;
	}


	/**
	 * Notification that an alien has been killed
	 */
	 public void notifyAlienKilled(int y,int x) {
		// reduce the alien count, if there are none left, the player has won!
		alienCount--;

		if (alienCount == 0) {
			notifyWin();
			for(int i=0;i<585-y;i+=2){
				addSpark(x+10,y+i,200,1,false,100,Color.WHITE,false);
			}
		}

		// if there are still some aliens left then they all need to get faster, so
		// speed up all the existing aliens
		for (int i=0;i<entities.size();i++) {
			Entity entity = (Entity) entities.get(i);

			if (entity instanceof AlienEntity) {
				// speed up by 2%
				entity.setHorizontalMovement(entity.getHorizontalMovement() * 1.02);
			}
		}
	 }

	 public void notifyAlienCreated() {
		 alienCount++;

		 for (int i=0;i<entities.size();i++) {
			 Entity entity = (Entity) entities.get(i);

			 if (entity instanceof AlienEntity) {
				 // slow down by 1%
				 entity.setHorizontalMovement(entity.getHorizontalMovement() * 0.98);
			 }
		 }
	 }

	 /**
	  * Attempt to fire a shot from the player. Its called "try"
	  * since we must first check that the player can fire at this 
	  * point, i.e. has he/she waited long enough between shots
	  */
	 public void tryToFire(boolean isEpic) {
		 // check that we have waiting long enough to fire
		 if (System.currentTimeMillis() - lastFire < firingInterval) {
			 return;
		 }

		 // if we waited long enough, create the shot entity, and record the time.
		 lastFire = System.currentTimeMillis();
		 ShotEntity shot = new ShotEntity(this,"sprites/shot.gif",ship.getX()+12,ship.getY()-5,upgradePanel.getLevel(0),0);
		 entities.add(shot);
		 shot = new ShotEntity(this,"sprites/shot.gif",ship.getX()+6,ship.getY()-5,upgradePanel.getLevel(0),0);
		 entities.add(shot);
		 if(isEpic&&power.getPower()>=10){
			 for(int i = 0;i<=upgradePanel.getLevel(1);i++){
				 int shotPos;
				 if(i%2==0){
					 shotPos = (9+((int) Math.ceil(((double)i)/2)));
				 } else {
					 shotPos = (9-((int) Math.ceil(((double)i)/2)));
				 }
				 shot = new ShotEntity(this,"sprites/shot2.gif",ship.getX()+shotPos,ship.getY()-5,1,0);
				 entities.add(shot);
			 }
			 shot = new ShotEntity(this,"sprites/shot2.gif",ship.getX()+9,ship.getY()-5,1,0);
			 entities.add(shot);
			 power.addPower(-10);

		 }
		 }

	 /**
	  * The main game loop. This loop is running during all game
	  * play as is responsible for the following activities:
	  * <p>
	  * - Working out the speed of the game loop to update moves
	  * - Moving the game entities
	  * - Drawing the screen contents (entities, text)
	  * - Updating game events
	  * - Checking Input
	  * <p>
	  */
	 public void gameLoop() throws Exception{
		 long lastLoopTime = System.currentTimeMillis();

		 // keep looping round until the game ends
		 while (gameRunning) {
			 // work out how long its been since the last update, this
			 // will be used to calculate how far the entities should
			 // move this loop
			 long delta = System.currentTimeMillis() - lastLoopTime;
			 lastLoopTime = System.currentTimeMillis();

			 // Get hold of a graphics context for the accelerated 
			 // surface and blank it out
			 Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
			 g.setColor(Color.black);
			 g.fillRect(0,0,800,600);
			 SpriteStore.get().getSprite("sprites/Earth.png").draw(g, 10, 200);
			 Color c= new Color(0,0,0,100);
			 if(damaged>0){
				 c = new Color(100,0,0,100);
				 damaged-=delta;
			 }
			 g.setColor(c);
			 g.fillRect(0,0,800,600);


			 // cycle round asking each entity to move itself
			 if (!waitingForKeyPress&&!paused) {
				 for (int i=0;i<entities.size();i++) {
					 Entity entity = (Entity) entities.get(i);

					 entity.move(delta);
				 }
			 }
			 upgradePanel.updateWindow();


			 for(int i=0;i<sparks.size();i++) {
				 Spark spark = (Spark) sparks.get(i);
				 spark.drawGlow(g);
			 }
			 for(int i=0;i<sparks.size();i++) {
				 Spark spark = (Spark) sparks.get(i);
				 spark.draw(g);
			 }

			 // cycle round drawing all the entities we have in the game
			 for (int i=0;i<entities.size();i++) {
				 Entity entity = (Entity) entities.get(i);

				 entity.draw(g);
				 if(entity instanceof bossAlien){
					 g.setColor(Color.DARK_GRAY);
					 g.drawRect(11,580,780, 2);
					 g.setColor(Color.RED);
					 g.fillRect(11,580,(int) (780*((double)((AlienEntity) entity).armour/((AlienEntity) entity).maxHealth)), 2);
				 } else if(entity instanceof AlienEntity){
					 g.setColor(Color.DARK_GRAY);
					 g.drawRect(entity.getX(), entity.getY()+entity.sprite.getHeight()+1, entity.sprite.getWidth(), 2);
					 g.setColor(Color.RED);
					 g.fillRect(entity.getX(), entity.getY()+entity.sprite.getHeight()+1,(int) (entity.sprite.getWidth()*((double)((AlienEntity) entity).armour/((AlienEntity) entity).maxHealth)), 2);


				 } else if(entity instanceof ShipEntity){
					 g.setColor(Color.DARK_GRAY);
					 g.drawRect(entity.getX(), entity.getY()+entity.sprite.getHeight()+1, entity.sprite.getWidth(), 2);
					 g.setColor(Color.RED);
					 g.fillRect(entity.getX(), entity.getY()+entity.sprite.getHeight()+1,(int) (entity.sprite.getWidth()*((double)((ShipEntity) entity).armour/((ShipEntity) entity).maxHealth)), 2);


				 }
			 }

			 power.draw(g);
			 XP.draw(g);


			 if(!waitingForKeyPress&&!epicPressed&&!paused) {
				 sparkCount++;
				 if(sparkCount>=1){
					 Random rand = new Random();
					 Spark spk = new Spark(this,power,null,true,power.getBarPos()[0]+(rand.nextInt()%40)-20,power.getBarPos()[1]-(rand.nextInt()%20),10,1,50,Color.orange,Spark.glowEnabled);
					 sparks.add(spk);
					 sparkCount=0;
				 }
			 }

			 // brute force collisions, compare every entity against
			 // every other entity. If any of them collide notify 
			 // both entities that the collision has occurred
			 if(!waitingForKeyPress&&!paused){
				 for (int p=0;p<entities.size();p++) {
					 for (int s=p+1;s<entities.size();s++) {
						 Entity me = (Entity) entities.get(p);
						 Entity him = (Entity) entities.get(s);

						 if (me.collidesWith(him)) {
							 me.collidedWith(him);
							 him.collidedWith(me);
						 }
					 }
				 }
			 }

			 // remove any entity that has been marked for clear up
			 entities.removeAll(removeList);
			 removeList.clear();


			 // if a game event has indicated that game logic should
			 // be resolved, cycle round every entity requesting that
			 // their personal logic should be considered.
			 if (logicRequiredThisLoop) {
				 for (int i=0;i<entities.size();i++) {
					 Entity entity = (Entity) entities.get(i);
					 entity.doLogic();
				 }

				 logicRequiredThisLoop = false;
			 }

			 // if we're waiting for an "any key" press then draw the 
			 // current message 
			 g.setColor(Color.white);
			 if(paused) {
				 g.setColor(Color.WHITE);
				 g.fillRect(350, 275,100,100);
				 g.setColor(Color.BLACK);
				 g.drawString("Paused",(800-g.getFontMetrics().stringWidth("Paused"))/2,300);
			 } else if (waitingForKeyPress) {
				 g.drawString(message,(800-g.getFontMetrics().stringWidth(message))/2,250);
				 g.drawString("Press any key",(800-g.getFontMetrics().stringWidth("Press any key"))/2,300);
			 }

			 g.drawString("Level "+(level+1),10,30);


			 //Draw all buttons
			 for(int i = 0;i<buttons.size();i++){
				 buttons.get(i).draw(g);
			 }

			 // finally, we've completed drawing so clear up the graphics
			 // and flip the buffer over
			 g.dispose();
			 strategy.show();

			 // resolve the movement of the ship. First assume the ship 
			 // isn't moving. If either cursor key is pressed then
			 // update the movement appropriately
			 ship.setHorizontalMovement(0);
			 if(!paused) {
				 if(mouseControls){
					 if(mouseX+10<ship.x+(ship.sprite.getWidth()/2)){
						 ship.setHorizontalMovement(-moveSpeed);
					 } else if(mouseX-10>ship.x+(ship.sprite.getWidth()/2)){
						 ship.setHorizontalMovement(moveSpeed);
					 }
				 } else {
					 if ((leftPressed) && (!rightPressed)) {
						 ship.setHorizontalMovement(-moveSpeed);
					 } else if ((rightPressed) && (!leftPressed)) {
						 ship.setHorizontalMovement(moveSpeed);
					 }
				 }
			 }

			 // if we're pressing fire, attempt to fire

			 if ((firePressed||(autoFire&&!mouseControls))&&!paused) {
				 tryToFire(false);
			 }

			 if ((epicPressed||(firePressed&&autoFire&&!mouseControls))&&!paused) {
				 tryToFire(true);
			 }

			 if(mouseControls&&!paused){
				 if(mousePressed){
					 tryToFire(true);
				 } else {
					 tryToFire(false);
				 }
			 }



			 // finally pause for a bit. Note: this should run us at about
			 // 100 fps but on windows this might vary each loop due to
			 // a bad implementation of timer
			 try { Thread.sleep(10); } catch (Exception e) {}
		 }

	 }


	 /**
	  * A class to handle keyboard input from the user. The class
	  * handles both dynamic input during game play, i.e. left/right 
	  * and shoot, and more static type input (i.e. press any key to
	  * continue)
	  * 
	  * This has been implemented as an inner class more through 
	  * habit then anything else. Its perfectly normal to implement
	  * this as separate class if slight less convenient.
	  * 
	  * @author Kevin Glass
	  */
	 private class MouseMoveHandler implements MouseMotionListener{

		 @Override
		 public void mouseDragged(MouseEvent e) {

			 if(mouseControls){
				 mouseX=e.getX();
				 mouseY=e.getY();
			 }
		 }

		 @Override
		 public void mouseMoved(MouseEvent e) {
			 if(mouseControls){
				 mouseX=e.getX();
				 mouseY=e.getY();
			 }
			 try{
				 if(buttons.get(0).isInside(e.getX(), e.getY())&&!buttons.get(0).hasMouseOver()){
					 buttons.get(0).changeMouseOver(true);
					 buttons.get(0).changeColors(Color.DARK_GRAY, Color.BLUE, Color.LIGHT_GRAY);
				 }
				 if(!buttons.get(0).isInside(e.getX(), e.getY())&&buttons.get(0).hasMouseOver()){
					 buttons.get(0).changeMouseOver(false);
					 buttons.get(0).changeColors(Color.DARK_GRAY,Color.LIGHT_GRAY,Color.BLACK);
				 }
				 if(buttons.get(1).isInside(e.getX(), e.getY())&&!buttons.get(1).hasMouseOver()){
					 buttons.get(1).changeMouseOver(true);
					 buttons.get(1).changeColors(Color.DARK_GRAY, Color.BLUE, Color.LIGHT_GRAY);
				 }
				 if(!buttons.get(1).isInside(e.getX(), e.getY())&&buttons.get(1).hasMouseOver()){
					 buttons.get(1).changeMouseOver(false);
					 buttons.get(1).changeColors(Color.DARK_GRAY,Color.LIGHT_GRAY,Color.BLACK);
				 }
			 } catch(NullPointerException ex){

			 }
		 }

	 }
	 private class MouseClickHandler implements MouseListener{
		 public void mouseClicked(MouseEvent e) {
			 if(paused){
				 if(buttons.get(0).isInside(e.getX(), e.getY())){
					 paused=false;
					 leftPressed = false;
					 rightPressed = false;
					 firePressed = false;
					 epicPressed=false;
					 buttons.get(0).changeAll(720, 40, 75, 15, "Pause(P)", Color.DARK_GRAY,Color.LIGHT_GRAY,Color.BLACK);
					 buttons.get(1).changeMouseOver(false);
					 buttons.get(1).changeActive(false);
				 }

			 } else {
				 if(buttons.get(0).isInside(e.getX(), e.getY())){
					 if(!waitingForKeyPress) {
						 paused=true;
						 buttons.get(0).changeAll(365, 350, 70, 15, "Unpause(P)", Color.DARK_GRAY,Color.LIGHT_GRAY,Color.BLACK);
						 buttons.get(1).changeMouseOver(false);
						 buttons.get(1).changeActive(true);
					 }

				 }
			 }
			 if(buttons.get(1).isInside(e.getX(), e.getY())){
				 if(!waitingForKeyPress) {
					 if(mouseControls&&buttons.get(1).isActive()){
						 mouseControls=false;
						 buttons.get(1).changeText("Mouse Controls");
					 } else {
						 mouseControls=true;
						 buttons.get(1).changeText("Key Controls");
					 }
				 }

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
			 if(mouseControls){
				 mousePressed=true;
			 }

		 }
		 @Override
		 public void mouseReleased(MouseEvent arg0) {
			 if(mouseControls){
				 mousePressed=false;
			 }

		 }
	 }

	 private class KeyInputHandler extends KeyAdapter {
		 /** The number of key presses we've had while waiting for an "any key" press */
		 private int pressCount = 1;

		 /**
		  * Notification from AWT that a key has been pressed. Note that
		  * a key being pressed is equal to being pushed down but *NOT*
		  * released. Thats where keyTyped() comes in.
		  *
		  * @param e The details of the key that was pressed 
		  */
		 @Override
		 public void keyPressed(KeyEvent e) {
			 // if we're waiting for an "any key" typed then we don't 
			 // want to do anything with just a "press"
			 if (waitingForKeyPress) {
				 return;
			 }


			 if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				 leftPressed = true;
			 }
			 if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				 rightPressed = true;
			 }
			 if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				 firePressed = true;
			 }
			 if (e.getKeyCode() == KeyEvent.VK_B) {
				 epicPressed = true;
			 }
			 if (e.getKeyCode() == KeyEvent.VK_F) {
				 if(autoFire){
					 autoFire = false;
				 } else {
					 autoFire = true;
				 }
			 }
			 if (e.getKeyCode() == KeyEvent.VK_P) {
				 if(!paused) {
					 if(!waitingForKeyPress) {
						 paused=true;
						 buttons.get(0).changeAll(365, 350, 70, 15, "Unpause(P)", Color.DARK_GRAY,Color.LIGHT_GRAY,Color.BLACK);
						 buttons.get(1).changeMouseOver(false);
						 buttons.get(1).changeActive(true);
					 }
				 } else {
					 paused=false;
					 buttons.get(0).changeAll(720, 40, 75, 15, "Pause(P)", Color.DARK_GRAY,Color.LIGHT_GRAY,Color.BLACK);
					 buttons.get(1).changeMouseOver(false);
					 buttons.get(1).changeActive(false);
				 }
				 leftPressed = false;
				 rightPressed = false;
				 firePressed = false;
			 }
		 } 

		 /**
		  * Notification from AWT that a key has been released.
		  *
		  * @param e The details of the key that was released 
		  */
		 @Override
		 public void keyReleased(KeyEvent e) {
			 // if we're waiting for an "any key" typed then we don't 
			 // want to do anything with just a "released"
			 if (waitingForKeyPress) {
				 return;
			 }

			 if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				 leftPressed = false;
			 }
			 if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				 rightPressed = false;
			 }
			 if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				 firePressed = false;
			 }
			 if (e.getKeyCode() == KeyEvent.VK_B) {
				 epicPressed = false;
			 }
		 }
		 /**
		  * Notification from AWT that a key has been typed. Note that
		  * typing a key means to both press and then release it.
		  *
		  * @param e The details of the key that was typed. 
		  */
		 @Override
		 public void keyTyped(KeyEvent e) {
			 // if we're waiting for a "any key" type then
			 // check if we've received any recently. We may
			 // have had a keyType() event from the user releasing
			 // the shoot or move keys, hence the use of the "pressCount"
			 // counter.
			 if (waitingForKeyPress) {
				 if (pressCount == 1) {
					 // since we've now received our key typed
					 // event we can mark it as such and start 
					 // our new game
					 waitingForKeyPress = false;
					 startGame();
					 pressCount = 0;
				 } else {
					 pressCount++;
				 }
			 }

			 // if we hit escape, then quit the game
			 if (e.getKeyChar() == 27) {
				 System.exit(0);
			 }
		 }
	 }

	 public void errorCatch() {
		 try {
			 gameLoop();
		 } catch(Exception e){
			 waitingForKeyPress=false;
			 startGame();
			 errorCatch();
		 }
	 }

	 /**
	  * The entry point into the game. We'll simply create an
	  * instance of class which will start the display and game
	  * loop.
	  * 
	  * @param argv The arguments that are passed into our game
	  */
	 public static void main(String args[]) {
		 Game g =new Game();

		 g.errorCatch();
	 }
}