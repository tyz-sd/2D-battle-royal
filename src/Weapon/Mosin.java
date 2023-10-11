package Weapon;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.Weapon;
import java.awt.*;
public class Mosin extends Weapon{
    public static Image image;
    static{
        try{
            image = ImageIO.read(new File("./resources/gun/Mosin.png"));
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
	public Mosin(int x, int y) {
        this.name = "Mosin Nagant";
        this.bullet = new a762_54r(9100,20);
        this.distance = 200;
        this.bullet_num = 0;
        this.max_bullet = 5;
        this.frate = 1100;
        this.stable = 0.95;
        this.num = 1;
        this.pos_x = x;
        this.pos_y = y;
        this.live = true;
        this.reloadTime = 5000;
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
