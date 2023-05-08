import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Frog {
	public static int x;
	public static int y, currY;
	public static Image image = new Image("images/frog.png", Frog.FROG_WIDTH, Frog.FROG_WIDTH, false, false);
	private final static int FROG_WIDTH = 70;
	static int speed = 1;
	static boolean didJump;

    public Frog(int x, int y) {
        this.x = x;
        this.y = y;
        this.currY = y;
        this.didJump = false;
        //this.render(Frog.image);
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
    		this.currY -= 5;
    	}
    	else{
    		this.currY = y;
    	}

    	this.didJump = false;
    }

    void win(){

    }

}
