package valoblob;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Game {

	private Stage stage;
	private Scene splashScene;
	private Scene gameScene;
	private Group root;
	private Canvas canvas;

	public final static int WINDOW_WIDTH = 600;
	public final static int WINDOW_HEIGHT = 600;



	public Game(){
		this.canvas = new Canvas( Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT );
		this.root = new Group();
        this.root.getChildren().add(this.canvas);
        this.gameScene = new Scene( root );
	}

	public void setStage(Stage stage) {
		this.stage = stage;
		stage.setTitle( "ValoBlob" );

		this.initSplash(stage);			// initializes the Splash Screen with the New Game button

		stage.setScene( this.splashScene );
        stage.setResizable(false);
		stage.show();
	}

	private void initSplash(Stage stage) {
		StackPane root = new StackPane();
        root.getChildren().addAll(this.createCanvas(),this.createVBox());
        this.splashScene = new Scene(root);
	}

	private Canvas createCanvas() {
    	Canvas canvas = new Canvas(Game.WINDOW_WIDTH,Game.WINDOW_HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Image bg = new Image("images/Welcome.png",Game.WINDOW_WIDTH,Game.WINDOW_HEIGHT,false,false);
        gc.drawImage(bg, 0, 0);
        return canvas;
    }

    private VBox createVBox() {
    	VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(30);

        Button b1 = new Button("New Game");
        Button b2 = new Button("Instructions");
        Button b3 = new Button("About");

        b1.setPrefSize(150, 35);
        b2.setPrefSize(150,35);
        b3.setPrefSize(150,35);

        vbox.getChildren().add(b1);
        vbox.getChildren().add(b2);
        vbox.getChildren().add(b3);

        b1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                setGame(stage);		// changes the scene into the game scene
            }
        });

        b2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                setInstruction(stage);		// changes the scene into the instruction scene
            }
        });

        b3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                setAbout(stage);		// changes the scene into the about scene
            }
        });


        return vbox;
    }

	void setGame(Stage stage) {
        stage.setScene( this.gameScene );

        GraphicsContext gc = this.canvas.getGraphicsContext2D();	// we will pass this gc to be able to draw on this Game's canvas

        GameTimer gameTimer = new GameTimer(gameScene, gc);
        gameTimer.start();			// this internally calls the handle() method of our GameTimer

	}


	void setInstruction(Stage stage) {
		this.root = new Group();
		this.gameScene = new Scene(root);

		this.canvas = new Canvas(Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);
		Image inst = new Image("images/Instructions.png",Game.WINDOW_WIDTH,Game.WINDOW_HEIGHT,false,false);
		GraphicsContext gc = this.canvas.getGraphicsContext2D();	// we will pass this gc to be able to draw on this Game's canvas
		gc.drawImage(inst, 0, 0);
		Button back = new Button ("Back");
		back.setPrefSize(90,30);
		this.root.getChildren().addAll(canvas,back);

		back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            	Game game = new Game();
                game.setStage(stage);	// changes the scene into the game scene
            }
        });

		stage.setTitle("Instructions");
        stage.setScene( this.gameScene );
        stage.show();
	}

	void setAbout(Stage stage) {
		this.root = new Group();
		this.gameScene = new Scene(root);

		this.canvas = new Canvas(Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);
		Image inst = new Image("images/About.png",Game.WINDOW_WIDTH,Game.WINDOW_HEIGHT,false,false);
		GraphicsContext gc = this.canvas.getGraphicsContext2D();	// we will pass this gc to be able to draw on this Game's canvas
		gc.drawImage(inst, 0, 0);
		Button back = new Button ("Back");
		back.setPrefSize(90,30);

		this.root.getChildren().addAll(canvas,back);

		back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            	Game game = new Game();
                game.setStage(stage);	// changes the scene into the game scene
            }
        });

		stage.setTitle("Instructions");
        stage.setScene( this.gameScene );
        stage.show();}


}
