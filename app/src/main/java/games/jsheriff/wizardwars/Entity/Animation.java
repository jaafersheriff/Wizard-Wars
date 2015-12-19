package games.jsheriff.wizardwars.Entity;

import android.graphics.Bitmap;

/**
 * Created by jaafe on 12/19/2015.
 */
public class Animation
{
    private Bitmap[] frames;
    private int currFrame;

    private long startTime;
    private long delay;

    private boolean playedOnce;

    public Animation()
    {
        playedOnce = false;
    }

    public void update()
    {
        if(delay == -1) return;

        long elapsed = System.currentTimeMillis() - startTime;
        if(elapsed > delay)
        {
            currFrame++;
            startTime = System.currentTimeMillis();
        }
        if(currFrame == frames.length)
        {
            currFrame = 0;
            playedOnce = true;
        }
    }

    public int getFrame() { return currFrame; }
    public Bitmap getImage() { return frames[currFrame]; }

    public void setDelay(long d) { delay = d; }
    public void setFrame(int i) { currFrame = i; }
    public void setFrames(Bitmap[] frames)
    {
        this.frames = frames;
        currFrame = 0;
        startTime = System.currentTimeMillis();
        playedOnce = false;
    }

    public boolean hasPlayedOnce() { return playedOnce; }

}
