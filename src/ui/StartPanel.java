package ui;
import java.awt.Graphics;

public class StartPanel extends BasicPanel{
	MyButton startButton = new MyButton("Game Start");
	MyButton helpButton = new MyButton("Help");
	
	StartPanel(){
		super("./resources/UI/title.jpg");
		startButton.setBounds(800, 400, 200, 50);
		this.add(startButton);
		helpButton.setBounds(800, 500, 200, 50);
		this.add(helpButton);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
}