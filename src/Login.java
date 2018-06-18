import javax.swing.*;
import javax.swing.border.LineBorder;
import java.io.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
/**
 * File: Login.java
 * <p>Mr. Anadarajan
 * <br/>ICS4U1
 * <br/>June 15, 2018
 * 
 * <p>Final Evaluation: Battleship Tournament
 * <br/> Description: The class which describes functions and variables of login dialog.
 * 
 * @author Benny Shen
 */
public class Login extends JDialog {
	private Boolean firstClick = true;															//first click boolean for hiding instructions
	private JPanel iconInter = new JPanel();													//interface panel which display "CLICK TO ENTER" message
	private JLabel bgi = new JLabel(new ImageIcon("loginBgi.png"));								//background image of login interface
	private JLabel startButton = new JLabel("CLICK TO ENTER",SwingConstants.CENTER);			//label prompt the user to click to login
	private JPanel loginInter = new JPanel();													//JPanel for login interface
	JTextField loginText = new JTextField("Please enter a username");					//textfield for input of username
	JButton okButton = new JButton(new ImageIcon("lOkButton.png"));						//OK button to confirm username login
	private JLabel mouseEffect = new JLabel(new ImageIcon("lMouse.png"));						//selection effect for OK button
    public Login() {
    	 setUndecorated(true);//hide top bars of the dialog
    	 setBackground(new Color(0,0,0,0));//set dialog background transparent
    	 setResizable(false);//avoid user to change the size of dialog
         setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);//set action: dispose after login
         setSize(500,250);//set size of login interface
         startButton.setForeground(Color.white);//set start button
         startButton.setBounds(320,80,100,100);
         startButton.addMouseListener(new MouseListener(){//MouseListener for start button
			public void mouseClicked(MouseEvent arg0) {
				showLogin();//show login interface on click
			}//end method
			public void mouseEntered(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mousePressed(MouseEvent arg0) {}
			public void mouseReleased(MouseEvent arg0) {}
         });//end MouseListener
         iconInter.add(startButton);
         bgi.setBounds(0,0,500,250);//set background image
         iconInter.add(bgi);
         iconInter.setLayout(null);//set initial interface absolute layout
         iconInter.setBackground(new Color(0,0,0,0));//set initial interface transparent
         add(iconInter);//add initial interface
         loginInter.setVisible(false);//do not display login interface
         setLocationRelativeTo(null);//display dialog at center of the screen to attract attention
         getRootPane().setDefaultButton(okButton);
         
         setVisible(true);//display login dialog
    }
    /**
     * The procedure type method shows the login interface after clicking of "CLICK TO ENTER";
     */
    private void showLogin(){
    	remove(iconInter);//remove initial interface
    	loginInter.setBackground(new Color(0,0,0,0));//set login interface transparent
        loginInter.setLayout(null);//set absolute layout
        loginInter.setSize(loginInter.getPreferredSize());//set size of login interface
        loginText.setBounds(280,110,175,25);//set login text field
        loginText.setBackground(new Color(10,10,10));
        loginText.setBorder(new LineBorder(new Color(200,200,200)));
        loginText.setForeground(new Color(100,100,100));
        loginText.addMouseListener(new MouseListener(){//MouseListener for login text field
			public void mouseClicked(MouseEvent arg0) {//hide the instruction on first click
				if(firstClick){
					loginText.setText("");
					firstClick= false;
				}//end if
			}//end method
			public void mouseEntered(MouseEvent e) {
			}
			public void mouseExited(MouseEvent e) {
			}
			public void mousePressed(MouseEvent e) {
			}
			public void mouseReleased(MouseEvent e) {
			}
        });//end MouseListener
        loginInter.add(loginText);
        okButton.setBounds(350,144,40,20);//set OK Button
        okButton.setBorder(new LineBorder(new Color(200,200,200)));
        okButton.addMouseListener(new MouseListener(){//MouseListener for OK Button
			public void mouseClicked(MouseEvent e) {}//*no need to hide after click since will not enter login interface before closing the game
			public void mouseEntered(MouseEvent e) {
				mouseEffect.setVisible(true);//visualize the selecting effect for okButton
			}//end method
			public void mouseExited(MouseEvent e) {
				mouseEffect.setVisible(false);//hide the effect when okButton is not selected
			}//end method
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
        });//end MouseListener
        loginInter.add(okButton);
        loginInter.add(mouseEffect);//add login interface
		mouseEffect.setVisible(false);//hide effect for button
        loginInter.add(bgi);//add background image
        mouseEffect.setBounds(345,139,50,30);//set effect for button
        add(loginInter);//add login interface
		loginInter.setVisible(true);//display login interface
		repaint();//refresh the interface
    }//end method
}//end class