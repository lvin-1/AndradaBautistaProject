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
	private boolean spawnedGuns = false;
	private boolean spawnedNeons = false;
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
		this.spawnNeons();
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

		for(Neon neon : this.neons){
			neon.render(this.gc);
		}

		this.jett.render(this.gc);
	}

	void moveSprites(){
		this.moveJett();

		for(Gun gun : this.guns){
			gun.move();
		}

		for(Neon neon : this.neons){
			//insert random movement here
			neon.moveWithJett();
		}
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

				for(Gun gun: this.guns){
					gun.setDX(this.jett.getSpeed());
				}

				for(Neon neon : this.neons){
					neon.setDX(this.jett.getSpeed());
				}

				this.backgroundX += this.jett.getSpeed();

			}else{
					this.jett.setDX(-this.jett.getSpeed());
			}

		}else if(GameTimer.goRight){

			if(this.backgroundX > -1595){

				for(Gun gun: this.guns){
					gun.setDX(-this.jett.getSpeed());
				}

				for(Neon neon : this.neons){
					neon.setDX(-this.jett.getSpeed());
				}

				this.backgroundX -= this.jett.getSpeed();
			}else{
				this.jett.setDX(this.jett.getSpeed());
			}
		}else if(GameTimer.goUp){

			if(this.backgroundY < -5){

				for(Gun gun: this.guns){
					gun.setDY(this.jett.getSpeed());
				}

				for(Neon neon : this.neons){
					neon.setDY(this.jett.getSpeed());
				}

				this.backgroundY += this.jett.getSpeed();
			}else{
				this.jett.setDY(-this.jett.getSpeed());
			}
		}else if(GameTimer.goDown){

			if(this.backgroundY > -1595){

				for(Gun gun: this.guns){
					gun.setDY(-this.jett.getSpeed());
				}

				for(Neon neon : this.neons){
					neon.setDY(-this.jett.getSpeed());
				}

				this.backgroundY -= this.jett.getSpeed();
			}else{
				this.jett.setDY(this.jett.getSpeed());
			}

		}else{

			this.jett.setDX(0);
			this.jett.setDY(0);

			for(Gun gun: this.guns){
				gun.setDX(0);
				gun.setDY(0);
			}

			for(Neon neon : this.neons){
				neon.setDX(0);
				neon.setDY(0);
			}
		}

		this.jett.move();
	}


	private void checkBlobIntersection(){
		for(int i = 0; i < GameTimer.GUN_COUNT; i++){
			Gun gun = this.guns.get(i);
			if(this.jett.intersectsWith(gun)){
				this.jett.increaseSize(gun.size);
				this.jett.loadImage(new Image("images/Valorant-Jett.png",this.jett.size,this.jett.size,false,false));


				//GUN RESPAWN to another location in the map
				Random r = new Random();
				gun.xPosSetter(r.nextInt(GameTimer.MAP_SIZE));
				gun.yPosSetter(r.nextInt(GameTimer.MAP_SIZE));
				gun.render(this.gc);
			}
		}

		for(int j = 0; j < this.neons.size(); j++){
			Neon neon = this.neons.get(j);

			if(this.jett.size > neon.size && this.jett.intersectsWith(neon)){
				this.jett.increaseSize(neon.size);
				this.jett.loadImage(new Image("images/Valorant-Jett.png",this.jett.size,this.jett.size,false,false));
				this.neons.remove(j);
			}
		}

	}

	private void spawnGuns(){
		if(!this.spawnedGuns){
			int xPos, yPos;
			Random r = new Random();

			for(int i = 0; i<GameTimer.GUN_COUNT; i++){
				xPos = r.nextInt(GameTimer.MAP_SIZE);
				yPos = r.nextInt(GameTimer.MAP_SIZE);
				this.guns.add(new Gun(xPos,yPos));
			}
			this.spawnedGuns = true;
		}


	}

	private void spawnNeons(){

		if(!this.spawnedNeons){
			int xPos, yPos;
			Random r = new Random();

			for(int i = 0; i<GameTimer.INITIAL_NEON_COUNT; i++){
				xPos = r.nextInt(GameTimer.MAP_SIZE);
				yPos = r.nextInt(GameTimer.MAP_SIZE);
				this.neons.add(new Neon(xPos,yPos));
			}
			this.spawnedNeons = true;
		}
	}


}
