package cmsc137;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;


import javax.imageio.ImageIO;

public class Splash implements Constants{
    JFrame frame= new JFrame();

    public Splash() throws Exception{
        JPanel panel = new JPanel();
        
        JTextField nameInput = new JTextField("Enter username");
		//nameInput.setBounds(475,150,200,30);
        nameInput.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e){
				nameInput.setText("");
			}

			
		});
        JTextField serverInput = new JTextField("Enter server");
		//serverInput.setBounds(125,150,200,30);
        serverInput.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e){
				serverInput.setText("");
			}

			
		});

        JButton start_btn = new JButton("Start Game",null );
        JButton instructions_btn = new JButton("Instructions", null);
        JButton about_btn = new JButton("About", null);
        JButton exit_btn = new JButton("Exit", null);

        start_btn.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent e){
                String username = nameInput.getText();
                String server = serverInput.getText();

                if(!username.equals("") && !server.equals("") ){
                    try {
                        new FrogLeap(server, username);
                        frame.dispose();
                    } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        System.out.println("FAILURE CREATING CLIENT!");
                        e1.printStackTrace();
                    }
                }
            }  
        });  

        about_btn.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent e){
                try {
                   

                    JDialog d = new JDialog(frame, "About");
                   
                    d.add(new JLabel(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("images/about.png")))));
                    d.setSize(800, 500);
 
                    // set visibility of dialog
                    d.setVisible(true);
                   
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    System.out.println("no such image");
                    e1.printStackTrace();
                }

            }  
        }); 

        instructions_btn.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent e){
                try {
                   

                    JDialog d = new JDialog(frame, "Instructions");
                   
                    d.add(new JLabel(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("images/instructions.png")))));
                    d.setSize(800, 500);
 
                    // set visibility of dialog
                    d.setVisible(true);
                   
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    System.out.println("no such image");
                    e1.printStackTrace();
                }

            }  
        }); 

        exit_btn.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent e){
                System.exit(0);
            }  
        }); 

        //serverInput.setAlignmentX(Component.CENTER_ALIGNMENT);
        start_btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        instructions_btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        about_btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        exit_btn.setAlignmentX(Component.CENTER_ALIGNMENT);

        frame.setContentPane(new JLabel(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("images/MS.gif")))));


        frame.setTitle(APP_NAME);
       

		panel.add(nameInput);
        panel.add(Box.createRigidArea(new Dimension(0,10)));
		panel.add(serverInput);
        panel.add(Box.createRigidArea(new Dimension(0,30)));
		panel.add(start_btn);
        panel.add(Box.createRigidArea(new Dimension(0,10)));
		panel.add(instructions_btn);
        panel.add(Box.createRigidArea(new Dimension(0,10)));
		panel.add(about_btn);
        panel.add(Box.createRigidArea(new Dimension(0,10)));
		panel.add(exit_btn);
        panel.setBounds(300, 150, 200, 250);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        frame.add(panel);

        frame.setFocusable(true);
		frame.requestFocusInWindow();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 500);
        frame.setVisible(true);
    }

    public static void main(String args[]) throws Exception{
		

		new Splash();
	}
}

