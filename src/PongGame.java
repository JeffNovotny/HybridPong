import java.awt.*;
import java.awt.event.*;
import java.util.Random;

import javax.swing.*;
import javax.swing.border.LineBorder;

public class PongGame extends JComponent implements ActionListener, MouseMotionListener, MouseListener, ComponentListener {

	private int screenWidth = 800;
	private int screenHeight = 600;
	
	//change the size of the ball
	private int ballDiam = 20;
	
	private int ballX = (screenWidth - ballDiam) / 2;
	private int ballY = (screenHeight - ballDiam) / 2;
	private int paddleX = 0;
	private int paddleY = 0;
	//change ball speed
	private double ballStartVelocity;
	
	private static JLabel selectDificulty = new JLabel("Please Select Dificulty:");
	private static JRadioButton easy = new JRadioButton("Easy");
	private static JRadioButton medium = new JRadioButton("Medium");
	private static JRadioButton hard = new JRadioButton("Hard");
	private static JButton startButton = new JButton("Start");
	
	private double ballVelocity = 0;
	private double ballAngle = Math.random()*2*Math.PI;
	//change paddle length/width
	private int paddleLong = 100;
	private int paddleShort = 15;
	
	//change ricochet severity 
	private double paddleRicochetRangeDegrees = 180;
	
	private double paddleRicochetRangeDegreesHalf = paddleRicochetRangeDegrees/2;
	
	private static int fps = 60;
	
	private static JFrame frame;
	private JPanel startMenu;
	
	private static Boolean gamePlaying = false;


	
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
	
	PongGame(){
	
		
		startMenu = new JPanel();
		startMenu.setBorder(new LineBorder(Color.black, 2));
		startMenu.setSize(200, 100);
		startMenu.setLocation(200, 300);
		startMenu.add(selectDificulty);
		startMenu.add(easy);
		startMenu.add(medium);
		startMenu.add(hard);
		startMenu.add(startButton);
		add(startMenu, BorderLayout.CENTER);
			
		medium.setSelected(true);
		
		easy.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				setEasyDificulty();
			}
		});
		
		medium.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				setMediumDificulty();
			}
			
		});
		
		hard.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				setHardDificulty();
			}
			
		});
		
		startButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				startMenu.setVisible(false);
			}
			
		});
	}

	protected double setEasyDificulty() {
		easy.setSelected(true);
		medium.setSelected(false);
		hard.setSelected(false);
		ballVelocity = 300;
		paddleLong = 125;
		return ballVelocity;
	}
	
	protected double setMediumDificulty() {
		// TODO Auto-generated method stub
		medium.setSelected(true);
		easy.setSelected(false);
		hard.setSelected(false);
		ballVelocity = 500;
		paddleLong = 100;
		return ballVelocity;
	}
	
	protected double setHardDificulty() {
		// TODO Auto-generated method stub
		hard.setSelected(true);
		medium.setSelected(false);
		easy.setSelected(false);
		ballVelocity = 800;
		paddleLong = 75;
		return ballVelocity;
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
		g.setColor(new Color(255, 0, 0));
		g.fillOval(ballX, ballY, ballDiam, ballDiam);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (gamePlaying) {
			int velocityX = (int)(ballVelocity/fps*Math.cos(ballAngle));
			int velocityY = (int)(ballVelocity/fps*Math.sin(ballAngle));
			
			// bottom paddle
			if(velocityY > 0 && ballX >= paddleX && ballX <= paddleX + paddleLong && ballY + ballDiam >= screenHeight - paddleShort && ballY + ballDiam <= screenHeight){
				ballY = screenHeight - paddleShort - ballDiam;
				double relativeBallX = ballX+(ballDiam/2)-paddleX;
				double percentage = relativeBallX/paddleLong;
				double targetAngle = 270;
				ballAngle = percentageDouble(Math.toRadians(targetAngle-paddleRicochetRangeDegreesHalf), Math.toRadians(targetAngle+paddleRicochetRangeDegreesHalf), percentage);
				velocityX = (int)(ballVelocity/fps*Math.cos(ballAngle));
				velocityY = (int)(ballVelocity/fps*Math.sin(ballAngle));
				ballHit();
			}
			//top paddle
			else if(velocityY < 0 && ballX >= paddleX && ballX <= paddleX + paddleLong && ballY <= 0 + paddleShort && ballY >= 0){
				ballY = 0 + paddleShort;
				double relativeBallX = ballX+(ballDiam/2)-paddleX;
				double percentage = relativeBallX/paddleLong;
				double targetAngle = 90;
				ballAngle = percentageDouble(Math.toRadians(targetAngle+paddleRicochetRangeDegreesHalf), Math.toRadians(targetAngle-paddleRicochetRangeDegreesHalf), percentage);
				velocityX = (int)(ballVelocity/fps*Math.cos(ballAngle));
				velocityY = (int)(ballVelocity/fps*Math.sin(ballAngle));
				ballHit();
			}
			// right paddle
			if(velocityX > 0  && ballY >= paddleY && ballY <= paddleY + paddleLong && ballX + ballDiam >= screenWidth - paddleShort && ballX + ballDiam <= screenWidth){
				ballX = screenWidth - paddleShort - ballDiam;
				double relativeBallY = ballY+(ballDiam/2)-paddleY;
				double percentage = relativeBallY/paddleLong;
				double targetAngle = 180;
				ballAngle = percentageDouble(Math.toRadians(targetAngle+paddleRicochetRangeDegreesHalf), Math.toRadians(targetAngle-paddleRicochetRangeDegreesHalf), percentage);
				velocityX = (int)(ballVelocity/fps*Math.cos(ballAngle));
				velocityY = (int)(ballVelocity/fps*Math.sin(ballAngle));
				ballHit();
			}
			//left paddle
			else if(velocityX < 0 && ballY >= paddleY && ballY <= paddleY + paddleLong && ballX <= 0 + paddleShort && ballX >= 0){
				ballX = 0 + paddleShort;
				double relativeBallY = ballY+(ballDiam/2)-paddleY;
				double percentage = relativeBallY/paddleLong;
				double targetAngle = 0;
				ballAngle = percentageDouble(Math.toRadians(targetAngle-paddleRicochetRangeDegreesHalf), Math.toRadians(targetAngle+paddleRicochetRangeDegreesHalf), percentage);
				velocityX = (int)(ballVelocity/fps*Math.cos(ballAngle));
				velocityY = (int)(ballVelocity/fps*Math.sin(ballAngle));
				ballHit();
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
				
				gameOver();
			}
			
			ballX += velocityX;
			ballY += velocityY;
			
			repaint();
		}
	}
	
	private void ballHit() {
		//effects after the ball was hit by a paddle
	}
	
	private void gameOver() {
		gamePlaying = false;
		startMenu.setVisible(true);
	}
	
	private double percentageDouble(double min, double max, double percentage) {
		return min + (max - min) * percentage;
	}


	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (gamePlaying) {
			paddleX = e.getX() - 50;
			paddleY = e.getY() - 75;
			repaint();
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		if(gamePlaying && ballVelocity == 0){
			ballVelocity = ballStartVelocity;
		} else if (!startMenu.isVisible()) {
			gamePlaying = true;
			mouseMoved(arg0);
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