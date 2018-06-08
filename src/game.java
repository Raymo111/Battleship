import java.awt.*;
import javax.swing.*;

public class game extends JPanel{
	JLabel timer = new JLabel(new ImageIcon("gTimer.png"));
    Insets bInsets = getInsets();
    JLabel backButton = new JLabel("theBackButton.png");
	public game(){
		setSize(1300,700);
		setLayout(null);
		backButton.setBounds(bInsets.left,bInsets.top,100,60);
		timer.setBounds(bInsets.left+550,bInsets.top,200,100);
		add(timer);
		setVisible(true);
		
	}
	
}
