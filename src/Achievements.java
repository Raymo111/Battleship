import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
/**
 * 
 * @author Martin Xu
 *
 */
public class Achievements extends JPanel implements MouseListener{
	private final static int num=16;
	private static JLabel BackButton=new JLabel();
	private static JScrollPane scrollPane = new JScrollPane();
	private static JPanel paneContent=new JPanel();
	private static JLabel[] achievementsNoShadow=new JLabel[num];
	private static JLabel bgi = new JLabel(new ImageIcon("achievementBgi.jpg"));
	private static boolean[]accomplished=new boolean[num];
	
	private static final String[] AName = new String[] {
			"Too young, too simple, sometimes naive",
			"I'm bald, I'm strong",
			"Omae wa mou shindeiru",
			"Main heroine of Asadora",
			"Letter from 1976",
			"Kimochi warui",
			"AAAHHHHH!!! Boku no o no chikara ga!!!!!",
			"Potato aiming",
			"Do u no dewae",
			"Fernando Torres",
			"Somebody toucha my spaghet",
			"Dormammu i've come to bargain",
			"Performance Artist",
			"24",
			"El psy congroo",
			"Jinrui Hokan"
	};
	
	private static final String[]reqirements=new String[] {
			"win 100 battles",
			"win 1000 battles",
			"win 2000 battles",
			"lose 10 battles",
			"lose 100 battles",
			"lose 1000 battles",
			"lose 2000 battles",
			"miss 100 times",
			"miss 1000 times",
			"miss 5000 times",
			"fight 10 battles",
			"fight 100 battles",
			"fight 5000 battles",
			"total 24 hours played",
			"total 2036 hours played",
			"Complete all other achievements"
	};

	Insets bInsets;
	
	public Achievements() {
		setSize(1300,700);
		setLayout(null);
		bInsets=getInsets();
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(bInsets.left+150,bInsets.top+20,1300,700);	
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
        makeLabel();
        add(scrollPane);
        scrollPane.setVisible(true);
		bgi.setBounds(bInsets.left,bInsets.top,1300,700);
		add(bgi);
		setVisible(true);
	}
	
	public static void makeLabel() {
		paneContent.setLayout(new BoxLayout(paneContent,BoxLayout.Y_AXIS));
		for(int i=0;i<num;i++) {
			if(accomplished[i]) {
				achievementsNoShadow[i]=new JLabel(new ImageIcon("bkgBasicNoShadow.png"));
			}else {
				achievementsNoShadow[i]=new JLabel(new ImageIcon("bkgBasicBWNoShadow.png"));
			}
			paneContent.add(achievementsNoShadow[i]);
		}
		paneContent.setOpaque(false);
		scrollPane.add(paneContent);
		scrollPane.setViewportView(paneContent);
	}

	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.add(new Achievements());
		f.setSize(f.getPreferredSize());
		f.setVisible(true);

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
		
	}

}
