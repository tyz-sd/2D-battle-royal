package Weapon;

import java.io.File;
import java.io.IOException;
import java.awt.*;

import javax.imageio.ImageIO;

import main.Weapon;

public class SVD extends Weapon{
    public static Image image;
    static{
        try{
            image = ImageIO.read(new File("./resources/gun/SVD.png"));
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
	public SVD(int x, int y) {
        this.name = "Dragunov";
        this.bullet = new a762_54r(8500,20);
        this.distance = 500;
        this.frate = 500;
        this.bullet_num = 0;
        this.max_bullet = 10;
        this.stable = 0.95;
        this.num = 1;
        this.pos_x = x;
        this.pos_y = y;
        this.live = true;
        this.reloadTime = 4000;
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
