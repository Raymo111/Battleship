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
    final Color darkBlue = new Color(0,0,40);
    final Color fogBlue = new Color(0,0,80);
    final Color darkRed = new Color(150,40,40);
    final Color darkGreen = new Color(0,30,0);
    JLabel[][] userMap = new JLabel[10][10];
    JLabel[][] enemMap = new JLabel[10][10];
    JLabel[] mapLabels = new JLabel[40];
    JLabel carrier = new JLabel(new ImageIcon("gCarrier.png"));
    JLabel battship = new JLabel(new ImageIcon("gBattleship.png"));
    JLabel cruiser = new JLabel(new ImageIcon("gCruiser.png"));
    JLabel submarine = new JLabel(new ImageIcon("gSubmarine.png"));
    JLabel destroyer = new JLabel(new ImageIcon("gDestroyer.png"));
    ArrayList<JLabel> gButtons = new ArrayList<JLabel>();
    ArrayList<JLabel> buttonEffects = new ArrayList<JLabel>();
    ArrayList<JLabel> ships = new ArrayList<JLabel>();
    int[] shipLength ={5,4,3,3,2};//positive for horizontal
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
	MouseListener unitDis = new MouseListener(){
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
	
	final MouseAdapter dragger = new MouseAdapter() {
        private JLabel selectedShip;
        private Point shipLocation ;
        private Point clickP;
        public void mousePressed(final MouseEvent e) {
            JLabel theShip = (JLabel)findComponentAt(e.getX(), e.getY());
            System.out.println(theShip.getIcon().toString());
            if (theShip != null&& ships.contains(theShip)) {
                selectedShip= (JLabel) theShip;
                shipLocation = selectedShip.getLocation();
                clickP = e.getPoint();
                setComponentZOrder(selectedShip, 0);
                selectedShip.repaint();
            }
        }
        public void mouseDragged(final MouseEvent e) {
            try{
                int theIndex = ships.indexOf((JLabel)findComponentAt(e.getX(), e.getY()));
                Point newclickP = e.getPoint();
                int newX = convertCoor(shipLength[theIndex],shipLocation.x + (newclickP.x - clickP.x));
                int newY = convertCoor(shipLength[theIndex],-(shipLocation.y + (newclickP.y - clickP.y)));
                if(newX!=-1&&newY!=-1){
                    markShip(ships.get(theIndex),shipLength[theIndex],darkRed);
                    selectedShip.setLocation(newX, newY);
                    markShip(ships.get(theIndex),shipLength[theIndex],darkGreen);
                }
            }catch(Exception exp){
            }
        }
    };
    public game(){
		setSize(1300,700);
		setLayout(null);
		ships.add(carrier);
		ships.add(battship);
		ships.add(cruiser);
		ships.add(submarine);
		ships.add(destroyer);
		for(int i =0;i<5;i++){
    		ships.get(i).setBounds(bInsets.left+150,bInsets.top+150+i*50,50*shipLength[i],50);
//    		System.out.println(shipLength[i]);
    		add(ships.get(i));
    	}//end for
		addMaps();
		for(int i =0;i<5;i++){
    		markShip(ships.get(i),shipLength[i],darkGreen);
		}
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
		addMouseListener(dragger);
		addMouseMotionListener(dragger);
		//start the game
//		removeMouseListener(dragger);
//		removeMouseMotionListener(dragger);
//		for(int i =0;i<10;i++){
//			for(int j =0;j<10;j++){
//				userMap[i][j].addMouseListener(unitDis);
//				enemMap[i][j].addMouseListener(unitDis);
//			}
//		}
		setVisible(true);
		
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

				add(userMap[i][j]);
				add(enemMap[i][j]);
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
	private int getColorCode(Color aColor){
		if(aColor.equals(darkBlue)){
			return 1;
		}else if(aColor.equals(fogBlue)){
			return 2;
		}else if(aColor.equals(darkRed)){
			return 3;
		}else{
			return 4;
		}
	}
	
	public static void main(String[] args){
		JFrame f = new JFrame();
		f.add(new game());
		f.setSize(f.getPreferredSize());
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
	
}
