package cmsc137;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.lang.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * The main game server. It just accepts the messages sent by one player to
 * another player
 * @author Joseph Anthony C. Hermocilla
 *
 */

public class GameServer implements Runnable, Constants{
	/**
	 * Placeholder for the data received from the player
	 */	 
	String playerData;
	
	/**
	 * The number of currently connected player
	 */
	public static int playerCount=0;
	
	/**
	 * The socket
	 */
    DatagramSocket serverSocket = null;
    
    /**
     * The current game state
     */
	GameState game;

	/**
	 * The current game stage
	 */
	int gameStage=WAITING_FOR_PLAYERS;
	
	/**
	 * Number of players
	 */
	int numPlayers;
	
	/**
	 * The main game thread
	 */
	Thread t = new Thread(this);
	static String powerupData;
    private static final Random random = new Random();
    // shouldPrint determine if random words should be generated
    static boolean shouldPrint = true;
    static boolean is_F_server,  is_R_server, powerOn = false;
    String randomWord;
	Timer timer = new Timer();

	
	/**
	 * Simple constructor
	 */
	public GameServer(int numPlayers){
		this.numPlayers = numPlayers;
		try {
            serverSocket = new DatagramSocket(PORT);
			serverSocket.setSoTimeout(100);
		} catch (IOException e) {
            System.err.println("Could not listen on port: "+PORT);
            System.exit(-1);
		}catch(Exception e){}
		//Create the game state
		game = new GameState();
		
		System.out.println("Game created...");
		
		//Start the game thread
		t.start();
	}
	
	/**
	 * Helper method for broadcasting data to all players
	 * @param msg
	 */
	public void broadcast(String msg){
		for(Iterator ite=game.getPlayers().keySet().iterator();ite.hasNext();){
			String name=(String)ite.next();
			NetPlayer player=(NetPlayer)game.getPlayers().get(name);			
			send(player,msg);	
		}
	}


	/**
	 * Send a message to a player
	 * @param player
	 * @param msg
	 */
	public void send(NetPlayer player, String msg){
		DatagramPacket packet;	
		byte buf[] = msg.getBytes();		
		packet = new DatagramPacket(buf, buf.length, player.getAddress(),player.getPort());
		try{
			serverSocket.send(packet);
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	public static String[] readWordsFromFile(String filename) {
	        List<String> wordsList = new ArrayList<>();
	        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
	            String line;
	            while ((line = reader.readLine()) != null) {
	                wordsList.add(line);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return wordsList.toArray(new String[0]);
	 }
	
	public static String getRandomWord(String[] words) {
	     Random random = new Random();
	     int index = random.nextInt(words.length);
	     return words[index];
	}
	static void setPowerup(String powerupdata) {
		powerupData = powerupdata;
	}
	
	/**
	 * The juicy part
	 */
	public void run(){
		
	  class LetterTask extends TimerTask {
	        @Override
	        public void run() {
	        	String filename = "words.txt";
	            String[] arr_words = readWordsFromFile(filename);
	            if (gameStage == IN_PROGRESS && shouldPrint) { //should only pprint when game is on going
	                if (arr_words != null && arr_words.length > 0) {
		                randomWord = getRandomWord(arr_words).toUpperCase();
		                powerupData = "POWERUP "+randomWord;
		                broadcast(powerupData);
		                powerOn = true; //this means that the powerUp is (NOT TAKEN)
		            } else {
		                System.out.println("No words found in the file.");
		            }
	            }
	        
	        }
	    }
		    
		
	  	timer.schedule(new LetterTask(), 0, 5000); // starts timer and call every 3 seconds
		while(true){
						
			// Get the data from players
			byte[] buf = new byte[256];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			try{
     			serverSocket.receive(packet);
			}catch(Exception ioe){}
			
			/**
			 * Convert the array of bytes to string
			 */
			playerData=new String(buf);
			
			//remove excess bytes
			playerData = playerData.trim();
			//if (!playerData.equals("")){
			//	System.out.println("Player Data:"+playerData);
			//}
		
			// process
			switch(gameStage){
				  case WAITING_FOR_PLAYERS:
						//System.out.println("Game State: Waiting for players...");
						if (playerData.startsWith("CONNECT")){
							String tokens[] = playerData.split(" ");
							NetPlayer player=new NetPlayer(tokens[1],packet.getAddress(),packet.getPort());
							System.out.println("Player connected: "+tokens[1]);
							game.update(tokens[1].trim(),player);
							broadcast("CONNECTED "+tokens[1]);
							playerCount++;
							if (playerCount==numPlayers){
								gameStage=GAME_START;
							}
						}
					  break;	
				  case GAME_START:
					  System.out.println("Game State: START");
					  broadcast("START");
					  gameStage=IN_PROGRESS;
					  break;
					  
				  case IN_PROGRESS:
					  //Player data was received!
					  if (playerData.startsWith("PLAYER")){
						  //Tokenize:
						  //The format: PLAYER <player name> <x> <y>
						  String[] playerInfo = playerData.split(" ");					  
						  String pname =playerInfo[1];
						  int x = Integer.parseInt(playerInfo[2].trim());
						  int y = Integer.parseInt(playerInfo[3].trim());
						
						  //Get the player from the game state
						  NetPlayer player=(NetPlayer)game.getPlayers().get(pname);					  
						  player.setX(x);
						  player.setY(y);
						  
						  //System.out.println(pname + ": X=" + x + " Y="+ y);
						  //Update the game state
						  
						  game.update(pname, player);
						  //Send to all the updated game state
						  broadcast(game.toString());
					  }else if (playerData.startsWith("CHAT")){
						System.out.println("Received chat...");
						String[] playerInfo = playerData.split("~");
						String chat = playerInfo[4].toUpperCase();
						broadcast(playerData);
					  }else if (playerData.startsWith("GET")){
							String[] chat = playerData.split(" ");
							String pname = chat[1];
							System.out.println("Correct! "+pname);
							String powerup = chat[2];
							int x = Integer.parseInt(chat[3].trim());
							int y = Integer.parseInt(chat[4].trim());
							NetPlayer player=(NetPlayer)game.getPlayers().get(pname);					  
							if (powerup.equals(randomWord)&& powerOn) {
								powerOn = false; //this means that the powerUp is taken
								System.out.println("Correct!");
								player.setX(x);
								player.setY(y);
								game.update(pname, player);
								
								//Send to all the updated game state
								broadcast(game.toString());
								powerupData = "RESET "+RESET_TOKEN+" "+pname; //reset the value of the powerup into long string
					            broadcast(powerupData);
							}else {
								System.out.println("Wrong!");
							}
							broadcast(playerData);
					  }else if (playerData.startsWith("WIN")){
							String[] playerInfo = playerData.split(" ");
							broadcast(playerData);
							shouldPrint = false; //will stop printing random words
					  }else if (playerData.startsWith("RESTART")) {
						  	
						    // format: CHOICE <int> 
							System.out.println("GAME RESTART");
							shouldPrint = true; //start the randomWords timer again
							broadcast("START");
							gameStage=IN_PROGRESS;
							break; 
					  }
					  else if (playerData.startsWith("EXIT")) {
						    // format: CHOICE <int> 
							System.out.println("GAME EXIT");
							//broadcast("START");
						    //gameStage=IN_PROGRESS;
							break; 
					  }
					  break;
					 
				}				  
		}
	}
	
	
	 
  
	
	
	public static void main(String args[]){
		if (args.length != 1){
			System.out.println("Usage: java -jar circlewars-server <number of players>");
			System.exit(1);
		}
		
		new GameServer(Integer.parseInt(args[0]));
	   
	
	}
}

