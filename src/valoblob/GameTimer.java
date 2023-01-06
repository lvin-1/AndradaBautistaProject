package valoblob;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;
import java.util.Random;

public class GameTimer extends AnimationTimer {
	private long startSpawn;
	private long startMove;
	private boolean spawned = false;
	private GraphicsContext gc;
	private Scene scene;
	private Jett jett;
	private ArrayList<Neon> neons;
	private ArrayList<Gun> guns;
	private ArrayList<Powerup> powerups;
	private static boolean goLeft;
	private static boolean goRight;
	private static boolean goUp;
	private static boolean goDown;
	private double backgroundX = -800;
	private double backgroundY = -800;
	private Image background = new Image("images/tryBG.png",2400,2400,false,true);

	public final static int GUN_COUNT = 50;
	public final static int INITIAL_NEON_COUNT = 10;
	//testing purposes
	public final static int INITIAL_AGENT_SPEED = 120/40;
	public final static int INITIAL_AGENT_SIZE = 40;
	public final static int POWERUP_SPAWN_DELAY = 10; //seconds
	public final static int MAP_SIZE = 2400;

	GameTimer(Scene scene, GraphicsContext gc){
		this.gc = gc;
		this.scene = scene;
		this.jett = new Jett();
		this.neons = new ArrayList<Neon>();
		this.guns = new ArrayList<Gun>();
		this.powerups = new ArrayList<Powerup>();
		this.prepareActionHandlers();
	}


	@Override
	public void handle(long currentNanoTime){
		this.redrawBackgroundImage();

		this.spawnGuns();
		this.renderSprites();
		this.moveSprites();
		this.checkBlobIntersection();
	}

	void redrawBackgroundImage(){
		this.gc.clearRect(0, 0, Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);
		this.gc.drawImage(background, this.backgroundX, this.backgroundY);

	}

	void renderSprites(){

		for(Gun gun : this.guns){
			gun.render(this.gc);
		}

		this.jett.render(this.gc);
	}

	void moveSprites(){
		this.moveJett();
	}

	private void prepareActionHandlers(){

		this.scene.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            public void handle(KeyEvent e)
            {
                String code = e.getCode().toString();
                if(code.equals("LEFT")) {
                	GameTimer.goLeft = true;
                }else if(code.equals("RIGHT")) {
                	GameTimer.goRight = true;
                }else if(code.equals("UP")){
                	GameTimer.goUp = true;
                }else if(code.equals("DOWN")){
                	GameTimer.goDown = true;
                }

            }
        });

		this.scene.setOnKeyReleased(new EventHandler<KeyEvent>(){
			public void handle(KeyEvent e){
				String code = e.getCode().toString();
                if(code.equals("LEFT")) {
                	GameTimer.goLeft = false;
                }else if(code.equals("RIGHT")) {
                	GameTimer.goRight = false;
                }else if(code.equals("UP")){
                	GameTimer.goUp = false;
                }else if(code.equals("DOWN")){
                	GameTimer.goDown = false;
                }
			}
		});
	}



	private void moveJett(){
		// -5 and -1595, bounds for the blob to stay in the map
		if(GameTimer.goLeft){

			if(this.backgroundX < -5){
				this.backgroundX += GameTimer.INITIAL_AGENT_SPEED;
			}else{
				this.jett.setDX(-GameTimer.INITIAL_AGENT_SPEED);
			}
		}else if(GameTimer.goRight){

			if(this.backgroundX > -1595){
				this.backgroundX -= GameTimer.INITIAL_AGENT_SPEED;
			}else{
				this.jett.setDX(GameTimer.INITIAL_AGENT_SPEED);
			}
		}else if(GameTimer.goUp){

			if(this.backgroundY < -5){
				this.backgroundY += GameTimer.INITIAL_AGENT_SPEED;
			}else{
				this.jett.setDY(-GameTimer.INITIAL_AGENT_SPEED);
			}
		}else if(GameTimer.goDown){

			if(this.backgroundY > -1595){
				this.backgroundY -= GameTimer.INITIAL_AGENT_SPEED;
			}else{
				this.jett.setDY(GameTimer.INITIAL_AGENT_SPEED);
			}
		}else{
			this.jett.setDX(0);
			this.jett.setDY(0);
		}

		//System.out.println(this.backgroundX +" "+ this.backgroundY);
		this.jett.move();
	}


	private void checkBlobIntersection(){
		for(int i = 0; i < GameTimer.GUN_COUNT; i++){
			Gun gun = this.guns.get(i);
			if(gun.intersectsWith(this.jett)){
				//hardcoded size increase for testing
				//this.jett.increaseSize(10);
				//System.out.println("Jett got a gun!");
				//this.guns.remove(i);

				//GUN RESPAWN to another location in the map
				Random r = new Random();
				gun.xPosSetter(r.nextInt(Game.WINDOW_WIDTH));
				gun.yPosSetter(r.nextInt(Game.WINDOW_HEIGHT));
				gun.render(this.gc);

				//SIZE INCREASE not working
				//this.jett.increaseSize(gun.size);
				//this.jett.render(this.gc);

			}
		}
	}

	private void spawnGuns(){
		if(!this.spawned){
			int xPos, yPos;
			Random r = new Random();

			for(int i = 0; i<GameTimer.GUN_COUNT; i++){
				xPos = r.nextInt(Game.WINDOW_WIDTH);
				yPos = r.nextInt(Game.WINDOW_HEIGHT);
				this.guns.add(new Gun(xPos,yPos));
			}
			this.spawned = true;
		}


	}
}
