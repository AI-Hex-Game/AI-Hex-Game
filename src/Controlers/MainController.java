package Controlers;

import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import Views.GameFrame;
import Views.LandingFrame;

public abstract class MainController {

	public static void main(String[] args) throws Exception{
		//apply a look & feel
		UIManager.setLookAndFeel( new NimbusLookAndFeel());

		while(GameRunner.replay) {
			//start my window
			LandingFrame app1 = new LandingFrame();
			app1.setVisible(true);
			Thread t1 = new Thread(app1);
			t1.start();
			t1.join();
			LandingFrame.setStartFalse();

			//		while(!LandingFrame.start)System.out.print("");
			app1.dispose();
			//start my window
			GameFrame app2 = new GameFrame();
			app2.setVisible(true);
			Thread t2 = new Thread(app2);
			t2.start();
			t2.join();
			GameRunner.setGamingTrue();
			app2.dispose();
		}
	}

}
