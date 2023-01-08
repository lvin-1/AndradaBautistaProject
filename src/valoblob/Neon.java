package valoblob;

import java.util.Random;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.util.Duration;

public class Neon extends Agent{
	private final static Image FINAL_NEON_IMAGE = new Image("Images/neon.png",40,40,false,false);
	private final static int LEFT = 1;
	private final static int RIGHT = 2;
	private final static int UP = 3;
	private final static int DOWN = 4;
	private boolean moving = false;



	Neon(int randX, int randY){
		super(randX, randY, Neon.FINAL_NEON_IMAGE);
	}


	void moveRandomly(long direction){

		this.setMoving(true);
		//this.setDX(this.getSpeed());

		/*
		Random r = new Random();
		int duration = r.nextInt(20)+1;
		int direction = r.nextInt(4)+1;
	    */
		if(direction+1 == Neon.LEFT){
			this.setDX(-this.getSpeed());
		}else if(direction+1 == Neon.RIGHT){
			this.setDX(this.getSpeed());
		}else if(direction+1 == Neon.UP){
			this.setDY(-this.getSpeed());
		}else{
			this.setDY(this.getSpeed());
		}

		this.move();



	}

	@Override
	void move(){
		if(this.xPos+this.dx >= GameTimer.MAP_SIZE && this.xPos+this.dx <= GameTimer.MAP_SIZE-this.size){
			this.xPos += this.dx;
		}

		if(this.yPos+this.dy >= 0 && this.yPos+this.dy <= Game.WINDOW_HEIGHT-this.size){
			this.yPos += this.dy;
		}
	}

	void moveWithJett(){
		this.xPos += this.dx;
		this.yPos += this.dy;
	}

	boolean getMoving(){
		return this.moving;
	}

	void setMoving(boolean value){
		this.moving = value;
	}

}
