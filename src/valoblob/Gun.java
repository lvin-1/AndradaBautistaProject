package valoblob;

import javafx.scene.image.Image;

public class Gun extends Sprite{
	private final static Image FINAL_GUN = new Image("images/vandal.png");
	Gun(){
		super(0,0,Gun.FINAL_GUN);
	}
}
