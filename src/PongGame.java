import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class PongGame extends JComponent implements ActionListener, MouseMotionListener, MouseListener, ComponentListener {
/*main() 		       		 89
 * game menu 	 			 119
 * radio buttons listeners   172
 * paint balls/paddles 		 322
 * game logic				 349
 */
	//ALL VARIABLES
	private static final Boolean START_GAME_ON_SCREEN_CLICK = false;
	private static final int MAX_NUM_OF_BALLS = 2;
	
	private static int screenWidth = 800;
	private static int screenHeight = 600;
	private int gameMenuWidth = 400;
	private int gameMenuHeight = 200;
	
	private int paddleX = 0;
	private int paddleY = 0;
	//change ball speed
	
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
	
	//change paddle length/width
	private int paddleLong = 100;
	private int paddleShort = 15;
	
	//change ricochet severity 
	private double paddleRicochetRangeDegrees = 180;
	
	private double paddleRicochetRangeDegreesHalf = paddleRicochetRangeDegrees/2;
	
	private static int fps = 60;
	
	private static int score;
	private static int scoreCount;
	
	//Game start menu components
	private static JFrame frame;
	private JPanel startMenu;
	private ButtonGroup difficultyGroup;
	private ButtonGroup sizeGroup;
	private static JLabel scoreCounter;
	
	private static Boolean gamePlaying = false;
	
	GameTimer gameTimer;
	Ball ballDefault = new Ball();
	private Ball[] balls = new Ball[MAX_NUM_OF_BALLS];
	int numOfBalls = 0;
	
	//This is for the radio buttons
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
	
	//Gameplay timer
	public JPanel timerPanel;
	public JLabel timeCounter;
	
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
	
	PongGame(){
		gameTimer = new GameTimer();
		
					//Game start menu
		startMenu = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0) );
		startMenu.setBorder(new LineBorder(Color.black, 2));
		add(startMenu, BorderLayout.CENTER);
		startMenu.setSize(gameMenuWidth, gameMenuHeight);
		startMenu.setLocation((screenWidth - gameMenuWidth) / 2, (screenHeight - gameMenuHeight) / 2);
		
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
		
		setNormalBall();
		
		//Adds the start button to the menu
		JPanel startButtonPanel = new JPanel();
		startButtonPanel.setPreferredSize( new Dimension(125, 50));
		startButtonPanel.add(startButton);
		startMenu.add(startButtonPanel, BorderLayout.CENTER);
		
		// timer and score tracker
		timerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		scoreCounter = new JLabel("Your Score is: " + score);
		timerPanel.setBorder(new LineBorder(Color.black, 2));
		add(timerPanel, BorderLayout.CENTER);
		timerPanel.setBounds((screenWidth - 200) / 2, 425, 200, 50);
		timerPanel.add(scoreCounter);
		
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
				scoreCount += 15;
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
//				if (numOfBalls == 0) {
//					createBall();
//				}
				startMenu.setVisible(false);
				if (!START_GAME_ON_SCREEN_CLICK)
					gameTimer = new GameTimer();
					gamePlaying = true;
				score = 0;
					scoreCounter.setVisible(true);
					scoreCounter.setText("Your Score is " + score);
					timerPanel.add(gameTimer);
//					gameTimer.task.run();
					
					createBall();
			}	
		});
		

	}
	
	void destroyBall(){
		
	}

	protected double setEasyDifficulty() {
		difficulty = Difficulty.Easy;
		easy.setSelected(true);
		ballDefault.velocity = 500;
		paddleLong = 125;
		return scoreCount = 5;
	}
	protected double setMediumDifficulty() {
		difficulty = Difficulty.Medium;
		medium.setSelected(true);
		ballDefault.velocity = 700;
		paddleLong = 100;
		return scoreCount = 10;
	}
	protected double setHardDifficulty() {
		difficulty = Difficulty.Hard;
		hard.setSelected(true);
		ballDefault.velocity = 900;
		paddleLong = 75;
		return scoreCount = 15;
	}
	
	protected double setLargeBall(){
		ballSize = changeBallSize.large;
		ballSizeLarge.setSelected(true);
		ballDefault.diameter = 30;
		return scoreCount += 5;
	}
	protected double setNormalBall(){
		ballSize = changeBallSize.normal;
		ballSizeNormal.setSelected(true);
		ballDefault.diameter = 20;
		return scoreCount += 10;
	}	
	protected double setSmallBall(){
		ballSize = changeBallSize.small;
		ballSizeSmall.setSelected(true);
		ballDefault.diameter = 10;
		return ballDefault.diameter;
	}
	
	private void gameOver() {
		gamePlaying = false;
		
		gameTimer.cancel();
//		switch (ballSize) {
//		case large:
//			setLargeBall();
//			break;
//		case normal:
//			setNormalBall();
//			break;
//		case small:
//			setSmallBall();
//			break;
//	}
		
		switch (difficulty) {
			case Easy:
				setEasyDifficulty();
				break;
			case Medium:
				setMediumDifficulty();
				break;
			case Hard:
				setHardDifficulty();
				break;
		}
		
		destroyBall();
		
		startMenu.setVisible(true);
		scoreCounter.setText("You Scored: " + score);
		
	}
	
	void createBall() {
		if (numOfBalls >= MAX_NUM_OF_BALLS) return;
		
		Ball ball = new Ball();
		ball.diameter = ballDefault.diameter;
		ball.x = (screenWidth - ball.diameter) / 2;
		ball.y = (screenHeight - ball.diameter) / 2;
		ball.angle = Math.random()*2*Math.PI;
		ball.velocity = ballDefault.velocity;
		
		balls[numOfBalls] = ball;
		
		numOfBalls++;
	}

	
	//draws the paddles and the ball on screen
	protected void paintComponent(Graphics g){
//		gameTimer = new GameTimer();
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
		
		//ball 1
		g.setColor(new Color(255, 0, 0));
		for (Ball ball : balls) {
			if (ball == null) continue;
			g.fillOval(ball.x, ball.y, ball.diameter, ball.diameter);
			if(gameTimer.secondsPassed == 0){
				createBall();
		}
		}	
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (gamePlaying) {
			for (Ball ball : balls) {
				if (ball == null) continue;
				
				int velocityX = (int)(ball.velocity/fps*Math.cos(ball.angle));
				int velocityY = (int)(ball.velocity/fps*Math.sin(ball.angle));
				
				// bottom paddle
				if(velocityY > 0 && ball.x >= paddleX && ball.x <= paddleX + paddleLong && ball.y + ball.diameter >= screenHeight - paddleShort && ball.y + ball.diameter <= screenHeight){
					ball.y = screenHeight - paddleShort - ball.diameter;
					double relativeBallX = ball.x+(ball.diameter/2)-paddleX;
					double percentage = relativeBallX/paddleLong;
					double targetAngle = 270;
					ball.angle = percentageDouble(Math.toRadians(targetAngle-paddleRicochetRangeDegreesHalf), Math.toRadians(targetAngle+paddleRicochetRangeDegreesHalf), percentage);
					velocityX = (int)(ball.velocity/fps*Math.cos(ball.angle));
					velocityY = (int)(ball.velocity/fps*Math.sin(ball.angle));
					ballHit();
				}
				//top paddle
				else if(velocityY < 0 && ball.x >= paddleX && ball.x <= paddleX + paddleLong && ball.y <= 0 + paddleShort && ball.y >= 0){
					ball.y = 0 + paddleShort;
					double relativeBallX = ball.x+(ball.diameter/2)-paddleX;
					double percentage = relativeBallX/paddleLong;
					double targetAngle = 90;
					ball.angle = percentageDouble(Math.toRadians(targetAngle+paddleRicochetRangeDegreesHalf), Math.toRadians(targetAngle-paddleRicochetRangeDegreesHalf), percentage);
					velocityX = (int)(ball.velocity/fps*Math.cos(ball.angle));
					velocityY = (int)(ball.velocity/fps*Math.sin(ball.angle));
					ballHit();
				}
	
				// right paddle
				if(velocityX > 0  && ball.y >= paddleY && ball.y <= paddleY + paddleLong && ball.x + ball.diameter >= screenWidth - paddleShort && ball.x + ball.diameter <= screenWidth){
					ball.x = screenWidth - paddleShort - ball.diameter;
					double relativeBallY = ball.y+(ball.diameter/2)-paddleY;
					double percentage = relativeBallY/paddleLong;
					double targetAngle = 180;
					ball.angle = percentageDouble(Math.toRadians(targetAngle+paddleRicochetRangeDegreesHalf), Math.toRadians(targetAngle-paddleRicochetRangeDegreesHalf), percentage);
					velocityX = (int)(ball.velocity/fps*Math.cos(ball.angle));
					velocityY = (int)(ball.velocity/fps*Math.sin(ball.angle));
					ballHit();
				}
				//left paddle
				else if(velocityX < 0 && ball.y >= paddleY && ball.y <= paddleY + paddleLong && ball.x <= 0 + paddleShort && ball.x >= 0){
					ball.x = 0 + paddleShort;
					double relativeBallY = ball.y+(ball.diameter/2)-paddleY;
					double percentage = relativeBallY/paddleLong;
					double targetAngle = 0;
					ball.angle = percentageDouble(Math.toRadians(targetAngle-paddleRicochetRangeDegreesHalf), Math.toRadians(targetAngle+paddleRicochetRangeDegreesHalf), percentage);
					velocityX = (int)(ball.velocity/fps*Math.cos(ball.angle));
					velocityY = (int)(ball.velocity/fps*Math.sin(ball.angle));
					ballHit();
				}
				
				//restart here
				if (ball.velocity != 0 &&
						   (ball.x + ball.diameter < 0 ||
						    ball.x > screenWidth ||
						    ball.y + ball.diameter < 0 ||
						    ball.y > screenHeight)) {
					//this means the ball went passed the screen
			
					velocityX = 0;
					velocityY = 0;
					ball.velocity = 0;
					ball.x = (screenWidth-ball.diameter) / 2;
					ball.y = (screenHeight-ball.diameter) / 2;
					ball.angle = Math.random()*2*Math.PI;
				
					destroyBall();
					gameOver();
					break;
				}
				
				ball.x += velocityX;
				ball.y += velocityY;
			}
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
		if(gamePlaying && (balls[0] == null || balls[0].velocity == 0)){
			for (Ball ball : balls) {
				if (ball == null) continue;
				ball.velocity = ballDefault.velocity;
			}
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
		for (Ball ball : balls) {
			if (ball == null) continue;
			if(ball.velocity == 0){
				ball.x = (screenWidth-ball.diameter) / 2;
				ball.y = (screenHeight-ball.diameter) / 2;
			}
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
