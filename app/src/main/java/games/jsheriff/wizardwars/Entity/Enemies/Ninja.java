package games.jsheriff.wizardwars.Entity.Enemies;

import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;

import games.jsheriff.wizardwars.Entity.Animation;
import games.jsheriff.wizardwars.Entity.Player;
import games.jsheriff.wizardwars.GameView;
import games.jsheriff.wizardwars.Sprites;

/**
 * Created by jaafe on 12/24/2015.
 */
public class Ninja extends Enemy{

    //animation
    private ArrayList<Rect[]> sprites;
    int[] numFrames = {4};
    private final int MOVING = 0;

    public Ninja(GameView gv, Player p)
    {
        super(gv, p);

        moveSpeed = maxSpeed = 4.11;

        width = height = 190;
        cwidth = 120;
        cheight = 165;

        health = 1;
        damage = 1;

        loadSkin();
        currAction = MOVING;
        animation = new Animation(gv, Sprites.NINJASHEET);
        animation.setFrames(sprites.get(MOVING));
        animation.setDelay(120);
        facingRight = true;
    }

    public void loadSkin()
    {
        Log.d("Loading skin", this.toString());

        sprites = new ArrayList<Rect[]>();

        int bmwidth = gv.getSprites().getSheet(Sprites.NINJASHEET).getWidth()/numFrames[currAction];
        int bmheight = gv.getSprites().getSheet(Sprites.NINJASHEET).getHeight()/numFrames.length;

        for(int i = 0; i < numFrames.length; i++)
        {
            Rect[] bm = new Rect[numFrames[i]];

            for(int j = 0; j < numFrames[i]; j++)
            {
                bm[j] = new Rect(bmwidth*j, bmheight*i, bmwidth*(j+1), bmheight*(i+1));
                Log.d("Src Rect created", bm[j].toString());
            }
            sprites.add(bm);
        }
    }

    private void getNextPosition()
    {
        double ddx = p.getx() - this.x;
        double ddy = p.gety() - this.y;
        double mag = Math.sqrt(ddx * ddx + ddy * ddy);
        ddx /= mag;
        ddy /= mag;

        dx = moveSpeed * ddx;
        dy = moveSpeed * ddy;

        double cross = Math.sqrt(dx*dx + dy*dy);

        if(Math.abs(cross) > maxSpeed)
        {
            dx = setMax(dx)/cross;
            dy = setMax(dy)/cross;
        }
        else if (Math.abs(dx) > maxSpeed) dx = setMax(dx);
        else if (Math.abs(dy) > maxSpeed) dy = setMax(dy);
    }

    double setMax(double in) {
        if(in < 0) return -maxSpeed;
        else return maxSpeed;
    }

    public void update()
    {
        //update position
        getNextPosition();
        checkTileMapCollision();
        setPosition(xtemp, ytemp);

        //update animation
        changeDirection();
        animation.update();
    }

    private void changeDirection()
    {
        if(dx < 0) facingRight = false;
        else facingRight = true;
    }

    public void draw()
    {
        super.draw();
    }
}
