package effect;

import map.Drawable;

public abstract class Effect implements Drawable{
    int time, timeLast;
    public int x, y;
    public boolean expired(){
        return time > timeLast;
    }
}
