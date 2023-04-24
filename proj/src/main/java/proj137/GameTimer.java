package proj137;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class GameTimer extends AnimationTimer {
    private GraphicsContext gc;
    private Scene theScene;
    Frog player;

    GameTimer(GraphicsContext gc, Scene theScene) {
        this.gc = gc;
        this.theScene = theScene;
        // this.player = new Frog(10,,250);
        // call method to handle mouse click event
        this.handleKeyPressEvent();
    }

    @Override
    public void handle(long arg0) {

    }

    // method that will listen and handle the key press events
    private void handleKeyPressEvent() {
        theScene.setOnKeyPressed(new EventHandler<KeyEvent>() {

            public void handle(KeyEvent e) {
                KeyCode code = e.getCode();
                if (code == KeyCode.SPACE) {

                }
            }

        });
    }
}
