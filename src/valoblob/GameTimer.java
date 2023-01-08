package valoblob;

import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class GameTimer extends AnimationTimer {
	private long startTime;
	private long startMove;
	//Possibly wrong data type
	private long timeAlive;
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
	private Image background = new Image("images/tryBG.png",2400,2400,false,false);

	public final static int GUN_COUNT = 50;
	public final static int INITIAL_NEON_COUNT = 10;
	//testing purposes
	public final static int INITIAL_AGENT_SPEED = 120/40;
	public final static int INITIAL_AGENT_SIZE = 40;
	public final static int POWERUP_SPAWN_DELAY = 10; //seconds
	public final static int MAP_SIZE = 2400;

	//For spawning powerups
	PauseTransition p = new PauseTransition(Duration.seconds(10));
	PauseTransition p1 = new PauseTransition(Duration.seconds(5));
	//PauseTransition p2 = new PauseTransition();

	Random r = new Random();

	GameTimer(Scene scene, GraphicsContext gc){
		this.gc = gc;
		this.scene = scene;
		this.jett = new Jett();
		this.neons = new ArrayList<Neon>();
		this.guns = new ArrayList<Gun>();
		this.powerups = new ArrayList<Powerup>();
		this.startTime = this.startMove = System.nanoTime();
		this.prepareActionHandlers();
	}


	@Override
	public void handle(long currentNanoTime){

		this.redrawBackgroundImage();

		this.spawnGuns();
		this.spawnNeons();

		//spawning powerup
		p.setOnFinished(event -> this.spawnPowerUp());
		p.play();

		//removing powerup
		if(!this.powerups.isEmpty()){
			p1.setOnFinished(event -> this.removePowerup());
			p1.play();
		}

		this.renderSprites();
		this.moveSprites(currentNanoTime);
		this.checkBlobIntersection();


		this.timeAlive = (currentNanoTime - this.startTime)/(1_000_000_000);
		//this.updateTimeAlive(currentNanoTime);
		this.drawGameStatus();

		if(this.jett.isAlive()){
			this.stop();
		}
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

		for(Powerup powerup : this.powerups){
			powerup.render(this.gc);
		}


		this.jett.render(this.gc);
	}

	void moveSprites(long currentNanoTime){
		this.moveJett();

		for(Gun gun : this.guns){
			gun.move();
		}

		for(Neon neon : this.neons){
			/*
			if(!neon.getMoving()){
				neon.moveRandomly();
			}*/
			//neon.moveRandomly();
			neon.moveRandomly(((currentNanoTime - this.startMove)/(1000000000))%4);

			neon.moveWithJett();
		}

		for(Powerup powerup : this.powerups){
			powerup.move();
		}

	}

	private void prepareActionHandlers(){

		this.scene.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            public void handle(KeyEvent e)
            {
                String code = e.getCode().toString();
                if(code.equals("LEFT")|| code.equals("A")) {
                	//System.out.println("Jett has moved left");
                	GameTimer.goLeft = true;
                }else if(code.equals("RIGHT")|| code.equals("D")) {
                	//System.out.println("Jett has moved right");
                	GameTimer.goRight = true;
                }else if(code.equals("UP")|| code.equals("W")){
                	//System.out.println("Jett has moved up");
                	GameTimer.goUp = true;
                }else if(code.equals("DOWN")|| code.equals("S")){
                	//System.out.println("Jett has moved down");
                	GameTimer.goDown = true;
                }

            }
        });

		this.scene.setOnKeyReleased(new EventHandler<KeyEvent>(){
			public void handle(KeyEvent e){
				String code = e.getCode().toString();
                if(code.equals("LEFT") || code.equals("A")) {
                	GameTimer.goLeft = false;
                }else if(code.equals("RIGHT")|| code.equals("D")) {
                	GameTimer.goRight = false;
                }else if(code.equals("UP")|| code.equals("W")){
                	GameTimer.goUp = false;
                }else if(code.equals("DOWN")|| code.equals("S")){
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

				for(Powerup powerup : this.powerups){
					powerup.setDX(this.jett.getSpeed());
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

				for(Powerup powerup : this.powerups){
					powerup.setDX(-this.jett.getSpeed());
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

				for(Powerup powerup : this.powerups){
					powerup.setDY(this.jett.getSpeed());
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

				for(Powerup powerup : this.powerups){
					powerup.setDY(-this.jett.getSpeed());
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

			for(Powerup powerup : this.powerups){
				powerup.setDX(0);
				powerup.setDY(0);
			}
		}

		this.jett.move();
	}


	private void checkBlobIntersection(){
		for(int i = 0; i < GameTimer.GUN_COUNT; i++){
			Gun gun = this.guns.get(i);
			if(this.jett.intersectsWith(gun)){
				this.jett.increaseSize(Agent.FOOD_SIZE_INCREASE);
				this.jett.loadImage(new Image("images/jett circle.png",this.jett.size,this.jett.size,false,false));


				//GUN RESPAWN to another location in the map
				Random r = new Random();
				int rX = r.nextInt(2);
				int rY = r.nextInt(2);

				if (rX == 1 && rY == 1){
					gun.xPosSetter(r.nextInt(GameTimer.MAP_SIZE));
					gun.yPosSetter(r.nextInt(GameTimer.MAP_SIZE));
				}else if (rX == 1 && rY == 0){
					gun.xPosSetter(r.nextInt(GameTimer.MAP_SIZE));
					gun.yPosSetter(0-r.nextInt(GameTimer.MAP_SIZE));
				}else if (rX == 0 && rY == 1){
					gun.xPosSetter(0-r.nextInt(GameTimer.MAP_SIZE));
					gun.yPosSetter(r.nextInt(GameTimer.MAP_SIZE));
				}
				else{
					gun.xPosSetter(0-r.nextInt(GameTimer.MAP_SIZE));
					gun.yPosSetter(0-r.nextInt(GameTimer.MAP_SIZE));
				}
				gun.render(this.gc);
				this.jett.increaseGunsCollected();

//				gun.xPosSetter(r.nextInt(GameTimer.MAP_SIZE));
//				gun.yPosSetter(r.nextInt(GameTimer.MAP_SIZE));
//				gun.render(this.gc);
			}
		}

		for(int j = 0; j < this.neons.size(); j++){
			Neon neon = this.neons.get(j);

			if(this.jett.size > neon.size && this.jett.intersectsWith(neon)){
				this.jett.increaseSize(neon.size);
				this.jett.loadImage(new Image("images/jett circle.png",this.jett.size,this.jett.size,false,false));
				this.neons.remove(j);
				this.jett.increaseEnemiesDefetead();
			}else{
				this.jett.die();
			}
		}


		for(int k = 0; k < this.powerups.size(); k++){
			Powerup powerup = this.powerups.get(k);

			PauseTransition duration = new PauseTransition(Duration.seconds(5));

			if(this.jett.intersectsWith(powerup)){
				if(powerup.type == Powerup.IMMUNITY){
					System.out.println("Jett used cloudburst and is currently immune.");
					this.jett.loadImage(new Image("images/cloudburst-black.png",this.jett.size,this.jett.size,false,false));
					this.jett.immunitySet(true);
					duration.setOnFinished(new EventHandler<ActionEvent>(){
						public void handle(ActionEvent arg0){
							jett.loadImage(new Image ("images/Valorant-Jett.png",jett.size,jett.size,false,false));
							jett.immunitySet(false);
						}
					});
					duration.play();
				}else{
					System.out.println("Jett used tailwind and currently has doubled speed.");
					this.jett.loadImage(new Image("images/tailwind-black.png",this.jett.size,this.jett.size,false,false));
					this.jett.speedDoubleSet(true);
					duration.setOnFinished(new EventHandler<ActionEvent>(){
						public void handle(ActionEvent arg0){
							jett.loadImage(new Image ("images/Valorant-Jett.png",jett.size,jett.size,false,false));
							jett.speedDoubleSet(false);
						}
					});
					duration.play();
				}

				this.powerups.clear();

			}
		}
	}


	private void spawnGuns(){
		if(!this.spawnedGuns){
			int xPos, yPos, xRNP, yRNP, xNP, yNP;
			Random r = new Random();

			for(int i = 0; i<GameTimer.GUN_COUNT; i++){

				xRNP = r.nextInt(2);
				yRNP = r.nextInt(2);

				if(xRNP == 0){
					xNP = 1;
				}else{
					xNP = -1;
				}

				if(yRNP == 0){
					yNP = 1;
				}else{
					yNP = -1;
				}

				xPos = r.nextInt(GameTimer.MAP_SIZE) * xNP;
				yPos = r.nextInt(GameTimer.MAP_SIZE) * yNP;
				this.guns.add(new Gun(xPos,yPos));
			}
			this.spawnedGuns = true;
		}

	}

	private void spawnNeons(){

		if(!this.spawnedNeons){
			int xPos, yPos, xRNP, yRNP, xNP, yNP;
			Random r = new Random();

			for(int i = 0; i<GameTimer.INITIAL_NEON_COUNT; i++){

				xRNP = r.nextInt(2);
				yRNP = r.nextInt(2);

				if(xRNP == 0){
					xNP = 1;
				}else{
					xNP = -1;
				}

				if(yRNP == 0){
					yNP = 1;
				}else{
					yNP = -1;
				}


				xPos = r.nextInt(Game.WINDOW_WIDTH) * xNP;
				yPos = r.nextInt(Game.WINDOW_HEIGHT) * yNP;
				this.neons.add(new Neon(xPos,yPos));
			}
			this.spawnedNeons = true;
		}
	}

	private void spawnPowerUp(){

		Random r = new Random();
		int powerupType = r.nextInt(2);
		int powerupXPos = r.nextInt(Game.WINDOW_WIDTH);
		int powerupYPos = r.nextInt(Game.WINDOW_HEIGHT);

		if(powerupType == Powerup.IMMUNITY){
			powerups.add(new Smoke(powerupXPos,powerupYPos,powerupType));
		}else{
			powerups.add(new Dash(powerupXPos,powerupYPos,powerupType));
		}

	}

	private void removePowerup(){
		this.powerups.clear();
	}

	private void drawGameStatus(){
		this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		this.gc.setFill(Color.RED);
		this.gc.fillText("Foods Eaten:", 20, 30);
		this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
		this.gc.setFill(Color.BLACK);
		this.gc.fillText(this.jett.getGunsCollected()+"", 170, 35);

		this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		this.gc.setFill(Color.RED);
		this.gc.fillText("Enemies Defeated:", 250, 30);
		this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
		this.gc.setFill(Color.BLACK);
		this.gc.fillText(this.jett.getEnemiesDefeated()+"", 470, 35);

		this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		this.gc.setFill(Color.RED);
		this.gc.fillText("Current Size:", 550, 30);
		this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
		this.gc.setFill(Color.BLACK);
		this.gc.fillText(this.jett.size+"", 700, 35);

		this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		this.gc.setFill(Color.RED);
		this.gc.fillText("Time Alive:", 20, 60);
		this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
		this.gc.setFill(Color.BLACK);
		this.gc.fillText(this.timeAlive+"", 160, 60);

	}

}
