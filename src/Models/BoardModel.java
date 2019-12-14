package Models;

import java.awt.Point;
import java.util.ArrayList;


public class BoardModel {
	/*
	 * Public constants
	 */
	public static final int RED_BORDER1_NODE = 0;
	public static final int BLUE_BORDER1_NODE = 1;
	public static final int RED_BORDER2_NODE = 2;
	public static final int BLUE_BORDER2_NODE = 3;

	private Node[][] graph;
	private AdjMatrix redAdjMatrix;
	private AdjMatrix blueAdjMatrix;
	private Point selected;
	private boolean changeOccured = true;
	private int size;

	public BoardModel(int size) {
		this.size = size;
		this.graph = new Node[size][size];
		this.redAdjMatrix = new AdjMatrix((int) Math.pow(size, 2) + 4);
		this.blueAdjMatrix = new AdjMatrix((int) Math.pow(size, 2) + 4);
		initGraph();
		initAdjMatrix();
	}

	private void initGraph() {
		/*
		 * initialize the graph board
		 */
		int nodeIndex = 4; // 0-3 are taken by borders
		for (int row = 0; row < size; row++)
			for (int column = 0; column < size; column++) {
				graph[row][column] = new Node(nodeIndex);//colour affected implicitly
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
				int node = getNodeId(row, column);
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
			int leftSideNeighbour = graph[row][0].getNodeID();
			int rightSideNeighbour = graph[row][size-1].getNodeID();
			blueAdjMatrix.write(BLUE_BORDER1_NODE, leftSideNeighbour,AdjMatrix.LINK);
			blueAdjMatrix.write(BLUE_BORDER2_NODE, rightSideNeighbour,AdjMatrix.LINK);
		}

		for (int column = 0; column < size; column++) {
			int northSideNeighbour = graph[0][column].getNodeID();
			int southSideNeighbour = graph[size-1][column].getNodeID();
			redAdjMatrix.write(RED_BORDER1_NODE, northSideNeighbour,AdjMatrix.LINK);
			redAdjMatrix.write(RED_BORDER2_NODE, southSideNeighbour,AdjMatrix.LINK);
		}
		/*
		 * finally, make all nodes reach themselves
		 */
		for (int nodeId = 0; nodeId < size; nodeId++) {
			redAdjMatrix.write(nodeId, nodeId, AdjMatrix.LINK);
			blueAdjMatrix.write(nodeId, nodeId, AdjMatrix.LINK);
		}
	}

	private ArrayList<Integer> getBoardNeighbours(int row, int column) {
		ArrayList<Point> temp = new ArrayList<Point>();
		temp.add(new Point(row - 1, column));
		temp.add(new Point(row + 1, column));
		temp.add(new Point(row, column - 1));
		temp.add(new Point(row, column + 1));
		temp.add(new Point(row + 1, column - 1));
		temp.add(new Point(row - 1, column + 1));

		ArrayList<Integer> neighbours = new ArrayList<Integer>();
		for (Point n : temp)
			if (n.x > -1 && n.y > -1 && n.x < size && n.y < size) {
				int nodeIndex = getNodeId(n.x, n.y);
				neighbours.add(nodeIndex);
			}
		return neighbours;
	}
	

	public void setNode(int x, int y, int value) {
		//note change in graph
		graph[x][y].setNodeColour(value);
		/*
		 * note change in adjacency matrixes 
		 */
		int nodeId = graph[x][y].getNodeID();
		switch(value) {
		case Node.BLUE:
			blueAdjMatrix.bypassNode(nodeId);
			redAdjMatrix.removeNode(nodeId);
			break;
		case Node.RED:
			redAdjMatrix.bypassNode(nodeId);
			blueAdjMatrix.removeNode(nodeId);
			break;
		}
	}

	public int getNodeId(int x, int y) {
		return graph[x][y].getNodeID();
	}

	public int getNodeOwner(int x, int y) {
		return graph[x][y].getNodeColour();
	}
	
	public Point getSelected() {
		return selected;
	}
	
	public void setSelected(Point selected) {
		this.selected = selected;
	}

	public void setSelected(int x, int y) {
		this.setSelected(new Point(x,y));
	}
	
	public boolean hasChanged() {
		return changeOccured;
	}

	public void changeNoted() {
		this.changeOccured = false;
	}
	
	public void changeOccured() {
		this.changeOccured = true;
	}
	
	
	public boolean redWins() {
		if (redAdjMatrix.read(RED_BORDER1_NODE, RED_BORDER2_NODE)==AdjMatrix.LINK)
			return true;
		return false;
	}
	
	
	public boolean blueWins() {
		if (blueAdjMatrix.read(BLUE_BORDER1_NODE, BLUE_BORDER2_NODE)==AdjMatrix.LINK)
			return true;
		return false;
		
	}
	
	
	public int getSize() {
		return this.size;
	}
}
