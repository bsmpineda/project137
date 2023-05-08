import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Frog {
	public static int x;
	public static int y, currY;
	public static Image image = new Image("images/frog.png", Frog.FROG_WIDTH, Frog.FROG_WIDTH, false, false);
	private final static int FROG_WIDTH = 70;
	
	static int speed = 5;
	static boolean didJump;
	private static Scene theScene;
	private static boolean spaceIsOnPressed, F_Press, R_Press, O_Press, G_Press;

    public Frog(int x, int y, Scene theScene) {
        this.x = x;
        this.y = y;
        this.currY = y;
        this.didJump = false;
        //this.render(Frog.image);
        this.theScene = theScene;
        this.handleKeyPressEvent();
        this.handleKeyReleaseEvent();
    }
    private void handleKeyPressEvent() {
        theScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent e) {
                KeyCode code = e.getCode();
                if (code == KeyCode.SPACE) {
                	if(!spaceIsOnPressed)
                		move();
                	spaceIsOnPressed = true;
                }else if (code == KeyCode.F) { //check Frog.powerUp(int type)
                	System.out.println("F"); // leaps 20 tiles
                	F_Press = true;
                }else if (code == KeyCode.R) { //check Frog.powerUp(int type)
                	System.out.println("R"); //doubles the speed
                	R_Press = true;
                }else if (code == KeyCode.O) { //check Frog.powerUp(int type)
                	System.out.println("O"); //doubles the speed
                	O_Press = true;
                }else if (code == KeyCode.G) { //check Frog.powerUp(int type)
                	System.out.println("G"); //doubles the speed
                	G_Press = true;
                }
            }

        });
    }
    private void handleKeyReleaseEvent() {
        theScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent e) {
                KeyCode code = e.getCode();
                if (code == KeyCode.SPACE) {
                	spaceIsOnPressed = false;
                }else if(code == KeyCode.F) {
                	F_Press = false;
                }else if(code == KeyCode.R) {
                	R_Press = false;
                }else if(code == KeyCode.O) {
                	O_Press = false;
                }else if(code == KeyCode.G) {
                	G_Press = false;
                }
            }

        });
    }
    
 
  //method to set the image to the image view node
  	public void render(GraphicsContext gc){
  		gc.drawImage(this.image, this.x, this.currY);

      }


    void move() {
    	if(this.x+Frog.FROG_WIDTH < GameStage.WINDOW_WIDTH){ //restrict frog to move beyond the window width
    		this.x += this.speed;
    	}
    	this.didJump = true;

    }
    

    //jumping-like effect
    void jump(){
    	if(didJump && this.currY == y){
    		this.currY -= 10;
    	}
    	else{
    		this.currY = y;
    	}

    	this.didJump = false;
    }

    void win(){

    }
	public boolean powerUp(int type) {
		// TODO Auto-generated method stub
		if ((type == 1) && (F_Press == true)) {
			speed += 50;
        	move();
        	speed -=50;
        	return true;
		}else if ((type == 2) && (R_Press == true) ) {
			speed += 5;
			move();
			return true;
		}else if ((type == 3) && (O_Press == true) ) {
			move();
			return true;
		}else if ((type == 4) && (G_Press == true) ) {
			move();
			return true;
		}
		return false;
	}

}
