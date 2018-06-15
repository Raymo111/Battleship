import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
import java.util.*;
/**
 * File: Base.java
 * <p>Mr. Anadarajan
 * <br/>ICS4U1
 * <br/>June 15, 2018
 * 
 * <p>Final Evaluation: Battleship Tournament
 * <br/> Description: The class which describes functions and variables of base interface.
 * 
 * @author Benny Shen
 */
public class Base extends JPanel{ 
    Dimension mButtonsDimen = new Dimension(270,180);									//dimension of six interface buttons at right
    Insets bInsets = getInsets();														//get and record boundaries of base interface
	JLabel achievementButton = new JLabel(new ImageIcon("mAchievementsButton.png"));	//achievement interface button
	JLabel taskButton = new JLabel(new ImageIcon("mTasksButton.png"));					//tasks interface button
	JLabel gameButton = new JLabel(new ImageIcon("mGAMEButton.png"));					//game interface button
	JLabel rankingButton = new JLabel(new ImageIcon("mRankingsButton.png"));			//rankings interface button
	JLabel factoryButton = new JLabel(new ImageIcon("mFactoryButton.png"));				//factory interface button
	JLabel settingButton = new JLabel(new ImageIcon("mSettingsButton.png"));			//setting interface button
	JLabel profileButton = new JLabel(new ImageIcon("mProfileButton.png"));				//profile interface button
	JLabel contractPane = new JLabel(new ImageIcon("mContractPane.png"));				//background for displaying contract numbers
	JLabel contractPaneButton = new JLabel(new ImageIcon("mContractPaneButton.png"));	//button to display contract numbers
    JLabel futurePost = new JLabel(new ImageIcon("futureFeatures.png"));				//message for interfaces will be released in future
	final int initXCoordinate=700;														//constant initial x-coordinate of buttons at right
	final int[] initYCoordinates = {30,50};												//constant initial y-coordinates of buttons at right
	ArrayList<JLabel> mRightButtons = new ArrayList<JLabel>();							//ArrayList of buttons which direct to each interface
	ArrayList<JLabel> mEffects = new ArrayList<JLabel>();								//ArrayList of effects for selection of buttons at right
	JLabel bgi = new JLabel(new ImageIcon("bgi.jpg"));									//background image of base interface
	MouseListener mMousListButtons = new MouseListener(){//MouseListener for displaying contract number and effects of the selecting buttons at right
		public void mouseClicked(MouseEvent e) {
			Object source = e.getSource();
			if(source.equals(contractPaneButton)&&contractPaneButton.isVisible()){//display contract number on click of button
				contractPaneButton.setVisible(false);
				contractLabel.setVisible(true);
				contractPane.setVisible(true);
			}//end if
			if(source.equals(contractPane)&&contractPane.isVisible()){//display contract button on click of background of displaying contract number
				contractPaneButton.setVisible(true);
				contractPane.setVisible(false);
				contractLabel.setVisible(false);
			}//end if
			try{
				int i = mRightButtons.indexOf((JLabel)e.getSource());//if one of button at right is clicked
				mEffects.get(i).setVisible(false);//hide corresponding effects
			}catch(Exception exp){
			}//end try catch
		}//end method
		public void mouseEntered(MouseEvent e) {//display effects when mouse entered buttons at right
			try{
				int i = mRightButtons.indexOf((JLabel)e.getSource());
				mEffects.get(i).setVisible(true);
			}catch(Exception exp){
			}//end try catch
		}//end method
		public void mouseExited(MouseEvent e) {//hide effects when mouse exited buttons at right
			try{
				int i = mRightButtons.indexOf((JLabel)e.getSource());
				mEffects.get(i).setVisible(false);
			}catch(Exception exp){
			}//end try catch
		}//end method
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
	};//end MouseListener
	String userName;								//username of the user
	int level;										//level of the user	
	int contractNum;								//contract number of user
	JLabel nameLabel = new JLabel();				//label for displaying username
	JLabel levelLabel = new JLabel();				//label for displaying level of the user
	JLabel contractLabel = new JLabel();			//label for displaying content of number of contract
	public Base(){
		setSize(1300,700);//set size of base interface
		setLayout(null);//set absolute layout
        mRightButtons.add(achievementButton);//add the buttons at right to the ArrayList
        mRightButtons.add(taskButton);
        mRightButtons.add(gameButton);
        mRightButtons.add(rankingButton);
        mRightButtons.add(factoryButton);
        mRightButtons.add(settingButton);
        for(int i =0;i<mRightButtons.size();i++){//treat all buttons and effects at right
    	   mEffects.add(new JLabel(new ImageIcon("mMouse.png")));//set effects
    	   mEffects.get(i).setBounds(initXCoordinate+i/3*300+bInsets.left-5,initYCoordinates[i/3]+i%3*200+bInsets.top-5,280,190);
    	   add(mEffects.get(i));
    	   mEffects.get(i).setVisible(false);//hide effects
    	   mRightButtons.get(i).setBounds(initXCoordinate+i/3*300+bInsets.left,initYCoordinates[i/3]+i%3*200+bInsets.top,mButtonsDimen.width,mButtonsDimen.height);//set buttons
    	   mRightButtons.get(i).addMouseListener(mMousListButtons);
    	   add(mRightButtons.get(i));
       }//end for
       contractPaneButton.setBounds(bInsets.left,bInsets.top+550,40,80);//set contract display button
       contractPaneButton.addMouseListener(mMousListButtons);
       add(contractPaneButton);
       contractPane.setBounds(bInsets.left,bInsets.top+550,500,80);//set background for displaying number of contracts
       contractPane.setVisible(false);
       nameLabel.setBounds(bInsets.left+105,bInsets.top,150,65);//set username label
       nameLabel.setForeground(Color.white);
       nameLabel.setFont(new Font(nameLabel.getFont().getName(),Font.PLAIN,20));
       add(nameLabel);//add labels for displaying user's informaiton
       levelLabel.setBounds(bInsets.left,bInsets.top,65,65);//set level label
       levelLabel.setForeground(Color.white);
       levelLabel.setHorizontalAlignment(JLabel.CENTER);
       levelLabel.setFont(new Font(nameLabel.getFont().getName(),Font.PLAIN,45));
       add(levelLabel);
       contractLabel.setBounds(bInsets.left,bInsets.top+550,160,80);//set contract number label
       contractLabel.setForeground(Color.white);
       contractLabel.setHorizontalAlignment(JLabel.CENTER);
       contractLabel.setFont(new Font(nameLabel.getFont().getName(),Font.PLAIN,45));
       contractLabel.setVisible(false);
       add(contractLabel);
       contractPane.addMouseListener(mMousListButtons);//set background for displaying number of contracts
       add(contractPane);
       profileButton.setBounds(bInsets.left,bInsets.top,300,100);//set profile interface button
       add(profileButton);
       futurePost.setBounds(bInsets.left,bInsets.top,650,700);//set future post message
       futurePost.addMouseListener(Game.hide);
       add(futurePost);
       futurePost.setVisible(false);//hide future post message
       bgi.setBounds(bInsets.left,bInsets.top,1300,700);//set background image
       add(bgi);
       setVisible(true);//display the base interface
	}//end contructor
	/**
	 * The procedure type setter method is used to update information displayed on base interface
	 * @param yourName updated username
	 * @param lv updated level number
	 * @param contracts updated number of contract(s) owned
	 */
	public void updateInfo(String yourName, int lv, int contracts){
		userName = yourName;//update username
		nameLabel.setText(userName);//reset the label for name
		level = lv;//update level
		levelLabel.setText(Integer.toString(level));//reset the label for level
		contractNum = contracts;//update number of contracts
		contractLabel.setText(Integer.toBinaryString(contractNum));//reset the label for contract
	}//end method
}//end class
