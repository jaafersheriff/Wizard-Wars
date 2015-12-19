package games.jsheriff.wizardwars.GameState;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;

import games.jsheriff.wizardwars.Entity.Animation;
import games.jsheriff.wizardwars.Entity.Player;
import games.jsheriff.wizardwars.GameView;
import games.jsheriff.wizardwars.R;

/**
 * Created by jaafe on 12/16/2015.
 */
public class MainMenuState extends GameState
{
    Bitmap[] BG;
    Animation animation;
    GameView gv;

    public ArrayList<String> options;

    Rect titleBounds;
    Rect[] optionBounds;

    public MainMenuState(GameStateManager gsm)
    {
        this.gsm = gsm;
        this.gv = gsm.gv;
    }

    @Override
    public void init()
    {
        //Set up options
        options = new ArrayList<String>();
        options.add("Start");
        options.add("About");
        options.add("Quit");

        //Set up animation
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        BG = new Bitmap[]{
                BitmapFactory.decodeResource(gv.getResources(), R.drawable.menubg, options),
                BitmapFactory.decodeResource(gv.getResources(), R.drawable.menubg1, options),
                BitmapFactory.decodeResource(gv.getResources(), R.drawable.menubg2, options),
                BitmapFactory.decodeResource(gv.getResources(), R.drawable.menubg3, options) };
        animation = new Animation();
        animation.setFrames(BG);
        animation.setDelay(400);
    }


    public void draw() {
        //Draw BG
        Log.d("New image", ""+animation.getFrame());
        Rect bgBounds = new Rect(0, 0, animation.getImage().getWidth(), animation.getImage().getHeight());
        Rect gvBounds = new Rect(0, 0, gv.getWidth(), gv.getHeight());
        gv.canvas.drawBitmap(animation.getImage(), bgBounds, gvBounds, gv.paint);


        //Draw title
        Bitmap title = BitmapFactory.decodeResource(gv.getResources(), R.drawable.title);
        Rect titleSrc = new Rect(0, 0, title.getWidth(), title.getHeight());
        titleBounds = new Rect(gv.getWidth() / 6, gv.getHeight() / 5, gv.getWidth() / 6 * 5, gv.getHeight() / 5 * 2);
        gv.paint.setColor(Color.BLACK);
        //gv.canvas.drawRect(titleBounds, gv.paint);
        gv.paint.setTextSize(65);
        gv.canvas.drawBitmap(title, titleSrc, titleBounds, gv.paint);

        //Draw options
        gv.paint.setTextSize(105);

        int[] cols = {Color.WHITE, Color.CYAN, Color.MAGENTA};
        int[] cols1 = {Color.CYAN, Color.MAGENTA, Color.WHITE};
        optionBounds = new Rect[options.size()];

        for (int i = 0; i < options.size(); i++) {
            gv.paint.setColor(cols[i]);
            //drawbounds
            optionBounds[i] = new Rect(
                    gv.getWidth() / 5,
                    titleBounds.bottom + 100 + i*250,
                    gv.getWidth() / 5 * 4,
                    titleBounds.bottom + 100 + i*250 + 150);
            //gv.canvas.drawRect(optionBounds[i], gv.paint);
            //draw text
            gv.paint.setColor(cols1[i]);
            gv.canvas.drawText(
                    options.get(i),
                    gv.getWidth() / 2 - 150,
                    titleBounds.bottom + 100 + i * 250 + 150,
                    gv.paint);
        }
    }

    @Override
    public void update()
    {

    }

    @Override
    public void select(float Px, float Py)
    {
        //Title
        if(Px > titleBounds.left && Px < titleBounds.right &&
                Py > titleBounds.top && Py < titleBounds.bottom)
        {

        }

        //Options
        for(int i = 0; i < options.size(); i++)
        {
            if(Px > optionBounds[i].left && gv.Px < optionBounds[i].right &&
                    Py > optionBounds[i].top && Py < optionBounds[i].bottom)
            {
                if(i == 0) //State
                {
                    gsm.setState(GameStateManager.LEVEL1STATE);
                }
                if(i == 1) //About
                {
                    gsm.setState(GameStateManager.ABOUTSTATE);
                }
                if(i == 2) //Quit
                {
                    System.exit(0);
                }
            }
        }
    }

    @Override
    public void setPlayer(Player p) {

    }

    public void deselect() { }
}
