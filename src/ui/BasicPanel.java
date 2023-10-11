package ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import main.MyFrame;

public abstract class BasicPanel extends JPanel {
	Image backgroundImg;
	
	BasicPanel(String imgPath){
		this.setPreferredSize(new Dimension(MyFrame.Window_Width, MyFrame.Window_Height));
		this.setLayout(null);
		try {
			backgroundImg = ImageIO.read(new File(imgPath));
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(backgroundImg, 0, 0, this);
	}
}
