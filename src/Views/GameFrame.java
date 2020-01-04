package Views;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import Controlers.GameRunner;

@SuppressWarnings("serial")
public class GameFrame extends JFrame implements Runnable{
	private JPanel contentPane;
	public GameFrame() {
		super("Hex Game");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(600, 400);
		this.setLocationRelativeTo( null );
		
		contentPane = (JPanel) this.getContentPane();
		contentPane.setLayout(new BorderLayout());
		
		GameRunner runner = new GameRunner(GameOptions.getRed(), GameOptions.getBlue(), GameOptions.getSize());
//		BoardPanel board = new BoardPanel(runner.getBoard());
		contentPane.add(runner.getBoardPanel(), BorderLayout.CENTER);
//		board.startAnimation();
		runner.start();
	}
	@Override
	public void run() {
		while(GameRunner.gaming)System.out.print("");
	}
	
//	public static void main(String[] args) throws Exception{
//		//apply a look & feel
//		UIManager.setLookAndFeel( new NimbusLookAndFeel());
//		
//		//start my window
//		GameFrame app = new GameFrame();
////		app.pack();
//		app.setVisible( true );
//	}
}
