package games.jsheriff.wizardwars.Entity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;

import games.jsheriff.wizardwars.GameState.BattleState;
import games.jsheriff.wizardwars.GameState.SettingsState;
import games.jsheriff.wizardwars.GameView;
import games.jsheriff.wizardwars.R;
import games.jsheriff.wizardwars.Sprites;

/**
 * Created by jaafe on 12/26/2015.
 */
public class HUD
{
    GameView gv;
    Player player;
    BattleState bs;

    //controls
    Rect exit;
    Rect pause;
    Rect[] controlBounds = new Rect[9];
    Rect scoreBounds;

    //visuals
    boolean drawControls;
    Bitmap life;
    Bitmap exitButton;
    Bitmap pauseButton;

    public HUD(BattleState bs, GameView gv, Player p)
    {
        this.gv = gv;
        this.bs = bs;
        this.player = p;

        exit = new Rect(gv.getWidth()/2-300, gv.getHeight()-120, gv.getWidth()/2-100, gv.getHeight()-20);
        pause = new Rect(gv.getWidth()/2, gv.getHeight()-120, gv.getWidth()/2+200, gv.getHeight()-20);
        scoreBounds = new Rect(20, gv.getHeight()-50, 120, gv.getHeight()-20);

        life = gv.getSprites().getSheet(Sprites.LIFE);
        exitButton = gv.getSprites().getSheet(Sprites.EXITBUTTON);
        pauseButton = gv.getSprites().getSheet(Sprites.PAUSEBUTTON);
    }

    public void draw()
    {
        //exit/pause
        Rect eSrc = new Rect(0, 0, exitButton.getWidth(), exitButton.getHeight());
        gv.canvas.drawBitmap(exitButton, eSrc, exit, gv.paint);
        Rect pSrc = new Rect(0, 0, pauseButton.getWidth(), pauseButton.getHeight());
        gv.canvas.drawBitmap(pauseButton, pSrc, pause, gv.paint);

        //kills
        gv.paint.setTypeface(Typeface.createFromAsset(gv.getCx().getAssets(), "Minecraft.ttf"));
        gv.paint.setTextSize(45);
        gv.paint.setColor(Color.BLACK);
        gv.canvas.drawText("Score: " + player.getKillCount(), scoreBounds.left, scoreBounds.bottom, gv.paint);
        gv.paint.setColor(Color.WHITE);
        gv.canvas.drawText("Score: " + player.getKillCount(), scoreBounds.left, scoreBounds.bottom - 7, gv.paint);

        //lives
        Rect Lsrc = new Rect(0, 0, life.getWidth(), life.getHeight());
        for (int i = 0; i < player.getLives(); i++) {
            Rect Ldst = new Rect(20 + i*120, 20 , (i+1)*120, 110);
            gv.canvas.drawBitmap(life, Lsrc, Ldst, gv.paint);
        }
    }

    public void select(int Px, int Py)
    {
        if(bs.isPaused()) bs.setPaused(!bs.isPaused());
        //exit
        if(exit.contains(Px, Py))
        {
            bs.back();
        }
        //pause
        if(pause.contains(Px, Py))
        {
            bs.setPaused(!bs.isPaused());
        }
    }
}
