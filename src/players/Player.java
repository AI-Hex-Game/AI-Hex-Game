package players;

import java.awt.Point;

import boards.GameBoard;
import gameMechanics.GameRunner;
import gameMechanics.Move;

public class Player {
	private GameRunner runner = null;
	private int colour = GameBoard.BLANK;

	public Player( GameRunner runner, int colour) {
		this.runner = runner;
		this.colour = colour;
	}

	public Move getMove() {
		switch (colour) {
		case GameBoard.RED:
			System.out.print("Red move: ");
			break;
		case GameBoard.BLUE:
			System.out.print("Blue move: ");
			break;
		}

		while (runner.getBoard().getSelected() == null) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
//				e.printStackTrace();
			}
		}
		Point choice = runner.getBoard().getSelected();

		Move move = new Move(colour, choice.x, choice.y);

		runner.getBoard().setSelected(null);
		return move;
	}
}
