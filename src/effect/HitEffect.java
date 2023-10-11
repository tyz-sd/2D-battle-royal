package effect;
import java.awt.*;
public class HitEffect extends Effect{
    int r = 1, dr = 1;

    public HitEffect(int x, int y){
        timeLast = 30;
        time = 0;
        this.x = x;
        this.y = y;
    }

    public void draw(int x, int y, Graphics g){
        if(time < 1e9) time += 1;
        if(time < timeLast){
            g.setColor(Color.orange);
            g.drawOval(x-r, y-r, r*2, r*2);
            r += dr;
        }
    }
}