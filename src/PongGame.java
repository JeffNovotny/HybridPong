import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class PongGame extends JComponent implements ActionListener, MouseMotionListener, MouseListener, ComponentListener {
																						
	//ALL VARIABLES
	private static final Boolean START_GAME_ON_SCREEN_CLICK = false;
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
	
	//Start menu Labels
	private static JLabel selectDifficulty = new JLabel("Please Select Difficulty:");
	private static JLabel selectBallSize = new JLabel("Select Ball Size: ");
	
	//Create difficulty radio buttons
	private static JRadioButton easy = new JRadioButton("Easy");
	private static JRadioButton medium = new JRadioButton("Medium");
	private static JRadioButton hard = new JRadioButton("Hard");
	
	//Create start button
	private static JButton startButton = new JButton("Start");
	
	//create ball size radio buttons
	private static JRadioButton ballSizeSmall = new JRadioButton("Small");
	private static JRadioButton ballSizeNormal = new JRadioButton("Normal");
	private static JRadioButton ballSizeLarge = new JRadioButton("Large");
	
	private double ballVelocity = 0;
	private double ballAngle = Math.random()*2*Math.PI;
	//change paddle length/width
	private int paddleLong = 100;
	private int paddleShort = 15;
	
	//change ricochet severity 
	private double paddleRicochetRangeDegrees = 180;
	
	private double paddleRicochetRangeDegreesHalf = paddleRicochetRangeDegrees/2;
	
	private static int fps = 60;
	
	private static int score;
	private static int scoreCount;
	
	private static JFrame frame;
	private JPanel startMenu;
	private ButtonGroup difficultyGroup;
	private ButtonGroup sizeGroup;
	private static JLabel scoreCounter;
	
	private static Boolean gamePlaying = false;
	
	private enum Difficulty {
		Easy,
		Medium,
		Hard
	}
	private static Difficulty difficulty;
	
	private enum changeBallSize {
		large,
		normal,
		small
	}
	
	private static changeBallSize ballSize;
	
	//END VARIABLES

	
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
		
		scoreCounter = new JLabel("Your Score is: " + score);
		scoreCounter.setVisible(false);
		frame.add(scoreCounter, BorderLayout.CENTER);
		
	}
	
	PongGame(){
	
					//Game start menu
		startMenu = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0) );
		startMenu.setBorder(new LineBorder(Color.black, 2));
		add(startMenu, BorderLayout.CENTER);
		startMenu.setSize(400, 200);
		startMenu.setLocation((screenWidth - 400) / 2, (screenHeight - 200) / 2);
		
					//adds the difficulty radio buttons to the menu
		JPanel difficulty = new JPanel();
		difficulty.add(selectDifficulty);
		difficulty.setPreferredSize( new Dimension(150, 125));
		difficultyGroup = new ButtonGroup();
		difficultyGroup.add(easy);
		difficultyGroup.add(medium);
		difficultyGroup.add(hard);
		difficulty.add(easy);
		difficulty.add(medium);
		difficulty.add(hard);
		startMenu.add(difficulty, BorderLayout.WEST);
					//default difficulty
		medium.setSelected(true);
		setMediumDifficulty();
		
					//Adds ball size radio buttons to the start menu
		JPanel ballSize = new JPanel();
		ballSize.add(selectBallSize);
		ballSize.setPreferredSize( new Dimension(150, 125));
		sizeGroup = new ButtonGroup();
		sizeGroup.add(ballSizeSmall);
		sizeGroup.add(ballSizeNormal);
		sizeGroup.add(ballSizeLarge);
		ballSize.add(ballSizeLarge);
		ballSize.add(ballSizeNormal);
		ballSize.add(ballSizeSmall);
		startMenu.add(ballSize, BorderLayout.EAST);
		
		ballSizeNormal.setSelected(true);
		
		//Adds the start button to the menu
		JPanel startButtonPanel = new JPanel();
		startButtonPanel.setPreferredSize( new Dimension(125, 50));
		startButtonPanel.add(startButton);
		startMenu.add(startButtonPanel, BorderLayout.CENTER);
		
		easy.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				setEasyDifficulty();
			}
		});
		
		medium.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				setMediumDifficulty();
			}
			
		});
		
		hard.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				setHardDifficulty();
			}
			
		});
		
		ballSizeSmall.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				setSmallBall();
			}
			
		});
		
		ballSizeNormal.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				setNormalBall();
			}
			
		});
		
		ballSizeLarge.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				setLargeBall();
			}
			
		});
		
		startButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				startMenu.setVisible(false);
				if (!START_GAME_ON_SCREEN_CLICK)
					gamePlaying = true;
				score = 0;
					scoreCounter.setVisible(true);
					scoreCounter.setText("Your Score is " + score);
			}
			
		});
	}

	protected double setEasyDifficulty() {
		difficulty = Difficulty.Easy;
		easy.setSelected(true);
		ballVelocity = 500;
		paddleLong = 125;
		return scoreCount = 5;
	}
	protected double setMediumDifficulty() {
		difficulty = Difficulty.Medium;
		medium.setSelected(true);
		ballVelocity = 700;
		paddleLong = 100;
		return scoreCount = 10;
	}
	protected double setHardDifficulty() {
		difficulty = Difficulty.Hard;
		hard.setSelected(true);
		ballVelocity = 900;
		paddleLong = 75;
		return scoreCount = 15;
	}
	
	protected double setLargeBall(){
		ballSize = changeBallSize.large;
		ballSizeLarge.setSelected(true);
		ballDiam = 30;
		return scoreCount += 5;
	}
	protected double setNormalBall(){
		ballSize = changeBallSize.normal;
		ballSizeNormal.setSelected(true);
		ballDiam = 20;
		return scoreCount += 10;
	}	
	protected double setSmallBall(){
		ballSize = changeBallSize.small;
		ballSizeSmall.setSelected(true);
		ballDiam = 10;
		return scoreCount += 15;
	}
	
	private void gameOver() {
		gamePlaying = false;
		switch (difficulty) {
			case Easy:
				switch (ballSize) {
				case large:
					setLargeBall();
					break;
				case normal:
					setNormalBall();
					break;
				case small:
					setSmallBall();
					break;
			}
				setEasyDifficulty();
				break;
			case Medium:
				setMediumDifficulty();
				break;
			case Hard:
				setHardDifficulty();
				break;
		}
		

		startMenu.setVisible(true);
		scoreCounter.setText("You Scored: " + score);
	}

	public Dimension getPreferredSize(){
		return new Dimension(screenWidth,screenHeight);
	}
	
	//draws the paddles and the ball on screen
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
		score = score + scoreCount;
		scoreCounter.setText("Your Score is: " + score);
	}
	
	private double percentageDouble(double min, double max, double percentage) {
		return min + (max - min) * percentage;
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
		if(gamePlaying && ballVelocity == 0){
			ballVelocity = ballStartVelocity;
			scoreCounter.setVisible(true);
		} else if (START_GAME_ON_SCREEN_CLICK && !startMenu.isVisible()) {
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
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
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
	}

	@Override
	public void componentShown(ComponentEvent e) {
	}

	@Override
	public void componentHidden(ComponentEvent e) {
	}
}