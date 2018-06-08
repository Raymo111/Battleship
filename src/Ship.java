
/*
 * Authors: Raymond Li, David Tuck
 * Date created: 2018-06-07
 * Description: Battleship ships
 */
import java.io.Serializable;

public class Ship implements Serializable {
	private static final long serialVersionUID = 5892849002266847494L;
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
				location[i] = Battleship.homeGrid[start.x + i][start.y];
			location[location.length] = end;
		} else {// Horizontal ship
			shipLength = end.x - start.x;
			location = new Square[shipLength + 1];
			location[0] = start;
			for (int i = 1; i < location.length - 1; i++)
				location[i] = Battleship.homeGrid[start.x][start.y + i];
			location[location.length] = end;
		}
		for (int i = 0; i < Battleship.shipLengths.length; i++)
			if (Battleship.shipLengths[i] == shipLength && !Battleship.usedShipNames.contains(shipName)) {
				shipName = Battleship.shipNames[i];
				Battleship.usedShipNames.add(shipName);
			}
	}

}