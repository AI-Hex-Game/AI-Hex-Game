package Models;

import java.awt.Point;

public class Player {
	protected BoardModel board = null;
	protected int colour;

	public Player( BoardModel board, int colour) {
		this.board = board;
		this.colour = colour;
	}

	
	public Move getMove() {
		switch (colour) {
		case Node.RED:
			System.out.print("Red move: ");
			break;
		case Node.BLUE:
			System.out.print("Blue move: ");
			break;
		}
		while (board.getSelected() == null)
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {}
		Point choice = board.getSelected();
		Move move = new Move(choice.x, choice.y, colour);
		board.setSelected(null);
		return move;
	}
	
	
	public int getColour() {
		return colour;
	}
}
