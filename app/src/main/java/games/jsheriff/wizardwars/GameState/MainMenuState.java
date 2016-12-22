package games.jsheriff.wizardwars.GameState;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;


import java.util.ArrayList;

import games.jsheriff.wizardwars.Entity.Player;
import games.jsheriff.wizardwars.GameView;
import games.jsheriff.wizardwars.R;
import games.jsheriff.wizardwars.Sounds;
import games.jsheriff.wizardwars.Sprites;

public class MainMenuState extends GameState
{
    BitmapFactory.Options o;
    int[] BGsource;
    int currFrame;
    long currFrameTime;
    GameView gv;
    boolean playedOnce;

    ArrayList<String> options;

    Rect titleBounds;
    Rect[] optionBounds;

    public MainMenuState(GameStateManager gsm)
    {
        this.gsm = gsm;
        this.gv = gsm.gv;

        init();
    }

    @Override
    public void init()
    {
        isinit = false;
        //Set up options
        options = new ArrayList<String>();
        options.add("Start");
        options.add("Stats");
        options.add("Settings");

        //Set up animation
        playedOnce = false;
        o = new BitmapFactory.Options();
        o.inScaled = false;
        BGsource = new int[]{
                R.drawable.menubg1, R.drawable.menubg2, R.drawable.menubg3,R.drawable.menubg4,
                R.drawable.menubg5, R.drawable.menubg6, R.drawable.menubg7, R.drawable.menubg8,
                R.drawable.menubg9, R.drawable.menubg10, R.drawable.menubg11, R.drawable.menubg12,
                R.drawable.menubg13, R.drawable.menubg14};
        currFrame = 0;
        currFrameTime = System.currentTimeMillis();

        isinit = true;
    }


    public void draw() {
        //Draw BG
        Bitmap BG = BitmapFactory.decodeResource(gv.getResources(), BGsource[currFrame], o);
        Rect bgBounds = new Rect(0, 0, BG.getWidth(), BG.getHeight());
        Rect gvBounds = new Rect(0, 0, gv.getWidth(), gv.getHeight());
        gv.canvas.drawBitmap(BG, bgBounds, gvBounds, gv.paint);

        long nextFrameTime = System.currentTimeMillis();
        if(nextFrameTime - currFrameTime >= 80)
        {
            if (currFrame == BGsource.length-1) playedOnce = true;
            currFrame = ++currFrame % BGsource.length;
            //if(currFrame == 0 && playedOnce) currFrame = 11;
            currFrameTime = nextFrameTime;
        }

        //Draw title
        gv.paint.setTypeface(Typeface.createFromAsset(gv.getCx().getAssets(), "Minecraft.ttf"));
        gv.paint.setColor(Color.WHITE);
        gv.paint.setTextSize(150);
        gv.canvas.drawText("Wizard", 400, 500, gv.paint);
        gv.canvas.drawText("Wars", 500, 670, gv.paint);

        //Draw options
        //create option button bounds
        optionBounds = new Rect[options.size()];
        for (int i = 0; i < options.size(); i++) {
            optionBounds[i] = new Rect(
                    gv.getWidth() / 5,
                    gv.getHeight()/2 + i*250 + 20,
                    gv.getWidth() / 5 * 4,
                    gv.getHeight()/2 + i*250 + 250);
        }
        for(int i = 0; i < options.size(); i++)
        {
            int source;
            if(i == 0) source = Sprites.STARTBUTTON;
            else if (i == 1) source = Sprites.STATSBUTTON;
            else source = Sprites.SETTINGSBUTTON;

            Bitmap button = gv.getSprites().getSheet(source);
            Rect osrc = new Rect(0, 0, button.getWidth(), button.getHeight());
            Rect odst = optionBounds[i];
            gv.canvas.drawBitmap(button, osrc, odst, gv.paint);
        }
    }

    @Override
    public void select(int Px, int Py)
    {
        //Options
        for(int i = 0; i < options.size(); i++)
        {
            if(optionBounds[i] == null) continue;
            if(optionBounds[i].contains(Px, Py))
            {
                gv.getSounds().playSound(Sounds.SHUTTER, 0.8f, 0);

                switch(i)
                {
                    case 0: //START
                        gsm.setState(GameStateManager.LEVEL1STATE);
                        break;
                    case 1: //STATS
                        gsm.setState(GameStateManager.STATSSTATE);
                        break;
                    case 2: //SETTINGS
                        gsm.setState(GameStateManager.SETTINGSSTATE);
                        break;
                    case 3: //QUIT
                        System.exit(0);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    public void hold(int Px, int Py) { }

    public void back()
    {
        System.exit(0);
    }

    @Override
    public void setPlayer(Player p) {}

    public void deselect() { }

    @Override
    public void update() { }

    public boolean isInit() { return isinit; }

}
