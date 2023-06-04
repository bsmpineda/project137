package cmsc137;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.FocusListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.*;
import javax.swing.text.StyledDocument;
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
	int x=10,xspeed=10,yspeed=1,prevX,prevY;
	private static final Color FINISH_LINE_COLOR = Color.RED;
	int y = 30;
	int frogSize = 80;
	boolean isJump, isDown, winner, isF, isR = false;
	boolean haveWinner = false;
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
	JTextField chatInput = new JTextField("Chat here...");
	JTextArea chatArea = new JTextArea("CHAT WINDOW:\n");
	JTextPane wordArea = new JTextPane();
	
	int gameWidth = 640;
	int gameHeight = 480;
	Color color = new Color(255,255,255);
	String randomWord;
	
	//font package
	File font_file = new File("Arcade Gamer.otf");
	Font game_font = Font.createFont(Font.TRUETYPE_FONT, font_file);
	/**
	 * Basic constructor
	 * @param server
	 * @param name
	 * @throws Exception
	 */
	public FrogLeap(String server,String name) throws Exception{
		this.server=server;
		this.name=name;
		
		chatInput.setBounds(640,410,300,30);
		chatInput.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e){
				chatInput.setText("");
			}

			
		});

		chatInput.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent ke){
				if(ke.getKeyCode()==KeyEvent.VK_ENTER){
					if(!(chatInput.getText().equals(""))){
						send("CHAT~`~"+name+"~`~"+chatInput.getText());
						if (randomWord.equals(chatInput.getText().toUpperCase())) { // if the word matches the random word, leap forward
							x+=randomWord.length()*3; // the movement is based on length of words
							send("GET "+name+" "+chatInput.getText().toUpperCase()+" "+x+" "+y);
						}else if ((haveWinner ||winner) && chatInput.getText().equals("1")) { // 
							send("RESTART");							
						}else if ((haveWinner ||winner) && chatInput.getText().equals("0")) { // 
							send("EXIT");
					}
					chatInput.setText("Chat here...");
					frame.requestFocusInWindow();
				}else if(ke.getKeyCode()==KeyEvent.VK_ESCAPE){
					chatInput.setText("Chat here...");
					frame.requestFocusInWindow();
				}
			}
		}});

		
		chatArea.setLineWrap(true);
		chatArea.setFocusable(false);
		

		wordArea.setFocusable(false);
		wordArea.setOpaque(false); //set to transparent
		
		JScrollPane scroll = new JScrollPane (chatArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setBounds(640,0,300,410);
		
		JScrollPane wordScroll = new JScrollPane (wordArea, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		wordScroll.setBounds(125,100,400,50);
		wordScroll.getViewport().setOpaque(false);
		wordScroll.setOpaque(false);
		wordScroll.setBorder(null); // remove white borders 
		StyledDocument doc = wordArea.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		
		
		
		//word area display 
		Font wordfont = new Font("Arial", Font.BOLD, 35);
		wordArea.setFont(wordfont);
		wordArea.setForeground(Color.WHITE);
	
		
		frame.setTitle(APP_NAME+":"+name);
		//set some timeout for the socket
		socket.setSoTimeout(100);
		frame.setResizable(false);
		
		//Some gui stuff i hate.
		frame.getContentPane().add(chatInput);
		frame.getContentPane().add(scroll);
		frame.getContentPane().add(wordScroll);
		frame.setFocusable(true);
		frame.requestFocusInWindow();
		frame.getContentPane().add(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(940, gameHeight);
		frame.setVisible(true);
		
		//create the buffer
		offscreen=new BufferedImage(gameWidth, gameHeight, BufferedImage.TYPE_INT_ARGB);;
		frog = ImageIO.read(getClass().getResourceAsStream("images/frog.png"));
		bg =ImageIO.read(getClass().getResourceAsStream("images/bg.png"));
		initBG = ImageIO.read(getClass().getResourceAsStream("images/matching_screen.jpg"));

		//Some gui stuff again...
		frame.addMouseMotionListener(new MouseMotionHandler());
		frame.addKeyListener(new KeyHandler());		
		frame.addWindowListener(new WindowHandaler());
		

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
				Thread.sleep(10);
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
				wordArea.setText("");
				offscreen.getGraphics().drawImage(initBG, 0, 0, gameWidth, gameHeight, getBackground(), frame);
				//show the changes
				frame.repaint();
			}else if (!connected){
				System.out.println("Connecting..");				
				send("CONNECT "+name);
				offscreen.getGraphics().clearRect(0, 0, gameWidth, gameHeight);
				offscreen.getGraphics().drawImage(initBG, 0, 0, gameWidth, gameHeight, getBackground(), frame);
				//show the changes
				frame.repaint();
			}else if (connected){

				//To show the sprites even if the space key was not clicked
				if(serverData.startsWith("START")){
					//clear chats
					chatArea.setText("");
					// isplay
					Font font = new Font("Arial", Font.BOLD, 15);
					chatArea.setFont(font);
					chatArea.setForeground(Color.BLACK);
					chatArea.append("Start Game!"+"\n");
					
					// resetting all the values
					this.x=10;
					this.xspeed=10;
					this.winner = false;
					send("PLAYER "+name+" "+this.x+" "+y); // x = 10 and y = 30 as initial starting point
					//start = false;
				}

				if (serverData.startsWith("PLAYER")){
					offscreen.getGraphics().clearRect(0, 0, gameWidth, gameHeight);
					offscreen.getGraphics().drawImage(bg, 0, 0, gameWidth, gameHeight, getBackground(), frame);
					frame.repaint();
					String[] playersInfo = serverData.split(":");
					for (int i=0;i<playersInfo.length;i++){
						//System.out.println(playersInfo.length);
						String[] playerInfo = playersInfo[i].split(" ");
						String pname =playerInfo[1];
						int playerX = Integer.parseInt(playerInfo[2]);
						int playerY = Integer.parseInt(playerInfo[3])*((2*i+6));
						//draw on the offscreen image	
						offscreen.getGraphics().drawImage(frog, playerX, playerY, frogSize, frogSize, frame); //draw the frog
						offscreen.getGraphics().drawString(pname,playerX+30,
						playerY+65);					
					}
					//show the changes
					frame.repaint();
				}else if(serverData.startsWith("CHAT")){
					String[] chatInfo = serverData.split("~`~");
					if(chatInfo[1].equals(name)){
						chatArea.append("[YOU] : "+chatInfo[2]+"\n");
						
					
					}
					else if(chatInfo[1].equals("SERVER")){
						chatArea.append("<<--- "+chatInfo[2]+" --->>\n");
						connected = false;
					}
					else{
						chatArea.append("["+chatInfo[1]+"] : "+chatInfo[2]+"\n");
					}

				}else if(serverData.startsWith("POWERUP")){
					chatArea.setText("");
					wordArea.setText("");
					String[] chatInfo = serverData.split(" ");
					randomWord = chatInfo[1];
					wordArea.setText(randomWord);
					chatArea.append("[WORD] : "+chatInfo[1]+"\n");
					
				}else if(serverData.startsWith("RESET")){
					// Format : RESET + <RESET_TOKEN> + <pname>
					haveWinner = false;
					chatArea.setText("");
					wordArea.setText("");
					String[] chatInfo = serverData.split(" ");
					randomWord = chatInfo[1];
					chatArea.append(chatInfo[2]+" got the correct answer!"+"\n");
				}else if(serverData.startsWith("WIN")){
					 chatArea.setText("");
					 wordArea.setText("");
					 Font font = new Font("Arial", Font.BOLD, 15);
					 chatArea.setFont(font);
					 chatArea.setForeground(Color.BLUE);
					String[] chatInfo = serverData.split(" ");
					wordArea.setText(chatInfo[1]+" WINS!!");
					chatArea.append(chatInfo[1]+" won the game!"+"\n");
					chatArea.append(chatInfo[1]+"\n");
					chatArea.append("TYPE 1 to restart the game"+"\n");
					chatArea.append("TYPE 2 to exit the game\n");
					haveWinner = true;
				}else if (serverData.startsWith("END")) {
					xspeed = 0;
					yspeed = 0;
					
				}
			}			
		}
	}
	
	/**
	 * Repainting method
	 */
	public void paintComponent(Graphics g){
		// Set the font color
		 g.setColor(Color.WHITE);
        
		 // Set the font
		 Font font = new Font("Arial", Font.BOLD, 30);
		 g.setFont(font);
		 
		g.drawImage(offscreen, 0, 0, null);
		
	}

	private void sendNotification() {
		System.out.println("Client exited");
        System.exit(0);
        socket.close();
    }
	
	class MouseMotionHandler extends MouseMotionAdapter{
		public void mouseMoved(MouseEvent me){
			if(me.getID()==MouseEvent.MOUSE_CLICKED){
				x=me.getX();y=me.getY();
				if (x<=gameWidth){
					frame.requestFocusInWindow();
				}else{
					chatInput.requestFocusInWindow();
				}
			}
							
		}
	}
	
	class WindowHandaler extends WindowAdapter{
		public void windowClosing(WindowEvent we) {
			System.out.println("GUI closed");
			send("CLOSE "+name);
			System.out.close();
			socket.close();
		}
	}

	class KeyHandler extends KeyAdapter{
		public void keyPressed(KeyEvent ke){
			switch (ke.getKeyCode()){
			case KeyEvent.VK_LEFT:
				if (!isJump && !haveWinner) {
					x+=xspeed;
					y-=yspeed;
					send("PLAYER "+name+" "+x+" "+y);
					isJump = true;
					isDown = false;
				}
				break;
			case KeyEvent.VK_RIGHT:
				if (!isDown) {
					y+=yspeed;
					send("PLAYER "+name+" "+x+" "+y);
					isDown = true;
					isJump = false;
				}
				break;
			case KeyEvent.VK_ENTER:
				chatInput.requestFocusInWindow();
				break;
			}
			if (x>=gameWidth-frogSize-60) { //determiner if someone wins
				xspeed = 0;
				send("WIN "+name);
				winner = true;
			}
			
		
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
