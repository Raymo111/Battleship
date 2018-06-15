import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

<<<<<<< HEAD
public class Achievements extends JPanel{
    JLabel backButton = new JLabel(new ImageIcon("theBackButton.png"));
    JLabel buttonEffect = new JLabel(new ImageIcon("gMouse.png"));
    MouseListener backMouseEffect = new MouseListener(){
=======
public class Achievements extends JPanel implements MouseListener {
	JLabel backButton = new JLabel(new ImageIcon("theBackButton.png"));
	JLabel buttonEffect = new JLabel(new ImageIcon("gMouse.png"));
	MouseListener backMouseEffect = new MouseListener() {
>>>>>>> fcd74addd94d754b4c50e48ab9e2bc3288eca85a
		public void mouseClicked(MouseEvent e) {
			buttonEffect.setVisible(false);
		}

		public void mouseEntered(MouseEvent e) {
			buttonEffect.setVisible(true);
		}

		public void mouseExited(MouseEvent e) {
			buttonEffect.setVisible(false);
		}
<<<<<<< HEAD
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
    };
	private final static int num=14;
	private static int accumExp = 0;
	private static JScrollPane scrollPane = new JScrollPane();
	private static JPanel paneContent=new JPanel();
	private static JLabel[] achievementsLabels=new JLabel[num];
	private static JLabel getRewardButton = new JLabel(new ImageIcon("aGetReward.png"));
	private static JLabel bgi = new JLabel(new ImageIcon("achievementBgi.jpg"));
	private static boolean[] accomplished=new boolean[num];
	private static final int[] achiNums = {100,1000,2000,100,1000,5000};
	private static final int[] achiRews = {100,100,1000,2000,100,1000,2000,100,1000,5000,10,100,5000,2036};
	Insets bInsets;	
	MouseListener getReward = new MouseListener(){
		public void mouseClicked(MouseEvent e) {
			JOptionPane.showMessageDialog(null,
				    "Total Exp Collected: "+accumExp+".",
				    "Reward Collected",
				    JOptionPane.INFORMATION_MESSAGE,
				    new ImageIcon("gResponse.png"));
			system.getExp(accumExp);
			accumExp = 0;
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
	public static void updateAchievement(){
		loadAchi();
		long now = System.currentTimeMillis();
		system.userInfo[16] = Long.toString(now-system.recordTime);
		system.recordTime = now;
		System.out.println("Time Updated");
		String[] battleRecords = system.userInfo[17].split(" ");;
		int winNum = Integer.parseInt(battleRecords[1]);
		int battleNum = Integer.parseInt(battleRecords[0]);
		int losNum = battleNum-winNum;
		int missNum = Integer.parseInt(system.userInfo[14]);
		int[] the3Var = {winNum,losNum,missNum};
		long hourNum = Long.parseLong(system.userInfo[16])/3600000;
		boolean[] newAccomplished = new boolean[num];
		System.out.println("Calculations completed");
		newAccomplished[0] = (winNum>=1);System.out.println(newAccomplished[0]);
		for(int i =1;i<10;i++){
			newAccomplished[i]=(the3Var[(i-1)/3]>=achiNums[((i-1)/6)*3+i%3]);
			System.out.println(i+" "+newAccomplished[i]+" "+((i/6)*3+i%3));
		}
		System.out.println("checked 1-10");
		newAccomplished[10] = (battleNum>=10);
		newAccomplished[11] = (battleNum>=100);
		newAccomplished[12] = (battleNum>=5000);
		newAccomplished[13] = (hourNum >= 2036);
		System.out.println("all checked");
		String checkedString = "";
		for(int i=0;i<num;i++){
			checkedString+=" "+newAccomplished[i];
			if((!accomplished[i])&&newAccomplished[i]){
				achievementsLabels[i].setIcon(new ImageIcon("A"+i+".png"));
				accumExp+=achiRews[i];
			}//end if
		}//end for
		System.out.println("Achievement updated");
		system.userInfo[28] = checkedString.substring(1);
	}
	private static void loadAchi(){
		String[] achiString = system.userInfo[28].split(" ");
		for(int i =0;i<num;i++){
			accomplished[i] = Boolean.parseBoolean(achiString[i]);
			System.out.println(i+" "+accomplished[i]);
		}
		System.out.println("Achievement loaded");
	}
=======

		public void mousePressed(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}
	};
	private final static int num = 14;
	private static JScrollPane scrollPane = new JScrollPane();
	private static JPanel paneContent = new JPanel();
	private static JLabel[] achievementsNoShadow = new JLabel[num];
	private static JLabel bgi = new JLabel(new ImageIcon("achievementBgi.jpg"));
	private static boolean[] accomplished = new boolean[num];

	private static final String[] AName = new String[] { "Too young, too simple, sometimes naive",
			"I'm bald, I'm strong", "Omae wa mou shindeiru", "Letter from 1976", "Kimochi warui",
			"AAAHHHHH!!! Boku no o no chikara ga!!!!!", "Potato aiming", "Do u no dewae", "Fernando Torres",
			"Somebody toucha my spaghet", "Dormammu i've come to bargain", "Performance Artist", "24",
			"El psy congroo", };

	private static final String[] reqirements = new String[] { "win 100 battles", "win 1000 battles",
			"win 2000 battles", "lose 100 battles", "lose 1000 battles", "lose 2000 battles", "miss 100 times",
			"miss 1000 times", "miss 5000 times", "fight 10 battles", "fight 100 battles", "fight 5000 battles",
			"total 24 hours played", "total 2036 hours played", };

	Insets bInsets;

>>>>>>> fcd74addd94d754b4c50e48ab9e2bc3288eca85a
	public Achievements() {
		setSize(1300, 700);
		setLayout(null);
<<<<<<< HEAD
		bInsets=getInsets();
		getRewardButton.setBounds(bInsets.left+10,bInsets.top+110,120,160);
		getRewardButton.addMouseListener(getReward);
		add(getRewardButton);
		backButton.setBounds(bInsets.left+10,bInsets.top+10,100,60);
=======
		bInsets = getInsets();
		backButton.setBounds(bInsets.left + 10, bInsets.top + 10, 100, 60);
>>>>>>> fcd74addd94d754b4c50e48ab9e2bc3288eca85a
		backButton.addMouseListener(backMouseEffect);
		add(backButton);
		buttonEffect.setBounds(bInsets.left, bInsets.top, 120, 80);
		add(buttonEffect);
		buttonEffect.setVisible(false);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(bInsets.left + 150, bInsets.top + 20, 1250, 650);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setOpaque(false);
		makeLabel();
		add(scrollPane);
		scrollPane.setVisible(true);
		bgi.setBounds(bInsets.left, bInsets.top, 1300, 700);
		add(bgi);
		setVisible(true);
	}
<<<<<<< HEAD
	public static void makeLabel() {
		paneContent.setLayout(new BoxLayout(paneContent,BoxLayout.Y_AXIS));
		for(int i=0;i<num;i++) {
			if(accomplished[i]) {
				achievementsLabels[i]=new JLabel(new ImageIcon("A"+i+".png"));
			}else {
				achievementsLabels[i]=new JLabel(new ImageIcon("aLocked.png"));
=======

	public static void makeLabel() {
		paneContent.setLayout(new BoxLayout(paneContent, BoxLayout.Y_AXIS));
		for (int i = 0; i < num; i++) {
			if (accomplished[i]) {
				achievementsNoShadow[i] = new JLabel(new ImageIcon("A" + i + ".png"));
			} else {
				achievementsNoShadow[i] = new JLabel(new ImageIcon("L.png"));
>>>>>>> fcd74addd94d754b4c50e48ab9e2bc3288eca85a
			}
			paneContent.add(achievementsLabels[i]);
		}
		paneContent.setOpaque(false);
		scrollPane.add(paneContent);
		scrollPane.setViewportView(paneContent);
	}

<<<<<<< HEAD
=======
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

>>>>>>> fcd74addd94d754b4c50e48ab9e2bc3288eca85a
}
