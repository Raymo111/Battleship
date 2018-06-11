import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
import java.util.*;


public class base extends JPanel{ 
    Dimension mButtonsDimen = new Dimension(270,180);
    Insets bInsets = getInsets();
	JLabel achievementButton = new JLabel(new ImageIcon("mAchievementsButton.png"));
	JLabel taskButton = new JLabel(new ImageIcon("mTasksButton.png"));
	JLabel gameButton = new JLabel(new ImageIcon("mGAMEButton.png"));
	JLabel rankingButton = new JLabel(new ImageIcon("mRankingsButton.png"));
	JLabel factoryButton = new JLabel(new ImageIcon("mFactoryButton.png"));
	JLabel settingButton = new JLabel(new ImageIcon("mSettingsButton.png"));
	JLabel profileButton = new JLabel(new ImageIcon("mProfileButton.png"));
	JLabel contractPane = new JLabel(new ImageIcon("mContractPane.png"));
	JLabel contractPaneButton = new JLabel(new ImageIcon("mContractPaneButton.png"));
	int initXCoordinate=700;
	int[] initYCoordinates = {30,50};
	ArrayList<JLabel> mRightButtons = new ArrayList<JLabel>();
	ArrayList<JLabel> mEffects = new ArrayList<JLabel>();
	JLabel bgi = new JLabel(new ImageIcon("bgi.jpg"));
	MouseListener mMousListButtons = new MouseListener(){
		public void mouseClicked(MouseEvent e) {
			Object source = e.getSource();
			if(source.equals(contractPaneButton)&&contractPaneButton.isVisible()){
				contractPaneButton.setVisible(false);
				contractLabel.setVisible(true);
				contractPane.setVisible(true);
//				contractPane.setText(contractLabel.getText());
//				contractPane.setForeground(Color.white);
			}
			if(source.equals(contractPane)&&contractPane.isVisible()){
				contractPaneButton.setVisible(true);
				contractPane.setVisible(false);
				contractLabel.setVisible(false);
			}
			try{
				int i = mRightButtons.indexOf((JLabel)e.getSource());
				mEffects.get(i).setVisible(false);
			}catch(Exception exp){
			}
		}
		public void mouseEntered(MouseEvent e) {
			try{
				int i = mRightButtons.indexOf((JLabel)e.getSource());
				mEffects.get(i).setVisible(true);
			}catch(Exception exp){
			}
		}
		public void mouseExited(MouseEvent e) {
			try{
				int i = mRightButtons.indexOf((JLabel)e.getSource());
				mEffects.get(i).setVisible(false);
			}catch(Exception exp){
			}
		}
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
	};
	String userName;
	int level;
	int contractNum;
	JLabel nameLabel = new JLabel("asdfasfafadfadfasfa");
	JLabel levelLabel = new JLabel("99");
	JLabel contractLabel = new JLabel("22");
	public base(){
		setSize(1300,700);
		setLayout(null);
        mRightButtons.add(achievementButton);
        mRightButtons.add(taskButton);
        mRightButtons.add(gameButton);
        mRightButtons.add(rankingButton);
        mRightButtons.add(factoryButton);
        mRightButtons.add(settingButton);
        contractPaneButton.setBounds(bInsets.left,bInsets.top+550,40,80);
        contractPane.setBounds(bInsets.left,bInsets.top+550,500,80);
        profileButton.setBounds(bInsets.left,bInsets.top,300,100);
        bgi.setBounds(bInsets.left,bInsets.top,1300,700);
        nameLabel.setBounds(bInsets.left+105,bInsets.top,150,65);
        nameLabel.setForeground(Color.white);
        nameLabel.setFont(new Font(nameLabel.getFont().getName(),Font.PLAIN,20));
        levelLabel.setBounds(bInsets.left,bInsets.top,65,65);
        levelLabel.setForeground(Color.white);
        levelLabel.setHorizontalAlignment(JLabel.CENTER);
        levelLabel.setFont(new Font(nameLabel.getFont().getName(),Font.PLAIN,45));
        contractLabel.setBounds(bInsets.left,bInsets.top+550,160,80);
        contractLabel.setForeground(Color.white);
        contractLabel.setHorizontalAlignment(JLabel.CENTER);
        contractLabel.setFont(new Font(nameLabel.getFont().getName(),Font.PLAIN,45));
       for(int i =0;i<mRightButtons.size();i++){
    	   mEffects.add(new JLabel(new ImageIcon("mMouse.png")));
    	   mEffects.get(i).setBounds(initXCoordinate+i/3*300+bInsets.left-5,initYCoordinates[i/3]+i%3*200+bInsets.top-5,280,190);
    	   add(mEffects.get(i));
    	   mEffects.get(i).setVisible(false);
    	   mRightButtons.get(i).setBounds(initXCoordinate+i/3*300+bInsets.left,initYCoordinates[i/3]+i%3*200+bInsets.top,mButtonsDimen.width,mButtonsDimen.height);
    	   mRightButtons.get(i).addMouseListener(mMousListButtons);
    	   add(mRightButtons.get(i));
       }
       contractPaneButton.addMouseListener(mMousListButtons);
       add(contractPaneButton);
       contractPane.setVisible(false);
       add(nameLabel);
       add(levelLabel);
       add(contractLabel);
       contractLabel.setVisible(false);
       contractPane.addMouseListener(mMousListButtons);
       add(contractPane);
       add(profileButton);
       add(bgi);
       setVisible(true);
		
	}
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
	
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		JFrame f = new JFrame();
//		base yourBase = new base();
//		f.add(yourBase);
//		f.setSize(f.getPreferredSize());
//		f.setVisible(true);
//	}

}


///https://tieba.baidu.com/p/3421221521?red_tag=0918606948
//zhong hua shen dun
//https://zh.moegirl.org/171