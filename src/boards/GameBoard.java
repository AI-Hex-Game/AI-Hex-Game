package boards;

import java.awt.Point;
import java.awt.Toolkit;

import gameMechanics.InvalidMoveException;
import gameMechanics.Move;

public class GameBoard {
	/*
	 * Public constants
	 */
	public static final int BLANK = 0;
	public static final int RED = 1;
	public static final int BLUE = 2;
	public static final int MAX_SUPPORTED_BOARD_SIZE = 99;
	public static final int MIN_SUPPORTED_BOARD_SIZE = 1;
	public static final int DEFAULT_BOARD_SIZE = 7;

	private int size;
	private Point selected;
	private BoardData data;
	private boolean changeOccured = true;

	public GameBoard(int size) {
		this.size = size;
		data = new BoardData(size);
	}

	public boolean makeMove(Move move) throws InvalidMoveException {

		boolean moveAccepted = false;
		int x = move.getX();
		int y = move.getY();
		int colour = move.getColour();
		
		if (x < 0 || x > size-1 || y < 0 ||y > size-1) {
			Toolkit.getDefaultToolkit().beep();
			throw new InvalidMoveException("Coordinates outside the play area!"/*, move,InvalidMoveException.OUTSIDE_BOARD*/);
		} else if (this.getOwner(x, y) == BLANK) {
			data.setNode(x, y, colour);
			moveAccepted = true;
			changeOccured = true;
		} else {
			Toolkit.getDefaultToolkit().beep();
			throw new InvalidMoveException("That hex is not blank!"/*, move,InvalidMoveException.ALREADY_TAKEN*/);
		}
		return moveAccepted;
	}

	public boolean checkwin(int player){
		return this.data.checkWin(player);
	}

	public void setSelected(Point selected) {
		this.selected = selected;
	}

	public void setSelected(int x, int y) {
		this.setSelected(new Point(x,y));
	}

	public Point getSelected() {
		return selected;
	}

	public int getSize() {
		return this.size;
	}
	
	public int getOwner(int x, int y) {
		return data.getNode(x, y).getNodeColour();
	}

	public boolean hasChanged() {
		return changeOccured;
	}

	public void changeNoted() {
		this.changeOccured = false;
	}
}
