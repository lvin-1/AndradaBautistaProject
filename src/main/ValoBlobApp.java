package main;

import javafx.application.Application;
import javafx.stage.Stage;
import valoblob.*;

public class ValoBlobApp extends Application{

	public static void main(String[] args){
		System.out.println("a");
        launch(args);
        System.out.println("?");
    }

    @Override
    public void start(Stage stage){
       Game game = new Game();
       game.setStage(stage);
    }



}
