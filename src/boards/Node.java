package boards;

public class Node {
	private int nodeId;
	private int colour;

	public Node(int id) {
		nodeId = id;
		colour = GameBoard.BLANK;
	}

	public int getNodeID() {
		return nodeId;
	}

	public int getNodeColour() {
		return colour;
	}

	public void setNodeColor(int colour) {
		this.colour = colour;
	}
}
