package valoblob;

import javafx.scene.image.Image;

public class Jett extends Agent{
	int gunsCollected;
	int enemiesDefeted;
	boolean speedDoubled;
	boolean hasImmunity;
	public final static Image FINAL_JETT_IMAGE = new Image("images/Valorant-Jett.png",40,40,false,false);
	public final static int INITIAL_MAIN_POSITION = 400;

	Jett(){
		super(Jett.INITIAL_MAIN_POSITION, Jett.INITIAL_MAIN_POSITION, Jett.FINAL_JETT_IMAGE);
	}

	void increaseGunsCollected(){
		this.gunsCollected += 1;
	}

	void increaseEnemiesDefetead(){
		this.enemiesDefeted += 1;
	}

	int getGunsCollected(){
		return this.gunsCollected;
	}

	int getEnemiesDefeated(){
		return this.enemiesDefeted;
	}
}
