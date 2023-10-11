package Weapon;

import java.io.IOException;
import java.io.File;
import javax.imageio.ImageIO;
import main.Player;
import client.client;

import java.awt.*;
import java.awt.image.BufferedImage;

import main.Entity;
import map.Drawable;

public class Bullet extends Entity implements Drawable{
    public String name;
    public int damage;//�ӵ��˺�
    public int speed;//�ӵ�����
    public double angle;
    public boolean type;//�Ƿ񱻷���
    public int no;//���
	public static Image image_1;
	public static Image image_2;
	public static Image image_3;
    public int pos_x;
    public int pos_y;
	static{
		try{
			image_1 = ImageIO.read(new File("./resources/gun/magazine_1.png"));
			image_2 = ImageIO.read(new File("./resources/gun/magazine_2.png"));
			image_3 = ImageIO.read(new File("./resources/gun/magazine_3.png"));
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
    public Bullet(int damage,int speed,int no){
        this.damage = damage;
        this.speed = speed;
        this.type = false;
        this.no = no;
    }
	@Override
	public void draw(Graphics g) {
		g.setColor(Color.BLUE);
		g.drawString(this.name, this.pos_x + 7, this.pos_y + 7);
		g.setColor(Color.RED);
		g.drawRect(this.pos_x-3, this.pos_y-3, 6, 6);
	}
	public void draw(int x, int y, Graphics g){
		if(!type) {
			switch(name) {
			case "7.62*39":
				g.drawImage(image_1, x - 32, y - 32, null);
				break;
			case "5.45*39":
				g.drawImage(image_2, x - 32, y - 32, null);
				break;
			case "7.62*54R":
				g.drawImage(image_3, x - 32, y - 32, null);
				break;
			}
			g.setColor(Color.BLUE);
			g.drawString(this.name, x -15, y + 40);
			/*
			g.setColor(Color.RED);
			g.drawRect(x-3, y-3, 6, 6);
			*/
		}
		else {
			g.setColor(Color.GREEN);
			try {
				BufferedImage a  = ImageIO.read(new File("resources/gun/bullet.png"));
				GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
				GraphicsDevice gd = ge.getDefaultScreenDevice();
				GraphicsConfiguration gc = gd.getDefaultConfiguration();
				BufferedImage rotateImg = Player.create(a,Math.PI/2 - this.angle,gc);
				int rotate_height = rotateImg.getHeight()/5;
				int rotate_width = rotateImg.getWidth()/5;
				g.drawImage(rotateImg, x-rotate_width/2, y-rotate_height/2, rotate_width,rotate_height,null);//����
				g.drawImage(rotateImg, x, y, null);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}

class a762_39 extends Bullet{
    a762_39(int damage,int speed){
    	super(damage,speed,-1);
        this.name = "7.62*39";
    }
}

class a545_39 extends Bullet{
	a545_39(int damage,int speed){
    	super(damage,speed,-1);
		this.name = "5.45*39";
	}
}

class a762_54r extends Bullet{
	a762_54r(int damage,int speed){
    	super(damage,speed,-1);
		this.name = "7.62*54R";
	}
}