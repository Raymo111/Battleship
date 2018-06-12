import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.util.*;

import javax.swing.*;

public class system extends JFrame {
	Boolean inGame = false;
	int userIndex;
	login startGame;
	String[] userInfo = new String[38];
	int firsthand = -1;
	/*
	 * dewae to execute code in game from Battleships:
	 * 2 Files: systemLog, inputLog
	 * game write to inputLog, read from systemLog
	 * Battleship write to systemLog, read from inputLog
	 * 
	 * yea we have a system log now
	 */
	
	Battleship oYa = new Battleship();

	MouseListener directory = new MouseListener() {
		public void mouseClicked(MouseEvent event) {
			JLabel source = (JLabel)event.getSource();
			try{
			if (source.equals(baseInter.gameButton)) {
				System.out.println(-1);
				remove(baseInter);
				add(gameInter);
				oYa.newGameProcedure();
				synchronized (oYa) {
					while(firsthand==-1){
						enterGame();
						oYa.wait();
					}
				}
				repaint();
			}
			if(source.equals(baseInter.rankingButton)){
				System.out.println(6);
				remove(baseInter);
				add(rankInter);
				repaint();
				updateRank(userIndex, "C",true);
				updateRank(userIndex, "L",true);
				updateRank(userIndex, "W",true);
			}
			if(source.getIcon().toString().equals("theBackButton.png")){
				System.out.println(0);
				getContentPane().removeAll();
				baseInter.updateInfo(userInfo[0],Integer.parseInt(userInfo[18]),Integer.parseInt(userInfo[15]));//update the base interface
				add(baseInter);
				repaint();
			}}catch(Exception exp){
				
			}
		}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
	};
	
	MouseListener loginOper = new MouseListener() {
		public void mouseClicked(MouseEvent event) {
			try {
				readLogin(startGame.loginText.getText());
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Asdfasfasfasfadfafasdaa");
			}
			startGame.dispose();
			check();
			baseInter.updateInfo(userInfo[0],Integer.parseInt(userInfo[18]),Integer.parseInt(userInfo[15]));//initialize the base interface
			add(baseInter);
			setVisible(true);
		}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
	};
	
	ActionListener gameProc = new ActionListener(){
		public void actionPerformed(ActionEvent arg0) {
			
		}
	};
	
	base baseInter = new base();
	game gameInter = new game();
	rankings rankInter;
	public system() throws IOException {
		startGame = new login();
		startGame.okButton.addMouseListener(loginOper);
		for(int i=0;i<6;i++){
			baseInter.mRightButtons.get(i).addMouseListener(directory);//add directory to all buttons in base interface
		}//end for
		gameInter.backButton.addMouseListener(directory);//add directory to buttons in game interface
		rankInter = new rankings();//initializing the rankings interface
		rankInter.backButton.addMouseListener(directory);
		addWindowListener(new java.awt.event.WindowAdapter() {//need to save the information before closing
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				try {
					//save the information
					PrintWriter saveWriter = new PrintWriter("User"+userIndex);//create PrintWriter
					for(String infoLn:userInfo){
						saveWriter.println(infoLn);//record the information line by line
					}//end for
					saveWriter.close();//close the saveWriter
				} catch (IOException e1) {
					e1.printStackTrace();
				}//end try catch
				System.exit(0);//terminate the program
			}//end method
		});//end WindowStateListener		this.setResizable(false);
		setSize(1300, 700);
        setLocationRelativeTo(null);
	}
	public void updateGame(){
	}
	public void enterGame(){
		if(!inGame){
			//will be modify to be round shape frame if there is time left
			Object[] options = {"User","AI"};
			firsthand = JOptionPane.showOptionDialog(null,
					"Battle started from:",
					"First hand",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					new ImageIcon("gNani.png"),     
					options,  
					options[0]); 
			if(firsthand==0){
				gameInter.userTurn = true;
			}else{
				gameInter.userTurn = false;
			}
		}
	}
	public void readLogin(String thisUsername) throws IOException{
	    	BufferedReader fileReader = new BufferedReader(new FileReader("usersRecord.txt"));
	    	int thisUserIndex = 0;
	    	try{
	    		while(true){//search till end of the list
	    			thisUserIndex++;//incrementing index of the user
	    			if(fileReader.readLine().equals(thisUsername)){//when the username is found in history
	    				loadUser(thisUserIndex);//load the user information
	    				break;//stop searching
	    			}
	    		}
	    	}catch(Exception e){
	    		//when the username is not found in history
	    		System.out.println("u"+thisUserIndex);
	    		createUser(thisUsername,thisUserIndex);
	    	}
    		userIndex = thisUserIndex;
    		fileReader.close();//close the BufferedReader
	 }//end method
	/**
	 * The procedure type method read and record all information in file to the system
	 * @param tUserIndex
	 * @throws IOException
	 */
	public void loadUser(int tUserIndex) throws IOException{
		BufferedReader fileReader = new BufferedReader(new FileReader("User"+tUserIndex));//create BufferedReader to read record
		for(int i =0;i<38;i++){
			userInfo[i]=fileReader.readLine();//read and record the information line by line in system
		}//end for
		fileReader.close();//close the fileReader
	}//end method
	/**
	 * The procedure type method update the rank information and files.
	 * @param tUserIndex the index of targeted user
	 * @param rankType the type of rank needs to be updated
	 * @throws IOException Exceptions for File IO
	 */
	public void updateRank(int tUserIndex, String rankType, Boolean isDisplaying) throws IOException{
		System.out.println("------------------------------------------"+rankType);
		File theRank = new File("rank"+rankType+".txt");
		BufferedReader rankReader = new BufferedReader(new FileReader(theRank));
		int rankInfo;//record the info of rank of target player for comparisons
		if(rankType.equals("C")){
			rankInfo = Integer.parseInt(userInfo[29]);
		}else if(rankType.equals("L")){
			rankInfo = Integer.parseInt(userInfo[30]);
		}else{
			rankInfo = Integer.parseInt(userInfo[31]);
		}//end if
		String newIndexes = "";//initialize empty String for the new indexes
		String newValues = "";//initialize empty String for the new values
		try{
			System.out.println("_______________________________________________ranked");
			String[] index = rankReader.readLine().split(" ");//read and record the indexes and values
			String[] val = rankReader.readLine().split(" ");
			rankReader.close();//close the IO fileReader
			boolean placed = false;
			for(int i =0;i<index.length;i++){
					if(placed){
						if(Integer.parseInt(index[i-1])!=tUserIndex){
							newIndexes+=index[i-1]+" ";
							newValues+=val[i-1]+" ";
							System.out.println(i+" "+1);
						}
					}else{
						if(Integer.parseInt(val[i])>rankInfo){
							if(Integer.parseInt(index[i])!=tUserIndex){
								newIndexes+=index[i]+" ";
								newValues+=val[i]+" ";
								System.out.println(i+" "+2);
							}
						}else{
							newIndexes+=tUserIndex+" ";
							newValues+=rankInfo+" ";
							System.out.println(i+" "+3);
							placed = true;//record the placement
						}//end if
					}//end if
			}//end for
			if(!placed){//add the user if ranked last
				newIndexes+=tUserIndex;
				newValues+=rankInfo;
				System.out.println(4);
			}else{//add the last if user is not last
				if(Integer.parseInt(index[index.length-1])!=tUserIndex){
					newIndexes+=index[index.length-1];
					newValues+=val[val.length-1];
					System.out.println(5);
				}
			}//end if
		}catch(Exception e){//add the user directly when there is no user exists in record rank
			System.out.println("_______________________________________________direct");
			newIndexes+=tUserIndex;
			newValues+=rankInfo;
			System.out.println(6);
		}//end try catch
		PrintWriter rankWriter = new PrintWriter(theRank);//create PrintWriter to write the file
		rankWriter.println(newIndexes);//write the indexes
		rankWriter.println(newValues);//write the values
		rankWriter.close();//close the fileWriter
		System.out.println("rkrkrkrkrkrkrkkrkrkrkrk\n"+isDisplaying);

		if(isDisplaying==true){//if the rankings needs to be displayed, convert the information
			System.out.println("rkrkrkrkrkrkrkkrkrkrkrk\n"+isDisplaying);

			rankInter.convertRank(rankType, newIndexes, newValues);
		}//end if
	}//end method
	/**
	 * The procedure type method initialize a new line in userInfo.
	 * @param iNum the number of times information needs to be repeated
	 * @param lineNum the number of lines the information needs to be repeated
	 * @param state the String which contains the information needs to be repeated
	 */
	public void initialn(int lnIndex, int iNum,int lineNum, String state){
		for(int i =lnIndex;i<lnIndex+lineNum;i++){
			userInfo[i]="";
			for(int j =0;j<iNum-1;j++){
				userInfo[i]+=state+" ";//add required number of String
			}//end for
			userInfo[i]+=state;//end the line
		}//end for
	}//end method
	/**
	 * The procedure type method initiates all information for a new user in system.
	 * @param newName the user name registered
	 * @param newIndex the new user's index
	 * @throws IOException Exceptions of using {@link #updateRank(int, String)}method}, creating new file and registering the new user.
	 */
	public void createUser(String newName, int newIndex) throws IOException{
	    	userInfo[0]=newName;//first line:username
	    	initialn(1,1,14,"null");//initialize the game board to not started
	    	initialn(15,1,2,"0");//initialize the contract number and time played
	    	initialn(17,2,1,"0");//initialize the battle numbers
	    	initialn(18,1,1,"1");;//initialize number of level
	    	initialn(19,1,2,"0");//initialize the exp number and assistant
	    	userInfo[21]="0 1 2 3 4";//initialize team setting
	    	initialn(22,100,2,"null");//initialize characters owned list and intimacy list
	    	initialn(24,1,1,"0");//initialize dismantle num
	    	initialn(25,1,1,"false");//initialize expert setting
	    	initialn(26,1,1,"100");//initialize % of sound
	    	initialn(27,11,1,"false");//initialize task completion
	    	initialn(28,26,1,"false");//initialize achievement completion
	    	initialn(29,1,3,"0");//initialize rank information
	    	updateRank(newIndex,"C",false);//update for all three ranks
	    	updateRank(newIndex,"L",false);
	    	updateRank(newIndex,"W",false);
	    	//initialize empty build informations
	    	for(int i =0;i<3;i++){
	    		initialn(32+i*2,1,1,"null");
	    		initialn(33+i*2,1,1,"0");
	    	}//end for
	    	new File("User"+newIndex).createNewFile();//create a new file for the new user
	    	PrintWriter recordWriter =  new PrintWriter(new FileWriter("usersRecord.txt",true));//create PrintWriter to record new user
	    	recordWriter.println(newName);//write the name into users' record list
	    	recordWriter.close();//close the record writer
	}//end method
	
	public void check(){
		for(int i=0;i<38;i++){
			System.out.println(userInfo[i]);
		}
	}

	public static void main(String[] args) throws IOException {
		system theGame = new system();
	}

}
