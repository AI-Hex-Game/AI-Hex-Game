package Models;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class AIPlayer2 extends Player{
	private int maxLength;
	private int myBorder1;
	private int myBorder2;
	private int oppBorder1;
	private int oppBorder2;
	private int oppColour;
	private int[][]S1;
	private int[][]S2;

	public AIPlayer2( BoardModel board, int colour) {
		super(board, colour);
		this.maxLength = board.getSize() * board.getSize();
		S1 = new int[board.getSize()][board.getSize()];
		S2 = new int[board.getSize()][board.getSize()];
		switch (colour) {
		case Node.RED:
			myBorder1 = BoardModel.RED_BORDER1_NODE;
			myBorder2 = BoardModel.RED_BORDER2_NODE;
			oppBorder1 = BoardModel.BLUE_BORDER1_NODE;
			oppBorder2 = BoardModel.BLUE_BORDER2_NODE;
			oppColour = Node.BLUE;
			break;
		case Node.BLUE:
			myBorder1 = BoardModel.BLUE_BORDER1_NODE;
			myBorder2 = BoardModel.BLUE_BORDER2_NODE;
			oppBorder1 = BoardModel.RED_BORDER1_NODE;
			oppBorder2 = BoardModel.RED_BORDER2_NODE;
			oppColour = Node.RED;
			break;
		}
	}
	
	@Override
	public Move getMove() {
		BoardModel simulationBoard = board.cloneBoard();
		for(int row = 0; row < board.getSize(); row++)
			for(int column = 0 ; column < board.getSize(); column++) 
				if(board.getNodeOwner(row, column)==Node.BLANK) {
					simulationBoard.setNode(row, column, colour);
					AdjMatrix mySimMat = simulationBoard.getMatrix(colour);
					int myLength = 0;
					while (!(mySimMat.read(myBorder1, myBorder2) > 0) && myLength < maxLength) {
						mySimMat = mySimMat.multiplay(simulationBoard.getMatrix(colour));
						myLength++;
					}
					AdjMatrix oppSimMat = simulationBoard.getMatrix(oppColour);
					int oppLength = 0;
					while (!(oppSimMat.read(oppBorder1, oppBorder2) > 0) && oppLength < maxLength) {
						oppSimMat = oppSimMat.multiplay(simulationBoard.getMatrix(oppColour));
						oppLength++;
					}
					S1[row][column] = myLength - oppLength;
					S2[row][column] = oppSimMat.read(oppBorder1, oppBorder2) - mySimMat.read(myBorder1, myBorder2);
					simulationBoard.setNode(row, column, Node.BLANK);
				}

		int minS1 = maxLength;
		int minS2;
		List<Point> S1MinTie = new ArrayList<Point>();
		for(int row = 0; row < board.getSize(); row++)
			for(int column = 0 ; column < board.getSize(); column++) 
				if(board.getNodeOwner(row, column)==Node.BLANK)
					if(S1[row][column] < minS1) {
						S1MinTie.clear();
						S1MinTie.add(new Point(row, column));
						minS1 = S1[row][column];
					}
					else if(S1[row][column] == minS1)
						S1MinTie.add(new Point(row, column));
		if(S1MinTie.size() > 1) {
			minS2 = S2[S1MinTie.get(0).x][S1MinTie.get(0).y];
			Point selected = S1MinTie.get(0);
			for(Point node : S1MinTie)
				if(S2[node.x][node.y] < minS2) {
					selected = node;
					minS2 = S2[node.x][node.y];
				}
			return new Move(selected.x , selected.y , colour);
		}
		else if(S1MinTie.size() == 1)
			return new Move(S1MinTie.get(0).x , S1MinTie.get(0).y , colour);
		else 
			return null;
	}
}
