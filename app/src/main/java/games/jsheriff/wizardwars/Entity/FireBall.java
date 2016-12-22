package games.jsheriff.wizardwars.Entity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;

import games.jsheriff.wizardwars.GameView;
import games.jsheriff.wizardwars.R;
import games.jsheriff.wizardwars.Sprites;

public class FireBall extends MapObject{

    private boolean hit;
    private boolean remove;
    private ArrayList<Rect[]> sprites;
    private final int[] numFrames = {4, 4};
    private final int FIRING = 0;
    private final int EXPLODING = 1;

    public FireBall(GameView gv, int direction)
    {
        super(gv);

        currentDirection = direction;
        moveSpeed = 17.7;
        setMoveSpeed(currentDirection);

        width = height = 150;
        cwidth = cheight = 112;

        //animation
        loadSkin();
        currAction = FIRING;
        animation = new Animation(gv, Sprites.FBSHEET);
        animation.setFrames(sprites.get(FIRING));
        animation.setDelay(80);
    }

    private void loadSkin()
    {
        sprites = new ArrayList<Rect[]>();
        int bmwidth = gv.getSprites().getSheet(Sprites.FBSHEET).getWidth()/numFrames[currAction];
        int bmheight = gv.getSprites().getSheet(Sprites.FBSHEET).getHeight()/numFrames.length;

        for(int i = 0; i < numFrames.length; i++)
        {
            Rect[] bm = new Rect[numFrames[i]];

            for(int j = 0; j < numFrames[i]; j++)
            {
                bm[j] = new Rect(bmwidth*j, bmheight*i, bmwidth*j+bmwidth, bmheight*i+bmheight);
            }
            sprites.add(bm);
        }
    }

    private void setMoveSpeed(int direction)
    {
        if(direction == RIGHT) dx = moveSpeed;
        if(direction == LEFT) dx = -moveSpeed;
        if(direction == UP) dy = -moveSpeed;
        if(direction == DOWN) dy = moveSpeed;
        if(direction == TL)
        {
            dx = dy = -moveSpeed/Math.sqrt(2);
        }
        if(direction == TR)
        {
            dx = moveSpeed/Math.sqrt(2);
            dy = -moveSpeed/Math.sqrt(2);
        }
        if(direction == BL)
        {
            dx = -moveSpeed/Math.sqrt(2);
            dy = moveSpeed/Math.sqrt(2);
        }
        if(direction == BR)
        {
            dx = dy = moveSpeed/Math.sqrt(2);
        }
        if(direction == -1)
        {
            dx = dy = 0;
        }
    }

    public void setHit()
    {
        if(hit) return;
        hit = true;
        animation.setFrames(sprites.get(EXPLODING));
        animation.setDelay(90);
    }

    public boolean shouldRemove() { return remove; }

    public void update()
    {
        checkTileMapCollision();
        setPosition(xtemp, ytemp);
        setSetHit();

        animation.update();
        if(hit)
        {
            cwidth = cheight += 1;
            if(dx > 0) dx--;
            if(dx < 0) dx++;
            if(dy < 0) dy++;
            if(dy > 0) dy--;
        }
        if(hit && animation.hasPlayedOnce()) remove = true;
    }

    @Override
    public void checkTileMapCollision() {
        if (x < 0 + cwidth / 2) x = cwidth / 2;
        if (x > gv.getWidth() - cwidth / 2) x = gv.getWidth() - cwidth / 2;
        if (y < 0 + cheight / 2) y = cheight / 2;
        if (y > gv.getHeight() - cheight / 2) y = gv.getHeight() - cheight / 2;

        xtemp = x + dx;
        ytemp = y + dy;
    }

    private void setSetHit()
    {
        int cd = currentDirection;
        if( (cd == TR || cd == TL || cd == BR || cd == BL) && !hit)
        {
            if(dx == 0) setHit();
            if(dy == 0) setHit();
        }
        if(dx == 0 && dy == 0 && !hit) setHit();
        if( (x <= cwidth/2 || x >= gv.getWidth()-cwidth/2 || y <= cheight/2 || y >= gv.getHeight()-cheight/2) && !hit)
        {
            dx = dy = 0;
            setHit();
        }
    }

    public void draw()
    {
        super.draw();
    }
}
