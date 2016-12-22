package games.jsheriff.wizardwars.GameState;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;

import java.util.logging.Level;

import games.jsheriff.wizardwars.Entity.Player;
import games.jsheriff.wizardwars.GameView;
import games.jsheriff.wizardwars.R;
import games.jsheriff.wizardwars.Sprites;

/**
 * Created by jaafe on 12/21/2015.
 */
public class DeathState extends GameState{

    GameStateManager gsm;
    GameView gv;

    Rect menuButton;
    Rect retryButton;

    Player player;
    GameState prevState;

    Bitmap BG;
    Bitmap menuImg;
    Bitmap retryImg;

    public DeathState(GameStateManager gsm)
    {
        this.gsm = gsm;
        this.gv = gsm.gv;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        BG = BitmapFactory.decodeResource(gv.getResources(), R.drawable.battlebg, options);
        menuImg = gv.getSprites().getSheet(Sprites.MENUBUTTON);
        retryImg = gv.getSprites().getSheet(Sprites.RETRYBUTTON);
    }

    @Override
    public void init()
    {
        menuButton = new Rect(200, 200, 600, 440);
        retryButton = new Rect(200, 500, 600, 740);
    }

    @Override
    public void setPlayer(Player p)
    {
        this.player = p;
    }

    public void setPrevState(GameState gs)
    {
        this.prevState = gs;
    }

    @Override
    public void select(int Px, int Py)
    {
        //menu
        if(menuButton.contains(Px, Py)) gsm.setState(GameStateManager.MENUSTATE);
        //retry
        if(retryButton.contains(Px, Py))
            gsm.setState(GameStateManager.LEVEL1STATE);
    }

    @Override
    public void draw()
    {
        //BG
        Rect BGsrc = new Rect(0, 0, BG.getWidth(), BG.getHeight());
        Rect BGdst = new Rect(0, 0, gv.getWidth(), gv.getHeight());
        gv.canvas.drawBitmap(BG, BGsrc, BGdst, gv.paint);

        //menu button
        Rect menuSrc = new Rect(0, 0, menuImg.getWidth(), menuImg.getHeight());
        gv.canvas.drawBitmap(menuImg, menuSrc, menuButton, gv.paint);

        //retry button
        Rect retrySrc = new Rect(0, 0, retryImg.getWidth(), retryImg.getHeight());
        gv.canvas.drawBitmap(retryImg, retrySrc, retryButton, gv.paint);
    }

    @Override
    public void update() {}

    @Override
    public void hold(int Px, int Py) {}

    @Override
    public void deselect() {}

    @Override
    public void back()
    {
        gsm.setState(GameStateManager.MENUSTATE);
    }

    public boolean isInit() { return true; }
}
