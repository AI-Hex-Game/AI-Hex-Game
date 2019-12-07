package boards;

import java.awt.Point;
import java.util.ArrayList;

public class BoardData {
	public static final int RED_BORDER1_NODE = 0;
	public static final int BLUE_BORDER1_NODE = 1;
	public static final int RED_BORDER2_NODE = 2;
	public static final int BLUE_BORDER2_NODE = 3;
	private Node[][] board;
	private AdjMatrix redAdjMatrix;
	private AdjMatrix blueAdjMatrix;
	private int size;

	public BoardData(int size) {
		this.size = size;
		this.board = new Node[size][size];
		this.redAdjMatrix = new AdjMatrix((int) Math.pow(size, 2) + 4);
		this.blueAdjMatrix = new AdjMatrix((int) Math.pow(size, 2) + 4);
		initBoard();
		initAdjMatrix();
	}

	private void initBoard(){
		int nodeIndex = 4; // 0-3 are taken by borders
		for (int row = 0; row < size; row++)
			for (int column = 0; column < size; column++) {
				board[row][column] = new Node(nodeIndex);
				nodeIndex++;
			}
	}

	private void initAdjMatrix() {
		/*
		 * first, we build identical neighbour relationships for the main bodies
		 * of both red and blue
		 */
		for (int row = 0; row < size; row++)
			for (int column = 0; column < size; column++) {
				int node = board[row][column].getNodeID();
				ArrayList<Integer> neighbours = getBoardNeighbours(row, column);
				for (int neighbour : neighbours) {
					blueAdjMatrix.write(node, neighbour, AdjMatrix.LINK);
					redAdjMatrix.write(node, neighbour, AdjMatrix.LINK);
				}
			}

		/*
		 * next, we build the neighbour relationships for the individual borders
		 */
		for (int row = 0; row < size; row++) {
			int leftSideNeighbour = board[row][0].getNodeID();
			int rightSideNeighbour = board[row][size-1].getNodeID();
			blueAdjMatrix.write(BLUE_BORDER1_NODE, leftSideNeighbour,AdjMatrix.LINK);
			blueAdjMatrix.write(BLUE_BORDER2_NODE, rightSideNeighbour,AdjMatrix.LINK);
		}

		for (int column = 0; column < size; column++) {
			int northSideNeighbour = board[0][column].getNodeID();
			int southSideNeighbour = board[size-1][column].getNodeID();
			redAdjMatrix.write(RED_BORDER1_NODE, northSideNeighbour,AdjMatrix.LINK);
			redAdjMatrix.write(RED_BORDER2_NODE, southSideNeighbour,AdjMatrix.LINK);
		}
		/*
		 * finally, make all nodes reach themselves
		 */
		for (int i = 0; i < redAdjMatrix.size(); i++) {
			redAdjMatrix.write(i, i, AdjMatrix.LINK);
			blueAdjMatrix.write(i, i, AdjMatrix.LINK);
		}
	}

	private ArrayList<Integer> getBoardNeighbours(int x, int y) {
		ArrayList<Point> temp = new ArrayList<Point>();
		temp.add(new Point(x - 1, y));
		temp.add(new Point(x + 1, y));
		temp.add(new Point(x, y - 1));
		temp.add(new Point(x, y + 1));
		temp.add(new Point(x + 1, y - 1));
		temp.add(new Point(x - 1, y + 1));

		ArrayList<Integer> neighbours = new ArrayList<Integer>();

		for (Point n : temp)
			if (n.x > -1 && n.y > -1 && n.x < size && n.y < size) {
				int nodeIndex = board[n.x][n.y].getNodeID();
				neighbours.add(nodeIndex);
			}
		return neighbours;
	}

	public boolean checkWin(int colour) {
		boolean returnVal = false;
		switch (colour) {
		case GameBoard.RED:
			if (redAdjMatrix.read(RED_BORDER1_NODE, RED_BORDER2_NODE) == AdjMatrix.LINK)
				returnVal = true;
			break;
		case GameBoard.BLUE:
			if (blueAdjMatrix.read(BLUE_BORDER1_NODE, BLUE_BORDER2_NODE) == AdjMatrix.LINK)
				returnVal = true;
			break;
		default:
			System.err.println("incorrect colour");
			break;
		}
		return returnVal;
	}

	public Node getNode(int x, int y) {
		return board[x][y];
	}

	public void setNode(int x, int y, int value) {
		int node = board[x][y].getNodeID();
		board[x][y].setNodeColor(value);

		switch (value) {
		case GameBoard.RED:
			redAdjMatrix.bypassAndRemoveNode(node);
			blueAdjMatrix.wipeNode(node);
			break;
		case GameBoard.BLUE:
			redAdjMatrix.wipeNode(node);
			blueAdjMatrix.bypassAndRemoveNode(node);
			break;
		default:
			System.err.println("incorrect colour");
			break;
		}
	}
}
