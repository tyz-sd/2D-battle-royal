package Weapon;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.*;
import main.Weapon;

public class AK extends Weapon {
    public static Image image;
    static{
        try{
            image = ImageIO.read(new File("./resources/gun/AK.png"));
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    public AK(int x, int y) {
        this.name = "AKM";
        this.bullet = new a762_39(3700,20);
        this.bullet_num = 0;
        this.max_bullet = 30;
        this.distance = 200;
        this.frate = 100;
        this.stable = 0.95;
        this.num = 1;
        this.pos_x = x;
        this.pos_y = y;
        this.live = true;
        this.reloadTime = 3000;
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