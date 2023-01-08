package valoblob;

import javafx.scene.image.Image;

public class Jett extends Agent{

	private int gunsCollected;
	private int enemiesDefeted;
	private boolean speedDoubled;
	private boolean hasImmunity;
	private boolean alive;
	public final static Image FINAL_JETT_IMAGE = new Image("images/jett circle.png",40,40,false,false);
	public final static int INITIAL_MAIN_POSITION = 400;

	Jett(){
		super(Jett.INITIAL_MAIN_POSITION, Jett.INITIAL_MAIN_POSITION, Jett.FINAL_JETT_IMAGE);
	}

	@Override
	double getSpeed(){
		if(this.speedDoubled){
			return (120/this.size)*2;
		}else{
			return 120/this.size;
		}
	}

	boolean isAlive(){
		return this.alive;
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

	void immunitySet(boolean value){
		this.hasImmunity = value;
	}

	void speedDoubleSet(boolean value){
		this.speedDoubled = value;
	}

	boolean isImmune(){
		return this.hasImmunity;
	}

	boolean speedDoubled(){
		return this.speedDoubled;
	}


	void die(){
		this.alive = false;
	}
}
