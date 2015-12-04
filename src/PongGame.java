import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PongGame extends JComponent implements ActionListener, MouseMotionListener, MouseListener, ComponentListener {

	private int screenWidth = 800;
	private int screenHeight = 600;
	private int ballDiam = 20;
	private int ballX = (screenWidth - ballDiam) / 2;
	private int ballY = (screenHeight - ballDiam) / 2;
	private int paddleX = 0;
	private int paddleY = 0;
	private double ballStartVelocity = 600;
	private double ballVelocity = 0;
	private double ballAngle = Math.random()*2*Math.PI;
	private int paddleLong = 100;
	private int paddleShort = 15;
	private double paddleRicochetRangeDegrees = 60;
	private double paddleRicochetRangeDegreesHalf = paddleRicochetRangeDegrees/2;
	private static JFrame frame;

	private static int fps = 60;
	
	public static void main(String[] args){
		frame = new JFrame("Pong");
		PongGame game = new PongGame();
		frame.add(game);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		//start game
		frame.addMouseListener((MouseListener) game);
		frame.addComponentListener((ComponentListener) game);
		
		//ball frame rate
		Timer timer = new Timer(1000/fps, game);
		timer.start();
		
		// paddles
		frame.addMouseMotionListener(game);
		
	}
	
	public Dimension getPreferredSize(){
		return new Dimension(screenWidth,screenHeight);
	}
	
	protected void paintComponent(Graphics g){
		
		//Background
//		g.setColor(new Color(92, 92, 92));
//		g.fillRect(0, 0, 800, 600);
		
		//bottom paddle
		g.setColor(new Color(107, 83, 31));
		g.fillRect(paddleX, screenHeight - paddleShort, paddleLong, paddleShort);
		
		//left paddle
		g.fillRect(0, paddleY, paddleShort, paddleLong);
		
		//right paddle
		g.fillRect(screenWidth - paddleShort, paddleY, paddleShort, paddleLong);
		
		//top paddle
		g.fillRect(paddleX, 0, paddleLong, paddleShort);
		
		//ball
		g.setColor(new Color(219, 61, 219));
		g.fillOval(ballX, ballY, ballDiam, ballDiam);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int velocityX = (int)(ballVelocity/fps*Math.cos(ballAngle));
		int velocityY = (int)(ballVelocity/fps*Math.sin(ballAngle));
		
		// bottom paddle
		if(velocityY > 0 && ballX >= paddleX && ballX <= paddleX + paddleLong && ballY + ballDiam >= screenHeight - paddleShort && ballY + ballDiam <= screenHeight){
			ballY = screenHeight - paddleShort - ballDiam;
			velocityY = -velocityY;
			ballAngle = Math.atan2(velocityY, velocityX);
		}
		//top paddle
		else if(velocityY < 0 && ballX >= paddleX && ballX <= paddleX + paddleLong && ballY <= 0 + paddleShort && ballY >= 0 ){
			ballY = 0 + paddleShort;
			velocityY = -velocityY;
			ballAngle = Math.atan2(velocityY, velocityX);
		}
		// right paddle
		if(velocityX > 0  && ballY >= paddleY && ballY <= paddleY + paddleLong && ballX + ballDiam >= screenWidth - paddleShort && ballX + ballDiam <= screenWidth){
			ballX = screenWidth - paddleShort - ballDiam;
			velocityX = -velocityX;
			ballAngle = Math.atan2(velocityY, velocityX);
		}
		//left paddle
		else if(velocityX < 0 && ballY >= paddleY && ballY <= paddleY + paddleLong && ballX <= 0 + paddleShort && ballX >= 0){
			ballX = 0 + paddleShort;
			velocityX = -velocityX;
			ballAngle = Math.atan2(velocityY, velocityX);
		}
		//restart here
		if (ballVelocity != 0 &&
				   (ballX + ballDiam < 0 ||
				    ballX > screenWidth ||
				    ballY + ballDiam < 0 ||
				    ballY > screenHeight)) {
					//this means the ball went passed the screen
			
					velocityX = 0;
					velocityY = 0;
					ballVelocity = 0;
					ballX = (screenWidth-ballDiam) / 2;
					ballY = (screenHeight-ballDiam) / 2;
					ballAngle = Math.random()*2*Math.PI;
				}
		
		ballX += velocityX;
		ballY += velocityY;
		
		repaint();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
		paddleX = e.getX() - 50;
		paddleY = e.getY() - 75;
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		if(ballVelocity == 0){
			ballVelocity = ballStartVelocity;
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void componentResized(ComponentEvent e) {
		Dimension newSize = frame.getContentPane().getBounds().getSize();
		screenWidth = newSize.width;
		screenHeight = newSize.height;
		if(ballVelocity == 0){
			ballX = (screenWidth-ballDiam) / 2;
			ballY = (screenHeight-ballDiam) / 2;
		}
    }

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}
}
