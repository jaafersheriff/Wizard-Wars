package games.jsheriff.wizardwars.Entity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.ArrayList;

import games.jsheriff.wizardwars.GameView;
import games.jsheriff.wizardwars.R;

/**
 * Created by jaafe on 12/16/2015.
 */
public class Player extends MapObject{

    //Animations
    Animation animation;
    private ArrayList<Bitmap[]> sprites;
    Bitmap sprite;
    private final int[] numFrames = {3, 1};
    private static int currentAction;
    private final int IDLE = 0;
    private final int FIRING = 1;

    GameView gv;

    public Player(GameView gv)
    {
        this.gv = gv;

        //dimensions
        width = height = 30;
        cwidth = cheight = 20;

        //speed
        moveSpeed = 2;
        maxSpeed = 16;
        stopSpeed = 1;

        //animation
        loadSkin();
        currentAction = IDLE;
        animation.setFrames(sprites.get(IDLE));
        animation.setDelay(400);
        currentDirection = -1;
        facingRight = true;
    }

    private void loadSkin()
    {
        sprites = new ArrayList<Bitmap[]>();
        for(int i = 0; i < numFrames.length; i++)
        {
            Bitmap[] bm = new Bitmap[numFrames[i]];
            for(int j = 0; i < numFrames[i]; j++)
            {
            }
        }
    }

    public int getCurrDirect() { return currentDirection; }
    public double getdx() { return dx; }
    public double getdy() { return dy; }

    private void getNextPosition()
    {
        if(right)
        {
            currentDirection = RIGHT;
            moveRight();
        }
        if(left)
        {
            currentDirection = LEFT;
            moveLeft();
        }
        if(up)
        {
            currentDirection = UP;
            moveUp();
        }
        if(down)
        {
            currentDirection = DOWN;
        }
        if(tl)
        {
            currentDirection = TL;
            moveUp(); moveLeft();
        }
        if(tr)
        {
            currentDirection = TR;
            moveUp(); moveRight();
        }
        if(bl)
        {
            currentDirection = BL;
            moveDown(); moveLeft();
        }
        if(br)
        {
            currentDirection = BR;
            moveDown(); moveRight();
        }

        if(dx > 0)
        {
            dx -= stopSpeed;
            if (dx < 0) dx = 0;
        }
        if(dx < 0)
        {
            dx += stopSpeed;
            if(dx > 0) dx = 0;
        }
        if(dy > 0)
        {
            dy -= stopSpeed;
            if(dy < 0) dy = 0;
        }
        if(dy < 0)
        {
            dy += stopSpeed;
            if(dy > 0) dy = 0;
        }
        if(dx == 0 && dy == 0)
        {
            currentDirection = -1;
        }
    }

    private void moveRight()
    {
        facingRight = true;
        dx += moveSpeed;
        if(dx > maxSpeed) dx = maxSpeed;
    }
    private void moveLeft()
    {
        facingRight = false;
        dx -= moveSpeed;
        if(dx < -maxSpeed) dx = -maxSpeed;
    }
    private void moveUp()
    {
        dy -= moveSpeed;
        if(dy < -maxSpeed) dy = -maxSpeed;
    }
    private void moveDown()
    {
        dy += moveSpeed;
        if(dy > maxSpeed) dy = maxSpeed;
    }

    public void update()
    {
        //update position
        getNextPosition();
        checkTileMapCollision();
        setPosition(xtemp, ytemp);

        //set animation
        //set direction
    }

    public void draw()
    {
        sprite = BitmapFactory.decodeResource(gv.getResources(), R.drawable.playerplayer);
        Rect src = new Rect((int)x-width/2, (int)y-height/2, (int)x+width/2, (int)y+height/2);
        Rect dst = new Rect((int)x-width, (int)y-height, (int)x+width, (int)y+height);
        gv.canvas.drawBitmap(sprite, src, dst, gv.paint);
        gv.canvas.drawBitmap(sprite, (float)x-width/2, (float)y-height/2, gv.paint);
    }

}
