
/*
 * Authors: Raymond Li, David Tuck
 * Date created: 2018-06-07
 * Description: Battleship ships
 */
import java.io.Serializable;
import java.util.ArrayList;

public class Ship implements Serializable {
	private static final long serialVersionUID = 5892849002266847494L;
	public int shipLength;
	public String shipName;
	public Square[] location;

	public Ship(Square[][] grid, Square start, Square end) {
		int shipLength;
		if (start.x == end.x) {// Vertical ship
			shipLength = Math.abs(end.y - start.y);
			location = new Square[shipLength + 1];
			location[0] = start;
			for (int i = 1; i < location.length - 1; i++) {
				location[i] = grid[start.x + i][start.y];
				grid[start.x + i][start.y].shipType = shipLength;
			}
			location[location.length - 1] = end;
		} else {// Horizontal ship
			shipLength = Math.abs(end.x - start.x);
			location = new Square[shipLength + 1];
			location[0] = start;
			for (int i = 1; i < location.length - 1; i++) {
				location[i] = grid[start.x][start.y + i];
				grid[start.x][start.y + 1].shipType = shipLength;
			}
			location[location.length - 1] = end;
		}
		ArrayList<String> usedShipNames = new ArrayList<String>(Battleship.shipNames.length);
		for (int i = 0; i < Battleship.shipLengths.length; i++)
			if (Battleship.shipLengths[i] == shipLength && usedShipNames.contains(shipName)) {
				shipName = Battleship.shipNames[i];
				usedShipNames.add(shipName);
			}
	}

}