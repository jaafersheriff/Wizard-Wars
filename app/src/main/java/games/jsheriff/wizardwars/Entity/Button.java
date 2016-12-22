package games.jsheriff.wizardwars.Entity;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;
import java.util.Currency;

import games.jsheriff.wizardwars.Entity.Animation;
import games.jsheriff.wizardwars.GameView;
import games.jsheriff.wizardwars.Sprites;

/**
 * Created by jaafe on 1/11/2016.
 */
public class Button {

    private GameView gv;
    private Rect bounds;
    private boolean is;

    protected Animation animation;
    protected int src;
    protected ArrayList<Rect[]> sprites;
    protected int[] numFrames;
    protected int currAction;
    protected final int SET = 0;
    protected final int UNSET = 1;

    public Button(GameView gv, boolean s, int left, int top, int right, int bottom, int src, int[] frames)
    {
        this.gv = gv;
        is = s;
        this.src = src;
        this.numFrames = frames;
        bounds = new Rect(left, top, right, bottom);
        animation = new Animation(gv, src);
        currAction = SET;
        loadSkin(src);
        animation.setFrames(sprites.get(currAction));
        animation.setDelay(100);
    }

    private void loadSkin(int src)
    {
        Log.d("Loading skin", this.toString());

        sprites = new ArrayList<Rect[]>();

        int bmwidth = gv.getSprites().getSheet(src).getWidth()/numFrames[currAction];
        int bmheight = gv.getSprites().getSheet(src).getHeight()/numFrames.length;

        for(int i = 0; i < numFrames.length; i++)
        {
            Rect[] bm = new Rect[numFrames[i]];

            for(int j = 0; j < numFrames[i]; j++)
                bm[j] = new Rect(bmwidth*j, bmheight*i, bmwidth*(j+1), bmheight*(i+1));
            sprites.add(bm);
        }
    }

    public void update()
    {
        if(animation != null)
            animation.update();
    }

    public void draw()
    {
        Rect src = this.animation.getSrc();
        gv.canvas.drawBitmap(animation.getSheet(), src, bounds, gv.paint);
    }

    public void action()

    {
        is = !is;
        if(currAction == SET)
            currAction = UNSET;
        else
            currAction = SET;
        animation.setFrames(sprites.get(currAction));
    }


    public Animation getAnimation() { return animation; }
    public Rect getBounds() { return bounds; }
    public boolean getBool() { return is; }

}
