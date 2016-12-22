package games.jsheriff.wizardwars;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.util.ArrayList;

public class Sprites {

    BitmapFactory.Options options;

    GameView gv;

    ArrayList<Bitmap> spriteSheets;
    public static final int PLAYERSHEET = 0;
    public static final int FBSHEET = 1;
    public static final int NINJASHEET = 2;
    public static final int EXPLOSION = 3;
    public static final int LIFE = 4;
    public static final int SOUNDBUTTON = 5;
    public static final int ENEMYBUTTON = 6;
    public static final int INVINCIBILITYBUTTON = 7;
    public static final int HITBOXBUTTON = 8;
    public static final int EXITBUTTON = 9;
    public static final int PAUSEBUTTON = 10;
    public static final int STARTBUTTON = 11;
    public static final int STATSBUTTON = 12;
    public static final int SETTINGSBUTTON = 13;
    public static final int BOSSSHEET = 14;
    public static final int BOSSFB = 15;
    public static final int CONTROLBUTTON = 16;
    public static final int FPSBUTTON = 17;
    public static final int MENUBUTTON = 18;
    public static final int RETRYBUTTON = 19;
    public static final int ANIMATIONBUTTON = 20;
    public static final int BOSSFBE = 21;

    public Sprites(GameView gv)
    {
        this.gv = gv;
        spriteSheets = new ArrayList<Bitmap>();
        options = new BitmapFactory.Options();
        options.inScaled = false;

        init();
    }

    public void init()
    {
        destroy();
        spriteSheets.add(BitmapFactory.decodeResource(gv.getResources(), R.drawable.playsprites, options));
        spriteSheets.add(BitmapFactory.decodeResource(gv.getResources(), R.drawable.pfb, options));
        spriteSheets.add(BitmapFactory.decodeResource(gv.getResources(), R.drawable.ninjasprites, options));
        spriteSheets.add(BitmapFactory.decodeResource(gv.getResources(), R.drawable.explosion, options));
        spriteSheets.add(BitmapFactory.decodeResource(gv.getResources(), R.drawable.hat, options));
        spriteSheets.add(BitmapFactory.decodeResource(gv.getResources(), R.drawable.soundbutton, options));
        spriteSheets.add(BitmapFactory.decodeResource(gv.getResources(), R.drawable.enemybutton, options));
        spriteSheets.add(BitmapFactory.decodeResource(gv.getResources(), R.drawable.invinsibilitybutton, options));
        spriteSheets.add(BitmapFactory.decodeResource(gv.getResources(), R.drawable.hitboxbutton, options));
        spriteSheets.add(BitmapFactory.decodeResource(gv.getResources(), R.drawable.exitbutton, options));
        spriteSheets.add(BitmapFactory.decodeResource(gv.getResources(), R.drawable.pausebutton, options));
        spriteSheets.add(BitmapFactory.decodeResource(gv.getResources(), R.drawable.startbutton, options));
        spriteSheets.add(BitmapFactory.decodeResource(gv.getResources(), R.drawable.statsbutton, options));
        spriteSheets.add(BitmapFactory.decodeResource(gv.getResources(), R.drawable.settingsbutton, options));
        spriteSheets.add(BitmapFactory.decodeResource(gv.getResources(), R.drawable.bosssprites, options));
        spriteSheets.add(BitmapFactory.decodeResource(gv.getResources(), R.drawable.bossfb, options));
        spriteSheets.add(BitmapFactory.decodeResource(gv.getResources(), R.drawable.controlbutton, options));
        spriteSheets.add(BitmapFactory.decodeResource(gv.getResources(), R.drawable.fpsbutton, options));
        spriteSheets.add(BitmapFactory.decodeResource(gv.getResources(), R.drawable.menubutton, options));
        spriteSheets.add(BitmapFactory.decodeResource(gv.getResources(), R.drawable.retrybutton, options));
        spriteSheets.add(BitmapFactory.decodeResource(gv.getResources(), R.drawable.animationbutton, options));
        spriteSheets.add(BitmapFactory.decodeResource(gv.getResources(), R.drawable.bossfbe, options));
    }

    public Bitmap getSheet(int i)
    {
        return spriteSheets.get(i);
    }

    public void destroy()
    {
        for(int i = 0; i < spriteSheets.size(); i++) spriteSheets.get(i).recycle();
        spriteSheets = new ArrayList<Bitmap>();
    }
}
