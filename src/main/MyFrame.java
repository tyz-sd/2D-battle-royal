package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.*;
import javax.swing.JPanel;

@SuppressWarnings({ "serial" })
public class MyFrame extends JPanel{
	public static final int Window_Height = 800;
	public static final int Window_Width = 1280;
	/*
	 * ���ش���
	 */
	public void loadFrame() {
		this.setPreferredSize(new Dimension(Window_Width, Window_Height));
		//this.setVisible(true);
		new Timer().schedule(new TimerTask() {
			public void run() {
				repaint();
			}}, 0,10);
	}
	/**
	 * ��ֹͼƬ��˸��ʹ��˫�ػ���
	 * 
	 * @param g
	 */
	Image backImg = null;
	
	@Override
	public void update(Graphics g) {
		if (backImg == null) {
			backImg = createImage(Window_Width, Window_Height);
		}
		Graphics backg = backImg.getGraphics();
		Color c = backg.getColor();
		backg.setColor(Color.white);
		backg.fillRect(0, 0, Window_Width, Window_Height);
		backg.setColor(c);
		paint(backg);		
		g.drawImage(backImg, 0, 0, null);
	}
	/**
	 * 
	 * 
	 * @param args
	 */
	class MyThread extends Thread{
		@Override
		public void run() {
			while(true){
				repaint();
				try {
					sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}





