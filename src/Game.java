<<<<<<< HEAD:src/Game.java
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.LineBorder;


import java.util.*;
import java.io.*;

public class Game extends JPanel{
	Insets bInsets = getInsets();
    static Boolean userTurn = true;
    static boolean firstClick = true; 
    static JLabel winWord = new JLabel(new ImageIcon("gWin.png"));
    static JLabel losWord = new JLabel(new ImageIcon("gLose.png"));
    JLabel bgi = new JLabel(new ImageIcon("gameBgi.png"));
	JLabel timerBoard = new JLabel(new ImageIcon("gTimer.png"));
	static JLabel timerLabel = new JLabel("00:00:00");
	static JLabel mapSeparator = new JLabel(new ImageIcon("gSeparator.png"));
	static JLabel uHit = new JLabel("0");
	static JLabel uMis = new JLabel("0");
	static JLabel eHit = new JLabel("0");
	static JLabel eMis = new JLabel("0");
	static Timer timer;
	static long timeUsed = 0;
	long lastRecordTime = System.currentTimeMillis();
    JLabel backButton = new JLabel(new ImageIcon("theBackButton.png"));
    JLabel startButton = new JLabel(new ImageIcon("gStartButton.png"));
    JLabel leaveButton = new JLabel(new ImageIcon("gLeaveButton.png"));
    JLabel userBoard = new JLabel(new ImageIcon("gUserSide.png"));
    JLabel enemBoard = new JLabel(new ImageIcon("gEnemySide.png"));
    String[] labelStr = {"1","2","3","4","5","6","7","8","9","10","A","B","C","D","E","F","G","H","I","J"};
    final static Color darkBlue = new Color(0,0,40);
    final static Color fogBlue = new Color(80,80,180);
    final static Color darkRed = new Color(150,40,40);
    static JLabel[][] userMap = new JLabel[10][10];
    static JLabel[][] enemMap = new JLabel[10][10];
    JLabel[] mapLabels = new JLabel[40];
    ArrayList<JLabel> gButtons = new ArrayList<JLabel>();
    ArrayList<JLabel> buttonEffects = new ArrayList<JLabel>();
	static MouseListener hide = new MouseListener(){
		public void mouseClicked(MouseEvent arg0) {
			((JLabel)arg0.getSource()).setVisible(false);
		}
		public void mouseEntered(MouseEvent arg0) {
		}
		public void mouseExited(MouseEvent arg0) {
		}
		public void mousePressed(MouseEvent arg0) {
		}
		public void mouseReleased(MouseEvent arg0) {
		}
		
	};
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
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
    };
	MouseListener unitDis = new MouseListener(){
		public void mouseClicked(MouseEvent e) {
			if(!system.inGame){
				JLabel source = (JLabel)e.getSource();
				setDis(source);
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
	MouseListener unitFire = new MouseListener(){
		public void mouseClicked(MouseEvent e) {
			System.out.println("Click at the time when : userTurn?"+userTurn+" inGame?"+system.inGame);
			if((!userTurn)&&system.inGame&&firstClick){
				system.AIRound();
				System.out.println(uHit.getText().equals("17") +" "+eHit.getText().equals("17"));
				if(uHit.getText().equals("17") ||eHit.getText().equals("17")){
					system.checkWin();
				}
				if(system.AIcombat){
					return;
				}
			}
			if(userTurn&&system.inGame){
				JLabel source = (JLabel)e.getSource();
				if(source.getBackground().equals(fogBlue)){//when the unit is not hit yet
					System.out.println("Round "+system.round+". Your turn.\nEnter coordinates to fire:");
					int hitX = 0;
					int hitY = 0;
					for(int i=0;i<10;i++){
						boolean found = false;
						for(int j =0;j<10;j++){
							if(enemMap[i][j].equals(source)){
								hitX = j;
								hitY = i;
								found = true;
								break;
							}
						}
						if(found){
							break;
						}
					}
					system.input = system.convertMessage(hitX, hitY);
					System.out.println("----Hit: "+hitX+" "+hitY+" "+system.input);
					if(system.areYouSure(system.input)){
						system.AIcheck(!(firstClick)&&(system.firstHand.equals("User")));
						system.AIRound();
						System.out.println(uHit.getText().equals("17")+" "+eHit.getText().equals("17"));
						if(uHit.getText().equals("17")||eHit.getText().equals("17")){
							system.checkWin();
						}
					}
					if(firstClick){
						firstClick = false;
					}
				}//end if
			}
		}//end method
		public void mouseEntered(MouseEvent e) {
		}
		public void mouseExited(MouseEvent e) {
		}
		public void mousePressed(MouseEvent e) {
		}
		public void mouseReleased(MouseEvent e) {
		}
	};
	
    public Game(){
		setSize(1300,700);
		setLayout(null);

		winWord.setBounds(bInsets.left,bInsets.top,1300,700);
		winWord.addMouseListener(hide);
		add(winWord);
		winWord.setVisible(false);
		losWord.setBounds(bInsets.left,bInsets.top,1300,700);
		losWord.addMouseListener(hide);
		add(losWord);
		losWord.setVisible(false);
		setMap(true);
		mapSeparator.setBounds(bInsets.left+647,bInsets.top+125,6,525);
		add(mapSeparator);
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
		uHit.setBounds(bInsets.left+420,bInsets.top+15,40,40);
		uHit.setForeground(Color.white);
		uHit.setHorizontalAlignment(JLabel.CENTER);
		uHit.setFont(new Font(uHit.getFont().getName(),Font.PLAIN,30));
		add(uHit);
		uMis.setBounds(bInsets.left+480,bInsets.top+55,40,40);
		uMis.setForeground(Color.white);
		uMis.setHorizontalAlignment(JLabel.CENTER);
		uMis.setFont(new Font(uMis.getFont().getName(),Font.PLAIN,30));
		add(uMis);
		eHit.setBounds(bInsets.left+840,bInsets.top+15,40,40);
		eHit.setForeground(Color.white);
		eHit.setHorizontalAlignment(JLabel.CENTER);
		eHit.setFont(new Font(eHit.getFont().getName(),Font.PLAIN,30));
		add(eHit);
		eMis.setBounds(bInsets.left+780,bInsets.top+55,40,40);
		eMis.setForeground(Color.white);
		eMis.setHorizontalAlignment(JLabel.CENTER);
		eMis.setFont(new Font(eMis.getFont().getName(),Font.PLAIN,30));
		add(eMis);
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
    public void reset(){
    	setMap(false);
    	uHit.setText("0");
    	uMis.setText("0");
    	eHit.setText("0");
    	eMis.setText("0");
    	timerLabel.setText("00:00:00");
    	Battleship.enemyShipsSunk = new boolean[Battleship.shipLengths.length];
    	Battleship.homeShipsSunk = new boolean[Battleship.shipLengths.length];
    	Battleship.enemyGrid = new Square[Battleship.boardSizeXY[0]][Battleship.boardSizeXY[1]];
    	Battleship.homeGrid = new Square[Battleship.boardSizeXY[0]][Battleship.boardSizeXY[1]];
    	Battleship.homeShips = new Ship[Battleship.shipLengths.length];
    	Battleship.enemyShips = new Ship[Battleship.shipLengths.length];
    	Battleship.enemyShotLog = new ArrayList<Square>(20);
    	Battleship.homeShotLog = new ArrayList<Square>(20);
    	Battleship.usedShipNames = new ArrayList<String>(Battleship.shipNames.length);
    	
    }
    /**
     * The procedure type method shadows the ship onto the grid by marking covered units green.
     * @param theShip the target ship
     * @param shipLength the length of the ship with format of positive indicates horizontal, negative indicates vertical
     */
    public void markShip(JLabel theShip, int shipLength, Color shadow){
    	int unitX = (theShip.getX()-150)/50;//calculate the starting unit of at ship's origin
    	int unitY = (theShip.getY()-150)/50;
    	int lengthX = 1;//create 2 int variable to store the length of the shadow in units
    	int lengthY = 1;
    	if(shipLength>0){//if horizontal
    		lengthX = shipLength;//reset horizontal length
    	}else{//if vertical
    		lengthY = -shipLength;//reset vertical length
    	}//end if
//    	System.out.println(theShip.getX()+" "+theShip.getY());
//    	System.out.println(unitX+" "+unitY+" "+lengthX+" "+lengthY);
    	for(int i =unitX;i<unitX+lengthX;i++){
    		for(int j =unitY;j<unitY+lengthY;j++){
//    			System.out.println(i+" "+j);
    			userMap[j][i].setBackground(shadow);
    		}//end for
    	}//end for
    	theShip.setBackground(darkRed);;
    }//end method
	public void setMap(boolean firstTime){
		if(firstTime){
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
		}
		for(int i=0;i<10;i++){
			for(int j=0;j<10;j++){
				if(firstTime){
					userMap[i][j]=new JLabel();
					enemMap[i][j]=new JLabel();
					userMap[i][j].setHorizontalAlignment(JLabel.CENTER);
					userMap[i][j].setBounds(bInsets.left+150+50*j,bInsets.top+150+50*i,50,50);
					enemMap[i][j].setBounds(bInsets.left+650+50*j,bInsets.top+150+50*i,50,50);

					userMap[i][j].setOpaque(true);
					enemMap[i][j].setOpaque(true);
					userMap[i][j].addMouseListener(unitDis);
					enemMap[i][j].addMouseListener(unitFire);
					add(userMap[i][j]);
					add(enemMap[i][j]);
				}
				if(!firstTime){
					userMap[i][j].setText("");
					enemMap[i][j].setText("");
				}
				userMap[i][j].setBorder(new LineBorder(Color.white));
				enemMap[i][j].setBorder(new LineBorder(Color.white));
				userMap[i][j].setBackground(darkBlue);
				enemMap[i][j].setBackground(fogBlue);
			}
		}
	}
	/**
	 * The return type method convert a coordinate of a ship to legal coordinates which fits the map and return -1 if out of range.
	 * @param shipLength the length of ship, follows + if horizontal, - if vertical
	 * @param coordinate the coordinate of the ship, follows + if x, - if y
	 * @return the converted coordinate or -1s
	 */
	public int convertCoor(int shipLength, int coordinate){
		if(coordinate<0){
			System.out.println(shipLength+" y"+coordinate);
		}else{
			System.out.println(shipLength+" x"+coordinate);
		}
		if(Math.abs(coordinate)<150){
			return -1;
		}//when the new origin locates at the top or left of map: illegal
		if(shipLength*coordinate>0){//in direction of the ship,
			if(coordinate>650-shipLength*50){
				return -1;
			}//if the ship goes out of the map: illegal
		}else{//in direction perpendicular to the ship
			if(coordinate>600){
				return -1;
			}//if the ship goes out of the map: illegal
		}//end if
		return (coordinate/50)*50;
	}//end method
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
	private static int setDis(JLabel theUnit){
		int colorIndex = -1;
			for(int i =0;i<6;i++){
				if(theUnit.getBackground().equals(system.unitColor[i])){
					colorIndex = i+1;
					break;
				}
			}
		theUnit.setBackground(system.unitColor[colorIndex]);
		if(colorIndex>3){
			theUnit.setForeground(Color.white);
		}else{
			theUnit.setForeground(Color.black);
		}
		if(colorIndex==6){
			theUnit.setText("");
		}else{
			theUnit.setText(Battleship.shipNames[colorIndex-1].substring(0,2));
		}
		return -1;
	}
	public static void countIncre(JLabel var){
		var.setText(Integer.toString(Integer.parseInt(var.getText())+1));
	}
	
}
=======
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

import java.util.*;
import java.io.*;

public class game extends JPanel {
	Insets bInsets = getInsets();
	static Boolean userTurn = null;
	static boolean firstClick = true;
	static JLabel winWord = new JLabel(new ImageIcon("gWin.png"));
	static JLabel losWord = new JLabel(new ImageIcon("gLose.png"));
	JLabel bgi = new JLabel(new ImageIcon("gameBgi.png"));
	JLabel timerBoard = new JLabel(new ImageIcon("gTimer.png"));
	static JLabel timerLabel = new JLabel("00:00:00");
	static JLabel mapSeparator = new JLabel(new ImageIcon("gSeparator.png"));
	static JLabel uHit = new JLabel("0");
	static JLabel uMis = new JLabel("0");
	static JLabel eHit = new JLabel("0");
	static JLabel eMis = new JLabel("0");
	static Timer timer;
	static long timeUsed = 0;
	long lastRecordTime = System.currentTimeMillis();
	JLabel backButton = new JLabel(new ImageIcon("theBackButton.png"));
	JLabel startButton = new JLabel(new ImageIcon("gStartButton.png"));
	JLabel leaveButton = new JLabel(new ImageIcon("gLeaveButton.png"));
	JLabel userBoard = new JLabel(new ImageIcon("gUserSide.png"));
	JLabel enemBoard = new JLabel(new ImageIcon("gEnemySide.png"));
	String[] labelStr = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "A", "B", "C", "D", "E", "F", "G", "H",
			"I", "J" };
	final static Color darkBlue = new Color(0, 0, 40);
	final static Color fogBlue = new Color(80, 80, 180);
	final static Color darkRed = new Color(150, 40, 40);
	static JLabel[][] userMap = new JLabel[10][10];
	static JLabel[][] enemMap = new JLabel[10][10];
	JLabel[] mapLabels = new JLabel[40];
	ArrayList<JLabel> gButtons = new ArrayList<JLabel>();
	ArrayList<JLabel> buttonEffects = new ArrayList<JLabel>();
	static MouseListener hide = new MouseListener() {
		public void mouseClicked(MouseEvent arg0) {
			((JLabel) arg0.getSource()).setVisible(false);
		}

		public void mouseEntered(MouseEvent arg0) {
		}

		public void mouseExited(MouseEvent arg0) {
		}

		public void mousePressed(MouseEvent arg0) {
		}

		public void mouseReleased(MouseEvent arg0) {
		}

	};
	MouseListener mouseEffect = new MouseListener() {
		public void mouseClicked(MouseEvent e) {
			try {
				int i = gButtons.indexOf((JLabel) e.getSource());
				buttonEffects.get(i).setVisible(false);
			} catch (Exception exp) {
			}
		}

		public void mouseEntered(MouseEvent e) {
			int i = gButtons.indexOf((JLabel) e.getSource());
			buttonEffects.get(i).setVisible(true);
		}

		public void mouseExited(MouseEvent e) {
			int i = gButtons.indexOf((JLabel) e.getSource());
			buttonEffects.get(i).setVisible(false);
		}

		public void mousePressed(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}
	};
	MouseListener unitDis = new MouseListener() {
		public void mouseClicked(MouseEvent e) {
			if (!system.inGame) {
				JLabel source = (JLabel) e.getSource();
				setDis(source);
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
	MouseListener unitFire = new MouseListener() {
		public void mouseClicked(MouseEvent e) {
			System.out.println("Click at the time when : userTurn?" + userTurn + " inGame?" + system.inGame);
			if ((!userTurn) && system.inGame && firstClick) {
				system.AIRound();
				System.out.println(uHit.getText().equals("17") + " " + eHit.getText().equals("17"));
				if (uHit.getText().equals("17") || eHit.getText().equals("17")) {
					system.checkWin();
				}
				if (system.AIcombat) {
					return;
				}
			}
			if (userTurn && system.inGame) {
				JLabel source = (JLabel) e.getSource();
				if (source.getBackground().equals(fogBlue)) {// when the unit is not hit yet
					System.out.println("Round " + system.round + ". Your turn.\nEnter coordinates to fire:");
					int hitX = 0;
					int hitY = 0;
					for (int i = 0; i < 10; i++) {
						boolean found = false;
						for (int j = 0; j < 10; j++) {
							if (enemMap[i][j].equals(source)) {
								hitX = j;
								hitY = i;
								found = true;
								break;
							}
						}
						if (found) {
							break;
						}
					}
					system.input = system.convertMessage(hitX, hitY);
					System.out.println("----Hit: " + hitX + " " + hitY + " " + system.input);
					if (system.areYouSure(system.input)) {
						system.AIcheck(!(firstClick) && (system.firstHand.equals("User")));
						system.AIRound();
						System.out.println(uHit.getText().equals("17") + " " + eHit.getText().equals("17"));
						if (uHit.getText().equals("17") || eHit.getText().equals("17")) {
							system.checkWin();
						}
					}
					if (firstClick) {
						firstClick = false;
					}
				} // end if
			}
		}// end method

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		public void mousePressed(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}
	};

	public game() {
		setSize(1300, 700);
		setLayout(null);

		winWord.setBounds(bInsets.left, bInsets.top, 1300, 700);
		winWord.addMouseListener(hide);
		add(winWord);
		winWord.setVisible(false);
		losWord.setBounds(bInsets.left, bInsets.top, 1300, 700);
		losWord.addMouseListener(hide);
		add(losWord);
		losWord.setVisible(false);
		setMap(true);
		mapSeparator.setBounds(bInsets.left + 647, bInsets.top + 125, 6, 525);
		add(mapSeparator);
		gButtons.add(backButton);
		gButtons.add(startButton);
		gButtons.add(leaveButton);
		for (int i = 0; i < 3; i++) {
			buttonEffects.add(new JLabel(new ImageIcon("gMouse.png")));
			buttonEffects.get(i).setBounds(bInsets.left + i * 120, bInsets.top, 120, 80);
			buttonEffects.get(i).setVisible(false);
			add(buttonEffects.get(i));
			gButtons.get(i).addMouseListener(mouseEffect);
			gButtons.get(i).setBounds(bInsets.left + 10 + i * 120, bInsets.top + 10, 100, 60);
			add(gButtons.get(i));
		}
		timerBoard.setBounds(bInsets.left + 550, bInsets.top, 200, 100);
		timerLabel.setBounds(bInsets.left + 550, bInsets.top, 200, 100);
		add(timerLabel);
		add(timerBoard);
		timerLabel.setForeground(Color.white);
		timerLabel.setHorizontalAlignment(JLabel.CENTER);
		timerLabel.setFont(new Font(timerLabel.getFont().getName(), Font.PLAIN, 40));
		uHit.setBounds(bInsets.left + 420, bInsets.top + 15, 40, 40);
		uHit.setForeground(Color.white);
		uHit.setHorizontalAlignment(JLabel.CENTER);
		uHit.setFont(new Font(uHit.getFont().getName(), Font.PLAIN, 30));
		add(uHit);
		uMis.setBounds(bInsets.left + 480, bInsets.top + 55, 40, 40);
		uMis.setForeground(Color.white);
		uMis.setHorizontalAlignment(JLabel.CENTER);
		uMis.setFont(new Font(uMis.getFont().getName(), Font.PLAIN, 30));
		add(uMis);
		eHit.setBounds(bInsets.left + 840, bInsets.top + 15, 40, 40);
		eHit.setForeground(Color.white);
		eHit.setHorizontalAlignment(JLabel.CENTER);
		eHit.setFont(new Font(eHit.getFont().getName(), Font.PLAIN, 30));
		add(eHit);
		eMis.setBounds(bInsets.left + 780, bInsets.top + 55, 40, 40);
		eMis.setForeground(Color.white);
		eMis.setHorizontalAlignment(JLabel.CENTER);
		eMis.setFont(new Font(eMis.getFont().getName(), Font.PLAIN, 30));
		add(eMis);
		timer = new Timer(1000, new ActionListener() {// initialize the timer
			public void actionPerformed(ActionEvent e) {
				long thisTime = System.currentTimeMillis();// record the current time
				timeUsed += thisTime - lastRecordTime;// add the new time interval to sum of time used
				timerLabel.setText(formatTime(timeUsed));// display the calculated time used
				lastRecordTime = thisTime;// record this action of recording
			}// end method
		});

		userBoard.setBounds(bInsets.left + 150, bInsets.top, 400, 125);
		add(userBoard);
		enemBoard.setBounds(bInsets.left + 750, bInsets.top, 400, 125);
		add(enemBoard);

		bgi.setBounds(bInsets.left, bInsets.top, 1300, 700);
		add(bgi);
		setBackground(Color.DARK_GRAY);
		setVisible(true);

	}

	public void reset() {
		setMap(false);
		uHit.setText("0");
		uMis.setText("0");
		eHit.setText("0");
		eMis.setText("0");
		timerLabel.setText("00:00:00");
		Battleship.enemyShipsSunk = new boolean[Battleship.shipLengths.length];
		Battleship.homeShipsSunk = new boolean[Battleship.shipLengths.length];
		Battleship.enemyGrid = new Square[Battleship.boardSizeXY[0]][Battleship.boardSizeXY[1]];
		Battleship.homeGrid = new Square[Battleship.boardSizeXY[0]][Battleship.boardSizeXY[1]];
		Battleship.homeShips = new Ship[Battleship.shipLengths.length];
		Battleship.enemyShips = new Ship[Battleship.shipLengths.length];
		Battleship.enemyShotLog = new ArrayList<Square>(20);
		Battleship.homeShotLog = new ArrayList<Square>(20);
		Battleship.usedShipNames = new ArrayList<String>(Battleship.shipNames.length);

	}

	/**
	 * The procedure type method shadows the ship onto the grid by marking covered
	 * units green.
	 * 
	 * @param theShip    the target ship
	 * @param shipLength the length of the ship with format of positive indicates
	 *                   horizontal, negative indicates vertical
	 */
	public void markShip(JLabel theShip, int shipLength, Color shadow) {
		int unitX = (theShip.getX() - 150) / 50;// calculate the starting unit of at ship's origin
		int unitY = (theShip.getY() - 150) / 50;
		int lengthX = 1;// create 2 int variable to store the length of the shadow in units
		int lengthY = 1;
		if (shipLength > 0) {// if horizontal
			lengthX = shipLength;// reset horizontal length
		} else {// if vertical
			lengthY = -shipLength;// reset vertical length
		} // end if
//    	System.out.println(theShip.getX()+" "+theShip.getY());
//    	System.out.println(unitX+" "+unitY+" "+lengthX+" "+lengthY);
		for (int i = unitX; i < unitX + lengthX; i++) {
			for (int j = unitY; j < unitY + lengthY; j++) {
//    			System.out.println(i+" "+j);
				userMap[j][i].setBackground(shadow);
			} // end for
		} // end for
		theShip.setBackground(darkRed);
		;
	}// end method

	public void setMap(boolean firstTime) {
		if (firstTime) {
			for (int i = 0; i < 40; i++) {
				mapLabels[i] = new JLabel(labelStr[i % 20]);
				mapLabels[i].setHorizontalAlignment(JLabel.CENTER);
				mapLabels[i].setVerticalAlignment(JLabel.CENTER);
				add(mapLabels[i]);
			}
			for (int i = 0; i < 20; i++) {
				mapLabels[i].setForeground(Color.white);
				mapLabels[i].setBorder(new LineBorder(Color.white));
				if (i < 10) {
					mapLabels[i].setBounds(bInsets.left + 150 + i * 50, bInsets.top + 125, 50, 25);
				} else {
					mapLabels[i].setBounds(bInsets.left + 125, bInsets.top + 150 + i % 10 * 50, 25, 50);
				}
			}
			for (int i = 20; i < 40; i++) {
				mapLabels[i].setForeground(Color.white);
				mapLabels[i].setBorder(new LineBorder(Color.white));
				if (i < 30) {
					mapLabels[i].setBounds(bInsets.left + 650 + i % 10 * 50, bInsets.top + 125, 50, 25);
				} else {
					mapLabels[i].setBounds(bInsets.left + 1150, bInsets.top + 150 + i % 10 * 50, 25, 50);
				}
			}
		}
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (firstTime) {
					userMap[i][j] = new JLabel();
					enemMap[i][j] = new JLabel();
					userMap[i][j].setHorizontalAlignment(JLabel.CENTER);
					userMap[i][j].setBounds(bInsets.left + 150 + 50 * j, bInsets.top + 150 + 50 * i, 50, 50);
					enemMap[i][j].setBounds(bInsets.left + 650 + 50 * j, bInsets.top + 150 + 50 * i, 50, 50);

					userMap[i][j].setOpaque(true);
					enemMap[i][j].setOpaque(true);
					userMap[i][j].addMouseListener(unitDis);
					enemMap[i][j].addMouseListener(unitFire);
					add(userMap[i][j]);
					add(enemMap[i][j]);
				}
				if (!firstTime) {
					userMap[i][j].setText("");
					enemMap[i][j].setText("");
				}
				userMap[i][j].setBorder(new LineBorder(Color.white));
				enemMap[i][j].setBorder(new LineBorder(Color.white));
				userMap[i][j].setBackground(darkBlue);
				enemMap[i][j].setBackground(fogBlue);
			}
		}
	}

	/**
	 * The return type method convert a coordinate of a ship to legal coordinates
	 * which fits the map and return -1 if out of range.
	 * 
	 * @param shipLength the length of ship, follows + if horizontal, - if vertical
	 * @param coordinate the coordinate of the ship, follows + if x, - if y
	 * @return the converted coordinate or -1s
	 */
	public int convertCoor(int shipLength, int coordinate) {
		if (coordinate < 0) {
			System.out.println(shipLength + " y" + coordinate);
		} else {
			System.out.println(shipLength + " x" + coordinate);
		}
		if (Math.abs(coordinate) < 150) {
			return -1;
		} // when the new origin locates at the top or left of map: illegal
		if (shipLength * coordinate > 0) {// in direction of the ship,
			if (coordinate > 650 - shipLength * 50) {
				return -1;
			} // if the ship goes out of the map: illegal
		} else {// in direction perpendicular to the ship
			if (coordinate > 600) {
				return -1;
			} // if the ship goes out of the map: illegal
		} // end if
		return (coordinate / 50) * 50;
	}// end method

	/**
	 * The return type method returns the formated String value of long variable for
	 * the number of milliseconds.
	 * 
	 * @param secondNum long which is the number of milliseconds
	 * @return String value in hours:minutes:seconds format
	 */
	private String formatTime(long secondNum) {
		secondNum /= 1000;// convert millisecond to seconds
		String[] timeInfo = new String[3];// store the converted number in a String array
		timeInfo[0] = Long.toString(secondNum / 3600);// calculate number of hours
		timeInfo[1] = Long.toString((secondNum % 3600) / 60);// calculate the number of minutes
		timeInfo[2] = Long.toString(secondNum % 60);// calculate the number of seconds
		String formatedTime = "";// create a String to return the result
		// for converted number, add to the result by format of no less than two digits
		for (int i = 0; i < 3; i++) {
			if (timeInfo[i].length() == 1) {
				formatedTime += "0";
			} // end if
			formatedTime += (timeInfo[i] + ":");
		} // end for
		return formatedTime.substring(0, formatedTime.length() - 1);// returnt the answer
	}// end method

	private static int setDis(JLabel theUnit) {
		int colorIndex = -1;
		for (int i = 0; i < 6; i++) {
			if (theUnit.getBackground().equals(system.unitColor[i])) {
				colorIndex = i + 1;
				break;
			}
		}
		theUnit.setBackground(system.unitColor[colorIndex]);
		if (colorIndex > 3) {
			theUnit.setForeground(Color.white);
		} else {
			theUnit.setForeground(Color.black);
		}
		if (colorIndex == 6) {
			theUnit.setText("");
		} else {
			theUnit.setText(Battleship.shipNames[colorIndex - 1].substring(0, 2));
		}
		return -1;
	}

	public static void countIncre(JLabel var) {
		var.setText(Integer.toString(Integer.parseInt(var.getText()) + 1));
	}

	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.add(new game());
		f.setSize(f.getPreferredSize());
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}

}
>>>>>>> fcd74addd94d754b4c50e48ab9e2bc3288eca85a:src/game.java
