import java.io.*;
import java.net.*;
import java.util.*;

public class FrogMovement {
    private static final int SERVER_PORT = 5000;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("Server started and listening on port " + SERVER_PORT);

            List<ClientHandler> clientHandlers = new ArrayList<>();

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());

                ClientHandler clientHandler = new ClientHandler(clientSocket, clientHandlers);
                clientHandlers.add(clientHandler);
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler extends Thread {
    private Socket clientSocket;
    private List<ClientHandler> clientHandlers;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    public ClientHandler(Socket clientSocket, List<ClientHandler> clientHandlers) {
        this.clientSocket = clientSocket;
        this.clientHandlers = clientHandlers;
    }

    @Override
    public void run() {
        try {
            outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            inputStream = new ObjectInputStream(clientSocket.getInputStream());

            while (true) {
                Object receivedObject = inputStream.readObject();
                if (receivedObject instanceof List<?>) {
                    List<FrogPosition> receivedPositions = (List<FrogPosition>) receivedObject;
                    System.out.println("Received frog positions from client: " + clientSocket.getInetAddress().getHostAddress());
                    for (FrogPosition position : receivedPositions) {
                        System.out.println("Frog #" + position.getFrogNumber() + ": " + position.getSpeed());
                    }

                    // Process the received positions and send updated positions back to the client
                    List<FrogPosition> updatedPositions = processFrogPositions(receivedPositions);

                    // Serialize the updated positions
                    byte[] serializedData = FrogPosition.serialize(updatedPositions);

                    // Create a DatagramPacket to send the serialized data to the client
                    DatagramPacket sendPacket = new DatagramPacket(serializedData, serializedData.length, clientSocket.getInetAddress(), clientSocket.getPort());

                    // Create a DatagramSocket to send the packet
                    DatagramSocket datagramSocket = new DatagramSocket();
                    datagramSocket.send(sendPacket);
                    datagramSocket.close();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } 
    }

    private List<FrogPosition> processFrogPositions(List<FrogPosition> receivedPositions) {
        // TODO: Process the received frog positions and update them accordingly
        // For this example, we'll just return the same positions as received
        return receivedPositions;
    }
}
