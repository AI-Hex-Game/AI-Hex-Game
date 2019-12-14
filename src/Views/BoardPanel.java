package Views;

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

import Models.BoardModel;
import Models.Node;

@SuppressWarnings("serial")
public class BoardPanel extends JPanel implements MouseListener, Runnable {
	private int size;
	private double sm;
	private double lg;
	protected Point chosenXY;
	protected BoardModel board = null;

	public BoardPanel(BoardModel board) {
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
		// --- Activate antialiasing flag ---
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		// --- Calculating lg and sm units ---
		double panelWidth = this.getSize().width;
		double panelHeight = this.getSize().height;
		double dim1 = panelWidth / (6 * size + 2);
		double dim2 = panelHeight / (4 * size + 2);
		this.sm = Math.min(dim1, dim2);
		this.lg = 2 * sm;
		// --- Start drawing the board ---
		drawBorders(g2);
		for (int row = 0; row < size; row++)
			for (int column = 0; column < size; column++) {
				drawHex(g2, row, column);
			}
	}
	

	private void drawBorders(Graphics2D g2){
		// --- Calculating the different Xs ---
		int xRight = (int) ((1 + 3 * size) * lg);
		int xCentre = (int) (xRight / 2);
		// --- Calculating the different Ys ---
		int yTopPoint = (int) (lg / 6);
		int yCentrePoint = (int) (sm + size * lg);
		int yBottomPoint = (int) (sm + 2 * size * lg + 7*sm / 10);//theoricaly +lg/6 instead of 7*sm/10
		// --- Calculating redTop triangle coordinates ---
		int[] xRedTop = {0, xCentre, xCentre};
		int[] yRedTop = {yCentrePoint, yCentrePoint, yTopPoint};
		// --- Calculating redBottom triangle coordinates ---
		int[] xRedBottom = {xCentre, xCentre, xRight};
		int[] yRedBottom = {yCentrePoint, yBottomPoint, yCentrePoint};
		// --- Calculating blueTop triangle coordinates ---
		int[] xBlueTop = {xCentre, xCentre, xRight};
		int[] yBlueTop = {yCentrePoint, yTopPoint, yCentrePoint};
		// --- Calculating blueBottom triangle coordinates ---
		int[] xBlueBottom = {0, xCentre, xCentre};
		int[] yBlueBottom = {yCentrePoint, yCentrePoint, yBottomPoint};
		// --- Drawing the borders ---
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
	}
	
	
	private Polygon Hex(int row, int column) {
		// --- Calculating the different Xs for that Hex ---
		double x_double = lg + ( column + row) * (sm + lg);
		int x1 = (int) (x_double);
		int x2 = (int) (x_double + sm);
		int x3 = (int) (x_double + sm + lg);
		int x4 = (int) (x_double + 2 * sm + lg);
		// --- Calculating the different Ys for that Hex ---
		double y_double = sm + lg * (size + row - column);
		int yt = (int) (y_double - lg);
		int ym = (int) (y_double);
		int yb = (int) (y_double + lg);
		// --- Hex coordinates ---
		int[] x = {x1, x2, x3, x4, x3, x2};
		int[] y = {ym, yt, yt, ym, yb, yb};

		return new Polygon(x, y, 6);
	}
	
	
	private void drawHex(Graphics2D g2, int row, int column) {
		Polygon polygon = Hex(row, column);
		// ---What colour would this Hex be?! ---
		Color Colour;
		switch (board.getNodeOwner(row, column)) {
		case Node.RED:
			Colour = Color.RED;
			break;
		case Node.BLUE:
			Colour = Color.BLUE;
			break;
		default:
			Colour = Color.WHITE;
		}
		// --- Drawing the Hex
		g2.setPaint(Colour);
		g2.fill(polygon);
		g2.setPaint(Color.BLACK);
		g2.draw(polygon);		
	}
	

	public void run() {
		// Just to be nice, lower this thread's priority
		// so it can't interfere with other processing going on.
		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
		// Remember the starting time.
		long startTime = System.currentTimeMillis();
		// This is the animation loop.
		while (true/*We will try to implement a stop condition*/) {
			// Advance the animation frame.
			// Display it.
			if (board.hasChanged()) {
				repaint();
				board.changeNoted();
			}
			// Delay depending on how far we are behind.
			try {
				startTime += 100;
				Thread.sleep(Math.max(0, startTime - System.currentTimeMillis()));
			} catch (InterruptedException e) {
				System.out.println("Animation thread is interrupted");
				break;
			}
		}

	}

	
	public void mouseClicked(MouseEvent e) {
		Point p = e.getPoint();
		for (int row = 0; row < size; row++)
			for (int column = 0; column < size; column++) {
				//construire le polygon
				Polygon poly = Hex(row, column);
				if(poly.contains(p)){
					board.setSelected(row, column);
					System.out.println(row+","+column);
					row = column = size;//Hex coordinates founded ==> break the loop
				}
			}
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
		new Thread(this).start();
	}
}
