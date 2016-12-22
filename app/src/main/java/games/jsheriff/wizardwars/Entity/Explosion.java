package games.jsheriff.wizardwars.Entity;

import android.graphics.Rect;

import games.jsheriff.wizardwars.GameState.GameState;
import games.jsheriff.wizardwars.GameView;

/**
 * Created by jaafe on 1/6/2016.
 */
public abstract class Explosion {

    protected GameView gv;

    protected int x, y;
    protected int width, height;

    protected Animation animation;
    protected Rect[] frames;
    protected int numFrames;

    protected boolean remove;

    public Explosion(GameView gv, int x, int y)
    {
        this.gv = gv;
        this.x = x;
        this.y = y;
    }

    public void update()
    {
        animation.update();
        if(animation.hasPlayedOnce()) remove = true;
    }

    public void draw()
    {
        Rect dst = new Rect(x - width/2, y-height/2, x+width/2, y+height/2);
        gv.canvas.drawBitmap(animation.getSheet(), animation.getSrc(), dst, gv.paint);
    }

    public boolean shouldRemove() { return remove; }
}
