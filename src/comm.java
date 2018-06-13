
public class comm extends Thread {
	static String sos="";
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public void run(){
		 synchronized(this){
			 game.userTurn = true;
//				while(!fired){}
				try {
					Thread.sleep(15000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("A1");
				game.fired = false;
				System.out.println(game.lastHitY+" "+game.lastHitX);
				sos=(Character.toString((char) ( game.lastHitY+ 65)) + Integer.toString( game.lastHitX + 1)).toUpperCase();
				notify();
		 }
	}

}
