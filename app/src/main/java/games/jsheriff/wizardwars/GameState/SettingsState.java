package games.jsheriff.wizardwars.GameState;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;

import java.util.ArrayList;

import games.jsheriff.wizardwars.Entity.Button;
import games.jsheriff.wizardwars.Entity.Player;
import games.jsheriff.wizardwars.GameView;
import games.jsheriff.wizardwars.R;
import games.jsheriff.wizardwars.Sprites;

public class SettingsState extends GameState {

    GameView gv;
    Bitmap BG;
    BitmapFactory.Options options;

    Rect backButton;
    Bitmap exitImg;

    ArrayList<Button> buttons;

    Button hitBoxButton;
    public static final int HITBOX = 0;

    Button enemyButton;
    public static final int ENEMY = 1;

    Button invicButton;
    public static final int INVINC = 2;

    Button fpsButton;
    public static final int FPS = 3;

    Button animationButton;
    public static final int ANIMATION = 4;

    public SettingsState(GameStateManager gsm)
    {
        this.gsm = gsm;
        this.gv = gsm.gv;

        animationButton = new Button(gv, false, 550, 550, 750, 670, Sprites.ANIMATIONBUTTON, new int[] {3, 3});
        fpsButton = new Button(gv, false, 300, 550, 500, 670, Sprites.FPSBUTTON, new int[] {1, 1});
        invicButton = new Button(gv, false, 300, 400, 500, 520, Sprites.INVINCIBILITYBUTTON, new int[] {1, 1});
        enemyButton = new Button(gv, true, 300, 700, 500, 820, Sprites.ENEMYBUTTON, new int[] {2, 2});
        hitBoxButton = new Button(gv, false, 550, 700, 750, 820, Sprites.HITBOXBUTTON, new int[] {1, 1});
        init();
    }

    @Override
    public void init()
    {
        isinit = false;

        //reference images
        options = new BitmapFactory.Options();
        options.inScaled = false;
        BG = BitmapFactory.decodeResource(gv.getResources(), R.drawable.battlebg, options);

        exitImg = gv.getSprites().getSheet(Sprites.EXITBUTTON);

        //create actual button bounds
        backButton = new Rect(0, 0, 200, 200);

        buttons = new ArrayList<Button>();
        buttons.add(hitBoxButton);
        buttons.add(enemyButton);
        buttons.add(invicButton);
        buttons.add(fpsButton);
        buttons.add(animationButton);

        isinit = true;
    }

    @Override
    public void draw()
    {
        //BG
        Rect BGsrc = new Rect(0, 0, BG.getWidth(), BG.getHeight());
        Rect BGdst = new Rect(0, 0, gv.getWidth(), gv.getHeight());
        gv.paint.setColor(Color.WHITE);
        gv.canvas.drawBitmap(BG, BGsrc, BGdst, gv.paint);

        Rect src = new Rect(0, 0, exitImg.getWidth(), exitImg.getHeight());
        gv.canvas.drawBitmap(exitImg, src, backButton, gv.paint);

        for(Button b : buttons)
            b.draw();
    }

    @Override
    public void select(int Px, int Py)
    {
        //back button
        if(backButton.contains(Px, Py)) back();

        //sound button
        for(Button b : buttons)
                if(b.getBounds().contains(Px, Py))
                {
                    b.action();
                }
    }

    public boolean getSetting(int src) { return this.buttons.get(src).getBool(); }

    @Override
    public void back()
    {
        gsm.setState(GameStateManager.MENUSTATE);
    }

    @Override
    public void deselect() {}

    @Override
    public void update()
    {
        for(Button b : buttons)
            b.update();
    }

    @Override
    public void setPlayer(Player p) {}

    @Override
    public void hold(int Px, int Py){}

    public boolean isInit() { return isinit; }
}
