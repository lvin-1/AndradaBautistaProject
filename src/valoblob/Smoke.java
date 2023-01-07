package valoblob;

import javafx.scene.image.Image;

public class Smoke extends Powerup {

	private final static Image FINAL_SMOKE = new Image("images/cloudburst-black.png",20,20,false,false);

	Smoke(int xPos, int yPos, int type){
		super(xPos, yPos, type, Smoke.FINAL_SMOKE);
	}

	void grantImmunity(){

	}
}
