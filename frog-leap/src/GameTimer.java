
import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


public class GameTimer extends AnimationTimer {
	public static final int F_TYPE = 1;
	public static final int R_TYPE = 2;
	public static final int O_TYPE = 3;
	public static final int G_TYPE = 4;
	
	
	private ArrayList<PowerUp> powerUps; // consists of FROG powerUps


    private static GraphicsContext gc;
    private static Scene theScene;
    private Frog player, player2, player3, player4;

   

    private long startSpawn, seconds, getSecondsCollected;
	public static final int POWER_SEC = 5;

    public GameTimer(GraphicsContext gc, Scene theScene) {
        this.gc = gc;
        this.theScene = theScene;
        this.player = new Frog(0,210, this.theScene);
        this.player2 = new Frog(0,400, this.theScene);
        this.startSpawn = System.nanoTime();
        
        // instantiate the ArrayList of Power-ups
     	this.powerUps = new ArrayList<PowerUp>();
     	
        // call method to handle mouse click event
        //this.handleKeyPressEvent();
        //this.handleKeyReleaseEvent();
       
        
      
        //thread for spawning additional power-ups
  		Timer timerP = new Timer();
  		timerP.schedule(new TimerTask() {
  			public void run() {
  				Random rand = new Random();
  				int pos = rand.nextInt(4) + 1;
  				if (pos == 1) {
  					spawnPowerUps(GameTimer.F_TYPE, pos);
  				} else if (pos == 2) {
  					spawnPowerUps(GameTimer.R_TYPE, pos);
  				}else if (pos == 3) {
  					spawnPowerUps(GameTimer.O_TYPE, pos);
  				}else {
  					spawnPowerUps(GameTimer.G_TYPE, pos);
  				}
  			}
  		}, 2000, 2000); // added after 10 seconds of start game, then every 10 seconds

	     
	     
        
    }

    @Override
    public void handle(long currentNanoTime) {
    	this.gc.clearRect(0, 0, GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT); //clear bg
    	long currentSec2 = TimeUnit.NANOSECONDS.toSeconds(currentNanoTime);
		long startSec2 = TimeUnit.NANOSECONDS.toSeconds(this.startSpawn);
		this.seconds = currentSec2 - startSec2;
		
    	Image bg = new Image("images/pond.png");
        this.gc.drawImage(bg, 0, 0);
        this.player.jump();
        
    	this.player.render(gc); //show the frog
    	this.player2.render(gc); //show the frog
    	checkPowerPick();
		powerUpRemove();
    	
    	renderPowerUp();
    	

    }

    // method that will listen and handle the key press events
    /**private void handleKeyPressEvent() {
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
    }**/

 // method that will listen and handle the key press events
    /**private void handleKeyReleaseEvent() {
        theScene.setOnKeyReleased(new EventHandler<KeyEvent>() {

            public void handle(KeyEvent e) {
                KeyCode code = e.getCode();
                if (code == KeyCode.SPACE) {
                	spaceIsOnPressed = false;
                }
            }

        });
    }**/
    
	private void spawnPowerUps(int type, int pos) {
		Random r = new Random();
		//int x = r.nextInt(GameStage.WINDOW_WIDTH / 2); // left side only
		//int y = r.nextInt(GameStage.WINDOW_HEIGHT - GameTimer.SCREEN_ADJUSTMENT); // adjusted for the whole image to be seen
		PowerUp power = new PowerUp(350, 100, this, type, seconds); // seconds to capture the second it was spawned
		powerUps.add(power);
	}
	
	private void renderPowerUp() { // draws starfish or pearl
		for (PowerUp p : this.powerUps) {
			p.render(this.gc);
		}
	}
	
	private void checkPowerPick() { // picking of power-ups
		for (int i = 0; i < this.powerUps.size(); i++) {
			PowerUp p = powerUps.get(i);
			if (player.powerUp(p.getType())) {
				//this.player.press(p.isVisible());
					/**if (p.getType() == GameTimer.STAR_TYPE) { //starfish adds 50 in ship's strength
						this.myShip.changeStrength(PowerUp.STAR_ENHANCE);
					}
					else { // for PEARL type
						p.setSecondCollected(seconds); // to set the second which it was collected
						this.pearlActive = true;
						this.bulletStrength = ((this.bulletStrength) * 2); // doubles bullet strength (from 10 to 20)
						this.getSecondsCollected = seconds;

					}**/
			powerUps.remove(i);
			i--;
			}
				
			}
		}
	//}
	
	private void powerUpRemove(){
		for (int i = 0; i < this.powerUps.size(); i++) {
			PowerUp p = powerUps.get(i);

			if ((seconds - p.getSeconds()) == POWER_SEC) { //removes a power-up when uncollected within 5 seconds
				powerUps.remove(i);
				i--;
			}

		}

	}
	

}
