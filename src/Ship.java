import java.io.Serializable;

public class Ship implements Serializable {
	public int shipLength;
	public String shipName;
	public Square[] location;

	public Ship(Square start, Square end) {
		if (start.x == end.x) {
			location = new Square[end.y - start.y + 1];
		} else {
			location = new Square[end.x - start.x + 1];
		}
	}

}