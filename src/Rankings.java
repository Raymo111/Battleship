import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
/**
 * File: Rankings.java
 * <p>Mr. Anadarajan
 * <br/>ICS4U1
 * <br/>June 15, 2018
 * 
 * <p>Final Evaluation: Battleship Tournament
 * <br/> Description: The class which describes functions and variables of rankings interface.
 * 
 * @author Benny Shen
 */
public class Rankings extends JPanel {
    private Insets bInsets = getInsets();													//get and record boundaries of rankings interface
    private JLabel bgi = new JLabel(new ImageIcon("rankBgi.jpg"));							//background image of rankings interface
    JLabel backButton = new JLabel(new ImageIcon("theBackButton.png"));				//backButton to base interface
    private JLabel buttonEffect = new JLabel(new ImageIcon("gMouse.png"));					//mouse effect for back button
    private JLabel collectionButton = new JLabel(new ImageIcon("rCollection.png"));			//collection ranking sub interface button
    private JLabel levelButton = new JLabel(new ImageIcon("rLevel.png"));					//level ranking sub interface button
    private JLabel winButton = new JLabel(new ImageIcon("rWin.png"));						//win number ranking sub interface button
    private JLabel screen = new JLabel(new ImageIcon("rTotalBack.png"));					//transparent background for content of rankings
    private JLabel rankBoard = new JLabel();												//ranking content label
    private JLabel userBoard = new JLabel("-----------------------------------");			//user rank label
    private int userRankIndex = -1;															//user's rank index
    private String getValueL,getValueW;														//user's ranking information for level and number of wins
    private JLabel userScreen = new JLabel(new ImageIcon("rUserBack.png"));					//transparent background for content of current user rankings
    private ArrayList<JLabel> subTypes = new ArrayList<JLabel>();							//ArrayList of sub interface buttons
    private String collectionRank = "Will be Released in Near Future",levelRank,winRank;	//String which record rankings content
    private ArrayList<String> nameList = new ArrayList<String>();							//list of usernames of rankings
    private MouseListener backMouseEffect = new MouseListener(){//MouseListener for effect of selecting back button
		public void mouseClicked(MouseEvent e) {
			buttonEffect.setVisible(false);//hide on click
		}//end method
		public void mouseEntered(MouseEvent e) {
			buttonEffect.setVisible(true);//display when mouse entered
		}//end method
		public void mouseExited(MouseEvent e) {
			buttonEffect.setVisible(false);//hide when mouse exit
		}//end method
		public void mousePressed(MouseEvent e) {
		}
		public void mouseReleased(MouseEvent e) {
		}
	};//end MouseListener
	private MouseListener switchRank = new MouseListener() {//MouseListener for switching rankings information display by sub interface buttons
		public void mouseClicked(MouseEvent e) {
			Object source = e.getSource();
			if (source.equals(collectionButton)) {//when collection button is clicked
				rankBoard.setText(collectionRank);//switch displayed content to Will be Released in Future
				userBoard.setText("-----------------------------------");//display nothing for user board
				return;//switch completed
			}//end if
			if (source.equals(levelButton)) {//when level button is clicked
				rankBoard.setText(levelRank);//switch display to sub interface level rankings
				userBoard.setText(userRankIndex + ".\t\t" + system.userInfo[0] + "-\t\t-" + "Lv." + getValueL);//display information about current user
				return;//switch completed
			}//end if
			if (source.equals(winButton)) {//when win button is clicked
				rankBoard.setText(winRank);//switch display to sub interface win number rankings
				userBoard.setText(userRankIndex + ".\t\t" + system.userInfo[0] + "-\t\t-" + getValueW + " win(s)");//display information about current user
				return;//switch completed
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
    public Rankings() throws IOException{
		setSize(1300,700);//set size of ranking interface
		setLayout(null);//set absolute layout
		BufferedReader nameReader = new BufferedReader(new FileReader("usersRecord.txt"));//record the list of username in system
		try {
			while (true) {
				String aRecord = nameReader.readLine();
				if (aRecord != null) {//before end of list
					nameList.add(aRecord);//add username to name list
				} else {
					break;//break loop of reading at end of list
				}//end if
			}
		} catch (Exception e) {
		}//end try catch
		nameReader.close();// close the BufferedReadeer
		backButton.setBounds(bInsets.left + 10, bInsets.top + 10, 100, 60);//set back button
		backButton.addMouseListener(backMouseEffect);
		add(backButton);
		buttonEffect.setBounds(bInsets.left, bInsets.top, 120, 80);//set selection effect
		add(buttonEffect);
		buttonEffect.setVisible(false);//hide selection effect
		subTypes.add(collectionButton);//add sub interface buttons at side to the ArrayList
		subTypes.add(levelButton);
		subTypes.add(winButton);
		for (int i = 0; i < 3; i++) {//for all buttons in the list
			subTypes.get(i).setBounds(bInsets.left + 10, bInsets.top + 110 + i * 170, 120, 160);//set button
			subTypes.get(i).addMouseListener(switchRank);
			add(subTypes.get(i));
		} // end for
		rankBoard.setBounds(bInsets.left + 185, bInsets.top + 290, 1050, 300);//set label for rankings content
		rankBoard.setForeground(Color.white);
		rankBoard.setHorizontalAlignment(JLabel.CENTER);
		rankBoard.setFont(new Font(rankBoard.getFont().getName(), Font.PLAIN, 28));
		add(rankBoard);
		userBoard.setBounds(bInsets.left + 185, bInsets.top + 80, 1050, 200);//set label for user's rankings
		userBoard.setForeground(Color.white);
		userBoard.setHorizontalAlignment(JLabel.CENTER);
		userBoard.setFont(new Font(userBoard.getFont().getName(), Font.PLAIN, 60));
		add(userBoard);
		userScreen.setBounds(bInsets.left + 185, bInsets.top + 80, 1050, 200);//set background for user's rankings
		add(userScreen);
		screen.setBounds(bInsets.left + 135, bInsets.top + 50, 1150, 600);//set background for rankings content
		add(screen);
		bgi.setBounds(bInsets.left, bInsets.top, 1300, 700);//set background image
		add(bgi);
		setVisible(true);//display rankings interface
	}//end constructor
    /**
     * The procedure type method which convert ranking information into rankings content
     * @param rankType the type of rank needs to be converted
     * @param indexes the indexes read from system
     * @param values the values read from system
     */
    void convertRank(String rankType, String indexes, String values){
		String convertedContent = "<html>";// create a String variable to store the result
		String[] getIndex = indexes.split(" ");//split information of each user
		String[] getValue = values.split(" ");
		for (int i = 1; i < Math.min(getIndex.length + 1, 11); i++) {//for only top 10 users
			if (system.userInfo[0].equals(nameList.get(Integer.parseInt(getIndex[i - 1]) - 1))) {
				userRankIndex = i;//record indexes for displaying user's rankings
				if (rankType.equals("L")) {//record values for displaying user's rankings
					getValueL = getValue[i - 1];
				} else if (rankType.equals("W")) {
					getValueW = getValue[i - 1];
				}//end if
			}//end if
			convertedContent += i + ".\t\t" + nameList.get(Integer.parseInt(getIndex[i - 1]) - 1) + "-\t\t-";//format the information
			if (rankType.equals("L")) {
				convertedContent += "Lv." + getValue[i - 1] + "<br>";
			} else if (rankType.equals("W")) {
				convertedContent += getValue[i - 1] + " win(s) <br>";
			} // end if
		} // end for
		convertedContent += "</html>";// end the input stream
		if (rankType.equals("L")) {//record the formated information
			levelRank = convertedContent;
		} else if (rankType.equals("W")) {
			winRank = convertedContent;
		} // end if
	}//end method
}//end class