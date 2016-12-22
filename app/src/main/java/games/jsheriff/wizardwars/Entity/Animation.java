package games.jsheriff.wizardwars.Entity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;

import games.jsheriff.wizardwars.GameView;
import games.jsheriff.wizardwars.R;

public class Animation
{
    private int spriteSheet;
    private Rect[] frames;
    private int currFrame;

    private long startTime;
    private long delay;

    private boolean playedOnce;

    GameView gv;

    private boolean flipped;


    public Animation(GameView gv, int sprite)
    {
        this.gv = gv;
        this.spriteSheet = sprite;
        playedOnce = false;
        flipped = false;
    }

    public void update()
    {
        if(delay == -1) return;

        long elapsed = System.currentTimeMillis() - startTime;
        if(elapsed > delay)
        {
            if(!flipped) currFrame++;
            else currFrame--;
            startTime = System.currentTimeMillis();
        }
        if(currFrame == frames.length)
        {
            currFrame = 0;
            playedOnce = true;
        }
        if(currFrame == -1)
        {
            currFrame = frames.length-1;
            playedOnce = true;
        }
    }

    public void flip()
    {
        currFrame = frames.length - currFrame - 1;
    }

    public int getFrame() { return currFrame; }
    public Rect[] getFrames() { return frames; }
    public Rect getSrc() { return frames[currFrame]; }
    public Bitmap getSheet() { return gv.getSprites().getSheet(spriteSheet); }
    public boolean isFlipped() { return flipped; }

    public void setFlipped(boolean b){ flipped = b; }
    public void setDelay(long d) { delay = d; }
    public void setFrame(int i) { currFrame = i; }
    public void setFrames(Rect[] frames)
    {
        this.frames = frames;
        if(!flipped) currFrame = 0;
        else currFrame = frames.length-1;
        startTime = System.currentTimeMillis();
        playedOnce = false;
    }

    public boolean hasPlayedOnce() { return playedOnce; }

}
