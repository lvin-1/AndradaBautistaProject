package valoblob;

import javafx.scene.image.Image;

public class Jett extends Agent{
	private final static Image FINAL_JETT_IMAGE = new Image("images/Valorant-Jett.png",40,40,false,false);

	Jett(){
		super(Agent.INITIAL_MAIN_POSITION, Agent.INITIAL_MAIN_POSITION, Jett.FINAL_JETT_IMAGE);
	}
}
