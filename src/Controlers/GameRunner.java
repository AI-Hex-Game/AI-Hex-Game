package Controlers;

import java.awt.Toolkit;

import Models.BoardModel;
import Models.Move;
import Models.Node;
import Models.Player;


public class GameRunner extends Thread {
	private BoardModel board;
	private Player red;
	private Player blue;
	private Player currentPlayer;
	private boolean finished = false;

	public GameRunner(int size) {
		this.board = new BoardModel(size);
		this.red = new Player(board, Node.RED);
		this.blue = new Player(board, Node.BLUE);
		this.currentPlayer = red;
	}


	public void run() {
		/*
		 * Main running loop
		 */
		while (!finished) {
			Move move;
			boolean moveAccepted = false;
			while(!moveAccepted) {
				move = currentPlayer.getMove();
				moveAccepted = makeMove(move);
				if(!moveAccepted) {
					Toolkit.getDefaultToolkit().beep();
					System.out.println("Move was not accepted, try again...");
				}
			}
			/*
			 * move has been accepted
			 */
			if (checkWin(currentPlayer)) {
				notifyWin(currentPlayer);
				finished = true;
			}
			switch (currentPlayer.getColour()) {
			case Node.RED:
				this.currentPlayer = blue;
				break;
			case Node.BLUE:
				this.currentPlayer = red;
				break;
			}
		}
	}


	private boolean makeMove(Move move) {
		boolean moveAccepted = false;
		int x = move.getX();
		int y = move.getY();
		int colour = move.getColour();

		if (board.getNodeOwner(x, y) == Node.BLANK) {
			board.setNode(x, y, colour);
			moveAccepted = true;
			board.changeOccured();
		}
		return moveAccepted;
	}


	private boolean checkWin(Player player) {
		switch (player.getColour()) {
			case Node.RED:
				return board.redWins();
			case Node.BLUE:
				return board.blueWins();
		}
		return false;	
	}


	public void notifyWin(Player player) {
		this.finished = true;
		java.awt.Toolkit.getDefaultToolkit().beep();
		switch (player.getColour()) {
		case Node.RED:
			System.out.println("Red wins!");
			break;
		case Node.BLUE:
			System.out.println("Blue wins!");
			break;
		}
	}


	public BoardModel getBoard() {
		return board;
	}


	public void startAct() {
		Thread gameThread = new Thread(this);
		gameThread.start();
	}
}
