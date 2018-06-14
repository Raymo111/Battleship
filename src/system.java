import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.LineBorder;

public class system extends JFrame {
	static Boolean inGame = false;
	int userIndex;
	login startGame;
	static String[] userInfo = new String[38];
	static String offset = "";
	static int difficulty = -1;
	static String firstHand = "";
	static boolean AIFirst, AIWin, userWin;
	static int round, x, y, shipNumber;// The index of a ship in homeShips grid
	static Square userShot, AIShot;
	static Ship ship;
	static boolean flag;
	static String input, validate;
	static AI Amadeus;//new name from Martin
	final static Color[] unitColor = {game.darkBlue,new Color(0,200,0), new Color(0,170,0), new Color(0,140,0),new Color(0,110,0),new Color(0,80,0),game.darkBlue,game.fogBlue,game.darkRed};
	
	
	MouseListener directory = new MouseListener() {
		public void mouseClicked(MouseEvent event) {
			JLabel source = (JLabel) event.getSource();
			try {
				if (source.equals(baseInter.gameButton)) {
					if(baseInter.futurePost.isVisible()){
						baseInter.futurePost.setVisible(false);
					}
					System.out.println(-1);
					remove(baseInter);
					add(gameInter);
					enterGame();
					repaint();
					return;
				}
				if (source.equals(baseInter.rankingButton)) {
					if(baseInter.futurePost.isVisible()){
						baseInter.futurePost.setVisible(false);
					}
					System.out.println(6);
					remove(baseInter);
					add(rankInter);
					repaint();
					updateRank(userIndex, "C", true);
					updateRank(userIndex, "L", true);
					updateRank(userIndex, "W", true);
					return;
				}
				if (source.equals(baseInter.achievementButton)) {
					if(baseInter.futurePost.isVisible()){
						baseInter.futurePost.setVisible(false);
					}
					System.out.println(8);
					remove(baseInter);
					add(achiInter);
					repaint();
					return;
				}
				if(source.equals(baseInter.taskButton)||source.equals(baseInter.factoryButton)||source.equals(baseInter.settingButton)){
					baseInter.futurePost.setVisible(true);
					return;
				}
				if (source.getIcon().toString().equals("theBackButton.png")) {
					if(source.equals(gameInter.backButton)){
						if(!game.userTurn){
							return;
						}
					}
					System.out.println(0);
					getContentPane().removeAll();
					baseInter.updateInfo(userInfo[0], Integer.parseInt(userInfo[18]), Integer.parseInt(userInfo[15]));// update base
					if(baseInter.futurePost.isVisible()){
						baseInter.futurePost.setVisible(false);
					}
					add(baseInter);
					repaint();
					return;
				}
			} catch (Exception exp) {
					
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
			baseInter.updateInfo(userInfo[0], Integer.parseInt(userInfo[18]), Integer.parseInt(userInfo[15]));// initialize base
			add(baseInter);
			setVisible(true);
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

	MouseListener gameOper = new MouseListener() {
		public void mouseClicked(MouseEvent e) {
			Object source = e.getSource();
			if (source.equals(gameInter.startButton)&&(!inGame)) {
				inGame = true;
				game.firstClick = true;
				gameInter.lastRecordTime = System.currentTimeMillis();
				game.timer.restart();
				game.eHit.setText("0");
				game.eMis.setText("0");
				game.uHit.setText("0");
				game.uMis.setText("0");
				try {
					newGameProcedure();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				// remove drag functions, start timer
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

	base baseInter = new base();
	game gameInter = new game();
	rankings rankInter;
	Achievements achiInter = new Achievements();

	public system() throws IOException {
		startGame = new login();
		startGame.okButton.addMouseListener(loginOper);
		for (int i = 0; i < 6; i++) {
			baseInter.mRightButtons.get(i).addMouseListener(directory);// add directory to all buttons in base interface
		} // end for
		gameInter.backButton.addMouseListener(directory);// add directory to buttons in game interface
		gameInter.startButton.addMouseListener(gameOper);
		rankInter = new rankings();// initializing the rankings interface
		rankInter.backButton.addMouseListener(directory);
		achiInter.backButton.addMouseListener(directory);
		addWindowListener(new java.awt.event.WindowAdapter() {// need to save the information before closing
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				try {
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
		setSize(1300, 700);
		setLocationRelativeTo(null);
	}

	public void enterGame() {
		if (!inGame) {
			do {
				Object[] options = { "User", "AI" };
				int firsthand = JOptionPane.showOptionDialog(null, "Battle started from:", "First hand",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon("gNani.png"), options,
						options[0]);
				if (firsthand == 0) {
					firstHand = "User";
					game.userTurn = true;
				} else {
					firstHand = "AI";
					game.userTurn = false;
				}
			} while (!areYouSure(firstHand));
			Object[] diOptions = { "Expert - Raymond", "Random", "Expert - David" };
			do {
				difficulty = JOptionPane.showOptionDialog(null, "Choose a difficulty level:", "Difficulty",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon("gNani.png"), diOptions,
						diOptions[0]);
			} while (!areYouSure(diOptions[difficulty].toString()));
			Object[] offsetOptions = { "Yes", "No" };
			int offsetInd;
			do {
				offsetInd = JOptionPane.showOptionDialog(null, "Offset edges?", "Offset?",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon("gNani.png"), offsetOptions,
						offsetOptions[0]);
				if (offsetInd == 0) {
					offset = "y";
				} else {
					offset = "n";
				}
			} while (!areYouSure(offsetOptions[offsetInd].toString()));
		}
	}

	public static boolean areYouSure(String confirmInfo) {
		Object[] optionsAg = { "Yes", "No" };
		int confirm = JOptionPane.showOptionDialog(null, confirmInfo + " -Are you sure?", "First hand Confirm",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon("gNani.png"), optionsAg,
				optionsAg[0]);
		if (confirm == 0) {
			return true;
		} else {
			return false;
		}
	}

	public void readLogin(String thisUsername) throws IOException {
		BufferedReader fileReader = new BufferedReader(new FileReader("usersRecord.txt"));
		int thisUserIndex = 0;
		try {
			while (true) {// search till end of the list
				thisUserIndex++;// incrementing index of the user
				if (fileReader.readLine().equals(thisUsername)) {// when the username is found in history
					loadUser(thisUserIndex);// load the user information
					break;// stop searching
				}
			}
		} catch (Exception e) {
			// when the username is not found in history
			System.out.println("u" + thisUserIndex);
			createUser(thisUsername, thisUserIndex);
		}
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
	public void loadUser(int tUserIndex) throws IOException {
		BufferedReader fileReader = new BufferedReader(new FileReader("User" + tUserIndex));// create BufferedReader to
																							// read record
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
	public void updateRank(int tUserIndex, String rankType, Boolean isDisplaying) throws IOException {
		System.out.println("------------------------------------------" + rankType);
		File theRank = new File("rank" + rankType + ".txt");
		BufferedReader rankReader = new BufferedReader(new FileReader(theRank));
		int rankInfo;// record the info of rank of target player for comparisons
		if (rankType.equals("C")) {
			rankInfo = Integer.parseInt(userInfo[29]);
		} else if (rankType.equals("L")) {
			rankInfo = Integer.parseInt(userInfo[30]);
		} else {
			rankInfo = Integer.parseInt(userInfo[31]);
		} // end if
		String newIndexes = "";// initialize empty String for the new indexes
		String newValues = "";// initialize empty String for the new values
		try {
			System.out.println("_______________________________________________ranked");
			String[] index = rankReader.readLine().split(" ");// read and record the indexes and values
			String[] val = rankReader.readLine().split(" ");
			rankReader.close();// close the IO fileReader
			boolean placed = false;
			for (int i = 0; i < index.length; i++) {
				if (placed) {
					if (Integer.parseInt(index[i - 1]) != tUserIndex) {
						newIndexes += index[i - 1] + " ";
						newValues += val[i - 1] + " ";
						System.out.println(i + " " + 1);
					}
				} else {
					if (Integer.parseInt(val[i]) > rankInfo) {
						if (Integer.parseInt(index[i]) != tUserIndex) {
							newIndexes += index[i] + " ";
							newValues += val[i] + " ";
							System.out.println(i + " " + 2);
						}
					} else {
						newIndexes += tUserIndex + " ";
						newValues += rankInfo + " ";
						System.out.println(i + " " + 3);
						placed = true;// record the placement
					} // end if
				} // end if
			} // end for
			if (!placed) {// add the user if ranked last
				newIndexes += tUserIndex;
				newValues += rankInfo;
				System.out.println(4);
			} else {// add the last if user is not last
				if (Integer.parseInt(index[index.length - 1]) != tUserIndex) {
					newIndexes += index[index.length - 1];
					newValues += val[val.length - 1];
					System.out.println(5);
				}
			} // end if
		} catch (Exception e) {// add the user directly when there is no user exists in record rank
			System.out.println("_______________________________________________direct");
			newIndexes += tUserIndex;
			newValues += rankInfo;
			System.out.println(6);
		} // end try catch
		PrintWriter rankWriter = new PrintWriter(theRank);// create PrintWriter to write the file
		rankWriter.println(newIndexes);// write the indexes
		rankWriter.println(newValues);// write the values
		rankWriter.close();// close the fileWriter
		System.out.println("rkrkrkrkrkrkrkkrkrkrkrk\n" + isDisplaying);

		if (isDisplaying == true) {// if the rankings needs to be displayed, convert the information
			System.out.println("rkrkrkrkrkrkrkkrkrkrkrk\n" + isDisplaying);

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
	public void initialn(int lnIndex, int iNum, int lineNum, String state) {
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
	public void createUser(String newName, int newIndex) throws IOException {
		userInfo[0] = newName;// first line:username
		initialn(1, 1, 14, "null");// initialize the game board to not started
		initialn(15, 1, 2, "0");// initialize the contract number and time played
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
		initialn(28, 26, 1, "false");// initialize achievement completion
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

	public void check() {
		for (int i = 0; i < 38; i++) {
			System.out.println(userInfo[i]);
		}
	}

	public static String convertMessage(int x, int y) {
		System.out.println(y + " " + x);
		return (Character.toString((char) (y + 65)) + Integer.toString(x + 1)).toUpperCase();
	}

	public static void fireResult(String result) {
		game.userTurn = false;
		if (result.equals("MISS")) {
			game.countIncre(game.uMis);
			game.enemMap[y][x].setBackground(game.darkBlue);
		} else {
			game.countIncre(game.uHit);
			game.enemMap[y][x].setBackground(game.darkRed);
		}
	}

	/**
	 * Functions that only need to be run at the beginning of a new game
	 * 
	 * @throws IOException
	 */
	public static void newGameProcedure() throws IOException {

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
	}

	/**
	 * The actual game method
	 * 
	 * @throws IOException
	 */
	public static void game() throws IOException {
		AIWin = false;
		userWin = false;
		round = 0;
		shipNumber = 0;
		userShot = null;
		AIShot = null;
		ship = Battleship.homeShips[0];
		game.timerLabel.setText("00:00:00");
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
	}

	public static String getFire(int y, int x) {
		Color unitStatus = game.userMap[x][y].getBackground();
		if (unitStatus.getBlue()==0) {
			String shipName = "";
			for(int i =1;i<6;i++){
				if(unitStatus.equals(unitColor[i])){
					shipName=Battleship.shipNames[i-1].toUpperCase();
					break;
				}
			}
			game.userMap[x][y].setBackground(game.darkRed);
			game.countIncre(game.eHit);
			int[] adx = { 0, 1, 0, -1 };
			int[] ady = { 1, 0, -1, 0 };
			for (int i = 0; i < 4; i++) {
				int newx = x + adx[i];
				int newy = y + ady[i];
				if (newx > 0 && newx < 10 && newy > 0 && newy < 10) {
					if (game.userMap[newx][newy].getBackground().getBlue()!=0) {
						System.out.println("AI HIT "+shipName);
						return "HIT "+shipName;
					}
				}
			}
			System.out.println("AI SUNK "+shipName);
			return "SUNK "+shipName;
		} else {
			if (game.userMap[x][y].getBackground().equals(game.darkBlue)) {
				game.userMap[x][y].setBackground(game.fogBlue);
			}
			
			game.userMap[x][y].setBorder(new LineBorder(Color.gray));
			game.countIncre(game.eMis);
			System.out.println("AI MISS");
			return "MISS";
		}
	}
	public static void getExp(long expInAddition){
		userInfo[19]= Long.toString(Long.parseLong(userInfo[19])+expInAddition);
		userInfo[18]= Long.toString(calLv(expInAddition));
	}
	public static long calLv(long exp){
		return (long) Math.floor((25+Math.sqrt(625+100*exp))/50);
	}
	public static void endGame(boolean userWin) {
		system.inGame = false;
		userInfo[1]= "null";
		game.userTurn = false;
		game.timer.stop();
		getExp(18000/(game.timeUsed/1000));
		if (userWin) {
			game.winWord.setVisible(true);
		} else {
			game.losWord.setVisible(true);
		}
		// and reset game method
	}

	public static void AIcheck(boolean isNotFirst) {
		System.out.println("isFirst? " + (!isNotFirst));
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
			fireResult("MISS");
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
					fireResult("HIT");

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
					fireResult("HIT");
				}
			} else {
				System.out.println("HIT, " + ship.shipName);
				fireResult("HIT");
			}
		}
		Battleship.enemyShotLog.add(Battleship.homeGrid[y][x]);// Add enemy shot to log
	}

	public static void AIRound() {
		round++;
		// Get AI's shot
		System.out.println("Round " + round + ". Amadeus's turn.");
		AIShot = Amadeus.aim(AIShot, Battleship.enemyGrid, Battleship.shipLengths);
		Battleship.displayPD(Battleship.enemyGrid);
		Battleship.homeShotLog.add(AIShot);// Add home shot to log

		// Print AI's shot's y-x coordinate converted to Battleship standards (e.g. A1)
		System.out.println("AI's shot coordinates: "
				+ (Character.toString((char) (AIShot.y + 65)) + Integer.toString(AIShot.x + 1)).toUpperCase());
		// Get user's response
		System.out.println("HIT or MISS?");
		input = getFire(AIShot.x, AIShot.y);

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
					if (ship.shipName.equals(Battleship.shipNames[i].toUpperCase())) {
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
		game.userTurn = true;
	}

	public static void checkWin() {
		// If user wins
		if (game.uHit.getText().equals("17")) {
			System.out.println("Congrats, you have won!");
			endGame(true);
		}
		if (game.eHit.getText().equals("17")) {
			System.out.println("Sorry, you have lost.");
			endGame(false);
		}
		System.out.println("What the heck?");
	}

	public static void main(String[] args) throws IOException {
		system theGame = new system();
	}

}
