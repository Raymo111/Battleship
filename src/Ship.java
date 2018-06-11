
/*
 * Authors: Raymond Li, David Tuck
 * Date created: 2018-06-07
 * Description: Battleship ships
 */

import java.util.ArrayList;

public class Ship {
	public int shipLength;
	public String shipName;
	public Square[] location;

	public Ship(Square[][] grid, Square start, Square end) {
		int shipLength;
		if (start.x == end.x) {// Vertical ship
			shipLength = Math.abs(end.y - start.y) + 1;
			location = new Square[shipLength];
			if (start.y > end.y)
				for (int i = 0; i < shipLength; i++) {// ship up
					location[i] = grid[start.y - i][start.x];
					grid[start.y - i][start.x].shipType = this;
				}
			else
				for (int i = 0; i < shipLength; i++) {// ship down
					location[i] = grid[start.y + i][start.x];
					grid[start.y + i][start.x].shipType = this;
				}
		} else {// Horizontal ship
			shipLength = Math.abs(end.x - start.x) + 1;
			location = new Square[shipLength];
			if (start.x > end.x)
				for (int i = 0; i < shipLength; i++) {// ship left
					location[i] = grid[start.y][start.x - i];
					grid[start.y][start.x - i].shipType = this;
				}
			else
				for (int i = 0; i < shipLength; i++) {// ship right
					location[i] = grid[start.y][start.x + i];
					grid[start.y][start.x + i].shipType = this;
				}
		}

		ArrayList<String> usedShipNames = new ArrayList<String>(Battleship.shipNames.length);
		for (int i = 0; i < Battleship.shipLengths.length; i++)
			if (Battleship.shipLengths[i] == shipLength && !usedShipNames.contains(shipName)) {
				shipName = Battleship.shipNames[i];
				usedShipNames.add(shipName);
			}
	}

}