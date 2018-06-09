import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
import java.util.*;

public class game extends JPanel{
	JLabel timer = new JLabel(new ImageIcon("gTimer.png"));
    Insets bInsets = getInsets();
    JLabel backButton = new JLabel(new ImageIcon("theBackButton.png"));
    JLabel startButton = new JLabel(new ImageIcon("gStartButton.png"));
    JLabel leaveButton = new JLabel(new ImageIcon("gLeaveButton.png"));
    String[] rows = {"A","B","C","D","E","F","G","H","I","J"};
    String[] cols = {"1","2","3","4","5","6","7","8","9","10"};
    JLabel[][] userMap = new JLabel[10][10];
    JLabel[][] enemMap = new JLabel[10][10];
    ArrayList<JLabel> gButtons = new ArrayList<JLabel>();
    ArrayList<JLabel> buttonEffects = new ArrayList<JLabel>();
    MouseListener mouseEffect = new MouseListener(){

		public void mouseClicked(MouseEvent e) {
			try{
				int i = gButtons.indexOf((JLabel)e.getSource());
				buttonEffects.get(i).setVisible(false);
			}catch(Exception exp){
				
			}
		}

		public void mouseEntered(MouseEvent e) {
			int i = gButtons.indexOf((JLabel)e.getSource());
			buttonEffects.get(i).setVisible(true);
		}

		public void mouseExited(MouseEvent e) {
			int i = gButtons.indexOf((JLabel)e.getSource());
			buttonEffects.get(i).setVisible(false);
		}

		public void mousePressed(MouseEvent e) {
			
		}

		public void mouseReleased(MouseEvent e) {
			
		}
    	
    };
	public game(){
		setSize(1300,700);
		setLayout(null);
		gButtons.add(backButton);
		gButtons.add(startButton);
		gButtons.add(leaveButton);
		for(int i =0;i<3;i++){
			buttonEffects.add(new JLabel(new ImageIcon("gMouse.png")));
			buttonEffects.get(i).setBounds(bInsets.left+i*120,bInsets.top,120,80);
			add(buttonEffects.get(i));
			gButtons.get(i).addMouseListener(mouseEffect);
			gButtons.get(i).setBounds(bInsets.left+10+i*120,bInsets.top+10,100,60);
			add(gButtons.get(i));
			buttonEffects.get(i).setVisible(false);
		}
		timer.setBounds(bInsets.left+550,bInsets.top,200,100);
		add(timer);
		setBackground(Color.black);

		setVisible(true);
		
	}
	
}
