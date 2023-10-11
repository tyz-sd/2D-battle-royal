package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import map.Drawable;

public class Medicine extends Entity implements Drawable{
	public String name;
	public double use_time;
	public int healing_point;
	public int power_point;
	public int pos_x, pos_y;
	public boolean Picked = false;
	public static Image image_1;
	public static Image image_2;
	public static Image image_3;
	public static Image image_4;
	public Medicine(int type, int _pos_x ,int _pos_y){
		pos_x = _pos_x;
		pos_y = _pos_y;
		try{
            image_1 = ImageIO.read(new File("./resources/medicine/bandage.png"));
            image_2 = ImageIO.read(new File("./resources/medicine/red_bull.png"));
            image_3 = ImageIO.read(new File("./resources/medicine/medicine_bag.png"));
            image_4 = ImageIO.read(new File("./resources/medicine/medicine_box.png"));
        }
        catch(IOException e){
            e.printStackTrace();
        }
		switch(type){
		case 0:
			name = "bandage";
 			use_time = 3500;
			healing_point = 1000;
			power_point = 0;
			break;
		case 1:
			name = "red bull";
			use_time = 2000;
			healing_point = 0;
			power_point = 4000;
			break;
		case 2:
			name = "medicine bag";
			use_time = 5000;
			healing_point = 5000;
			power_point = 0;
			break;
		case 3:
			name = "medicine box";
			use_time = 8000;
			healing_point = 10000;
			power_point = 0;
			break;
		}
	}
	public void draw(int x, int y, Graphics g){
		switch(name) {
		case "bandage":
			g.drawImage(image_1, x-64, y-32, null);
			break;
		case "red bull":
			g.drawImage(image_2, x-64, y-32, null);
			break;
		case "medicine bag":
			g.drawImage(image_3, x-64, y-32, null);
			break;
		case "medicine box":
			g.drawImage(image_4, x-64, y-32, null);
			break;
		}
	}
	public void draw(Graphics g) {
		g.setColor(Color.BLUE);
		g.drawString(this.name, this.pos_x + 7, this.pos_y + 7);
		g.setColor(Color.RED);
		g.drawRect(this.pos_x-3, this.pos_y-3, 6, 6);
	}
}
