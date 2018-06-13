import java.util.ArrayList;

/*
 * Authors: Raymond Li, David Tuck
 * Date created: 2018-06-07
 * Description: Battleship ships
 */

public class Ship {
	public int shipLength;
	public String shipName;
	public ArrayList<Square> location;

	public Ship(Square[][] grid, String startShipName, int startShipLength) {
		shipName = startShipName;
		shipLength = startShipLength;
	}

	public Ship(Square[][] grid, Square start, Square end) {
		int shipLength;
		if (start.x == end.x) {// Vertical ship
			shipLength = Math.abs(end.y - start.y) + 1;
			location = new ArrayList<Square>(shipLength);
			if (start.y > end.y)
				for (int i = 0; i < shipLength; i++) {// ship up
					location.add(grid[start.y - i][start.x]);
					grid[start.y - i][start.x].shipType = this;
				}
			else
				for (int i = 0; i < shipLength; i++) {// ship down
					location.add(grid[start.y + i][start.x]);
					grid[start.y + i][start.x].shipType = this;
				}
		} else {// Horizontal ship
			shipLength = Math.abs(end.x - start.x) + 1;
			location = new ArrayList<Square>(shipLength);
			if (start.x > end.x)
				for (int i = 0; i < shipLength; i++) {// ship left
					location.add(grid[start.y][start.x - i]);
					grid[start.y][start.x - i].shipType = this;
				}
			else
				for (int i = 0; i < shipLength; i++) {// ship right
					location.add(grid[start.y][start.x + i]);
					grid[start.y][start.x + i].shipType = this;
				}
		}
		for (int i = 0; i < Battleship.shipLengths.length; i++)
			if (Battleship.shipLengths[i] == shipLength
					&& !Battleship.usedShipNames.contains(Battleship.shipNames[i])) {
				shipName = Battleship.shipNames[i];
				Battleship.usedShipNames.add(shipName);
				break;
			}
	}

}