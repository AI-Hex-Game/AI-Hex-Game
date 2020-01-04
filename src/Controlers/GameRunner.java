package Controlers;

import java.awt.Toolkit;

import javax.swing.JOptionPane;

import Models.AIPlayer;
import Models.AIPlayer2;
import Models.BoardModel;
import Models.Move;
import Models.Node;
import Models.Player;
import Views.BoardPanel;


public class GameRunner extends Thread {
	public static boolean replay = true;
	public static boolean gaming = true;
	private BoardModel board;
	private Player red;
	private Player blue;
	private Player currentPlayer;
	private boolean finished = false;
	BoardPanel boardPane;

	public GameRunner(int redPlayer, int bluePlayer, int size) {
		this.board = new BoardModel(size);
		switch(redPlayer) {
		case 1:
			this.red = new Player(board, Node.RED);
			break;
		case 2:
			this.red = new AIPlayer(board, Node.RED);
			break;
		case 3:
			this.red = new AIPlayer2(board, Node.RED);
			break;
		}
		switch(bluePlayer) {
		case 1:
			this.blue = new Player(board, Node.BLUE);
			break;
		case 2:
			this.blue = new AIPlayer(board, Node.BLUE);
			break;
		case 3:
			this.blue = new AIPlayer2(board, Node.BLUE);
			break;
		}
		this.currentPlayer = red;
		boardPane = new BoardPanel(board);
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
					//Boîte du message d'erreur
					JOptionPane jop3 = new JOptionPane();
					jop3.showMessageDialog(null, "Move was not accepted, try again...", "Erreur", JOptionPane.ERROR_MESSAGE);
				}
			}
			boardPane.repaint();
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
		int range = -1;
		String[] rep = {"Rejouer", "Quiter"};
		JOptionPane jop;
		this.finished = true;
		java.awt.Toolkit.getDefaultToolkit().beep();
		switch (player.getColour()) {
		case Node.RED:
			System.out.println("Red wins!");
			jop = new JOptionPane();
			range = jop.showOptionDialog(null, "Red Wins!", "How's the winner?!", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, rep, rep[1]);
			break;
		case Node.BLUE:
			System.out.println("Blue wins!");
			jop = new JOptionPane();
			range = jop.showOptionDialog(null, "Blue Wins!", "How's the winner?!", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, rep, rep[1]);
			break;
		}
		switch(range) {
		case 0:
			gaming = false;
			replay = true;
			break;
		case 1:
			gaming = false;
			replay = false;
			break;
		default:
			gaming = false;
			replay = false;
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

	public BoardPanel getBoardPanel() {
		return boardPane;
	}
	
	public static void setGamingTrue() {
		gaming = true;
	}
}
