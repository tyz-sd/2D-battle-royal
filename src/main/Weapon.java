package main;

import java.awt.Color;
import map.Drawable;
import java.awt.Graphics;
import java.awt.*;

import Weapon.Bullet;

public class Weapon extends Entity implements Drawable{
	public static Image image;
    public String name;
    public Bullet bullet;
    public int bullet_num;
    public int max_bullet;
    public double distance;
    public boolean Picked = false;
    public int frate;//¿ª»ð¼ä¸ô
    public double stable;
    public int num;
    public int pos_x,pos_y;
	public long reloadTime;
	public long last_shoot_time;
	@Override
	public void draw(Graphics g) {
		g.setColor(Color.GREEN);
		g.drawString(this.name, this.pos_x + 7, this.pos_y + 7);
		g.setColor(Color.RED);
		g.drawRect(this.pos_x-3, this.pos_y-3, 6, 6);
	}
	public void draw(int x, int y, Graphics g){
		if(image == null){
			g.setColor(Color.GREEN);
			g.drawString(this.name, x + 7, y + 7);
			g.setColor(Color.RED);
			g.drawRect(x-3, y-3, 6, 6);
		}
		else{
			g.drawImage(image, x-64, y-32, null);
		}
	}
}

