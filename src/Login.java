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
 * <br/> Description: The class which describes functions and variables of login interface.
 * 
 * @author Benny Shen
 */
public class Login extends JDialog {
	Boolean firstClick = true;
	JPanel iconInter = new JPanel();
	JLabel bgi = new JLabel(new ImageIcon("loginBgi.png"));
	JLabel startButton = new JLabel("CLICK TO ENTER",SwingConstants.CENTER);
	JPanel loginInter = new JPanel();
	JTextField loginText = new JTextField("Please enter a username");
	JLabel okButton = new JLabel(new ImageIcon("lOkButton.png"));
	JLabel mouseEffect = new JLabel(new ImageIcon("lMouse.png"));
    public Login() {
    	 setUndecorated(true);
    	 setBackground(new Color(0,0,0,0));
    	 setResizable(false);
         setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
         setSize(500,250);
         okButton.setBackground(Color.yellow);
         startButton.setBackground(Color.LIGHT_GRAY);
         startButton.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent arg0) {
				showLogin();
			}
			public void mouseEntered(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mousePressed(MouseEvent arg0) {}
			public void mouseReleased(MouseEvent arg0) {}
         });
         iconInter.setLayout(null);
         iconInter.setBackground(new Color(0,0,0,0));
         bgi.setBounds(0,0,500,250);
         startButton.setForeground(Color.white);
         startButton.setBounds(320,80,100,100);
         iconInter.add(startButton);
         iconInter.add(bgi);
         add(iconInter);
         loginInter.setVisible(false);
         setLocationRelativeTo(null);
         setVisible(true);
    }
    public void showLogin(){
    	remove(iconInter);
    	loginInter.setBackground(new Color(0,0,0,0));
        loginInter.setLayout(null);
        loginInter.setSize(loginInter.getPreferredSize());
        loginText.setBounds(280,110,175,25);
        loginText.setBackground(new Color(10,10,10));
        loginText.setBorder(new LineBorder(new Color(200,200,200)));
        loginText.setForeground(new Color(100,100,100));
        loginText.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent arg0) {
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
        
        });
        loginInter.add(loginText);
        okButton.setBounds(350,144,40,20);
        okButton.setBorder(new LineBorder(new Color(200,200,200)));
        okButton.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {
				mouseEffect.setVisible(true);//visualize the selecting effect for okButton
			}
			public void mouseExited(MouseEvent e) {
				mouseEffect.setVisible(false);//hide the effect when okButton is not selected
			}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
        });
		loginInter.add(mouseEffect);
		mouseEffect.setVisible(false);
        loginInter.add(okButton);
        loginInter.add(bgi);
        mouseEffect.setBounds(345,139,50,30);
        add(loginInter);
		loginInter.setVisible(true);
		repaint();
		System.out.print(-1);
    }
}