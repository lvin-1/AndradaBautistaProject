package valoblob;

import javafx.scene.image.Image;
import java.util.Random;

public class Gun extends Sprite{

	int randX;
	int randY;
	private final static Image FINAL_GUN = new Image("images/vandal.png",20,20,false,false);
	public final static int FINAL_GUN_SIZE = 20;

	Gun(int xPos, int yPos){
		super(xPos,yPos,Gun.FINAL_GUN);

	}

	void move(){

		this.xPos += this.dx;
		this.yPos += this.dy;
	}

	public void xPosSetter(int x){
		this.xPos = x;
	}

	public void yPosSetter(int y){
		this.yPos = y;
	}
}
