package effect;
import java.awt.*;
public class KillEffect extends Effect{
    int r = 1, dr = 1;

    public KillEffect(int x, int y){
        time = 0;
        timeLast = 90;
        this.x = x;
        this.y = y;
    }

    public void draw(int x, int y, Graphics g){
        if(time < 1e9) time += 1;
        if(time < timeLast){
            g.setColor(Color.red);
            g.drawOval(x-r, y-r, r*2, r*2);
            g.drawOval(x-r, y-r, r*2+2, r*2+2);
            r += dr;
        }
    }
}