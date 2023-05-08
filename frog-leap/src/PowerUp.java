import javafx.scene.image.Image;
/*************************************************************************************************************************
 * CMSC 22 Object-Oriented Programming
 * Project: Taken from original code template of CMSC 22 Project Template
 * (c) Institute of Computer Science, CAS, UPLB

 * Mini Ship Shooting Game


 * Description (taken from Problem Domain of Module 9):

 * Upon creation, the ship is given an initial x position equal to 150, initial y position equal to 250 and a randomized initial
	strength between 100-150. The ship can move by 10 pixels at a time. The ship can also shoot bullets when the
	spacebar is pressed. The bullets shot by the ship move towards the right side of the screen in a straight line and
	disappear when the end of the screen is reached. Many disturbed fish appear from the right side of the screen and
	glide towards the left side and back. A fish is initially alive and has a random movement speed between 1-5, inclusive.
	At the start, there are 7 fish but 3 more are spawned every 5 seconds.

 * When a fish hits the ship, the ship�s strength is reduced by 30, and the fish dies and disappears. When the ship�s
	strength reaches 0, the game is over and the ship loses. When a fish is hit by the ship�s bullet, it dies and disappears
	from the screen.
 * After one minute, the Boss Fish will appear. In order to win, it should die. When your strength reaches 0, you will lose.

 * Throughout the game, power-ups appear at random locations on the left half of the screen at 10-second intervals.
	These can be collected by the ship. Power-ups disappear after 5 seconds if uncollected.

	MECHANICS:
	A player starts with their ship that can move around using the arrow keys UP, LEFT ,DOWN, and RIGHT.
	The ships initial strength is randomized from 100 to 150.
	The ship needs to shoot the fishes around the sea, a bullet is launched to kill fishes by pressing the SPACE BAR.
	Each time a bullet hits a fish, it automatically dies.
	When a fish hits the ship, it dies and the ship�s strength decreases by 30.
	After 60 seconds, the Boss Fish will appear. The strength of the Boss will be the initial strength of your ship.
	If your strength reaches 0 before killing the Boss, you lose.
	If you successfully killed the Boss Fish, you will win.

	POWERUPS:
	Every 10 seconds, a power up will appear.
	STARFISH- increases the ship�s strength by 50
	PEARL- doubles the strength of the bullet
	The power-ups will appear alternately. If it is uncollected after 5 seconds, it will disappear.

	STATS:
	Ship�s Strength (100-150)
	Boss Strength (100-150)
	Bullet Strength (10)
	Bullet Strength with Pearl (20)
	Strength with Starfish (+50)

	Scoring:
	Score is incremented by the total number of fish killed.
	Killing the Boss Fish will give additional 50 points
	Time elapsed is recorded as well

* @original_author: UPLB CAS-ICS (2021)
* @author (modified by) Jasmine Amor Y. Avelino; Earl Samuel R. Capuchino
* @created_date 2020-05-20 12:07

************************************************************************************************************************/

public class PowerUp extends Sprite {
	public static final int MAX_FISH_SPEED = 5;
	public final static Image F_IMAGE = new Image("images/F.png", PowerUp.POWERUP_WIDTH, PowerUp.POWERUP_WIDTH,
			false, false);
	public final static Image R_IMAGE = new Image("images/R.png", PowerUp.POWERUP_WIDTH, PowerUp.POWERUP_WIDTH,
			false, false);
	public final static Image O_IMAGE = new Image("images/O.png", PowerUp.POWERUP_WIDTH, PowerUp.POWERUP_WIDTH,
			false, false);
	public final static Image G_IMAGE = new Image("images/G.png", PowerUp.POWERUP_WIDTH, PowerUp.POWERUP_WIDTH,
			false, false);
	public final static int POWERUP_WIDTH = 100;
	public final static int STAR_ENHANCE = 50;
	private int type;
	private long seconds, secondsCollected;

	//attribute that will determine if a fish will initially move to the right



	public PowerUp(int x, int y, GameTimer gametimer, int type, long seconds) {

		super(x, y, gametimer);
		this.setType(type); // star or pearl
		this.seconds = seconds;

		if (this.getType() == GameTimer.F_TYPE) {
			this.loadImage(PowerUp.F_IMAGE);
		} else if(this.getType() == GameTimer.R_TYPE) {
			this.loadImage(PowerUp.R_IMAGE);
		}else if(this.getType() == GameTimer.O_TYPE) {
			this.loadImage(PowerUp.O_IMAGE);
		}
		else if(this.getType() == GameTimer.G_TYPE) {
			this.loadImage(PowerUp.G_IMAGE);
		}

		this.setVisible(true);
		this.secondsCollected = 0;
	}



	public int getType() {
		return type;
	}

	public long getSeconds() {
		return seconds;
	}

	private void setType(int type) {
		this.type = type;
	}

	public void setSecondCollected(long secondsCollected) { // for the second it was collected
		this.secondsCollected = secondsCollected;
	}

	public long getSecondCollected() {
		return this.secondsCollected;
	}


}

//end of the program
