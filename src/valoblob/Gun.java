package valoblob;

import javafx.scene.image.Image;
import java.util.Random;

public class Gun extends Sprite{

	int randX;
	int randY;
	private final static Image FINAL_GUN = new Image("images/vandal.png",Gun.FINAL_GUN_SIZE,Gun.FINAL_GUN_SIZE,false,false);
	public final static int FINAL_GUN_SIZE = 20;


	Gun(int xPos, int yPos){
		super(xPos,yPos,Gun.FINAL_GUN);

	}

	void move(){

		this.xPos += this.dx;
		this.yPos += this.dy;
	}

	void respawnToOtherLoc(){
		Random r = new Random();
		int rX = r.nextInt(2);
		int rY = r.nextInt(2);

		if (rX == 1 && rY == 1){
			this.xPosSetter(r.nextInt(1000));
			this.yPosSetter(r.nextInt(1000));
		}else if (rX == 1 && rY == 0){
			this.xPosSetter(r.nextInt(1000));
			this.yPosSetter(0-r.nextInt(1000));
		}else if (rX == 0 && rY == 1){
			this.xPosSetter(0-r.nextInt(1000));
			this.yPosSetter(r.nextInt(1000));
		}
		else{
			this.xPosSetter(0-r.nextInt(1000));
			this.yPosSetter(0-r.nextInt(1000));
		}
	}

	public void xPosSetter(int x){
		this.xPos = x;
	}

	public void yPosSetter(int y){
		this.yPos = y;
	}
}
