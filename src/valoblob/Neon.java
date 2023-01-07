package valoblob;

import javafx.scene.image.Image;

public class Neon extends Agent {
	private final static Image FINAL_NEON_IMAGE = new Image("Images/neon.png",40,40,false,false);

	Neon(int randX, int randY){
		super(randX, randY, Neon.FINAL_NEON_IMAGE);
	}


	void moveWithJett(){
		this.xPos += this.dx;
		this.yPos += this.dy;
	}
}
