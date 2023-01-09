package valoblob;

import javafx.scene.image.Image;

public class Dash extends Powerup{

	private final static Image FINAL_DASH = new Image("images/tailwind-black.png",20,20,false,false);

	Dash(int xPos, int yPos, int type){
		super(xPos,yPos,type,Dash.FINAL_DASH);
	}

	void doubleSpeed(){
		this.grantPower(this.type);
	}
}
