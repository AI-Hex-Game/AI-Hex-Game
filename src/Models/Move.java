package Models;

public class Move {
	private int colour;
	private int x;
	private int y;

	public Move(int x, int y, int colour) {
		this.colour = colour;
		this.x = x;
		this.y = y;
	}

	public int getColour() {
		return this.colour;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}
}