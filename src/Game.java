import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.LineBorder;


import java.util.*;
/**
 * File: Game.java
 * <p>Mr. Anadarajan
 * <br/>ICS4U1
 * <br/>June 15, 2018
 * 
 * <p>Final Evaluation: Battleship Tournament
 * <br/> Description: The class which describes functions and variables of game interface.
 * 
 * @author Benny Shen
 */
public class Game extends JPanel{
	Insets bInsets = getInsets();												//coordinates of game interface boundary
    static Boolean userTurn = true;												//if this is a user's turn/if user is allowed to operate game
    static boolean firstClick = true; 											//if the first click of the game has been clicked
    static JLabel winWord = new JLabel(new ImageIcon("gWin.png"));				//win message
    static JLabel losWord = new JLabel(new ImageIcon("gLose.png"));				//lose message
    JLabel bgi = new JLabel(new ImageIcon("gameBgi.png"));						//background image of game interface
	JLabel timerBoard = new JLabel(new ImageIcon("gTimer.png"));				//background for timer
	static JLabel timerLabel = new JLabel("00:00:00");							//label for displaying time
	static JLabel mapSeparator = new JLabel(new ImageIcon("gSeparator.png"));	//separator between maps
	static JLabel uHit = new JLabel("0");										//user number of hits
	static JLabel uMis = new JLabel("0");										//user number of miss
	static JLabel eHit = new JLabel("0");										//AI number of hits
	static JLabel eMis = new JLabel("0");										//AI number of miss
	static Timer timer;															//timer recording gaming time
	static long timeUsed = 0;													//time used in game
	long lastRecordTime = System.currentTimeMillis();							//last recorded time
    JLabel backButton = new JLabel(new ImageIcon("theBackButton.png"));			//back button
    JLabel startButton = new JLabel(new ImageIcon("gStartButton.png"));			//start button
    JLabel userBoard = new JLabel(new ImageIcon("gUserSide.png"));				//user board
    JLabel enemBoard = new JLabel(new ImageIcon("gEnemySide.png"));				//enemy board
    String[] labelStr = {"1","2","3","4","5","6","7","8","9","10","A","B","C","D","E","F","G","H","I","J"};//constants for labels of the maps
    final static Color darkBlue = new Color(0,0,40);							//constant dark blue color
    final static Color fogBlue = new Color(80,80,180);							//constant light blue color
    final static Color darkRed = new Color(150,40,40);							//constant customized red color	
    static JLabel[][] userMap = new JLabel[10][10];								//user's map
    static JLabel[][] enemMap = new JLabel[10][10];								//enemy's map
    JLabel[] mapLabels = new JLabel[40];										//labels for columns and rows of the map
    ArrayList<JLabel> gButtons = new ArrayList<JLabel>();						//buttons at top left corner
    ArrayList<JLabel> buttonEffects = new ArrayList<JLabel>();					//effects for buttons at top left corner
	static MouseListener hide = new MouseListener(){//MouseListener which hide component on click
		public void mouseClicked(MouseEvent arg0) {
			((JLabel)arg0.getSource()).setVisible(false);//hide source
		}//end method
		public void mouseEntered(MouseEvent arg0) {
		}
		public void mouseExited(MouseEvent arg0) {
		}
		public void mousePressed(MouseEvent arg0) {
		}
		public void mouseReleased(MouseEvent arg0) {
		}
		
	};
    MouseListener mouseEffect = new MouseListener(){//MouseListener which display effect of selected when mouse on
		public void mouseClicked(MouseEvent e) {
			try{
				int i = gButtons.indexOf((JLabel)e.getSource());
				buttonEffects.get(i).setVisible(false);//hide effect after click
			}catch(Exception exp){
			}//end try catch
		}//end method
		public void mouseEntered(MouseEvent e) {
			int i = gButtons.indexOf((JLabel)e.getSource());
			buttonEffects.get(i).setVisible(true);//display effect when mouse entered
		}//end method
		public void mouseExited(MouseEvent e) {
			int i = gButtons.indexOf((JLabel)e.getSource());
			buttonEffects.get(i).setVisible(false);//hide effect after mouse exit
		}//end method
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
    };//end MouseListener
	MouseListener unitDis = new MouseListener(){//MouseListener for setting units when human against AI
		public void mouseClicked(MouseEvent e) {
			if(!system.inGame){//when the game is not on going
				JLabel source = (JLabel)e.getSource();
				setDis(source);//switch display of the unit to next on click
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
	};//end MouseListener
	MouseListener unitFire = new MouseListener(){//MouseListener for auto change of display of units on enemy grid
		public void mouseClicked(MouseEvent e) {
			if((!userTurn)&&system.inGame&&firstClick){
				system.AIRound();
				if(uHit.getText().equals("17") ||eHit.getText().equals("17")){
					system.checkWin();
				}//end if
				if(system.AIcombat){
					return;//if in AI combat abandon command for first click
				}//end if
			}//end if
			if(userTurn&&system.inGame){//if this is user's turn and game is on going
				JLabel source = (JLabel)e.getSource();//get the source
				if(source.getBackground().equals(fogBlue)){//when the unit is not hit yet
					int hitX = 0;
					int hitY = 0;
					for(int i=0;i<10;i++){//search for the source unit which has been hit
						boolean found = false;
						for(int j =0;j<10;j++){
							if(enemMap[i][j].equals(source)){
								hitX = j;
								hitY = i;
								found = true;
								break;
							}//end if
						}//end for
						if(found){
							break;
						}//end if
					}//end for
					system.input = system.convertMessage(hitX, hitY);//call convert message to input to game algorithm
					if(system.areYouSure(system.input)){
						system.AIcheck(!(firstClick)&&(system.firstHand.equals("User")));//AI check if hit
						system.AIRound();//AI shot and check hit
						System.out.println(uHit.getText().equals("17")+" "+eHit.getText().equals("17"));
						if(uHit.getText().equals("17")||eHit.getText().equals("17")){
							system.checkWin();//check if either side wins
						}//end if
					}//end if
					if(firstClick){
						firstClick = false;//record first click
					}//end if
				}//end if
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
	};
    public Game(){
		setSize(1300,700);//set size of game interface
		setLayout(null);//set absolute layout
		winWord.setBounds(bInsets.left,bInsets.top,1300,700);//set win message
		winWord.addMouseListener(hide);
		add(winWord);
		winWord.setVisible(false);//hide win message
		losWord.setBounds(bInsets.left,bInsets.top,1300,700);//set lost message
		losWord.addMouseListener(hide);
		add(losWord);
		losWord.setVisible(false);//hide lost message
		mapSeparator.setBounds(bInsets.left+647,bInsets.top+125,6,525);//set separator of maps
		add(mapSeparator);
		setMap(true);//initialize both maps
		gButtons.add(backButton);//add buttons to the ArrayList
		gButtons.add(startButton);
		for(int i =0;i<2;i++){//treat ArrayList to set all buttons and effects
			buttonEffects.add(new JLabel(new ImageIcon("gMouse.png")));//set new effects
			buttonEffects.get(i).setBounds(bInsets.left+i*120,bInsets.top,120,80);
			buttonEffects.get(i).setVisible(false);
			add(buttonEffects.get(i));
			gButtons.get(i).addMouseListener(mouseEffect);//set buttons
			gButtons.get(i).setBounds(bInsets.left+10+i*120,bInsets.top+10,100,60);
			add(gButtons.get(i));
		}//end for
		timerBoard.setBounds(bInsets.left+550,bInsets.top,200,100);//set timer background
		timerLabel.setBounds(bInsets.left+550,bInsets.top,200,100);//set timer numbers
		add(timerLabel);
		add(timerBoard);
		timerLabel.setForeground(Color.white);//format timer label text
		timerLabel.setHorizontalAlignment(JLabel.CENTER);
		timerLabel.setFont(new Font(timerLabel.getFont().getName(),Font.PLAIN,40));
		uHit.setBounds(bInsets.left+420,bInsets.top+15,40,40);//set user hit label
		uHit.setForeground(Color.white);
		uHit.setHorizontalAlignment(JLabel.CENTER);
		uHit.setFont(new Font(uHit.getFont().getName(),Font.PLAIN,30));
		add(uHit);
		uMis.setBounds(bInsets.left+480,bInsets.top+55,40,40);//set user miss label
		uMis.setForeground(Color.white);
		uMis.setHorizontalAlignment(JLabel.CENTER);
		uMis.setFont(new Font(uMis.getFont().getName(),Font.PLAIN,30));
		add(uMis);
		eHit.setBounds(bInsets.left+840,bInsets.top+15,40,40);//set enemy hit label
		eHit.setForeground(Color.white);
		eHit.setHorizontalAlignment(JLabel.CENTER);
		eHit.setFont(new Font(eHit.getFont().getName(),Font.PLAIN,30));
		add(eHit);
		eMis.setBounds(bInsets.left+780,bInsets.top+55,40,40);//set enemy miss label
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
		});//end Timer
		userBoard.setBounds(bInsets.left+150,bInsets.top,400,125);//set user board
		add(userBoard);
		enemBoard.setBounds(bInsets.left+750,bInsets.top,400,125);//set enemy board
		add(enemBoard);
		bgi.setBounds(bInsets.left,bInsets.top,1300,700);//set background image
		add(bgi);
		setVisible(true);//display interface
	}//end constructor
    /**
     * The procedure type method reset the entire game.
     */
    public void reset(){
    	setMap(false);//reset the maps
    	uHit.setText("0");//reset hits and misses labels
    	uMis.setText("0");
    	eHit.setText("0");
    	eMis.setText("0");
    	timerLabel.setText("00:00:00");//reset timer label
    	//reset variables of console game
    	Battleship.enemyShipsSunk = new boolean[Battleship.shipLengths.length];
    	Battleship.homeShipsSunk = new boolean[Battleship.shipLengths.length];
    	Battleship.enemyGrid = new Square[Battleship.boardSizeXY[0]][Battleship.boardSizeXY[1]];
    	Battleship.homeGrid = new Square[Battleship.boardSizeXY[0]][Battleship.boardSizeXY[1]];
    	Battleship.homeShips = new Ship[Battleship.shipLengths.length];
    	Battleship.enemyShips = new Ship[Battleship.shipLengths.length];
    	Battleship.enemyShotLog = new ArrayList<Square>(20);
    	Battleship.homeShotLog = new ArrayList<Square>(20);
    	Battleship.usedShipNames = new ArrayList<String>(Battleship.shipNames.length);
    }//end method
    /**
     * The procedure type method reset or initialize the map
     * @param firstTime if needs to initialize
     */
	public void setMap(boolean firstTime){
		if(firstTime){//if it is the first time
			for(int i =0;i<40;i++){//initialize map labels and add to interface
				mapLabels[i]=new JLabel(labelStr[i%20]);
				mapLabels[i].setHorizontalAlignment(JLabel.CENTER);
				mapLabels[i].setVerticalAlignment(JLabel.CENTER);
				add(mapLabels[i]);
			}//end for
			for(int i =0;i<20;i++){//initialize user map labels
				mapLabels[i].setForeground(Color.white);
				mapLabels[i].setBorder(new LineBorder(Color.white));
				if(i<10){
					mapLabels[i].setBounds(bInsets.left+150+i*50,bInsets.top+125,50,25);
				}else{
					mapLabels[i].setBounds(bInsets.left+125,bInsets.top+150+i%10*50,25,50);
				}//end if
			}//end for
			for(int i =20;i<40;i++){//initialize enemy map labelss
				mapLabels[i].setForeground(Color.white);
				mapLabels[i].setBorder(new LineBorder(Color.white));
				if(i<30){
					mapLabels[i].setBounds(bInsets.left+650+i%10*50,bInsets.top+125,50,25);
				}else{
					mapLabels[i].setBounds(bInsets.left+1150,bInsets.top+150+i%10*50,25,50);
				}//end if
			}//end for
		}//end if
		for(int i=0;i<10;i++){//set all units for both maps
			for(int j=0;j<10;j++){
				if(firstTime){//initialize and add if it is first time
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
				}//end if
				if(!firstTime){//reset text if not the first time
					userMap[i][j].setText("");
					enemMap[i][j].setText("");
				}//end if
				//(re)set colors of units
				userMap[i][j].setBorder(new LineBorder(Color.white));
				enemMap[i][j].setBorder(new LineBorder(Color.white));
				userMap[i][j].setBackground(darkBlue);
				enemMap[i][j].setBackground(fogBlue);
			}//end for
		}//end for
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
	/**
	 * The procedure type method set the unit as next ship units follows the order of shipNames in Battleship.java.
	 * @param theUnit the unit clicked
	 */
	private static void setDis(JLabel theUnit){
		int colorIndex = -1;
			for(int i =0;i<6;i++){//search for the current index
				if(theUnit.getBackground().equals(system.unitColor[i])){
					colorIndex = i+1;//increment the index
					break;//stop searching
				}//end if
			}//end for
		theUnit.setBackground(system.unitColor[colorIndex]);//set new background color
		if(colorIndex>3){
			theUnit.setForeground(Color.white);//set text color to form contrast between light and dark
		}else{
			theUnit.setForeground(Color.black);
		}//end if
		if(colorIndex==6){//clear text if the unit is clicked to change back to none
			theUnit.setText("");
		}else{
			theUnit.setText(Battleship.shipNames[colorIndex-1].substring(0,2));//set text to abbreviation of two letters of ship name
		}//end if
	}//end method
	/**
	 * The procedure type increase the value JLabel displays by 1.
	 * @param var the JLabel needs to be incremented
	 */
	public static void countIncre(JLabel var){
		var.setText(Integer.toString(Integer.parseInt(var.getText())+1));//set text to original value+1
	}//end method
	/**
	 * The return type method check if the ships are properly placed in human against AI mode and returns the error message or correct message.
	 * @return message which indicates the problem or true if the ships are placed properly
	 */
	public static String checkShip(){
		ArrayList<Integer>[] shipCoorsX = new ArrayList[5];//array of ArrayList to record x-coordinates of ship units
		ArrayList<Integer>[] shipCoorsY = new ArrayList[5];//array of ArrayList to record y-coordinates of ship units
		for(int i =0;i<5;i++){//initialize all ArrayLists of both arrays
			shipCoorsX[i] = new ArrayList<Integer>();
			shipCoorsY[i] = new ArrayList<Integer>();
		}//end for
		for(int i =0;i<10;i++){//loop through all units from left and top to record units coordinates in ascending order
			for(int j =0;j<10;j++){
				for(int k = 0;k<5;k++){//search for the ship color
					if(userMap[i][j].getText().equals(Battleship.shipNames[k].substring(0,2))){
						shipCoorsX[k].add(i);//record coordinates
						shipCoorsY[k].add(j);
						break;//stop searching color
					}//end if
				}//end for
			}//end for
		}//end for
		for(int i =0;i<5;i++){//search for each type of ship
			boolean isX = false;
			boolean isY = false;
			if(shipCoorsX[i].size()!=Battleship.shipLengths[i]){//if the number of units does not equals to length
				return Battleship.shipNames[i]+" is not placed properly.";//return error message
			}//end if
			for(int j =0;j<shipCoorsX[i].size()-1;j++){//for each units of the ship
				if(shipCoorsX[i].get(j)==shipCoorsX[i].get(j+1)){//if 2 x-coordinates are equal
					isX = true;//ship is vertical
				}else{
					if(isX){//if checked twice and the ship was vertical before but not now
						return Battleship.shipNames[i]+" is not in a line.";//return ship units are not in a line
					}else{
						if(shipCoorsX[i].get(j)==(shipCoorsX[i].get(j+1)-1)){//check if 2 x-coordinates are continuous in ascending order
							isY = true;//if so mark ship horizontal
						}else{//otherwise indicates x-coordinates are not in order
							return Battleship.shipNames[i]+" is not continuous";//return the ship is not in continuous order
						}//end if
					}//end if
				}//end if
				if(isY){//if the ship is marked horizontal
					if(shipCoorsY[i].get(j)!=shipCoorsY[i].get(j+1)){//if x-coordinate does equal
						return Battleship.shipNames[i]+" is not in a line.";//return that the ship is not in a horizontal line
					}//end if
				}//end if
				if(isX){//if the ship is marked vertical
					if(shipCoorsY[i].get(j)!=shipCoorsY[i].get(j+1)-1){//if the y-coordinates are not incrementing in ascending order ->not in proper order
						return Battleship.shipNames[i]+" is not continuous.";//return the ship units are not continuous
					}//end if
				}//end if
			}//end for
		}//end for
		return "true";//if no error was reported, return true
	}//end method
}//end class