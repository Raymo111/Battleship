import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

public class Rankings extends JPanel {
    Insets bInsets = getInsets();
    JLabel bgi = new JLabel(new ImageIcon("rankBgi.jpg"));
    JLabel backButton = new JLabel(new ImageIcon("theBackButton.png"));
    JLabel buttonEffect = new JLabel(new ImageIcon("gMouse.png"));
    JLabel collectionButton = new JLabel(new ImageIcon("rCollection.png"));
    JLabel levelButton = new JLabel(new ImageIcon("rLevel.png"));
    JLabel winButton = new JLabel(new ImageIcon("rWin.png"));
    JLabel screen = new JLabel(new ImageIcon("rTotalBack.png"));
    JLabel rankBoard = new JLabel();
    JLabel userBoard = new JLabel("-----------------------------------");
    int userRankIndex = -1;
    String getValueL,getValueW;
    JLabel userScreen = new JLabel(new ImageIcon("rUserBack.png"));
    ArrayList<JLabel> subTypes = new ArrayList<JLabel>();
    String collectionRank = "Will be Released in Near Future",levelRank,winRank;
    ArrayList<String> nameList = new ArrayList<String>();
    MouseListener backMouseEffect = new MouseListener(){
		public void mouseClicked(MouseEvent e) {
			buttonEffect.setVisible(false);
		}

		public void mouseEntered(MouseEvent e) {
			buttonEffect.setVisible(true);
		}

		public void mouseExited(MouseEvent e) {
			buttonEffect.setVisible(false);
		}

		public void mousePressed(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}
	};
	MouseListener switchRank = new MouseListener() {
		public void mouseClicked(MouseEvent e) {
			Object source = e.getSource();
			if (source.equals(collectionButton)) {
				System.out.println(777);
				rankBoard.setText(collectionRank);
				userBoard.setText("-----------------------------------");
				return;
			}
			if (source.equals(levelButton)) {
				rankBoard.setText(levelRank);
				userBoard.setText(userRankIndex + ".\t\t" + system.userInfo[0] + "-\t\t-" + "Lv." + getValueL);
				return;
			}
			if (source.equals(winButton)) {
				rankBoard.setText(winRank);
				userBoard.setText(userRankIndex + ".\t\t" + system.userInfo[0] + "-\t\t-" + getValueW + " win(s)");
				return;
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
    public Rankings() throws IOException{
		setSize(1300,700);
		setLayout(null);
		BufferedReader nameReader = new BufferedReader(new FileReader("usersRecord.txt"));
		try {
			while (true) {
				String aRecord = nameReader.readLine();
				if (aRecord != null) {
					nameList.add(aRecord);
				} else {
					break;
				}
			}
		} catch (Exception e) {
		}
		nameReader.close();// close the BufferedReadeer
		backButton.setBounds(bInsets.left + 10, bInsets.top + 10, 100, 60);
		backButton.addMouseListener(backMouseEffect);
		add(backButton);
		buttonEffect.setBounds(bInsets.left, bInsets.top, 120, 80);
		add(buttonEffect);
		buttonEffect.setVisible(false);
		subTypes.add(collectionButton);
		subTypes.add(levelButton);
		subTypes.add(winButton);
		for (int i = 0; i < 3; i++) {
			subTypes.get(i).setBounds(bInsets.left + 10, bInsets.top + 110 + i * 170, 120, 160);
			subTypes.get(i).addMouseListener(switchRank);
			add(subTypes.get(i));
		} // end for
		rankBoard.setBounds(bInsets.left + 185, bInsets.top + 290, 1050, 300);
		rankBoard.setForeground(Color.white);
		rankBoard.setHorizontalAlignment(JLabel.CENTER);
		rankBoard.setFont(new Font(rankBoard.getFont().getName(), Font.PLAIN, 28));
		add(rankBoard);
		userBoard.setBounds(bInsets.left + 185, bInsets.top + 80, 1050, 200);
		userBoard.setForeground(Color.white);
		userBoard.setHorizontalAlignment(JLabel.CENTER);
		userBoard.setFont(new Font(userBoard.getFont().getName(), Font.PLAIN, 60));
		add(userBoard);
		userScreen.setBounds(bInsets.left + 185, bInsets.top + 80, 1050, 200);
		add(userScreen);
		screen.setBounds(bInsets.left + 135, bInsets.top + 50, 1150, 600);
		add(screen);
		bgi.setBounds(bInsets.left, bInsets.top, 1300, 700);
		add(bgi);
		setBackground(Color.DARK_GRAY);
		setVisible(true);
	}

	public void convertRank(String rankType, String indexes, String values) throws IOException {
		System.out.println(rankType + " " + indexes + " " + values);
		System.out.println(nameList.toString());
		String convertedContent = "<html>";// create a String variable to store the result
		String[] getIndex = indexes.split(" ");
		String[] getValue = values.split(" ");
		for (int i = 1; i < Math.min(getIndex.length + 1, 11); i++) {
			if (system.userInfo[0].equals(nameList.get(Integer.parseInt(getIndex[i - 1]) - 1))) {
				userRankIndex = i;
				if (rankType.equals("L")) {
					getValueL = getValue[i - 1];
				} else if (rankType.equals("W")) {
					getValueW = getValue[i - 1];
				}
			}
			convertedContent += i + ".\t\t" + nameList.get(Integer.parseInt(getIndex[i - 1]) - 1) + "-\t\t-";
			if (rankType.equals("L")) {
				convertedContent += "Lv." + getValue[i - 1] + "<br>";
			} else if (rankType.equals("W")) {
				convertedContent += getValue[i - 1] + " win(s) <br>";
			} // end if
			System.out.println(-i);
		} // end for
		convertedContent += "</html>";// end the input stream
		System.out.println(0);
		if (rankType.equals("L")) {
			levelRank = convertedContent;
			System.out.println(20);

		} else if (rankType.equals("W")) {
			winRank = convertedContent;
			System.out.println(30);

		} // end if
		System.out.println("rrrrrrrrrrrrrrrrrrrrrrrrrrrrr\n" + collectionRank);

	}
}
