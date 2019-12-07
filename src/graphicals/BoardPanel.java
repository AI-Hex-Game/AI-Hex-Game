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
		this.addMouseListener(this);

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		double panelWidth = this.getSize().width;
		double panelHeight = this.getSize().height;
		double dim1 = panelWidth / (6 * size + 2);
		double dim2 = panelHeight / (4 * size + 2);
		this.sm = Math.min(dim1, dim2);
		this.lg = 2 * sm;

		//				drawBorders(g2);
		int xRight = (int) ((1 + 3 * size) * lg);
		int xCentre = (int) (xRight / 2);

		int yTopPoint = (int) (lg / 6);
		int yCentrePoint = (int) (sm + size * lg);
		int yBottomPoint = (int) (sm + 2 * size * lg + 7*sm / 10);//theoricaly +lg/6 instead of 7*sm/10

		int[] xRedTop = {0, xCentre, xCentre};
		int[] yRedTop = {yCentrePoint, yCentrePoint, yTopPoint};

		int[] xRedBottom = {xCentre, xCentre, xRight};
		int[] yRedBottom = {yCentrePoint, yBottomPoint, yCentrePoint};

		int[] xBlueTop = {xCentre, xCentre, xRight};
		int[] yBlueTop = {yCentrePoint, yTopPoint, yCentrePoint};

		int[] xBlueBottom = {0, xCentre, xCentre};
		int[] yBlueBottom = {yCentrePoint, yCentrePoint, yBottomPoint};

		Polygon RedTop = new Polygon(xRedTop, yRedTop, 3);
		Polygon RedBottom = new Polygon(xRedBottom, yRedBottom, 3);
		Polygon BlueTop = new Polygon(xBlueTop, yBlueTop, 3);
		Polygon BlueBottom = new Polygon(xBlueBottom, yBlueBottom, 3);

		g2.setPaint(Color.RED);
		g2.fill(RedTop);
		g2.fill(RedBottom);

		g2.setPaint(Color.BLUE);
		g2.fill(BlueTop);
		g2.fill(BlueBottom);

		g2.setPaint(Color.BLACK);
		g2.draw(RedTop);
		g2.draw(RedBottom);
		g2.draw(BlueTop);
		g2.draw(BlueBottom);

		for (int row = 0; row < size; row++)
			for (int column = 0; column < size; column++) {
				//						drawHex(g2, x, y);

				double x_double = lg + ( column + row) * (sm + lg);
				int x1 = (int) (x_double);
				int x2 = (int) (x_double + sm);
				int x3 = (int) (x_double + sm + lg);
				int x4 = (int) (x_double + 2 * sm + lg);
				double y_double = sm + lg * (size + row - column);
				int yt = (int) (y_double - lg);
				int ym = (int) (y_double);
				int yb = (int) (y_double + lg);
				int[] x = {x1, x2, x3, x4, x3, x2};
				int[] y = {ym, yt, yt, ym, yb, yb};

				Polygon polygon = new Polygon(x, y, 6);

				int value = board.getOwner(row, column);
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
		for (int row = 0; row < size; row++)
			for (int column = 0; column < size; column++) {
				//construire le polygon
				double x_double = lg + ( column + row) * (sm + lg);
				int x1 = (int) (x_double);
				int x2 = (int) (x_double + sm);
				int x3 = (int) (x_double + sm + lg);
				int x4 = (int) (x_double + 2 * sm + lg);

				double y_double = sm + lg * (size + row - column);
				int yt = (int) (y_double - lg);
				int ym = (int) (y_double);
				int yb = (int) (y_double + lg);
				int[] x = {x1, x2, x3, x4, x3, x2};
				int[] y = {ym, yt, yt, ym, yb, yb};

				Polygon poly = new Polygon(x, y, 6);

				if(poly.contains(p)){
					((GameBoard)board).setSelected(row, column);
					System.out.println(row+","+column);
					row = column = size;
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
