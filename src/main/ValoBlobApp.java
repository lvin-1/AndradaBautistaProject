package main;

import javafx.application.Application;
import javafx.stage.Stage;
import valoblob.*;

public class ValoBlobApp extends Application{

	public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage stage){
       Game game = new Game();
       game.setStage(stage);
    }



}
