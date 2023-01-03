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
	private Image background = new Image("images/bg2.png",2400,2400,false,false);

	public final static int FOOD_COUNT = 50;
	public final static int INITIAL_NEON_COUNT = 10;
	public final static int INITIAL_AGENT_SPEED = 120/40;
	public final static int INITIAL_AGENT_SIZE = 40;
	public final static int POWERUP_SPAWN_DELAY = 10; //seconds


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

		this.renderSprites();
		this.moveSprites();
	}

	void redrawBackgroundImage(){
		this.gc.clearRect(0, 0, Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);
		this.gc.drawImage(background, this.backgroundX, this.backgroundY);
	}

	void renderSprites(){
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
		if(GameTimer.goLeft){
			this.jett.setDX(-GameTimer.INITIAL_AGENT_SPEED);
			this.backgroundX += GameTimer.INITIAL_AGENT_SPEED;
		}else if(GameTimer.goRight){
			this.jett.setDX(GameTimer.INITIAL_AGENT_SPEED);
			this.backgroundX -= GameTimer.INITIAL_AGENT_SPEED;
		}else if(GameTimer.goUp){
			this.jett.setDY(-GameTimer.INITIAL_AGENT_SPEED);
			this.backgroundY += GameTimer.INITIAL_AGENT_SPEED;
		}else if(GameTimer.goDown){
			this.jett.setDY(GameTimer.INITIAL_AGENT_SPEED);
			this.backgroundY -= GameTimer.INITIAL_AGENT_SPEED;
		}else{
			this.jett.setDX(0);
			this.jett.setDY(0);
		}

		this.jett.move();
	}
}
