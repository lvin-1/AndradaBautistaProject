package valoblob;

import javafx.scene.image.Image;

public class Powerup extends Sprite {

	int type;
	public final static int SPEED_BOOST = 1;
	public final static int IMMUNITY = 0;
	public final static int FINAL_POWERUP_SIZE = 20;

	Powerup(int xPos, int yPos, int type, Image image){
		super(xPos,yPos,image);
		this.type = type;
	}

	void move(){
		this.xPos += this.dx;
		this.yPos += this.dy;
	}
}
