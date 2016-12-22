package games.jsheriff.wizardwars.Entity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.Set;

import games.jsheriff.wizardwars.Entity.Enemies.Enemy;
import games.jsheriff.wizardwars.GameState.SettingsState;
import games.jsheriff.wizardwars.GameView;
import games.jsheriff.wizardwars.Sprites;

/**
 * Created by jaafe on 12/17/2015.
 */
public abstract class MapObject {
    //Tilemap stuff?
    GameView gv;

    //Position/Vector
    protected double x, y, dx, dy;

    //Dimensions
    protected int width, height;

    //collision box
    protected int cwidth, cheight;

    //collision
    protected double xtemp, ytemp;

    //animation
    public boolean cbox;
    public boolean showAnimation;
    protected Animation animation;
    protected boolean facingRight;
    protected int currAction;

    //movement
    protected int currentDirection;
    protected boolean right, left, up, down, tl, tr, bl, br;
    protected double minSpeed, moveSpeed, maxSpeed, stopSpeed;

    //global actions
    public static final int RIGHT = 0;
    public static final int LEFT = 1;
    public static final int UP = 2;
    public static final int DOWN = 3;
    public static final int TL = 4;
    public static final int TR = 5;
    public static final int BL = 6;
    public static final int BR = 7;

    public MapObject(GameView gv)
    {
        this.gv = gv;
        cbox = gv.getSettings().getSetting(SettingsState.HITBOX);
        showAnimation = gv.getSettings().getSetting(SettingsState.ANIMATION);
    }

    public boolean intersects(MapObject o) {
        return this.getRectangle().intersect(o.getRectangle());
    }

    public Rect getRectangle() {
        return new Rect(
                (int) x - cwidth / 2,
                (int) y - cheight / 2,
                (int) x + cwidth / 2,
                (int) y + cheight / 2);
    }

    public void checkTileMapCollision() {
        if (x < cwidth / 2) x = cwidth / 2;
        if (x > gv.getWidth() - cwidth / 2) x = gv.getWidth() - cwidth / 2;
        if (y < cheight / 2) y = cheight / 2;
        if (y > gv.getHeight() - cheight / 2) y = gv.getHeight() - cheight / 2;

        xtemp = x + dx;
        ytemp = y + dy;
    }

    public int getx(){ return (int) x; }
    public int gety(){ return (int) y; }
    public double getdx() { return dx; }
    public double getdy() { return dy; }
    public int getWidth(){ return width; }
    public int getHeight(){ return height; }
    public int getCWidth() { return cwidth; }
    public int getCHeight() { return cheight; }
    public Animation getAnimation() { return animation; }
    public boolean isFacingRight() { return facingRight; }

    public void setPosition(double x, double y) { this.x = x; this.y = y; }
    public void setVector(double dx, double dy) { this.dx = dx; this.dy = dy; }

    //public void setMapPosition
    public void setRight(boolean b){ right = b; }
    public void setLeft(boolean b) { left = b; }
    public void setUp(boolean b) { up = b; }
    public void setDown(boolean b) { down = b; }
    public void setTL(boolean b) { tl = b; }
    public void setTR(boolean b) { tr = b; }
    public void setBL(boolean b) { bl = b; }
    public void setBR(boolean b) { br = b; }

    //public boolean notOnScreen() { }

    public void draw()
    {
        //draw cbox
        if(cbox) drawCRect();

        //draw sprite
        Bitmap toDraw = animation.getSheet();
        Rect src = animation.getSrc();
        Rect dst = new Rect((int)x-width/2, (int)y-height/2, (int)x+width/2, (int)y+height/2);
        if(!facingRight)
        {
            toDraw = flip(toDraw);
            if(!animation.isFlipped())
            {
                animation.flip();
                animation.setFlipped(true);
            }
        }
        else animation.setFlipped(false);
        gv.canvas.drawBitmap(toDraw, src, dst, gv.paint);

        //draw black frames
        if(showAnimation) {
            gv.paint.setColor(Color.BLACK);
            for (Rect Bsrc : animation.getFrames()) {
                gv.canvas.drawRect(
                        (int) this.x - animation.getSheet().getWidth(),
                        (int) this.y - height / 2 - animation.getSheet().getHeight() * 2 + Bsrc.top * 2,
                        (int) this.x + animation.getSheet().getWidth(),
                        (int) this.y - height / 2 - animation.getSheet().getHeight() * 2 + Bsrc.bottom * 2,
                        gv.paint);
            }

            //draw white frame
            gv.paint.setColor(Color.WHITE);
            Rect Wsrc = animation.getFrames()[animation.getFrame()];
            Rect Wdst = new Rect(
                    (int) this.x - animation.getSheet().getWidth() + Wsrc.left * 2,
                    (int) this.y - height / 2 - animation.getSheet().getHeight() * 2 + Wsrc.top * 2,
                    (int) this.x - animation.getSheet().getWidth() + Wsrc.right * 2,
                    (int) this.y - height / 2 - animation.getSheet().getHeight() * 2 + Wsrc.bottom * 2);
            gv.canvas.drawRect(Wdst, gv.paint);

            //draw bm
            Rect BMsrc = new Rect(0, 0, animation.getSheet().getWidth(), animation.getSheet().getHeight());
            Rect BMdst = new Rect(
                    (int) this.x - animation.getSheet().getWidth(),
                    (int) this.y - height / 2 - animation.getSheet().getHeight() * 2,
                    (int) this.x + animation.getSheet().getWidth(),
                    (int) this.y - height / 2);
            gv.canvas.drawBitmap(animation.getSheet(), BMsrc, BMdst, gv.paint);
        }
    }

    public static Bitmap flip(Bitmap in)
    {
        Matrix m = new Matrix();
        m.preScale(-1.0f, 1.0f);
        Bitmap dst = Bitmap.createBitmap(in, 0, 0, in.getWidth(), in.getHeight(), m, true);
        return dst;
    }

    public void drawCRect()
    {
        if(this instanceof Enemy) gv.paint.setColor(Color.RED);
        if(this instanceof FireBall) gv.paint.setColor(Color.YELLOW);
        if(this instanceof Player) gv.paint.setColor(Color.GREEN);
        gv.canvas.drawRect((int)x-cwidth/2, (int)y-cheight/2, (int)x+cwidth/2, (int)y+cheight/2, gv.paint);
    }
}
