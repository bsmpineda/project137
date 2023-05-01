
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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GameStage {
    private Scene scene;
    private Stage stage;
    private Group root;
    private Canvas canvas;
    private Scene splashScene;

    public static final int WINDOW_HEIGHT = 500;
    public static final int WINDOW_WIDTH = 800;

    // the class constructor
    public GameStage() {
        this.root = new Group();
        this.scene = new Scene(root);
        this.canvas = new Canvas(GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT);
        this.root.getChildren().add(this.canvas);
    }

    // method to add stage elements for the Menu page
    public void setStage(Stage stage) {
        this.stage = stage;
        stage.setTitle("LEAP FROG");

        this.initSplash(stage); // initializes the Splash Screen with the New Game button

        stage.setScene(this.splashScene);
        stage.setResizable(false);
        stage.show();
    }

    // initialize the splashscreen of the menu page
    private void initSplash(Stage stage) {
        StackPane root = new StackPane();
        root.getChildren().addAll(this.createCanvas(), this.createVBox());
        this.splashScene = new Scene(root);
    }

    // create the canvas for the menu page
    private Canvas createCanvas() {
        Canvas canvas = new Canvas(GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Image bg = new Image("images/MS.png");
        // gc.drawImage(bg, 0, 0);
        return canvas;
    }

    // SPLASHSCREEN
    // create four buttons for the menu page
    private VBox createVBox() {
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.BOTTOM_CENTER);
        vbox.setPadding(new Insets(50));
        vbox.setSpacing(8);

        // buttons
        Button b1 = new Button("START GAME");
        Button b2 = new Button("EXIT");
        Button b3 = new Button("INSTRUCTIONS");
        Button b4 = new Button("ABOUT");

        b1.setMaxSize(150, 50);
        b2.setMaxSize(150, 50);
        b3.setMaxSize(150, 50);
        b4.setMaxSize(150, 50);

        vbox.getChildren().addAll(b1, b3, b4, b2);

        b1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                setGame(stage); // changes the scene into the game scene
            }
        });
        b2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                System.exit(0); // exits the game
            }
        });
        b3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                setIns(stage);
                // changes the scene into instructions scene
            }
        });
        b4.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                setAbt(stage);
                // changes the scene into about scene
            }
        });

        return vbox;
    }

    // called when START GAME BUTTON is clicked
    // set the gameSage
    void setGame(Stage stage) {
        stage.setScene(this.scene);

        GraphicsContext gc = this.canvas.getGraphicsContext2D();

         GameTimer gameTimer = new GameTimer(gc, scene);
         gameTimer.start();

    }

    // set the instructions scene
    void setIns(Stage stage) {
        stage.setScene(this.scene);
        Canvas canvas = new Canvas(GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        stage.setTitle("Instructions");

        /**
         * ADD IMAGE FOR INSTRUCTION
         */

        // Image bg = new Image("images/Instructions.png");
        // gc.drawImage(bg, 0, 0);

        StackPane root = new StackPane();

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.TOP_RIGHT);
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);

        Button b1 = new Button("BACK");
        b1.setMaxSize(130, 50);

        vbox.getChildren().addAll(b1);

        b1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                setStage(stage); // changes the scene into the game scene
            }
        });
        root.getChildren().addAll(canvas, vbox);
        this.splashScene = new Scene(root);

        stage.setScene(this.splashScene);
        stage.setResizable(false);
        stage.show();
    }

    // set the ABOUT scene
    void setAbt(Stage stage) {
        stage.setScene(this.scene);
        Canvas canvas = new Canvas(GameStage.WINDOW_WIDTH, 800);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        stage.setTitle("About");
        // Image bg = new Image("images/About.png");
        // gc.drawImage(bg, 0, 0);
        /**
         * ADD IMAGE FOR ABOUT
         */

        StackPane root = new StackPane();

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.TOP_RIGHT);
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);

        Button b1 = new Button("BACK");
        b1.setMaxSize(130, 50);

        vbox.getChildren().addAll(b1);

        b1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                setStage(stage); // changes the scene into the game scene
            }
        });
        root.getChildren().addAll(canvas, vbox);
        this.splashScene = new Scene(root);

        stage.setScene(this.splashScene);
        stage.setResizable(false);
        stage.show();
    }

}
