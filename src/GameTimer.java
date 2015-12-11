import java.awt.*;
import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;



public class GameTimer extends JPanel{
	
	JLabel lblSeconds;
	
	public int secondsPassed = 5;
	public Timer timePassed;
	public TimerTask task;
	
	public GameTimer() {
		timePassed = new Timer();
		task = new TimerTask() {
			public void run() {
				if(secondsPassed > 0){
				secondsPassed = secondsPassed - 1;
				}
				lblSeconds.setText("Next ball in: " + secondsPassed);
				lblSeconds.setVisible(true);
			}
			
		};
		timePassed.scheduleAtFixedRate(task, 1000, 1000);
		lblSeconds = new JLabel();		
		add(lblSeconds, BorderLayout.CENTER);
	}
	
//	public void scheduleAtFixedRate() {
//		scheduleAtFixedRate(1000, 1000);
//	}
//	
//	public void scheduleAtFixedRate(long delay, long period) {
//		timePassed.scheduleAtFixedRate(task, delay, period);
//	}
	public void cancel() {
		timePassed.cancel();
		lblSeconds.setVisible(false);
	}

}
