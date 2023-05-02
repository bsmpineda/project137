
import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class GameTimer extends AnimationTimer {
    private static GraphicsContext gc;
    private static Scene theScene;
    static Frog player;
    private static boolean spaceIsOnPressed;

    GameTimer(GraphicsContext gc, Scene theScene) {
        this.gc = gc;
        this.theScene = theScene;
        this.player = new Frog(0,210);
        this.spaceIsOnPressed = false;
        // call method to handle mouse click event
        this.handleKeyPressEvent();
        this.handleKeyReleaseEvent();
    }

    @Override
    public void handle(long arg0) {
    	this.gc.clearRect(0, 0, GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT); //clear bg
    	Image bg = new Image("images/pond.png");
        this.gc.drawImage(bg, 0, 0);
    	this.player.render(gc); //show the frog
    	
    }

    // method that will listen and handle the key press events
    private void handleKeyPressEvent() {
        theScene.setOnKeyPressed(new EventHandler<KeyEvent>() {

            public void handle(KeyEvent e) {
                KeyCode code = e.getCode();
                if (code == KeyCode.SPACE) {
                	if(!spaceIsOnPressed)
                		player.move();
                	spaceIsOnPressed = true;
                }
            }

        });
    }

 // method that will listen and handle the key press events
    private void handleKeyReleaseEvent() {
        theScene.setOnKeyReleased(new EventHandler<KeyEvent>() {

            public void handle(KeyEvent e) {
                KeyCode code = e.getCode();
                if (code == KeyCode.SPACE) {
                	spaceIsOnPressed = false;
                }
            }

        });
    }
}
