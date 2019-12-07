package gameMechanics;

//import java.util.logging.Level;
//import java.util.logging.Logger;

import boards.GameBoard;
import players.Player;

public class GameRunner extends Thread {

	private GameBoard board;
	private Player red;
	private Player blue;
	private int currentPlayer = GameBoard.RED;
	private boolean finished = false;
	private Move move;
	//	private volatile boolean stop = false;
	//	private String commentary = "";

	public GameRunner(int size) {
		this.board = new GameBoard(size);
		this.red = new Player(this, GameBoard.RED);
		this.blue = new Player(this, GameBoard.BLUE);
	}

	public void run() {
		/*
		 * Main running loop
		 */
		while (!finished /*&& !stop*/) {

			boolean moveAccepted = false;

			while(!moveAccepted) {
				move = null;
				switch (currentPlayer) {
				case GameBoard.RED:
					move = red.getMove();
					break;
				case GameBoard.BLUE:
					move = blue.getMove();
					break;
				default:
					System.err.println("invoking mystery player");
					System.exit(1);
					break;
				}
				try {
					moveAccepted = board.makeMove(move);
				} catch (InvalidMoveException ex) {
					//				Logger.getLogger(GameRunner.class.getName()).log(Level.SEVERE, null, ex);
				}
				if (!moveAccepted)
					System.out.println("Move was not accepted, try again...");
			}


			/*
			 * move has been accepted
			 */

			if (board.checkwin(currentPlayer)) {
				notifyWin(currentPlayer);
				finished = true;
			}


			switch (currentPlayer) {
			case GameBoard.RED:
				this.currentPlayer = GameBoard.BLUE;
				break;
			case GameBoard.BLUE:
				this.currentPlayer = GameBoard.RED;
				break;
			default:
				System.err.println("invoking mystery player");
				System.exit(1);
				break;
			}
		}
	}

	public void notifyWin(int player) {
		this.finished = true;
		java.awt.Toolkit.getDefaultToolkit().beep();
		switch (player) {
		case GameBoard.RED:
			System.out.println("Red wins!");
			//			announce("Red Wins!");
			break;
		case GameBoard.BLUE:
			System.out.println("Blue wins!");
			//			announce("Blue Wins!");
			break;
		}
	}

	//	private void announce(String announcement) {
	//		this.commentary = announcement;
	//	}

	public GameBoard getBoard() {
		return board;
	}

	public void startAct() {
		Thread gameThread = new Thread(this);
		gameThread.start();
	}
}
