package valoblob;

import javafx.scene.image.Image;

public class Powerup extends Sprite {

	private final static Image FINAL_POWERUP = new Image("images/tailwind.png");

	Powerup(){
		super(0,0,Powerup.FINAL_POWERUP);
	}
}
