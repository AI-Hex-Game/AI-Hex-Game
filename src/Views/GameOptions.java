package Views;

public abstract class GameOptions {
	private static int redPlayer;
	private static int bluePlayer;
	private static int size;
	public static void setOptions(int p, int q, int s) {
		redPlayer = p;
		bluePlayer = q;
		size = s;
	}
	public static int getRed() {
		return redPlayer;
	}
	public static int getBlue() {
		return bluePlayer;
	}
	public static int getSize() {
		return size;
	}
}
