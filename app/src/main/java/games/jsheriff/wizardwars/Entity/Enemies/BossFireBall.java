package games.jsheriff.wizardwars.Entity.Enemies;

import android.graphics.Rect;

import java.util.ArrayList;

import games.jsheriff.wizardwars.Entity.Animation;
import games.jsheriff.wizardwars.Entity.Player;
import games.jsheriff.wizardwars.GameView;
import games.jsheriff.wizardwars.Sprites;

/**
 * Created by jaafe on 1/6/2016.
 */
public class BossFireBall extends Enemy{

    GameView gv;
    private Player player;
    private Boss boss;

    private double speedScale;
    private boolean hit;
    private boolean remove;

    private ArrayList<Rect[]> sprites;
    int[] numFrames = {4};


    public BossFireBall(GameView gv, Player p, Boss b, double x, double y)
    {
        super(gv);

        this.gv = gv;

        this.player = p;
        this.boss = b;

        moveSpeed = 12;

        width = height = 150;
        cwidth = cheight = 112;

        setPosition(x, y);
        setMoveSpeed();

        currAction = 0;
        loadSkin();
        animation = new Animation(gv, Sprites.BOSSFB);
        animation.setFrames(sprites.get(currAction));
        animation.setDelay(70);
    }

    private void loadSkin()
    {

        sprites = new ArrayList<Rect[]>();

        int bmwidth = gv.getSprites().getSheet(Sprites.BOSSFB).getWidth()/numFrames[currAction];
        int bmheight = gv.getSprites().getSheet(Sprites.BOSSFB).getHeight()/numFrames.length;

        for(int i = 0; i < numFrames.length; i++)
        {
            Rect[] bm = new Rect[numFrames[i]];

            for(int j = 0; j < numFrames[i]; j++)
            {
                bm[j] = new Rect(bmwidth*j, bmheight*i, bmwidth*(j+1), bmheight*(i+1));
            }
            sprites.add(bm);
        }
    }

    @Override
    public void checkTileMapCollision() {
        if (x < 0 + cwidth / 2) dx = 0;
        if (x > gv.getWidth() - cwidth / 2) dx = 0;
        if (y < 0 + cheight / 2) dy = 0;
        if (y > gv.getHeight() - cheight / 2) dy = 0;

        xtemp = x + dx;
        ytemp = y + dy;
    }

    private void setMoveSpeed()
    {
        double ddx = player.getx() - this.x;
        double ddy = player.gety() - this.y;
        double mag = Math.sqrt(ddx * ddx + ddy * ddy);
        if(mag > 0) {
            ddx /= mag;
            ddy /= mag;
        }

        dx = moveSpeed * ddx;
        dy = moveSpeed * ddy;
    }

    public void update()
    {
        checkTileMapCollision();
        setPosition(xtemp, ytemp);

        setSetHit();

        checkDirection();
        animation.update();
        if(hit && animation.hasPlayedOnce())
        {
            remove = dead = true;
        }
    }

    @Override
    public void hit(int a){ setHit(); }

    private void setSetHit()
    {
        if(dx == 0 && Math.abs(dy) != 0) setHit();
        if(dy == 0 && Math.abs(dx) != 0) setHit();
    }

    public void setHit()
    {
        if(hit) return;

        hit = true;
        dx = dy = 0;
    }

    public boolean shouldRemove() { return remove; }

    private void checkDirection()
    {
        if(dx < 0) facingRight = false;
        else facingRight = true;
    }


}
