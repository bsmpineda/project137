package proj137;

import javafx.scene.image.Image;

public class Frog {
    int x;
    int y;
    Image image;

    public Frog(int x, int y) {
        this.x = x;
        this.y = y;
    }

    void move() {
        this.x += 1;
    }

}
