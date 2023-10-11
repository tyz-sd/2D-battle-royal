package ui;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JButton;
public class MyButton extends JButton{
	MyButton(String text){
		this.setText(text);
		this.setBackground(new Color(0,0,0,50));
		this.setForeground(Color.WHITE);
		this.setOpaque(false);
		this.setFocusPainted(false);
		//this.setBorderPainted(false);
		this.setFont(new Font("ÐÂËÎ", Font.BOLD, 30));
	}
	@Override
    protected void paintComponent(Graphics g) {
        if (!isOpaque() && getBackground().getAlpha() < 255) {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        super.paintComponent(g);
    }
}
