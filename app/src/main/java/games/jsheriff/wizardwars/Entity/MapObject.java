package games.jsheriff.wizardwars.Entity;

import android.graphics.Rect;

import games.jsheriff.wizardwars.GameView;

/**
 * Created by jaafe on 12/17/2015.
 */
public abstract class MapObject
{
    //Tilemap stuff?

    //Position/Vector
    protected double x, y, dx, dy;

    //Dimensions
    protected int width, height;

    //collision box
    protected int cwidth, cheight;

    //collision
    protected int currRow, currCol;
    protected double xdest, ydest, xtemp, ytemp;
    protected boolean topLeft, topRight, bottomLeft, bottomRight;

    //animation
    protected boolean facingRight;

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

    public MapObject()
    {

    }

    public boolean intersects(MapObject o)
    {
        return this.getRectangle().intersect(o.getRectangle());
    }

    public Rect getRectangle()
    {
        return new Rect(
                (int) x-cwidth,
                (int) y-cheight,
                cwidth,
                cheight);
    }

    public void calculateCorners()
    {

    }

    public void checkTileMapCollision()
    {
        xtemp = x + dx;
        ytemp = y + dy;
    }

    public int getx(){ return (int) x; }
    public int gety(){ return (int) y; }
    public int getWidth(){ return width; }
    public int getHeight(){ return height; }
    public int getCWidth() { return cwidth; }
    public int getCHeight() { return cheight; }
    public void setPosition(double x, double y) { this.x = x; this.y = y; }
    public void setVector(double dx, double dy) { this.dx = dx; this.dy = dy; }
    //public void setMapPosition
    public void setRight(boolean b)
    {
        right = b;
        setTR(); setBR();
    }
    public void setLeft(boolean b)
    {
        left = b;
        setTL(); setBL();
    }

    public void setUp(boolean b)
    {
        up = b;
        setTL(); setTR();
    }

    public void setDown(boolean b)
    {
        down = b;
        setBL(); setBR();
    }

    public void setTL()
    {
        if(up && left) tl = true;
        else tl = false;
    }

    public void setTR()
    {
        if(up && right) tr = true;
        else tr = false;
    }

    public void setBL()
    {
        if(down && left) bl = true;
        else bl = false;
    }

    public void setBR()
    {
        if(down && right) br = true;
        else br = false;
    }

    //public boolean notOnScreen() { }

    public void draw(GameView gv)
    {
    }
}
