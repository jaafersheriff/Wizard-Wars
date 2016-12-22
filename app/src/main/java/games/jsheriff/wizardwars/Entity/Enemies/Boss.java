package games.jsheriff.wizardwars.Entity.Enemies;

import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import games.jsheriff.wizardwars.Entity.Animation;
import games.jsheriff.wizardwars.Entity.Player;
import games.jsheriff.wizardwars.GameState.BattleState;
import games.jsheriff.wizardwars.GameView;
import games.jsheriff.wizardwars.Sprites;

public class Boss extends Enemy{

    private Player player;
    private GameView gv;
    final int PLAYERX = 300;
    private final int PATHTIMER = 1000;
    private static long movetimer;

    private static long firetimer;
    private final int TIMEPERFB = 600;

    private ArrayList<Rect[]> sprites;
    private int[] numFrames = {3};

    public int MAXHEALTH = 400 ;

    public Boss(GameView gv, Player p)
    {
        super(gv);

        this.gv = gv;
        this.player = p;

        dx = dy = 0;
        moveSpeed = 10;
        maxSpeed = 15;

        width = height = 190;
        cwidth = cheight = 165;

        health = MAXHEALTH += 100 * (int) (player.getKillCount() / 30);
        damage = 1;
        firetimer = System.currentTimeMillis();
        movetimer = System.currentTimeMillis();

        currAction = 0; //Boss only has 1 action
        loadFrames();
        animation = new Animation(gv, Sprites.BOSSSHEET);
        animation.setFrames(sprites.get(currAction));
        animation.setDelay(250);
    }


    private void loadFrames() {
        sprites = new ArrayList<Rect[]>();

        int bmwidth = gv.getSprites().getSheet(Sprites.BOSSSHEET).getWidth() / numFrames[currAction];
        int bmheight = gv.getSprites().getSheet(Sprites.BOSSSHEET).getHeight() / numFrames.length;

        for (int i = 0; i < numFrames.length; i++) {
            Rect[] bm = new Rect[numFrames[i]];
            for (int j = 0; j < numFrames[i]; j++)
                bm[j] = new Rect(bmwidth * j, bmheight * i, bmwidth * (j + 1), bmheight * (i + 1));
            sprites.add(bm);
        }
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

    public void getNextPosition()
    {
        int WALL = BattleState.WALL;

        double pdx = dx;
        double pdy = dy;

        if((dx == 0 && dy == 0)) {
            Random r = new Random();
            pdx = this.x - r.nextInt(gv.getWidth());
            pdy = this.y - r.nextInt(gv.getHeight());
            movetimer = System.currentTimeMillis();
        }

        if(this.x < WALL) {
            pdx = WALL * (WALL - this.x);
            double mag = Math.sqrt(pdx * pdx + pdy * pdy);
            if(mag > 0)
                pdx /= mag;
        }
        else if (this.x > gv.getWidth() - WALL) {
            pdx = (gv.getWidth() - WALL) * ((gv.getWidth() - WALL) - this.x);
            double mag = Math.sqrt(pdx * pdx + pdy * pdy);
            if(mag > 0)
                pdx /= mag;
        }

        if(this.y < WALL) {
            pdy = WALL * (WALL - this.y);
            double mag = Math.sqrt(pdx * pdx + pdy * pdy);
            if(mag > 0)
                pdy /= mag;
        }
        else if (this.y > gv.getHeight() - WALL) {
            pdy = (gv.getHeight() - WALL) * ((gv.getHeight() - WALL) - this.y);
            double mag = Math.sqrt(pdx * pdx + pdy * pdy);
            if(mag > 0)
                pdy /= mag;
        }

        Rect prect = new Rect(player.getx() - PLAYERX, player.gety() - PLAYERX, player.getx() + PLAYERX, player.gety() + PLAYERX);
        if(prect.contains((int)this.x, (int)this.y)) {
            pdx = this.x - player.getx();
            pdy = this.y - player.gety();
            double mag = Math.sqrt(pdx * pdx + pdy * pdy);
            if(mag > 0) {
                pdx /= mag;
                pdy /= mag;
            }
        }

        dx = moveSpeed * pdx;
        dy = moveSpeed * pdy;

        double length = Math.sqrt(dx * dx + dy * dy);
        if(length > maxSpeed)
        {
            dx /= length;
            dy /= length;
        }
        else if(Math.abs(dx) > maxSpeed) dx = setMax(dx);
        else if(Math.abs(dy) > maxSpeed) dy = setMax(dy);
    }

    double setMax(double in){
        if(in < 0) return -maxSpeed;
        else return maxSpeed;
    }

    private void changeDirection()
    {
        if(this.x < player.getx())
            facingRight = false;
        else
            facingRight = true;
    }

    public int getMaxHealth() { return MAXHEALTH; }

    public void draw()
    {
        super.draw();
    }

    public int getHealth() { return health; }
}
