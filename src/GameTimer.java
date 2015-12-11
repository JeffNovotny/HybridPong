import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;


public class GameTimer extends JPanel{
	
	JLabel lblSeconds;
	
	int secondsPassed = 0;
	Timer timePassed;
	TimerTask task;
	
	public GameTimer() {
		timePassed = new Timer();
		task = new TimerTask() {
			public void run() {
				secondsPassed++;
				lblSeconds = new JLabel("" + secondsPassed);			
				add(lblSeconds, BorderLayout.CENTER);
			}
		};
	}
	
	public void scheduleAtFixedRate() {
		scheduleAtFixedRate(1000, 1000);
	}
	
	public void scheduleAtFixedRate(long delay, long period) {
		timePassed.scheduleAtFixedRate(task, delay, period);
	}
	
	public void cancel() {
		timePassed.cancel();
	}

}
