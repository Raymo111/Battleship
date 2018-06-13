import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

public class achGUI extends JFrame implements ActionListener{
	private JMenuBar menuBar = new JMenuBar();
	private JPanel seaEnemy = new JPanel();
	private JPanel seaFriendly = new JPanel();
	private JPanel LeftInfoBar = new JPanel();
	private JPanel RigtInfoBar = new JPanel();
	private final int sideLength=10;
	
	public achGUI(){
		setSize(1200,800);
		setTitle("Battleship!");
		setJMenuBar(menuBar);
		setLayout(new FlowLayout());
		menuBar.setVisible(true);
		setVisible(true);
	}
	
	public void InitializeGrid() {
		add(seaFriendly);
		add(seaEnemy);
	}
	
	public void InitializeLeftBar() {
		add(LeftInfoBar);
	}
	
	public void InitializeRigtBar() {
		add(RigtInfoBar);
	}
	
	public void actionPerformed(ActionEvent e) {
		
	}

	public static void main(String[] args) {
		achGUI c=new achGUI();

	}

}
