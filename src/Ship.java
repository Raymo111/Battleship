
/*
 * Authors: Raymond Li, David Tuck
 * Date created: 2018-06-07
 * Description: Battleship ships
 */
import java.io.Serializable;

public class Ship implements Serializable {
	public int shipLength;
	public String shipName;
	public Square[] location;

	public Ship(Square start, Square end) {
		int shipLength;
		if (start.x == end.x) {// Vertical ship
			shipLength = end.y - start.y;
			location = new Square[shipLength + 1];
			location[0] = start;
			for (int i = 1; i < location.length - 1; i++)
				location[i] = Battleship.homeGrid[start.y + i][end.y + i];
			location[location.length] = end;
		} else {// Horizontal ship
			shipLength = end.x - start.x;
			location = new Square[shipLength + 1];
			location[0] = start;
			for (int i = 1; i < location.length - 1; i++)
				location[i] = Battleship.homeGrid[start.x + i][end.x + i];
			location[location.length] = end;
		}
		shipName = Battleship.shipNames[shipLength - 1];
	}

}