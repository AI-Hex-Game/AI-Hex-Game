package Models;

public class Node {
	/*
	 * Public constants
	 */
	public static final int BLANK = 0;
	public static final int RED = 1;
	public static final int BLUE = 2;

	private int nodeId;
	private int colour;

	public Node(int id) {
		nodeId = id;
		colour = BLANK;
	}

	public Node(int nodeId, int colour) {
		this.nodeId = nodeId;
		this.colour = colour;
	}

	public int getNodeID() {
		return nodeId;
	}

	public int getNodeColour() {
		return colour;
	}

	public void setNodeColour(int colour) {
		this.colour = colour;
	}

	public Node clone() {
		return new Node(nodeId, colour);
	}

}
