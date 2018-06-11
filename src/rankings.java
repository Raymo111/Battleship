import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

public class rankings extends JPanel {
    Insets bInsets = getInsets();

    JLabel bgi = new JLabel(new ImageIcon("rankBgi.jpg"));
    JLabel backButton = new JLabel(new ImageIcon("theBackButton.png"));
    JLabel buttonEffect = new JLabel(new ImageIcon("gMouse.png"));
    JLabel collectionButton = new JLabel(new ImageIcon("rCollection.png"));
    JLabel levelButton = new JLabel(new ImageIcon("rLevel.png"));
    JLabel winButton = new JLabel(new ImageIcon("rWin.png"));
    JLabel screen = new JLabel(new ImageIcon("rTotalBack.png"));
    JLabel userScreen = new JLabel(new ImageIcon("rUserBack.png"));
    ArrayList<JLabel> subTypes = new ArrayList<JLabel>();
    MouseListener mouseEffect = new MouseListener(){
		public void mouseClicked(MouseEvent e) {
			buttonEffect.setVisible(false);
		}
		public void mouseEntered(MouseEvent e) {
			buttonEffect.setVisible(true);
		}
		public void mouseExited(MouseEvent e) {
			buttonEffect.setVisible(false);
		}
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
    };
	public rankings(){
		setSize(1300,700);
		setLayout(null);
		backButton.setBounds(bInsets.left+10,bInsets.top+10,100,60);
		backButton.addMouseListener(mouseEffect);
		add(backButton);
		buttonEffect.setBounds(bInsets.left,bInsets.top,120,80);
		add(buttonEffect);
		buttonEffect.setVisible(false);
		subTypes.add(collectionButton);
		subTypes.add(levelButton);
		subTypes.add(winButton);
		for(int i =0;i<3;i++){
			subTypes.get(i).setBounds(bInsets.left+10,bInsets.top+110+i*170,120,160);
			add(subTypes.get(i));
		}//end for
		userScreen.setBounds(bInsets.left+185,bInsets.top+80,1050,200);
		add(userScreen);
		screen.setBounds(bInsets.left+135,bInsets.top+50,1150,600);
		add(screen);
		bgi.setBounds(bInsets.left,bInsets.top,1300,700);
		add(bgi);
		setBackground(Color.DARK_GRAY);
		setVisible(true);
	}
	public static void main(String[] args){
		JFrame f = new JFrame();
		f.add(new rankings());
		f.setSize(f.getPreferredSize());
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
}
