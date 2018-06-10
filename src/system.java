import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.swing.*;

public class system extends JFrame {
	Boolean inGame = false;
	login startGame;

	MouseListener directory = new MouseListener() {
		public void mouseClicked(MouseEvent event) {
			JLabel source = (JLabel)event.getSource();
			if (source.equals(baseInter.gameButton)) {
				System.out.println(-1);
				remove(baseInter);
				add(gameInter);
				enterGame();
				repaint();
			}
			if(source.getIcon().toString().equals("theBackButton.png")){
				System.out.println(0);
				getContentPane().removeAll();
				add(baseInter);
				repaint();
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
			} catch (FileNotFoundException e1) {
			}
			startGame.dispose();
			setVisible(true);
		}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
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
	public void enterGame(){
		if(!inGame){
			//will be modify to be round shape frame if there is time left
			Object[] options = {"User","AI"};
			int n = JOptionPane.showOptionDialog(null,
					"Battle started from:",
					"First hand",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					new ImageIcon("gNani.png"),     
					options,  
					options[0]); 
			if(n==0){
				gameInter.userTurn = true;
			}else{
				gameInter.userTurn = false;
			}
		}
	}
	public void readLogin(String thisUsername) throws FileNotFoundException{
	    	BufferedReader fileReader = new BufferedReader(new FileReader("usersRecord.txt"));
	    	int userIndex = 0;
	    	try{
	    		while(true){//search till end of the list
	    			userIndex++;//incrementing index of the user
	    			if(fileReader.readLine().equals(thisUsername)){//when the username is found in history
	    				break;
	    			}
	    		}
	    	}catch(Exception e){
	    		//when the username is not found in history
	    		
	    	}
	 }
	 public void createUser(String newName, int newIndex){
	    	String newInfo = "";
	    	newInfo+=newName+"\n";//first line:username
	    	for(int i =0;i<14;i++){
	    		newInfo+="null\n";//enter null game info for the user who have not play a game
	    	}//end for
	    	
			File thisUser = new File("User"+newIndex);
			
	    }
	public static void main(String[] args) {
		system theGame = new system();
	}

}
