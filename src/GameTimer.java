import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;


public class GameTimer extends JPanel{
	
	JLabel lblSeconds;
	
	int secondsPassed = 0;
	Timer timePassed = new Timer();
	TimerTask task = new TimerTask() {
		
		public void run() {
			secondsPassed++;
			lblSeconds = new JLabel("" + secondsPassed);
			
			add(lblSeconds, BorderLayout.CENTER);
		}
	};
	
	public GameTimer() {
		timePassed.scheduleAtFixedRate(task, 1000, 1000);
		
	}

}
