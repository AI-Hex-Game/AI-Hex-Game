package boards;
import java.util.ArrayList;

import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.UpperSPDPackMatrix;

public class AdjMatrix {
	public static final int NO_LINK = 0;
	public static final int LINK = 1;
	private Matrix data;

	public AdjMatrix(int size) {
		//remember to add the border nodes as well
		this.data = new UpperSPDPackMatrix(size);
	}

	public void wipeNode(int nodeId) {
		for (int i = 0; i < size(); i++)
			if (i != nodeId)
				internalWrite(i, nodeId, NO_LINK);
	}

	public void bypassAndRemoveNode(int node) {
		ArrayList<Integer> neighbours = new ArrayList<Integer>();
		/*
		 * populate neighbour list
		 */
		for (int i = 0; i < size(); i++)
			if (read(node, i) == LINK)
				neighbours.add(i);
		/*
		 * create links between all neighbours
		 */
		for (int neighbour : neighbours)
			for (int newNeighbour : neighbours)
				write(neighbour, newNeighbour, LINK);
		/*
		 * remove links to and from original node
		 */
		wipeNode(node);
	}

	public int read(int i, int j) {
		// TODO Auto-generated method stub
		return (int) data.get(i, j);
	}

	public void write(int i, int j, int value) {
		/*
		 * i & j are nodeIds
		 */
		if (i == j)
			value = LINK;
		internalWrite(i, j, value);
	}

	private void internalWrite(int i, int j, int value) {
		/*
		 * i & j are nodeIds
		 */
		data.set(i, j, value);
		data.set(i, j, value);
	}

	public int size() {
		return data.numColumns();
	}
}
