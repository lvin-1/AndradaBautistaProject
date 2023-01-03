package valoblob;


import javafx.scene.image.Image;;

public class Agent extends Sprite {
	private final static Image FINAL_AGENT_IMAGE = new Image("images/Valorant-Jett.png",40,40,false,false);
	Agent(){
		super(400,400,Agent.FINAL_AGENT_IMAGE);
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
