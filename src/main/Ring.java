package main;
import java.awt.*;
import map.Drawable;
import java.awt.geom.*;
import client.client;

public class Ring implements Drawable{
	int radius;
	public Ring(int r) {
		radius = r;
	}
	@Override
	public void draw(int x,int y,Graphics g) {
		g.setColor(Color.RED);
		g.drawOval(x-radius,y-radius,radius*2,radius*2);
		
		Graphics2D g2d = (Graphics2D)g;
        Area outter = new Area(new Rectangle(0, 0, client.gameMap.getWidth(), client.gameMap.getHeight()));
        Ellipse2D.Double inner = new Ellipse2D.Double(x-radius, y-radius, radius*2, radius*2);
        outter.subtract(new Area(inner));// remove the ellipse from the original area

        g2d.setColor(new Color(0,0,150,50));
        g2d.fill(outter);
	}
}
