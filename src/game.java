import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

import java.util.*;

public class game extends JPanel{
    Insets bInsets = getInsets();
    Boolean userTurn = null;
    JLabel bgi = new JLabel(new ImageIcon("gameBgi.png"));
	JLabel timerBoard = new JLabel(new ImageIcon("gTimer.png"));
	JLabel timerLabel = new JLabel("00:00:00");
	Timer timer;
	long timeUsed = 0;
	long lastRecordTime = System.currentTimeMillis();
    JLabel backButton = new JLabel(new ImageIcon("theBackButton.png"));
    JLabel startButton = new JLabel(new ImageIcon("gStartButton.png"));
    JLabel leaveButton = new JLabel(new ImageIcon("gLeaveButton.png"));
    JLabel userBoard = new JLabel(new ImageIcon("gUserSide.png"));
    JLabel enemBoard = new JLabel(new ImageIcon("gEnemySide.png"));
    String[] labelStr = {"1","2","3","4","5","6","7","8","9","10","A","B","C","D","E","F","G","H","I","J"};
    Color darkBlue = new Color(0,0,40);
    Color fogBlue = new Color(0,0,80);
    Color darkRed = new Color(150,40,40);
    JLabel[][] userMap = new JLabel[10][10];
    JLabel[][] enemMap = new JLabel[10][10];
    JLabel[] mapLabels = new JLabel[40];
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
	MouseListener unitBehavior = new MouseListener(){
		public void mouseClicked(MouseEvent e) {
			if(!timer.isRunning()){
				timer.start();
			}
			JLabel source = (JLabel)e.getSource();
			//hits
			source.setBackground(darkRed);
			//misses
//			source.setBackground(((LineBorder)source.getBorder()).getLineColor());
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
	
    public game(){
		setSize(1300,700);
		setLayout(null);
		addMaps();
		gButtons.add(backButton);
		gButtons.add(startButton);
		gButtons.add(leaveButton);
		for(int i =0;i<3;i++){
			buttonEffects.add(new JLabel(new ImageIcon("gMouse.png")));
			buttonEffects.get(i).setBounds(bInsets.left+i*120,bInsets.top,120,80);
			buttonEffects.get(i).setVisible(false);
			add(buttonEffects.get(i));
			gButtons.get(i).addMouseListener(mouseEffect);
			gButtons.get(i).setBounds(bInsets.left+10+i*120,bInsets.top+10,100,60);
			add(gButtons.get(i));
		}
		timerBoard.setBounds(bInsets.left+550,bInsets.top,200,100);
		timerLabel.setBounds(bInsets.left+550,bInsets.top,200,100);
		add(timerLabel);
		add(timerBoard);
		timerLabel.setForeground(Color.white);
		timerLabel.setHorizontalAlignment(JLabel.CENTER);
		timerLabel.setFont(new Font(timerLabel.getFont().getName(),Font.PLAIN,40));
		timer = new Timer(1000, new ActionListener() {//initialize the timer
			public void actionPerformed(ActionEvent e) {
				long thisTime = System.currentTimeMillis();//record the current time
				timeUsed += thisTime - lastRecordTime;//add the new time interval to sum of time used
				timerLabel.setText(formatTime(timeUsed));//display the calculated time used
				lastRecordTime = thisTime;//record this action of recording
			}//end method
		});
		
		userBoard.setBounds(bInsets.left+150,bInsets.top,400,125);
		add(userBoard);
		enemBoard.setBounds(bInsets.left+750,bInsets.top,400,125);
		add(enemBoard);
		bgi.setBounds(bInsets.left,bInsets.top,1300,700);
		add(bgi);
		setBackground(Color.DARK_GRAY);
		setVisible(true);
		
	}
	public void addMaps(){
		for(int i =0;i<40;i++){
			mapLabels[i]=new JLabel(labelStr[i%20]);
			mapLabels[i].setHorizontalAlignment(JLabel.CENTER);
			mapLabels[i].setVerticalAlignment(JLabel.CENTER);
			add(mapLabels[i]);
		}
		for(int i =0;i<20;i++){
			mapLabels[i].setForeground(Color.white);
			mapLabels[i].setBorder(new LineBorder(Color.white));
			if(i<10){
				mapLabels[i].setBounds(bInsets.left+150+i*50,bInsets.top+125,50,25);
			}else{
				mapLabels[i].setBounds(bInsets.left+125,bInsets.top+150+i%10*50,25,50);
			}
		}
		for(int i =20;i<40;i++){
			mapLabels[i].setForeground(Color.white);
			mapLabels[i].setBorder(new LineBorder(Color.white));
			if(i<30){
				mapLabels[i].setBounds(bInsets.left+650+i%10*50,bInsets.top+125,50,25);
			}else{
				mapLabels[i].setBounds(bInsets.left+1150,bInsets.top+150+i%10*50,25,50);
			}
		}
		for(int i=0;i<10;i++){
			for(int j=0;j<10;j++){
				userMap[i][j]=new JLabel();
				enemMap[i][j]=new JLabel();
				userMap[i][j].setBounds(bInsets.left+150+50*j,bInsets.top+150+50*i,50,50);
				enemMap[i][j].setBounds(bInsets.left+650+50*j,bInsets.top+150+50*i,50,50);
				userMap[i][j].setBorder(new LineBorder(Color.white));
				enemMap[i][j].setBorder(new LineBorder(Color.white));
				userMap[i][j].setBackground(darkBlue);
				enemMap[i][j].setBackground(fogBlue);
				userMap[i][j].setOpaque(true);
				enemMap[i][j].setOpaque(true);
				userMap[i][j].addMouseListener(unitBehavior);
				enemMap[i][j].addMouseListener(unitBehavior);
				add(userMap[i][j]);
				add(enemMap[i][j]);
			}
		}
	}
	/**
	 * The return type method returns the formated String value of long variable for the number of milliseconds.
	 * @param secondNum long which is the number of milliseconds
	 * @return String value in hours:minutes:seconds format
	 */
	private String formatTime(long secondNum) {
		secondNum /= 1000;//convert millisecond to seconds
		String[] timeInfo = new String[3];//store the converted number in a String array
		timeInfo[0] = Long.toString(secondNum / 3600);//calculate number of hours
		timeInfo[1] = Long.toString((secondNum % 3600) / 60);//calculate the number of minutes
		timeInfo[2] = Long.toString(secondNum % 60);//calculate the number of seconds
		String formatedTime = "";//create a String to return the result
		//for converted number, add to the result by format of no less than two digits
		for (int i = 0; i < 3; i++) {
			if (timeInfo[i].length() == 1) {
				formatedTime += "0";
			}//end if
			formatedTime += (timeInfo[i] + ":");
		}//end for
		return formatedTime.substring(0, formatedTime.length() - 1);//returnt the answer
	}//end method
	public static void main(String[] args){
		JFrame f = new JFrame();
		f.add(new game());
		f.setSize(f.getPreferredSize());
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
	
}
