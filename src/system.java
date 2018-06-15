import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
/**
 * File: system.java
 * <p>Mr. Anadarajan
 * <br/>ICS4U1
 * <br/>June 15, 2018
 * 
 * <p>Final Evaluation: Battleship Tournament
 * <br/> Description: The major class which allows the user to start tournament or play against AI
 * 
 * @author Benny Shen
 * <br/> program includes integration from console game designed by Raymond Li and David Tuck
 * <br/> authors of methods integrated are specified in header before the method.
 */
public class system extends JFrame {
	static Boolean inGame = false;						//if the game in game interface has started
	private int userIndex;										//the index of the user
	static boolean AIcombat = false;					//if the game is AI combat or human play against AI.
	static long recordTime = -1;						//record the last recorded time of from login or updating time in userInfo
	static String[] userInfo = new String[38];			//information from user's profile recorded in system for change
	static String offset = "";							//if the game in game interface offset edges
	static int difficulty = -1;							//difficulty index of game in game interface
	static String firstHand = "";						//describes which side is firstHand for running game in game interface
	static boolean AIFirst, AIWin, userWin;				//variables from console game: if AI goes first, if AI wins, if user wins
	private static int round, x, y, shipNumber;					//variables from console game: round#, x coordinate of shot, y coordinate of shot, index of ship type
	private static Square userShot, AIShot;						//variables from console game: user's shot, AI's shot
	private static Ship ship;									//variables from console game: target ship
	private static boolean flag;								//variables from console game: boolean used in searching
	static String input;								//variables from console game: String which contains information input from GUI		
	private static AI Amadeus;									//variables from console game: the AI in game
	final static Color[] unitColor = {Game.darkBlue,new Color(0,200,0), new Color(0,170,0), new Color(0,140,0),new Color(0,110,0),new Color(0,80,0),Game.darkBlue};
														//constant array of colors of ships for placing ships and checking existence of ship in shot in human against AI mode
	private MouseListener directory = new MouseListener() {//MouseListener for directing the display between interfaces
		public void mouseClicked(MouseEvent event) {
			JLabel source = (JLabel) event.getSource();//source of button
			try {
				if (source.equals(baseInter.gameButton)) {//when game button in base clicked
					remove(baseInter);//remove base interface
					add(gameInter);//add game interfaces
					repaint();//refresh display
					enterGame();//execute enterGame() procedures
					return;//source found, no need to continue to check if source equals to other buttons
				}//end if
				if (source.equals(baseInter.rankingButton)) {//when rankings button in base clicked
					remove(baseInter);//remove base interface
					add(rankInter);//add rankings interface
					repaint();//refresh the display
					updateRank(userIndex, "L", true);//update level rankings
					updateRank(userIndex, "W", true);//update win rankings
					return;//source found
				}//end if
				if (source.equals(baseInter.achievementButton)) {//when achievement button in base clicked
					Achievements.updateAchievement();//update the achievement status
					remove(baseInter);//remove base interface
					add(achiInter);//add achievements interface
					repaint();//refresh the display
					return;//source found
				}//end if
				if(source.equals(baseInter.taskButton)||source.equals(baseInter.factoryButton)||source.equals(baseInter.settingButton)){//other buttons in base clicked
					baseInter.futurePost.setVisible(true);//display message about unimplemented interfaces
					return;//source found
				}//end if
				if (source.getIcon().toString().equals("theBackButton.png")||source.equals(Game.winWord)||source.equals(Game.losWord)) {//backButton in any interfaces/game end win message/game end lost message in game interface clicked
					if(source.equals(gameInter.backButton)||source.equals(Game.winWord)||source.equals(Game.losWord)){//if from game interface
						if(!Game.userTurn){//if it is an invalid click when program is processing
							return;//do nothing
						}//end if
						if(source.equals(Game.winWord)||source.equals(Game.losWord)){//if game ended
							gameInter.reset();//reset game interface
						}//end if
					}//end if
					getContentPane().removeAll();//remove the current interface
					baseInter.updateInfo(userInfo[0], Integer.parseInt(userInfo[18]), Integer.parseInt(userInfo[15]));//update base
					if(baseInter.futurePost.isVisible()){
						baseInter.futurePost.setVisible(false);
					}//hide futurePost
					add(baseInter);//add base interface
					repaint();//refresh the display
					return;//source found
				}//end if
			} catch (Exception exp) {
			}//try catch to throw any exceptions during calling the methods
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

	private MouseListener loginOper = new MouseListener() {//MouseListener for login operations
		public void mouseClicked(MouseEvent event) {
			try {
				readLogin(startGame.loginText.getText());//perform readLogin operation to user's input
			} catch (Exception e) {
				e.printStackTrace();
			}//handling IOException
			startGame.dispose();//exit login interface
			baseInter.updateInfo(userInfo[0], Integer.parseInt(userInfo[18]), Integer.parseInt(userInfo[15]));//initialize base
			add(baseInter);//add base interface
			setVisible(true);//set main frame visible
		}//end method
				/////
		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		public void mousePressed(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}
	};//end MouseListener

	private MouseListener gameOper = new MouseListener() {//MouseListener for game operations
		public void mouseClicked(MouseEvent e) {
			if(!AIcombat){//if human placing ships
				String shipPosition = Game.checkShip();//check if ships are placed properly and record checked response
				if(!shipPosition.equals("true")){//when the response says improper
					JOptionPane.showMessageDialog(null,
						    shipPosition,
						    "Ship Placement Error Detected",
						    JOptionPane.INFORMATION_MESSAGE,
						    new ImageIcon("gResponse.png"));//display error image if ships are not placed properly.
					return;//do not start the game
				}//end if
			}//end if
			if (!inGame) {//when the game is not already started
				inGame = true;//record game start
				Game.firstClick = true;//set first click not clicked
				gameInter.lastRecordTime = System.currentTimeMillis();//start recording game time
				Game.timeUsed = 0;//reset time used
				Game.timer.restart();//restart the timer
				Game.eHit.setText("0");//reset enemy board
				Game.eMis.setText("0");
				Game.uHit.setText("0");//reset user board
				Game.uMis.setText("0");
				try {
					newGameProcedure();//execute game initializing procedures
				} catch (IOException e1) {
					e1.printStackTrace();
				}//handling IOExceptions
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
	
	Login startGame;//login interface
	private Base baseInter = new Base();//base interface
	private Game gameInter = new Game();//game interface
	private Rankings rankInter;//rankings interface
	private Achievements achiInter;//achievement interface

	public system() throws IOException {
		startGame = new Login();//initialize login interface
		startGame.okButton.addMouseListener(loginOper);//add MouseListener to the OK Button
		for (int i = 0; i < 6; i++) {
			baseInter.mRightButtons.get(i).addMouseListener(directory);//add directory to all buttons in base interface
		} // end for
		recordTime = System.currentTimeMillis();//record current time
		gameInter.backButton.addMouseListener(directory);//add directory to buttons in game interface
		gameInter.startButton.addMouseListener(gameOper);//add gameOper to start button in game interface
		Game.winWord.addMouseListener(directory);//add directory to win message in game interface
		Game.losWord.addMouseListener(directory);//add directory to lost message in game interface
		rankInter = new Rankings();// initializing the Rankings interface
		rankInter.backButton.addMouseListener(directory);//add directory to back button of rankings interface
		achiInter = new Achievements();//initializing the achievement interface
		achiInter.backButton.addMouseListener(directory);//add directory to back button in achievement interface
		addWindowListener(new java.awt.event.WindowAdapter() {// need to save the information before closing
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				try {
					// save the time
					userInfo[16] = Long.toString(System.currentTimeMillis()-recordTime+Long.parseLong(userInfo[16]));
					// save the information
					PrintWriter saveWriter = new PrintWriter("User" + userIndex);// create PrintWriter
					for (String infoLn : userInfo) {
						saveWriter.println(infoLn);// record the information line by line
					} // end for
					saveWriter.close();// close the saveWriter
				} catch (IOException e1) {
					e1.printStackTrace();
				} // end try catch
				System.exit(0);// terminate the program
			}// end method
		});// end WindowStateListener this.setResizable(false);
		setSize(1300, 700);//set the main frame to a size that allows the user to click sides to return to work but large enough to let user forget about other windows while playing
		setLocationRelativeTo(null);//set the window at the center of the screen
	}//end constructor
	/**
	 * The procedure type method execute procedures to enter game in game interfaces.
	 */
	private void enterGame() {
		if (!inGame) {//when the game is not already started
			Object[] modeOptions = { "AI Combat", "Human against AI" };//ask for mode
			int modeIndex;
			do {
				modeIndex = JOptionPane.showOptionDialog(null, "Choose a game mode", "Game mode",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon("gNani.png"), modeOptions,
						modeOptions[0]);
				AIcombat = (modeIndex==0);
			} while (!areYouSure(modeOptions[modeIndex].toString()));//confirm
			Object[] firstHandOptions = { "User", "AI" };//ask for first hand
			if(AIcombat){//it is an AI combat
				for(int i =0;i<10;i++){//reset the user board of game interface
					for(int j =0;j<10;j++){
						Game.enemMap[i][j].setText("");;
						Game.userMap[i][j].setBackground(Color.white);
						Game.userMap[i][j].removeMouseListener(gameInter.unitDis);
					}//end for
				}//end for
				firstHandOptions[0] = "Them";
				firstHandOptions[1] = "Us";
			}//end if
			int firsthandIndex;
			do {//ask for first hand
				firsthandIndex = JOptionPane.showOptionDialog(null, "Battle started from:", "First hand",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon("gNani.png"), firstHandOptions,
						firstHandOptions[0]);
				if (firsthandIndex == 0) {
					firstHand = "User";
					Game.userTurn = true;
				} else {
					firstHand = "AI";
					Game.userTurn = false;
				}
			} while (!areYouSure(firstHandOptions[firsthandIndex].toString()));//confirm
			Object[] diOptions = { "Expert - Raymond", "Random", "Expert - David","Extreme" };
			do {//ask for difficulty level
				difficulty = JOptionPane.showOptionDialog(null, "Choose a difficulty level:", "Difficulty",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon("gNani.png"), diOptions,
						diOptions[0]);
			} while (!areYouSure(diOptions[difficulty].toString()));//confirm
			Object[] offsetOptions = { "Yes", "No" };
			int offsetInd;
			do {//ask for offset
				offsetInd = JOptionPane.showOptionDialog(null, "Offset edges?", "Offset?",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon("gNani.png"), offsetOptions,
						offsetOptions[0]);
				if (offsetInd == 0) {
					offset = "y";
				} else {
					offset = "n";
				}//end if
			} while (!areYouSure(offsetOptions[offsetInd].toString()));//confirm
		}//end if
	}//end method
	/**
	 * The return type method for universal confirmation pop-up for all choices user made in pop-up windows.
	 * @param confirmInfo information user previously selected
	 * @return whether user confirms true or not
	 */
	static boolean areYouSure(String confirmInfo) {
		Object[] optionsAg = { "Yes", "No" };//choices of confirmations
		int confirm = JOptionPane.showOptionDialog(null, confirmInfo + " -Are you sure?", "First hand Confirm",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon("gNani.png"), optionsAg,
				optionsAg[0]);//display confirm message
		if (confirm == 0) {
			return true;
		} else {
			return false;
		}//end if
	}//end method
	/**
	 * The procedure type method read the username in login interface and process to load current user or create new user profile.
	 * @param thisUsername the username user enters in login interface
	 * @throws IOException Exceptions of loading users' profiles
	 */
	private void readLogin(String thisUsername) throws IOException {
		BufferedReader fileReader = new BufferedReader(new FileReader("usersRecord.txt"));//read userRecord.txt
		int thisUserIndex = 0;
		try {
			while (true) {// search till end of the list
				thisUserIndex++;// incrementing index of the user
				if (fileReader.readLine().equals(thisUsername)) {// when the username is found in history
					loadUser(thisUserIndex);// load the user information
					break;// stop searching
				}//end if
			}//end while
		} catch (Exception e) {
			// when the username is not found in history
			System.out.println("u" + thisUserIndex);
			createUser(thisUsername, thisUserIndex);
		}//end try catch
		userIndex = thisUserIndex;
		fileReader.close();// close the BufferedReader
	}// end method
	/**
	 * The procedure type method read and record all information in file to the
	 * system
	 * 
	 * @param tUserIndex
	 * @throws IOException
	 */
	private void loadUser(int tUserIndex) throws IOException {
		BufferedReader fileReader = new BufferedReader(new FileReader("User" + tUserIndex));// create BufferedReader to
		for (int i = 0; i < 38; i++) {
			userInfo[i] = fileReader.readLine();// read and record the information line by line in system
		} // end for
		fileReader.close();// close the fileReader
	}// end method
	/**
	 * The procedure type method update the rank information and files.
	 * 
	 * @param tUserIndex
	 *            the index of targeted user
	 * @param rankType
	 *            the type of rank needs to be updated
	 * @throws IOException
	 *             Exceptions for File IO
	 */
	private void updateRank(int tUserIndex, String rankType, Boolean isDisplaying) throws IOException {
		File theRank = new File("rank" + rankType + ".txt");
		BufferedReader rankReader = new BufferedReader(new FileReader(theRank));
		int rankInfo =-1;// record the info of rank of target player for comparisons
		if (rankType.equals("L")) {
			rankInfo = Integer.parseInt(userInfo[18]);
		} else if(rankType.equals("W")){
			rankInfo = Integer.parseInt(userInfo[17].split(" ")[1]);
		} // end if
		String newIndexes = "";// initialize empty String for the new indexes
		String newValues = "";// initialize empty String for the new values
		try {
			String[] index = rankReader.readLine().split(" ");// read and record the indexes and values
			String[] val = rankReader.readLine().split(" ");
			rankReader.close();// close the IO fileReader
			boolean placed = false;
			for (int i = 0; i < index.length; i++) {
				if (placed) {//if user has been placed
					if (Integer.parseInt(index[i - 1]) != tUserIndex) {//if the original is not the user
						newIndexes += index[i - 1] + " ";//record one before
						newValues += val[i - 1] + " ";
					}//end if
				} else {//check if user need to be placed
					if (Integer.parseInt(val[i]) > rankInfo) {//record original
						if (Integer.parseInt(index[i]) != tUserIndex) {
							newIndexes += index[i] + " ";
							newValues += val[i] + " ";
						}
					} else {
						newIndexes += tUserIndex + " ";
						newValues += rankInfo + " ";
						placed = true;// record the placement
					} // end if
				} // end if
			} // end for
			if (!placed) {// add the user if ranked last
				newIndexes += tUserIndex;
				newValues += rankInfo;
			} else {// add the last if user is not last
				if (Integer.parseInt(index[index.length - 1]) != tUserIndex) {
					newIndexes += index[index.length - 1];
					newValues += val[val.length - 1];
				}
			} // end if
		} catch (Exception e) {// add the user directly when there is no user exists in record rank
			newIndexes += tUserIndex;
			newValues += rankInfo;
		} // end try catch
		PrintWriter rankWriter = new PrintWriter(theRank);// create PrintWriter to write the file
		rankWriter.println(newIndexes);// write the indexes
		rankWriter.println(newValues);// write the values
		rankWriter.close();// close the fileWriter
		if (isDisplaying == true) {// if the rankings needs to be displayed, convert the information
			rankInter.convertRank(rankType, newIndexes, newValues);
		} // end if
	}// end method
	/**
	 * The procedure type method initialize a new line in userInfo.
	 * 
	 * @param iNum
	 *            the number of times information needs to be repeated
	 * @param lineNum
	 *            the number of lines the information needs to be repeated
	 * @param state
	 *            the String which contains the information needs to be repeated
	 */
	private void initialn(int lnIndex, int iNum, int lineNum, String state) {
		for (int i = lnIndex; i < lnIndex + lineNum; i++) {
			userInfo[i] = "";
			for (int j = 0; j < iNum - 1; j++) {
				userInfo[i] += state + " ";// add required number of String
			} // end for
			userInfo[i] += state;// end the line
		} // end for
	}// end method
	/**
	 * The procedure type method initiates all information for a new user in system.
	 * 
	 * @param newName
	 *            the user name registered
	 * @param newIndex
	 *            the new user's index
	 * @throws IOException
	 *             Exceptions of using {@link #updateRank(int, String)}method},
	 *             creating new file and registering the new user.
	 */
	private void createUser(String newName, int newIndex) throws IOException {
		userInfo[0] = newName;// first line:username
		initialn(1, 1, 13, "null");// initialize the game board to not started
		initialn(14, 1, 3, "0");// initialize the miss number, contract number and time played
		initialn(17, 2, 1, "0");// initialize the battle numbers
		initialn(18, 1, 1, "1");
		;// initialize number of level
		initialn(19, 1, 2, "0");// initialize the exp number and assistant
		userInfo[21] = "0 1 2 3 4";// initialize team setting
		initialn(22, 100, 2, "null");// initialize characters owned list and intimacy list
		initialn(24, 1, 1, "0");// initialize dismantle num
		initialn(25, 1, 1, "false");// initialize expert setting
		initialn(26, 1, 1, "100");// initialize % of sound
		initialn(27, 11, 1, "false");// initialize task completion
		initialn(28, 14, 1, "false");// initialize achievement completion
		initialn(29, 1, 3, "0");// initialize rank information
		updateRank(newIndex, "C", false);// update for all three ranks
		updateRank(newIndex, "L", false);
		updateRank(newIndex, "W", false);
		// initialize empty build informations
		for (int i = 0; i < 3; i++) {
			initialn(32 + i * 2, 1, 1, "null");
			initialn(33 + i * 2, 1, 1, "0");
		} // end for
		new File("User" + newIndex).createNewFile();// create a new file for the new user
		PrintWriter recordWriter = new PrintWriter(new FileWriter("usersRecord.txt", true));// create PrintWriter to
																							// record new user
		recordWriter.println(newName);// write the name into users' record list
		recordWriter.close();// close the record writer
	}// end method
	/**
	 * The return type method convert coordinate to message to console game codes.
	 * @param x x-coordinate needs to be converted
	 * @param y y-coordinate needs to be converted
	 * @return converted String: letter+index start from 1
	 */
	static String convertMessage(int x, int y) {
		return (Character.toString((char) (y + 65)) + Integer.toString(x + 1)).toUpperCase();
	}//end method
	/**
	 * The procedure type method record result of being fired in AI's map.
	 * @param result input message of result
	 * @param nameIndex index of shipName
	 */
	private static void fireResult(String result, int nameIndex) {
		if(AIcombat){//display message if in AI combat mode
			JOptionPane.showMessageDialog(null,
				    "That was: "+result,
				    "Response to other AI",
				    JOptionPane.INFORMATION_MESSAGE,
				    new ImageIcon("gResponse.png"));
		}//end if
		Game.userTurn = false;
		if (result.equals("MISS")) {
			Game.countIncre(Game.uMis);
			Game.enemMap[y][x].setBackground(Game.darkBlue);
		} else {
			Game.countIncre(Game.uHit);
			Game.enemMap[y][x].setBackground(Game.darkRed);
			Game.enemMap[y][x].setForeground(Color.white);
			Game.enemMap[y][x].setHorizontalAlignment(JLabel.CENTER);
			Game.enemMap[y][x].setText(result.substring(nameIndex,nameIndex+2));
		}
	}//end method
	/**
	 * Functions that only need to be run at the beginning of a new game (from console game)
	 * @author Raymond
	 * @throws IOException
	 */
	private static void newGameProcedure() throws IOException {

		// Initialize enemy grid
		for (int i = 0; i < Battleship.enemyGrid.length; i++)
			for (int j = 0; j < Battleship.enemyGrid[i].length; j++)
				Battleship.enemyGrid[i][j] = new Square(j, i);

		// Initialize home grid
		for (int i = 0; i < Battleship.homeGrid.length; i++)
			for (int j = 0; j < Battleship.homeGrid[i].length; j++)
				Battleship.homeGrid[i][j] = new Square(j, i);

		// Create a new AI - God's warrior angel
		Amadeus = new AI(true);
		Battleship.displayPD(Battleship.enemyGrid);
		Battleship.displayShips(Battleship.homeGrid);
		game();
	}//end method
	/**
	 * The actual game method from console game.
	 * @author Raymond
	 * @throws IOException
	 */
	private static void game() throws IOException {
		AIWin = false;
		userWin = false;
		round = 0;
		shipNumber = 0;
		userShot = null;
		AIShot = null;
		ship = Battleship.homeShips[0];
		Game.timerLabel.setText("00:00:00");
		// Who goes first
		System.out.println("You first or Amadeus (the AI) first?");

		input = firstHand;
		// User wants AI to go first
		if (input.contains("A")) {
			AIFirst = true;
			System.out.println("Amadeus is going first.");
		} else {
			AIFirst = false;
			System.out.println("You are going first.");
		}

		// Place user's ships
		System.out.println("Place your ships on a separate grid. When you're ready, press ENTER to continue...");
		for (int i = 0; i < Battleship.enemyShips.length; i++)
			Battleship.enemyShips[i] = new Ship(Battleship.enemyGrid, Battleship.shipNames[i],
					Battleship.shipLengths[i]);
	}//end method
	/**
	 * The return type method which repsonds to AI's shot.
	 * @param y y-coordinate of AI's shot
	 * @param x x-coordinate of AI's shot
	 * @param AIcoor AI's coordinates in String format
	 * @return output result String
	 */
	private static String getFire(int y, int x, String AIcoor) {
		if(AIcombat){//ask for response if in AI combat mode
			Object[] reOptions = { "MISS", "HIT", "SUNK" };//results type options
			int responseIndex = -1;
			do {
				responseIndex = JOptionPane.showOptionDialog(null, "Shot: "+AIcoor, "Get Response",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon("gNani.png"), reOptions,
						reOptions[0]);//display ask result type
			} while (!areYouSure(reOptions[responseIndex].toString()));//confirm
			if(responseIndex==0){
				Game.countIncre(Game.eMis);//count a miss
				return "MISS";//return miss
			}else{//if hit or sunk as for ship type
				Game.countIncre(Game.eHit);//count a hit
				String ultResponse = "HIT, ";
				if(responseIndex==2){
					ultResponse += "SUNK ";
				}//end if
				Object[] hitShipOptions = Battleship.shipNames;//ship name options
				int hitShipIndex = -1;
				do {
					hitShipIndex = JOptionPane.showOptionDialog(null, "Response from other AI?", "Get Response",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon("gNani.png"), hitShipOptions,
							hitShipOptions[0]);
				} while (!areYouSure(hitShipOptions[hitShipIndex].toString()));//confirm
				return ultResponse+Battleship.shipNames[hitShipIndex];//return after adding name
			}//end if
		}//end if
		//auto check the result if play with human
		Color unitStatus = Game.userMap[x][y].getBackground();
		if (unitStatus.getBlue()==0) {
			String shipName = "";
			for(int i =1;i<6;i++){//find ship name by background color
				if(unitStatus.equals(unitColor[i])){
					shipName=Battleship.shipNames[i-1];
					break;
				}//end if
			}//end for
			Game.userMap[x][y].setBackground(Game.darkRed);//mark hit
			Game.countIncre(Game.eHit);//record hit
			//Breadth First Search
			int[] adx = { 0, 1, 0, -1 };//change of x coordinate
			int[] ady = { 1, 0, -1, 0 };//change of y coordinate
			ArrayList<Integer> shipX = new ArrayList<Integer>();//units waiting to be checked
			ArrayList<Integer> shipY = new ArrayList<Integer>();
			ArrayList<Integer> visitedx = new ArrayList<Integer>();//unit checked
			ArrayList<Integer> visitedy = new ArrayList<Integer>();
			shipX.add(x);//add original ship hit
			shipY.add(y);
			while(!shipX.isEmpty()){
				int thisx = shipX.remove(0);//read the first waiting unit and record, remove from list
				int thisy = shipY.remove(0);
				visitedx.add(thisx);//mark visited
				visitedy.add(thisy);
				for (int i = 0; i < 4; i++) {
					int newx = thisx + adx[i];//calculate coordinate for adjacent unit
					int newy = thisy + ady[i];
					if (newx > -1 && newx < 10 && newy > -1 && newy < 10) {
						System.out.println("checkingUNIT: "+newx+" "+newy+" "+Game.userMap[newx][newy].getBackground().toString()+" "+Game.userMap[newx][newy].getText()+" "+(visitedx.indexOf(newx))+" "+(visitedy.indexOf(newy)));
						if (Game.userMap[newx][newy].getText().equals(Game.userMap[x][y].getText())&&(Game.userMap[newx][newy].getBackground().getBlue()==0)) {
							System.out.println("AI HIT "+shipName+" "+newx+" "+newy);
							return "HIT "+shipName;//return hit message
						}//end if
						if(Game.userMap[newx][newy].getText().equals(Game.userMap[x][y].getText())&&Game.userMap[newx][newy].getBackground().equals(Game.darkRed)&&((visitedx.indexOf(newx)==-1)||(visitedy.indexOf(newy)==-1))){
							System.out.println("red "+newx+" "+newy);
							shipX.add(newx);//add damaged ship to waiting list
							shipY.add(newy);
						}//end if
					}//end if
				}//end for
			}//end while
			System.out.println("AI HIT, SUNK "+shipName);
			return "HIT, SUNK "+shipName;//return sunk message
		} else {
			if (Game.userMap[x][y].getBackground().equals(Game.darkBlue)) {
				Game.userMap[x][y].setBackground(Game.fogBlue);
			}//end if
			Game.userMap[x][y].setBorder(new LineBorder(Color.gray));//mark miss
			Game.countIncre(Game.eMis);//record miss
			System.out.println("AI MISS");
			return "MISS";
		}//end if
	}//end method
	/**
	 * The procedure type method add exp to user in system.
	 * @param expInAddition exp amount in addition
	 */
	static void getExp(long expInAddition){
		Long expUpdated = Long.parseLong(userInfo[19])+expInAddition;//record the new exp after addition
		userInfo[19]= Long.toString(expUpdated);//update exp amount
		userInfo[18]= Long.toString(calLv(expUpdated));//calculate and update Lv number
	}//end method
	/**
	 * The procedure type method record the battle and win when game ends.
	 * @param userWin boolean which indicates whether user/other AI wins the game
	 */
	private static void recordBattle(boolean userWin){
		String[] nums = userInfo[17].split(" ");//read battle number and win number
		int winNum = Integer.parseInt(nums[1]);//record original number of win
		if((userWin&&(!AIcombat))){
			winNum++;//increment for win number if the user wins
		}//end if
		userInfo[17] = (Integer.parseInt(nums[0])+1)+" "+winNum;//updated battle+1 and updated win number
	}//end method
	/**
	 * The return type method calculate the level after exp is added.
	 * @param exp the amount of exp needs to be converted
	 * @return the new Lv number
	 */
	private static long calLv(long exp){
		return (long) Math.floor((25+Math.sqrt(625+100*exp))/50);//return calculated with formula
	}//end method
	/**
	 * The procedure type method which performs game end procedures including mark not in game, show end game message, add exp, record battle.
	 * @param userWin boolean indicates whether user/other AI wins
	 */
	private static void endGame(boolean userWin) {
		inGame = false;//record not in game
		userInfo[1]= "null";//record not in game in user information
		Game.userTurn = true;//allows user to operate components
		String missNum = "";//use String to record miss
		if(AIcombat){
			missNum = Game.eMis.getText();//record AI(this side)'s miss
		}else{
			missNum = Game.uMis.getText();//record user's miss
		}//end if
		userInfo[14] = Integer.toString(Integer.parseInt(userInfo[14])+Integer.parseInt(missNum));//record miss
		Game.timer.stop();//stop timer
		getExp(18000/(Game.timeUsed/1000));//add exp
		recordBattle(userWin);//record battle
		if (userWin) {
			Game.winWord.setVisible(true);//win message displayed
		} else {
			Game.losWord.setVisible(true);//lost message displayed
		}//end if
	}//end method
	/**
	 * The procedure type method which check if fire from user/other AI miss, hit or sunk ship.
	 * @author Raymond
	 * @editor Benny (Integration)
	 * @param isNotFirst if this is the first time checking
	 */
	static void AIcheck(boolean isNotFirst) {
		if (!isNotFirst) {
			round++;
		}
		x = Integer.parseInt(input.substring(1, input.length())) - 1;
		y = ((int) input.charAt(0)) - 65;// ASCII value for A~J = 65~74
		System.out.println("check " + round + " " + x + " " + y);
		if (x == 1) {// Get second digit, could be 10
			try {
				if (Integer.parseInt(input.substring(2, 3)) == 0)
					x = 9;
			} catch (Exception e) {
			}
		}
		// Check for hit or miss on home grid
		userShot = Battleship.homeGrid[y][x];
		if (userShot.shipType == null) {// Miss
			userShot.status = SquareTypes.MISS;
			System.out.println("MISS");
			fireResult("MISS",-1);
		} else {// Hit
			Battleship.homeGrid[y][x].status = SquareTypes.HIT;
			for (int i = 0; i < Battleship.homeShips.length; i++)
				for (int j = 0; j < Battleship.homeShips[i].location.size(); j++)
					if (Battleship.homeShips[i].location.get(j) == userShot) {
						ship = Battleship.homeShips[i];
						if (isNotFirst) {
							shipNumber = i;
						}
						break;
					}
			if (isNotFirst) {
				flag = true;
				for (int i = 0; i < ship.location.size(); i++)
					if (ship.location.get(i).status == SquareTypes.UNKNOWN) {
						flag = false;
						break;
					}
				if (flag)
					Battleship.homeShipsSunk[shipNumber] = true;
				else
					Battleship.homeShipsSunk[shipNumber] = false;
				if (Battleship.homeShipsSunk[shipNumber]) {
					System.out.println("HIT, SUNK " + Battleship.homeShips[shipNumber].shipName);
					fireResult("HIT, SUNK " + Battleship.homeShips[shipNumber].shipName,10);

					// Check for win (all ships sunk)
					flag = true;
					for (int i = 0; i < Battleship.homeShipsSunk.length; i++)
						if (!Battleship.homeShipsSunk[i]) {
							flag = false;
							break;
						}
					if (flag) // User has won
						userWin = true;
				} else{
					System.out.println("HIT, " + ship.shipName);
					fireResult("HIT, " + ship.shipName,5);
				}
			} else {
				System.out.println("HIT, " + ship.shipName);
				fireResult("HIT, " + ship.shipName,5);
			}
		}
		Battleship.enemyShotLog.add(Battleship.homeGrid[y][x]);// Add enemy shot to log
	}//end method
	/**
	 * The procedure type method which performs the round of AI as AI fires and user/other AI check results
	 * @author Raymond
	 * @editor Benny (Integration)
	 */
	static void AIRound() {
		round++;
		// Get AI's shot
		System.out.println("Round " + round + ". Amadeus's turn.");
		AIShot = Amadeus.aim(AIShot, Battleship.enemyGrid, Battleship.shipLengths);
		Battleship.displayPD(Battleship.enemyGrid);
		Battleship.homeShotLog.add(AIShot);// Add home shot to log
		String AIshot = (Character.toString((char) (AIShot.y + 65)) + Integer.toString(AIShot.x + 1)).toUpperCase();
		// Print AI's shot's y-x coordinate converted to Battleship standards (e.g. A1)
		System.out.println("AI's shot coordinates: "
				+ AIshot);
		// Get user's response
		System.out.println("HIT or MISS?");
		input = getFire(AIShot.x, AIShot.y, AIshot).toUpperCase();
		System.out.println("getInput--------------: "+input);

		// AI hit a ship
		if (input.contains("HIT")) {
			AIShot.status = SquareTypes.HIT;

			// Get ship name
			for (int i = 0; i < Battleship.shipNames.length; i++)
				if (input.contains(Battleship.shipNames[i].toUpperCase())) {
					System.out.println("found ship: "+Battleship.shipNames[i]);
					ship = Battleship.enemyShips[i];
					ship.location.add(AIShot);
					break;
				}
			// Check if sunk
			if (input.contains("SUNK")) {
				int temp = 0;
				for (int i = 0; i < ship.location.size(); i++)// Set all location squares of ship to status sunk
					ship.location.get(i).status = SquareTypes.SUNK;
				for (int i = 0; i < Battleship.shipNames.length; i++)
					if (ship.shipName.equalsIgnoreCase(Battleship.shipNames[i])) {
						temp = i;
						break;
					}
				Battleship.enemyShipsSunk[temp] = true;
				
				// Check if AI has won
				AIWin = true;
				for (boolean b : Battleship.enemyShipsSunk)
					if (!b) {
						AIWin = false;
						break;
					}
				System.out.println("and AIWin? " + AIWin);
			}
		}

		// AI missed
		else if (input.contains("MISS")) {
			AIShot.status = SquareTypes.MISS;
		}
		Game.userTurn = true;
	}//end method
	/**
	 * The procedure type method check if either side win the game at current status by counting number of hits.
	 */
	static void checkWin() {
		boolean uWin = Game.uHit.getText().equals("17");//check if user/other AI has hit 17 times (all ships)
		boolean eWin = Game.eHit.getText().equals("17");//check same thing for AI side
		if ((uWin&&(!AIcombat))||(eWin&&AIcombat)) {//reverse the result if in AI combat
			System.out.println("Congrats, you have won!");
			endGame(true);//execute end game procedures
			return;//check completed
		}//end if
		if ((uWin&&AIcombat)||eWin&&(!AIcombat)) {
			System.out.println("Sorry, you have lost.");
			endGame(false);//execute end game procedures
			return;//check completed
		}//end if
	}//end method
	public static void main(String[] args) throws IOException {
		system theGame = new system();
	}//end method
}//end class
