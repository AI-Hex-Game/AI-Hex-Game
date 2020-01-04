package Models;
import java.util.ArrayList;

import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.UpperSPDPackMatrix;

public class AdjMatrix {
	/*
	 * Public constants
	 */
	public static final int NO_LINK = 0;
	public static final int LINK = 1;

	private Matrix data;

	public AdjMatrix(int size) {
		//remember to add the border nodes as well
		this.data = new UpperSPDPackMatrix(size);
	}

	public AdjMatrix(Matrix adj) {
		this.data = adj;
	}

	public void removeNode(int nodeId) {
		for (int id = 0; id < size(); id++)
			write(id, nodeId, NO_LINK);//except for id==nodeId we maintain a link
	}

	public void bypassNode(int nodeId) {
		ArrayList<Integer> neighbours = new ArrayList<Integer>();
		/*
		 * populate neighbour list
		 */
		for (int id = 0; id < size(); id++)
			if (read(nodeId, id) == LINK)
				neighbours.add(id);
		/*
		 * create links between all neighbours
		 */
		for (int neighbour1 : neighbours)
			for (int neighbour2 : neighbours)
				write(neighbour1, neighbour2, LINK);
		/*
		 * remove links to and from original node
		 */
		removeNode(nodeId);
	}

	public int read(int id1, int id2) {
		// TODO Auto-generated method stub
		return (int) data.get(id1, id2);
	}

	public void write(int id1, int id2, int linkingValue) {
		if (id1 == id2)
			linkingValue = LINK;
		data.set(id1, id2, linkingValue);
		data.set(id1, id2, linkingValue);
	}

	public int size() {
		return data.numColumns();
	}

	public AdjMatrix multiplay(AdjMatrix mat) {
		// both are assumed to be the same size
		if (mat.size() == this.size()) {
			AdjMatrix retMatrix = new AdjMatrix(size());
			data.mult(mat.data, retMatrix.data);
			return retMatrix;
		} else
			return null;
	}

	public AdjMatrix clone() {
		return new AdjMatrix(data.copy());
	}

	public void meetNode(int node, ArrayList<Integer> boardNeighbours) {
		for(int neighbour1 : boardNeighbours)
			for(int neighbour2 : boardNeighbours)
				write(neighbour1, neighbour2, NO_LINK);
		keepNode(node, boardNeighbours);
	}

	public void keepNode(int node, ArrayList<Integer> boardNeighbours) {
		for(int neighbour : boardNeighbours)
			write(neighbour, node, LINK);
	}
}
