package graphicals;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import boards.GameBoard;

@SuppressWarnings("serial")
public class BoardPanel extends JPanel implements MouseListener, Runnable {
	private int size;
	private double sm;
	private double lg;
	protected Point chosenXY;
	protected GameBoard board = null;

	public BoardPanel(GameBoard board) {
		this.board = board;
		size = this.board.getSize();
		this.setOpaque(false);
		this.setPreferredSize(new Dimension(200, 100));
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		System.out.println("repaint");
		this.addMouseListener(this);//make it in other way , make it short and simple

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		double panelWidth = this.getSize().width;
		double panelHeight = this.getSize().height;
		double dim1 = panelWidth / (6 * size + 2);
		double dim2 = panelHeight / (4 + 3 * (size - 1));
		this.sm = Math.min(dim1, dim2);
		this.lg = 2 * sm;

		//				drawBorders(g2);
		int xBottomRight = (int) (((size * 3) + 1) * lg);
		int xTopRight = (int) (2 * size * lg + (5 * sm) / 4);
		int xBottomLeft = (int) (size * lg + (3 * sm) / 4);
		int xCentre = xBottomRight / 2;

		int yBottomPoint = (int) (size * (lg + sm) + sm);
		int yCentrePoint = yBottomPoint / 2;

		int[] xLeft = {0, xCentre, xBottomLeft};
		int[] yLeft = {0, yCentrePoint, yBottomPoint};

		int[] xTop = {0, xTopRight, xCentre};
		int[] yTop = {0, 0, yCentrePoint};
		int[] xBottom = {xCentre, xBottomRight, xBottomLeft};
		int[] yBottom = {yCentrePoint, yBottomPoint, yBottomPoint};
		int[] xRight = {xTopRight, xBottomRight, xCentre};
		int[] yRight = {0, yBottomPoint, yCentrePoint};

		Polygon left = new Polygon(xLeft, yLeft, 3);
		Polygon right = new Polygon(xRight, yRight, 3);
		Polygon top = new Polygon(xTop, yTop, 3);
		Polygon bottom = new Polygon(xBottom, yBottom, 3);

		g2.setPaint(Color.RED);
		g2.fill(top);
		g2.fill(bottom);

		g2.setPaint(Color.BLUE);
		g2.fill(left);
		g2.fill(right);

		g2.setPaint(Color.BLACK);
		g2.draw(top);
		g2.draw(bottom);
		g2.draw(left);
		g2.draw(right);

		for (int y = 0; y < size; y++)
			for (int x = 0; x < size; x++) {
				//						drawHex(g2, x, y);
				int column=x;
				int row=y;

				double xl_double = (2 * lg * column) + (lg * row) + lg;
				int xl = (int) (xl_double);
				int xm = (int) (xl_double + lg);
				int xr = (int) (xl_double + lg * 2);
				double y1_double = row * (lg + sm);
				int y1 = (int) y1_double;
				int y2 = (int) (y1_double + sm);
				int y3 = (int) (y1_double + sm + lg);
				int y4 = (int) (y1_double + sm + lg + sm);
				int[] xb = {xm, xr, xr, xm, xl, xl};
				int[] yb = {y1, y2, y3, y4, y3, y2};

				Polygon polygon = new Polygon(xb, yb, 6);

				int value = board.getOwner(y, x);
				Color Colour;
				switch (value) {
				case GameBoard.RED:
					Colour = Color.RED;
					break;
				case GameBoard.BLUE:
					Colour = Color.BLUE;
					break;
				default:
					Colour = Color.WHITE;
				}

				g2.setPaint(Colour);
				g2.fill(polygon);
				g2.setPaint(Color.BLACK);
				g2.draw(polygon);
			}

	}

	public void run() {
		// Just to be nice, lower this thread's priority
		// so it can't interfere with other processing going on.
//		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

		// Remember the starting time.
		long startTime = System.currentTimeMillis();

		// Remember which thread we are.
		//		Thread currentThread = Thread.currentThread();

		// This is the animation loop.
		while (/*currentThread == animatorThread*/ true) {
			// Advance the animation frame.
			// Display it.
			
			if (board.hasChanged()) {
				repaint();
				board.changeNoted();
			}
			// Delay depending on how far we are behind.
			try {
				startTime += 10;
				Thread.sleep(Math.max(0, startTime - System.currentTimeMillis()));
			} catch (InterruptedException e) {
				break;
			}
		}

	}

	@SuppressWarnings("unused")
	public void mouseClicked(MouseEvent e) {
		Point p = e.getPoint();
		row: for (int y = 0; y < size; y++) {
			column:	for (int x = 0; x < size; x++) {
				//construire le polygon
				int column=x;
				int row=y;
				double xl_double = (2 * lg * column) + (lg * row) + lg;
				int xl = (int) (xl_double);
				int xm = (int) (xl_double + lg);
				int xr = (int) (xl_double + lg * 2);
				double y1_double = row * (lg + sm);
				int y1 = (int) y1_double;
				int y2 = (int) (y1_double + sm);
				int y3 = (int) (y1_double + sm + lg);
				int y4 = (int) (y1_double + sm + lg + sm);
				int[] xb = {xm, xr, xr, xm, xl, xl};
				int[] yb = {y1, y2, y3, y4, y3, y2};

				Polygon poly = new Polygon(xb, yb, 6);

				if(poly.contains(p)){
					((GameBoard)board).setSelected(y,x);
					System.out.println(y+","+x);
					break row;
				}
			}
		}
		//				((HexGamePPanel) e.getComponent()).click(e.getPoint());
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void startAnimation() {
		Thread animatorThread = new Thread(this);
		animatorThread.start();
	}
}
