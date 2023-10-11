package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import map.Drawable;

public class Armor extends Entity implements Drawable{
	public String name;
    public int pos_x,pos_y;
    public int durability;
    public double protect;
	public boolean Picked = false;
	public static Image image_1;
	public static Image image_2;
	public static Image image_3;
	public Armor(int type, int _pos_x ,int _pos_y){
		pos_x = _pos_x;
		pos_y = _pos_y;
		try{
            image_1 = ImageIO.read(new File("./resources/armor/armor_1.png"));
            image_2 = ImageIO.read(new File("./resources/armor/armor_2.png"));
            image_3 = ImageIO.read(new File("./resources/armor/armor_3.png"));
        }
        catch(IOException e){
            e.printStackTrace();
        }
		switch(type){
		case 0:
			name = "l1 armor";
			durability = 100;
			protect = 0.7;
			break;
		case 1:
			name = "l2 armor";
			durability = 150;
			protect = 0.6;
			break;
		case 2:
			name = "l3 armor";
			durability = 200;
			protect = 0.45;
			break;
		}
	}
	public void draw(int x, int y, Graphics g){
		switch(name) {
		case "l1 armor":
			g.drawImage(image_1, x-64, y-32, null);
			break;
		case "l2 armor":
			g.drawImage(image_2, x-64, y-32, null);
			break;
		case "l3 armor":
			g.drawImage(image_3, x-64, y-32, null);
			break;
		}
		g.drawString(""+this.durability, x-50, y+28);
	}
	public void draw(Graphics g) {
		g.setColor(Color.BLUE);
		g.drawString(this.name, this.pos_x + 7, this.pos_y + 7);
		g.setColor(Color.RED);
		g.drawRect(this.pos_x-3, this.pos_y-3, 6, 6);
	}
}