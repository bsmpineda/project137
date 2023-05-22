import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class FrogMovementClient extends Application {
    private static final String SERVER_IP = "127.0.0.1"; // Replace with the actual server IP
    private static final int SERVER_PORT = 5000;
    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 500;
    private static final int FROG_RADIUS = 12;
    private static final int FROG_DISTANCE = 20;
    private static final int NUM_FROGS = 4;

    private List<Frog> frogs;
    private DatagramSocket datagramSocket;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Create frog objects with initial positions and colors
        frogs = new ArrayList<>();

        // Calculate the x-axis position for the leftmost frog
        double startX = FROG_RADIUS + FROG_DISTANCE;
        double startY = (SCREEN_HEIGHT - (NUM_FROGS * (2 * FROG_RADIUS + FROG_DISTANCE))) / 2;
        Color[] colors = {Color.GREEN, Color.BLUE, Color.YELLOW, Color.RED};

        try {
            InetAddress serverAddress = InetAddress.getByName(SERVER_IP);
            datagramSocket = new DatagramSocket();

            VBox root = new VBox();
            root.setAlignment(Pos.CENTER_LEFT); // Align the frogs to the left
            root.setSpacing(FROG_DISTANCE);

            for (int i = 0; i < NUM_FROGS; i++) {
                double y = startY + i * (2 * FROG_RADIUS + FROG_DISTANCE);
                Frog frog = new Frog(15, startX, y, colors[i], FROG_RADIUS);
                frogs.add(frog);
                root.getChildren().add(frog);
            }

            Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);
            primaryStage.setTitle("Frog Movement");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();

            scene.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.SPACE) {
                    moveFrogsWithJump();
                }
            });

            animateFrogMovements();

            //clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void moveFrogsWithJump() {
        for (Frog frog : frogs) {
            TranslateTransition transition = new TranslateTransition(Duration.seconds(0.16), frog);
            transition.setToX(frog.getTranslateX() + frog.getSpeed());
            transition.setAutoReverse(true);
            transition.setCycleCount(3);
            transition.play();
        }
    }

    private void animateFrogMovements() {
        new Thread(() -> {
            try {
                while (true) {
                    // Send frog positions to the server
                    List<FrogPosition> frogPositions = new ArrayList<>();
                    for (int i = 0; i < frogs.size(); i++) {
                        Frog frog = frogs.get(i);
                        FrogPosition position = new FrogPosition(i + 1, frog.getSpeed(), frog.getCenterX(), frog.getCenterY(),
                                new SerializableColor(frog.getRed(), frog.getGreen(), frog.getBlue()), datagramSocket.getLocalAddress(), datagramSocket.getLocalPort());
                        frogPositions.add(position);
                    }

                    // Serialize the frog positions
                    byte[] serializedData = FrogPosition.serialize(frogPositions);

                    // Create a DatagramPacket to send the serialized data to the server
                    DatagramPacket sendPacket = new DatagramPacket(serializedData, serializedData.length, InetAddress.getByName(SERVER_IP), SERVER_PORT);

                    // Send the packet
                    datagramSocket.send(sendPacket);

                    // Receive updated frog positions from the server
                    byte[] receiveBuffer = new byte[1024];
                    DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                    datagramSocket.receive(receivePacket);

                    // Deserialize the received frog positions
                    List<FrogPosition> receivedPositions = FrogPosition.deserialize(receivePacket.getData());

                    Platform.runLater(() -> {
                        updateFrogPositions(receivedPositions);
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void updateFrogPositions(List<FrogPosition> positions) {
        for (FrogPosition position : positions) {
            Frog frog = frogs.get(position.getFrogNumber() - 1);
            frog.setSpeed(position.getSpeed());
            frog.setCenterX(position.getX());
            frog.setCenterY(position.getY());
        }
    }
}
