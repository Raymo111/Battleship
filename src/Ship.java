import java.io.Serializable;

public class Ship implements Serializable {
	public int shipLength;
	public String shipName;
	public SquareTypes status;

	public Ship(int newShipLength, String newName) {
		shipLength = newShipLength;
		shipName = newName;
		status = SquareTypes.UNKNOWN;
	}

}
