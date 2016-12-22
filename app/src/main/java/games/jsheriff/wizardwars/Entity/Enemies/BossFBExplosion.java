package games.jsheriff.wizardwars.Entity.Enemies;

import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;

import games.jsheriff.wizardwars.Entity.Animation;
import games.jsheriff.wizardwars.Entity.Explosion;
import games.jsheriff.wizardwars.GameView;
import games.jsheriff.wizardwars.Sprites;

/**
 * Created by jaafe on 1/6/2016.
 */
public class BossFBExplosion extends Explosion{

    public BossFBExplosion(GameView gv, int x, int y)
    {
        super(gv, x, y);

        width = height = 175;

        numFrames = 4;
        loadSkin();
        animation = new Animation(gv, Sprites.BOSSFBE);
        animation.setFrames(frames);
        animation.setDelay(70);
    }

    private void loadSkin()
    {
        Log.d("Loading skin", this.toString());

        int bmwidth = gv.getSprites().getSheet(Sprites.BOSSFBE).getWidth()/numFrames;
        int bmheight = gv.getSprites().getSheet(Sprites.BOSSFBE).getHeight();

        frames = new Rect[numFrames];

        for(int j = 0; j < numFrames; j++)
        {
            frames[j] = new Rect(bmwidth*j, 0, bmwidth*j+bmwidth, bmheight);
        }
    }
}
