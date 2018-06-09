import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

public class system extends JFrame {
	Boolean inGame = false;
	MouseListener directory = new MouseListener() {
		public void mouseClicked(MouseEvent event) {
			JLabel source = (JLabel)event.getSource();
			if (source.equals(baseInter.gameButton)) {
				System.out.println(-1);
				remove(baseInter);
				add(gameInter);
				if(!inGame){
					//will be modify to be round shape frame if there is time left
					Object[] options = {"User","AI"};
					int n = JOptionPane.showOptionDialog(null,
							"Battle started from:",
							"First hand",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE,
							new ImageIcon("gNani.png"),     
							options,  
							options[0]); 
					if(n==0){
						gameInter.userTurn = true;
					}else{
						gameInter.userTurn = false;
					}
				}
				repaint();
			}
			if(source.getIcon().toString().equals("theBackButton.png")){
				System.out.println(0);
				getContentPane().removeAll();;
				add(baseInter);
				repaint();
			}
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		public void mousePressed(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}
	};
	base baseInter = new base();
	game gameInter = new game();

	public system() {
		baseInter.gameButton.addMouseListener(directory);
		gameInter.backButton.addMouseListener(directory);
		add(baseInter);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		setSize(1300, 700);
		setVisible(true);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		system theGame = new system();
	}

}
