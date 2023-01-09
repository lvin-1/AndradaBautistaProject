package valoblob;

import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.paint.Color;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
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

	private Group root; //game over
	private Stage stage;
	private Canvas canvas;
	private Scene gameOverScene;

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
	private Image background = new Image("images/finalBG.png",GameTimer.MAP_SIZE,GameTimer.MAP_SIZE,false,false);

	public final static int GUN_COUNT = 50;
	public final static int INITIAL_NEON_COUNT = 10;
	public final static int INITIAL_AGENT_SIZE = 40;
	public final static int POWERUP_SPAWN_DELAY = 10; //seconds
	public final static int GAME_BOUNDS = 1600;
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

		this.root = new Group();
		this.gameOverScene = new Scene (root);
		this.canvas = new Canvas (Game.WINDOW_HEIGHT,Game.WINDOW_WIDTH);
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

		if(!this.jett.isAlive()){
			this.stop();
			this.drawGameOver();
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
		Random r = new Random();

		for(Gun gun : this.guns){
			gun.move();
		}

		int i = r.nextInt(this.neons.size());
		if(((currentNanoTime - this.startMove)/1000000000)%(r.nextInt(10)+1) == 0){

			this.neons.get(i).moveRandomly();
			this.neons.get(i).move();
		}else{
			this.neons.get(i).setDX(0);
			this.neons.get(i).setDY(0);
		}

		for(Neon neon : this.neons){
			//neon.move();
			neon.moveWithJett();
		}


		for(Powerup powerup : this.powerups){
			powerup.move();
		}

		this.moveJett();

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

			if(this.backgroundX < -5 && this.jett.xPos < Jett.INITIAL_MAIN_POSITION){

				if(this.jett.xPos > 5){
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
				}


			}else{
					this.jett.setDX(-this.jett.getSpeed());
			}

		}else if(GameTimer.goRight){

			if(this.backgroundX > -1595 && this.jett.xPos > Jett.INITIAL_MAIN_POSITION){
				if(this.jett.xPos < 760){

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
				}

			}else{
				this.jett.setDX(this.jett.getSpeed());
			}
		}else if(GameTimer.goUp){

			if(this.backgroundY < -5 && this.jett.yPos < Jett.INITIAL_MAIN_POSITION){

				if(this.jett.yPos > 5){

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
				}

			}else{
				this.jett.setDY(-this.jett.getSpeed());
			}
		}else if(GameTimer.goDown){

			if(this.backgroundY > -1595 && this.jett.yPos > Jett.INITIAL_MAIN_POSITION){

				if(this.jett.yPos < 760){
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
				}

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

			/*
			for(Neon neon : this.neons){
				neon.setDX(0);
				neon.setDY(0);
			}*/

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

				if(this.jett.isImmune()){
					this.jett.loadImage(new Image("images/Immune.png",this.jett.size,this.jett.size,false,false));
				}else if(this.jett.speedDoubled()){
					this.jett.loadImage(new Image("images/Speed.png",this.jett.size,this.jett.size,false,false));
				}else{
					this.jett.loadImage(new Image("images/jett circle.png",this.jett.size,this.jett.size,false,false));
				}

				gun.respawnToOtherLoc();
				gun.render(this.gc);
				this.jett.increaseGunsCollected();
			}

			for(int j = 0; j < this.neons.size(); j++){
				Neon neon = this.neons.get(j);
				if(neon.intersectsWith(gun)){
					neon.increaseSize(Agent.FOOD_SIZE_INCREASE);
					neon.loadImage(new Image("images/neon circle.png",neon.size,neon.size,false,false));
					System.out.println("Neon has eaten a food");
					gun.respawnToOtherLoc();
					gun.render(this.gc);
				}
			}
		}

		for(int j = 0; j < this.neons.size(); j++){
			Neon neon = this.neons.get(j);
			if(this.jett.size > neon.size && this.jett.intersectsWith(neon)){
				this.jett.increaseSize(neon.size);
				if(this.jett.isImmune()){
					this.jett.loadImage(new Image("images/Immune.png",this.jett.size,this.jett.size,false,false));
				}else if(this.jett.speedDoubled()){
					this.jett.loadImage(new Image("images/Speed.png",this.jett.size,this.jett.size,false,false));
				}else{
					this.jett.loadImage(new Image("images/jett circle.png",this.jett.size,this.jett.size,false,false));
				}
				this.neons.remove(j);
				this.jett.increaseEnemiesDefetead();
			}else if (this.jett.size < neon.size && this.jett.intersectsWith(neon)){
				if(!this.jett.isImmune()){
					this.jett.die();
				}
			}

			for(int k = 0; k < this.neons.size(); k++){
				Neon neon2 = this.neons.get(k);
				if(j != k && neon.intersectsWith(neon2)){
					neon.increaseSize(neon2.size);
					neon.loadImage(new Image("images/neon circle.png",neon.size,neon.size,false,false));
					System.out.println("Neon has eaten another neon");
					this.neons.remove(k);
				}
			}
		}

		for(int k = 0; k < this.powerups.size(); k++){
			Powerup powerup = this.powerups.get(k);

			PauseTransition duration = new PauseTransition(Duration.seconds(5));

			if(this.jett.intersectsWith(powerup)){
				powerup.jettSetter(this.jett);
				if(powerup.type == Powerup.IMMUNITY){
					this.jett.loadImage(new Image("images/Immune.png",this.jett.size,this.jett.size,false,false));
					powerup.grantPower(powerup.type);
					duration.setOnFinished(new EventHandler<ActionEvent>(){
						public void handle(ActionEvent arg0){
							jett.loadImage(new Image ("images/jett circle.png",jett.size,jett.size,false,false));
							powerup.removePower();
						}
					});
					duration.play();
				}else{
					this.jett.loadImage(new Image("images/Speed.png",this.jett.size,this.jett.size,false,false));
					powerup.grantPower(powerup.type);
					duration.setOnFinished(new EventHandler<ActionEvent>(){
						public void handle(ActionEvent arg0){
							jett.loadImage(new Image ("images/jett circle.png",jett.size,jett.size,false,false));
							powerup.removePower();
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
			int xPos, yPos, rX, rY;
			Random r = new Random();

			for(int i = 0; i<GameTimer.GUN_COUNT; i++){

				rX = r.nextInt(2);
				rY = r.nextInt(2);

				if (rX == 1 && rY == 1){
					xPos = (r.nextInt(1000));
					yPos = (r.nextInt(1000));
				}else if (rX == 1 && rY == 0){
					xPos = (r.nextInt(1000));
					yPos = (0-r.nextInt(1000));
				}else if (rX == 0 && rY == 1){
					xPos = (0-r.nextInt(1000));
					yPos = (r.nextInt(1000));
				}
				else{
					xPos = (0-r.nextInt(1000));
					yPos = (0-r.nextInt(1000));
				}

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


				xPos = r.nextInt(1000) * xNP;
				yPos = r.nextInt(1000) * yNP;
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

	private void drawGameOver(){
		PauseTransition delay = new PauseTransition(Duration.seconds(1));

		delay.setOnFinished(new EventHandler<ActionEvent>(){

			public void handle(ActionEvent arg0){
				Image gameOver = new Image("images/Game Over.png",Game.WINDOW_WIDTH,Game.WINDOW_HEIGHT,false,false);
				gc.clearRect(0, 0, Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);
				gc.drawImage(gameOver, 0, 0);

				//Change layout and fonts Not visible but will use the same variables as the drawGameStatus
				gc.setFont(Font.font("Impact", FontWeight.BOLD, 20));
				gc.setFill(Color.RED);
				gc.fillText("Foods Eaten:", 20, 30);
				gc.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
				gc.setFill(Color.WHITE);
				gc.fillText(jett.getGunsCollected()+"", 170, 30);

				gc.setFont(Font.font("Impact", FontWeight.BOLD, 20));
				gc.setFill(Color.RED);
				gc.fillText("Enemies Defeated:", 20, 60);
				gc.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
				gc.setFill(Color.WHITE);
				gc.fillText(jett.getEnemiesDefeated()+"", 230, 60);

				gc.setFont(Font.font("Impact", FontWeight.BOLD, 20));
				gc.setFill(Color.RED);
				gc.fillText("Size:", 20, 90);
				gc.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
				gc.setFill(Color.WHITE);
				gc.fillText(jett.size+"", 80, 95);

				gc.setFont(Font.font("Impact", FontWeight.BOLD, 20));
				gc.setFill(Color.RED);
				gc.fillText("Time Alive:", 20, 120);
				gc.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
				gc.setFill(Color.WHITE);
				gc.fillText(timeAlive+" seconds", 150, 125);
			}
		});

		delay.play();


	}
	private void drawGameStatus(){
		this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		this.gc.setFill(Color.RED);
		this.gc.fillText("Foods Eaten:", 20, 30);
		this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
		this.gc.setFill(Color.WHITE);
		this.gc.fillText(this.jett.getGunsCollected()+"", 170, 35);

		this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		this.gc.setFill(Color.RED);
		this.gc.fillText("Enemies Defeated:", 250, 30);
		this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
		this.gc.setFill(Color.WHITE);
		this.gc.fillText(this.jett.getEnemiesDefeated()+"", 470, 35);

		this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		this.gc.setFill(Color.RED);
		this.gc.fillText("Current Size:", 550, 30);
		this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
		this.gc.setFill(Color.WHITE);
		this.gc.fillText(this.jett.size+"", 700, 35);

		this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		this.gc.setFill(Color.RED);
		this.gc.fillText("Time Alive:", 20, 60);
		this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
		this.gc.setFill(Color.WHITE);
		this.gc.fillText(this.timeAlive+"", 160, 60);

	}

}
