import java.awt.*;
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

	MouseListener directory = new MouseListener() {
		public void mouseClicked(MouseEvent event) {
			JLabel source = (JLabel) event.getSource();
			if (source.equals(baseInter.gameButton)) {
				System.out.println(-1);
				remove(baseInter);
				add(gameInter);
				enterGame();
				repaint();
			}
			if (source.getIcon().toString().equals("theBackButton.png")) {
				System.out.println(0);
				getContentPane().removeAll();
				add(baseInter);
				repaint();
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
			}
			startGame.dispose();
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
	base baseInter = new base();
	game gameInter = new game();

	public system() {
		startGame = new login();
		startGame.okButton.addMouseListener(loginOper);

		baseInter.gameButton.addMouseListener(directory);
		gameInter.backButton.addMouseListener(directory);
		add(baseInter);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		setSize(1300, 700);
		setLocationRelativeTo(null);
	}

	public void enterGame() {
		if (!inGame) {
			// will be modify to be round shape frame if there is time left
			Object[] options = { "User", "AI" };
			int n = JOptionPane.showOptionDialog(null, "Battle started from:", "First hand", JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE, new ImageIcon("gNani.png"), options, options[0]);
			if (n == 0) {
				gameInter.userTurn = true;
			} else {
				gameInter.userTurn = false;
			}
		}
	}

	public void readLogin(String thisUsername) throws IOException {
		BufferedReader fileReader = new BufferedReader(new FileReader("usersRecord.txt"));
		int userIndex = 0;
		try {
			while (true) {// search till end of the list
				userIndex++;// incrementing index of the user
				if (fileReader.readLine().equals(thisUsername)) {// when the username is found in history
					break;
				}
			}
		} catch (Exception e) {
			// when the username is not found in history
			System.out.println(userIndex);
			createUser(thisUsername, userIndex);
		}
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
	public void updateRank(int tUserIndex, String rankType) throws IOException {
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
			String[] index = rankReader.readLine().split(" ");// read and record the indexes and values
			String[] val = rankReader.readLine().split(" ");
			rankReader.close();// close the IO fileReader
			boolean placed = false;
			for (int i = 0; i < index.length; i++) {
				if (placed) {
					newIndexes += index[i - 1] + " ";
					newValues += val[i - 1] + " ";
				} else {
					if (Integer.parseInt(val[i]) > rankInfo) {
						newIndexes += index[i] + " ";
						newValues += val[i] + " ";
					} else {
						newIndexes += tUserIndex + " ";
						newValues += rankInfo + " ";
					} // end if
				} // end if
			} // end for
			if (!placed) {// add the user if ranked last
				newIndexes += tUserIndex;
				newValues += rankInfo;
			} else {// add the last if user is not last
				newIndexes += index[index.length - 1];
				newValues += val[val.length - 1];
			} // end if
		} catch (Exception e) {// add the user directly when there is no user exists in record rank
			newIndexes += tUserIndex;
			newValues += rankInfo;
		} // end try catch
		PrintWriter rankWriter = new PrintWriter(theRank);// create PrintWriter to write the file
		rankWriter.println(newIndexes);// write the indexes
		rankWriter.println(newValues);// write the values
		rankWriter.close();// close the fileWriter
	}

	/**
	 * The procedure type method initialize a new line in userInfo.
	 * 
	 * @param iNum
	 *            the number of times information needs to be repeated
	 * @param lineNum
	 *            the number of lines the information needs to be repeated
	 * @param state
	 *            the String which contains the infomation needs to be repeated
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
	 *             Exceptions of using {@link #updateRank(int, String)}method} and
	 *             creating new file
	 */
	public void createUser(String newName, int newIndex) throws IOException {
		userInfo[0] = newName + "\n";// first line:username
		initialn(1, 1, 14, "null");// initialize the game board to not started
		initialn(15, 1, 2, "0");// initialize the contract number and time played
		initialn(17, 2, 1, "0");// initialize the battle numbers
		initialn(18, 1, 1, "1");
		;// initialize number of level
		initialn(19, 1, 2, "0");// initialize the exp number and assistant
		userInfo[21] = "0 1 2 3 4\n";// initialize team setting
		initialn(22, 100, 2, "null");// initialize characters owned list and intimacy list
		initialn(24, 1, 1, "0");// initialize dismantle num
		initialn(25, 1, 1, "false");// initialize expert setting
		initialn(26, 1, 1, "100");// initialize % of sound
		initialn(27, 11, 1, "false");// initialize task completion
		initialn(28, 26, 1, "false");// initialize achievement completion
		initialn(29, 1, 3, "0");// initialize rank information
		updateRank(newIndex, "C");// update for all three ranks
		updateRank(newIndex, "L");
		updateRank(newIndex, "W");
		// initialize empty build informations
		for (int i = 0; i < 3; i++) {
			initialn(32 + i * 2, 1, 1, "null");
			initialn(33 + i * 2, 1, 1, "0");
		} // end for
		new File("User" + newIndex).createNewFile();// create a new file for the new user
	}// end method

	public static void main(String[] args) {
		system theGame = new system();
	}

}
