package valoblob;

import javafx.scene.image.Image;
import java.util.Random;

public class Gun extends Sprite{

	int randX;
	int randY;
	private final static Image FINAL_GUN = new Image("images/vandal.png",20,20,false,false);
	public final static int FINAL_GUN_SIZE = 20;

	private final static int LEFT = 1;
	private final static int RIGHT = 2;
	private final static int UP = 3;
	private final static int DOWN = 4;

	Gun(int xPos, int yPos){
		super(xPos,yPos,Gun.FINAL_GUN);

	}

	void move(){

		this.xPos += this.dx;
		this.yPos += this.dy;
	}

	public void randomMovement(){
		Random r = new Random();
		int direction = r.nextInt(4)+1;
		if(direction == Gun.LEFT){
			this.setDX(r.nextInt(20)+1);
		}else if(direction == Gun.RIGHT){
			this.setDX(-r.nextInt(20)+1);
		}else if(direction == Gun.UP){
			this.setDY(-r.nextInt(20)+1);
		}else{
			this.setDY(r.nextInt(20)+1);
		}
	}
	public void xPosSetter(int x){
		this.xPos = x;
	}

	public void yPosSetter(int y){
		this.yPos = y;
	}
}
