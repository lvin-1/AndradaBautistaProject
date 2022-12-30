package valoblob;

import javafx.scene.image.Image;
import javafx.scene.canvas.GraphicsContext;
import javafx.geometry.Rectangle2D;
import javafx.scene.shape.Circle;

public class Sprite {

	protected Image img;
	protected double xPos, yPos, dx, dy;
	protected boolean visible;
	protected double size;


	public Sprite(int xPos, int yPos, Image image){
		this.xPos = xPos;
		this.yPos = yPos;
		this.loadImage(image);
		this.visible = true;
	}

	protected void loadImage(Image img){
		try{
			this.img = img;
	        this.setSize();
		} catch(Exception e){}
	}

	void render(GraphicsContext gc){
		gc.drawImage(this.img, this.xPos, this.yPos);

    }


	private void setSize(){
		this.size = this.img.getWidth();
	    //this.height = this.img.getHeight();
	}


	public boolean collidesWith(Sprite c2){
		Circle circle1 = this.getBounds();
		Circle circle2 = c2.getBounds();

		return circle1.intersects(circle2.getBoundsInLocal());

	}

	private Circle getBounds(){
		return new Circle(this.xPos, this.yPos, this.size);
	}

	Image getImage(){
		return this.img;
	}

	public double getX(){
		return this.xPos;
	}

	public double getY(){
		return this.yPos;
	}

	public void setDX(double dx){
		this.dx = dx;
	}


	public void setDY(double dy){
		this.dy = dy;
	}


	public void setSize(double size){
		this.size = size;
	}

	public void setVisible(boolean value){
		this.visible = value;
	}



}
