package valoblob;


import javafx.scene.image.Image;;

public class Agent extends Sprite {
	private final static Image FINAL_AGENT_IMAGE = new Image("images/Valorant-Jett.png");
	Agent(){
		super(0,0,Agent.FINAL_AGENT_IMAGE);
	}

	void move(){
		if(this.xPos+this.dx >= 0 && this.xPos+this.dx <= Game.WINDOW_WIDTH-this.size){
			this.xPos += this.dx;
		}

		if(this.yPos+this.dy >= 0 && this.yPos+this.dy <= Game.WINDOW_WIDTH-this.size){
			this.yPos += this.dy;
		}
	}
}
