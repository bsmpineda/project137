package cmsc137;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
/**
 * The game client itself!
 * @author Joseph Anthony C. Hermocilla
 *
 */

public class FrogLeap extends JPanel implements Runnable, Constants{
	/**
	 * Main window
	 */
	JFrame frame= new JFrame();
	
	/**
	 * Player position, speed etc.
	 */
	int x=10,xspeed=2,yspeed=2,prevX,prevY;
	int y = 10;
	/**
	 * Game timer, handler receives data from server to update game state
	 */
	Thread t=new Thread(this);
	
	/**
	 * Nice name!
	 */
	String name="Joseph";
	
	/**
	 * Player name of others
	 */
	String pname;
	
	/**
	 * Server to connect to
	 */
	String server="localhost";

	/**
	 * Flag to indicate whether this player has connected or not
	 */
	boolean connected=false, start = false;
	
	/**
	 * get a datagram socket
	 */
    DatagramSocket socket = new DatagramSocket();

	
    /**
     * Placeholder for data received from server
     */
	String serverData;
	
	/**
	 * Offscreen image for double buffering, for some
	 * real smooth animation :)
	 */
	BufferedImage offscreen, frog, bg, initBG;

	int gameWidth = 640;
	int gameHeight = 480;
	Color color = new Color(255,255,255);
	/**
	 * Basic constructor
	 * @param server
	 * @param name
	 * @throws Exception
	 */
	public FrogLeap(String server,String name) throws Exception{
		this.server=server;
		this.name=name;
		
		frame.setTitle(APP_NAME+":"+name);
		//set some timeout for the socket
		socket.setSoTimeout(100);
		
		//Some gui stuff i hate.
		frame.getContentPane().add(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(gameWidth, gameHeight);
		frame.setVisible(true);
		
		//create the buffer
		offscreen=new BufferedImage(gameWidth, gameHeight, BufferedImage.TYPE_INT_ARGB);;
		frog = ImageIO.read(getClass().getResourceAsStream("images/frog.png"));
		bg =ImageIO.read(getClass().getResourceAsStream("images/pond.png"));
		initBG = ImageIO.read(getClass().getResourceAsStream("images/matching_screen.jpg"));

		//Some gui stuff again...
		frame.addKeyListener(new KeyHandler());		
		

		//tiime to play
		t.start();		
	}
	
	/**
	 * Helper method for sending data to server
	 * @param msg
	 */
	public void send(String msg){
		try{
			byte[] buf = msg.getBytes();
        	InetAddress address = InetAddress.getByName(server);
        	DatagramPacket packet = new DatagramPacket(buf, buf.length, address, PORT);
        	socket.send(packet);
        }catch(Exception e){}
		
	}
	
	/**
	 * The juicy part!
	 */
	public void run(){
		while(true){
			try{
				Thread.sleep(1);
			}catch(Exception ioe){}
						
			//Get the data from players
			byte[] buf = new byte[256];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			try{
     			socket.receive(packet);
			}catch(Exception ioe){/*lazy exception handling :)*/}
			
			serverData=new String(buf);
			serverData=serverData.trim();
			
			//if (!serverData.equals("")){
			//	System.out.println("Server Data:" +serverData);
			//}

			//Study the following kids. 
			if (!connected && serverData.startsWith("CONNECTED")){
				connected=true;
				System.out.println("Connected.");
				//start = true;
				offscreen.getGraphics().drawImage(initBG, 0, 0, gameWidth, gameHeight, getBackground(), frame);
				//show the changes
				frame.repaint();
			}else if (!connected){
				System.out.println("Connecting..");				
				send("CONNECT "+name);
			}else if (connected){

				//To show the sprites even if the space key was not clicked
				if(serverData.startsWith("START")){
					send("PLAYER "+name+" "+x+" "+y);
					//start = false;
				}

				if (serverData.startsWith("PLAYER")){
					offscreen.getGraphics().clearRect(0, 0, gameWidth, gameHeight);
					offscreen.getGraphics().drawImage(bg, 0, 0, gameWidth, gameHeight, getBackground(), frame);

					String[] playersInfo = serverData.split(":");
					for (int i=0;i<playersInfo.length;i++){
						//System.out.println(playersInfo.length);
						String[] playerInfo = playersInfo[i].split(" ");
						String pname =playerInfo[1];
						int playerX = Integer.parseInt(playerInfo[2]);
						int playerY = Integer.parseInt(playerInfo[3])*(10*i);
						//draw on the offscreen image	
						offscreen.getGraphics().drawImage(frog, playerX, playerY, 80, 80, frame); //draw the frog
						offscreen.getGraphics().drawString(pname,playerX+30,
						playerY+65);					
					}
					//show the changes
					frame.repaint();
				}			
			}			
		}
	}
	
	/**
	 * Repainting method
	 */
	public void paintComponent(Graphics g){
		// Set the font color
         // Set the font color
		 g.setColor(Color.WHITE);
        
		 // Set the font
		 Font font = new Font("Arial", Font.BOLD, 30);
		 g.setFont(font);
		 
		g.drawImage(offscreen, 0, 0, null);
		
	}
	
	
	

	class KeyHandler extends KeyAdapter{
		public void keyPressed(KeyEvent ke){

			switch (ke.getKeyCode()){
			case KeyEvent.VK_SPACE:x+=xspeed;break;
			}
		
			send("PLAYER "+name+" "+x+" "+y);
		
		}
	}
	
	
	public static void main(String args[]) throws Exception{
		if (args.length != 2){
			System.out.println("Usage: java -jar FrogLeapeap-client <server> <player name>");
			System.exit(1);
		}

		new FrogLeap(args[0],args[1]);
	}
}