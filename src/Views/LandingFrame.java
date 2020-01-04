package Views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class LandingFrame extends JFrame implements Runnable{
	public static boolean start = false;
	JComboBox<String> players1;
	JComboBox<String> players2;
	//	JComboBox<String> diffLevels;
	JComboBox<String> size;

	//	private JPanel contentPane;
	public LandingFrame() throws IOException {
		super("Hex Game");
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(600, 400);
		this.setLocationRelativeTo( null );

		JPanelWithBackground contentPane = new JPanelWithBackground(new BorderLayout(), "C:\\Users\\echch\\Desktop\\hex.jpg");
		this.setContentPane(contentPane);

		Object[] playersList = new Object[]{"Human", "AI Player", "AI Player2"};
		//		Object[] diffLevelsList = new Object[]{"level 1", "level 2"};
		Object[] sizesList = new Object[] {"3", "7", "11"};

		players1 = new JComboBox(playersList);
		players1.setEditable(true);
		players1.setSelectedItem("First player (Red)");
		players1.setEditable(false);
		players1.setAlignmentX(CENTER_ALIGNMENT);
		//		players1.addActionListener(new ActionListener() {
		//
		//			@Override
		//			public void actionPerformed(ActionEvent e) {
		//				if(players1.getSelectedItem().toString().equals("AI Player")||players2.getSelectedItem().toString().equals("AI Player"))
		//					diffLevels.setVisible(true);
		//				else
		//					diffLevels.setVisible(false);
		//			}
		//		});

		players2 = new JComboBox(playersList);
		players2.setEditable(true);
		players2.setSelectedItem("Second player (blue)");
		players2.setEditable(false);
		players2.setAlignmentX(CENTER_ALIGNMENT);
		//		players2.addActionListener(new ActionListener() {
		//
		//			@Override
		//			public void actionPerformed(ActionEvent e) {
		//				if(players1.getSelectedItem().toString().equals("AI Player")||players2.getSelectedItem().toString().equals("AI Player"))
		//					diffLevels.setVisible(true);
		//				else
		//					diffLevels.setVisible(false);
		//			}
		//		});

		//		diffLevels = new JComboBox(diffLevelsList);
		//		diffLevels.setEditable(true);
		//		diffLevels.setSelectedItem("AI difficulty level");
		//		diffLevels.setEditable(false);
		//		diffLevels.setAlignmentX(CENTER_ALIGNMENT);
		//		diffLevels.setVisible(false);

		//		JLabel sliderLabel = new JLabel("Board Size", JLabel.CENTER);
		//      sliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		//		JSlider sizeSlider = new JSlider(JSlider.HORIZONTAL, 3, 11, 7);
		//		sizeSlider.setMajorTickSpacing(4);
		//		sizeSlider.setPaintTicks(true);
		//		sizeSlider.setPaintLabels(true);
		//		JPanel size = new JPanel();
		//		size.add(sliderLabel);
		//		size.add(sizeSlider);
		size = new JComboBox(sizesList);
		size.setEditable(true);
		size.setSelectedItem("Board Size");
		size.setEditable(false);
		size.setAlignmentX(CENTER_ALIGNMENT);

		JButton play = new JButton("Start the game!");
		play.setAlignmentX(CENTER_ALIGNMENT);
		play.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int p, q, s=0;
				switch(players1.getSelectedItem().toString()) {
				case "Human":
					p = 1;
					break;
				case "AI Player":
					p = 2;
					break;
				case "AI Player2":
					p = 3;
					break;
				default:
					p = 0;
					break;
				}
				switch(players2.getSelectedItem().toString()) {
				case "Human":
					q = 1;
					break;
				case "AI Player":
					q = 2;
					break;
				case "AI Player2":
					q = 3;
					break;
				default:
					q = 0;
					break;
				}
				try{
					s = Integer.parseInt(size.getSelectedItem().toString());
				}catch(NumberFormatException ex) {}
				if(p==0||q==0||s==0) {
					System.out.println("veuillez remplir les champs");
					//Boîte du message préventif
					JOptionPane jop2 = new JOptionPane();
					jop2.showMessageDialog(null, "Attention: Veuillez remplir les champs", "Attention", JOptionPane.WARNING_MESSAGE);
				}
				else {
					GameOptions.setOptions(p, q, s);
					start=true;
				}
			}
		});

		JPanel formPane = new JPanel();
		BoxLayout layout = new BoxLayout(formPane, BoxLayout.Y_AXIS);
		formPane.setLayout(layout);
		formPane.setPreferredSize(new Dimension(200,140));
		formPane.setOpaque(false);

		formPane.add(players1);
		formPane.add(players2);
		//		formPane.add(diffLevels);
		formPane.add(size);
		formPane.add(play);

		JPanel pane = new JPanel();
		pane.setOpaque(false);
		pane.add(formPane);

		contentPane.add(pane,BorderLayout.WEST);
	}

	@Override
	public void run() {
		while(!start)System.out.print("");
	}
	
	public static void setStartFalse() {
		start = false;
	}
}


@SuppressWarnings("serial")
class JPanelWithBackground extends JPanel {
	private Image backgroundImage;
	// Some code to initialize the background image.
	// Here, we use the constructor to load the image.
	public JPanelWithBackground(LayoutManager layout, String fileName) throws IOException {
		super(layout);
		backgroundImage = ImageIO.read(new File(fileName));
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// Draw the background image.
		g.drawImage(backgroundImage, 0, 0, this);
	}
}
