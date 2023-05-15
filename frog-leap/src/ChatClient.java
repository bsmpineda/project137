
import java.io.*;
import java.net.*;

public class ChatClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 1234);
            System.out.println("Connected to server...");

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

            String serverMessage, clientMessage;

            while (true) {
                serverMessage = reader.readLine();
                if (serverMessage != null) {
                    System.out.println("Server: " + serverMessage);
                }

                clientMessage = consoleReader.readLine();
                writer.println(clientMessage);

                if (clientMessage.equalsIgnoreCase("bye")) {
                    break;
                }
            }

            reader.close();
            writer.close();
            consoleReader.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
