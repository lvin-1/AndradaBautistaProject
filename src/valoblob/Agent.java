package valoblob;


import javafx.scene.image.Image;;

public class Agent extends Sprite {
	public final static int INITIAL_MAIN_POSITION = 400;
	public final static int INITIAL_AGENT_SIZE = 40;

	Agent(int xPos, int yPos, Image image){
		super(xPos,yPos,image);
	}

	void move(){
		if(this.xPos+this.dx >= 0 && this.xPos+this.dx <= Game.WINDOW_WIDTH-this.size){
			this.xPos += this.dx;
		}

		if(this.yPos+this.dy >= 0 && this.yPos+this.dy <= Game.WINDOW_WIDTH-this.size){
			this.yPos += this.dy;
		}
	}

	void increaseSize(int size){
		this.size += size;
	}
}
