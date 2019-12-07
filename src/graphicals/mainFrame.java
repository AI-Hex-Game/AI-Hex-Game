package graphicals;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import gameMechanics.GameRunner;

@SuppressWarnings("serial")
public class mainFrame extends JFrame {
	private JPanel contentPane;
	public mainFrame() {
		super("Hex Game");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(600, 400);
		this.setLocationRelativeTo( null );
		
		contentPane = (JPanel) this.getContentPane();
		contentPane.setLayout(new BorderLayout());
		
		GameRunner runner = new GameRunner(7);
		BoardPanel board = new BoardPanel(runner.getBoard());
		contentPane.add(board, BorderLayout.CENTER);
		board.startAnimation();
		runner.start();
	}
	
	public static void main(String[] args) throws Exception{
		//apply a look & feel
		UIManager.setLookAndFeel( new NimbusLookAndFeel());
		
		//start my window
		mainFrame app = new mainFrame();
//		app.pack();
		app.setVisible( true );
	}
}
