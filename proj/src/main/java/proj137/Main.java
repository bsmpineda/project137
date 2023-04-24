package proj137;

import javafx.application.Application;
import javafx.stage.Stage;
//import proj137.GameStage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) {
        GameStage theGameStage = new GameStage();
        theGameStage.setStage(stage);
    }

}
