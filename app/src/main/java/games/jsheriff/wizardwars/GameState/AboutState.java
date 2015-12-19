package games.jsheriff.wizardwars.GameState;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;

import games.jsheriff.wizardwars.Entity.Player;
import games.jsheriff.wizardwars.GameView;
import games.jsheriff.wizardwars.R;

/**
 * Created by jaafe on 12/17/2015.
 */
public class AboutState extends GameState{

    Bitmap BG;
    GameView gv;

    public AboutState(GameStateManager gsm)
    {
        this.gsm = gsm;
        this.gv = gsm.gv;
        BG = BitmapFactory.decodeResource(gv.getResources(), R.drawable.thumbnail);
    }

    @Override
    public void init() {

    }

    @Override
    public void update() {

    }

    @Override
    public void setPlayer(Player p) {

    }

    public void draw()
    {
        Rect src = new Rect(0, 0, BG.getWidth(), BG.getHeight());
        Rect dst = new Rect(0, 0, gv.getWidth(), gv.getHeight());
        gv.canvas.drawBitmap(BG, src, dst, gv.paint);
        gv.paint.setColor(Color.WHITE);
        gv.canvas.drawText("WELCOME TO THA GAME", gv.getWidth()/2-150, gv.getHeight()/10*9, gv.paint);
    }

    @Override
    public void select(float Px, float Py)
    {
        gsm.setState(GameStateManager.MENUSTATE);
    }

    public void deselect(){}
}
