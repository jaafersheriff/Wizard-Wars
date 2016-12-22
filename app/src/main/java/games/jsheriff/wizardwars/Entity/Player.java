package games.jsheriff.wizardwars.Entity;

import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;

import games.jsheriff.wizardwars.Entity.Enemies.Enemy;
import games.jsheriff.wizardwars.GameState.SettingsState;
import games.jsheriff.wizardwars.GameView;
import games.jsheriff.wizardwars.Sounds;
import games.jsheriff.wizardwars.Sprites;

public class Player extends MapObject{

    //player stuff
    private boolean invic;
    private int lives;
    private boolean flinching;
    private long flinchTimer;
    private int killCount;
    public static final int MAXLIVES = 5;
    public static final int ONEUPCOUNT = 10;

    //fire stuff
    private final int FIREBALLTIMER = 100;
    private int firing;
    private boolean isFiring;
    public ArrayList<FireBall> fireBalls;
    private long fireBallStartTimer;

    //Animations
    private ArrayList<Rect[]> sprites;
    private final int[] numFrames = {4, 4};
    //Animation actions
    private final int IDLE = 0;
    private final int FIRING = 1;

    public Player(GameView gv)
    {
        super(gv);

        //dimensions
        width = height = 220;
        cwidth = 130;
        cheight = 175;

        //speed
        moveSpeed = 1.9;
        maxSpeed = 10.1;
        stopSpeed = 0.63;

        lives = 3;
        killCount = 0;

        invic = gv.getSettings().getSetting(SettingsState.INVINC);
        fireBalls = new ArrayList<FireBall>();
        fireBallStartTimer = System.currentTimeMillis();

        //animation
        loadSkin();
        currentDirection = -1;
        currAction = IDLE;
        animation = new Animation(gv, Sprites.PLAYERSHEET);
        animation.setFrames(sprites.get(IDLE));
        animation.setDelay(150);
        facingRight = true;
    }

    private void loadSkin()
    {
        Log.d("Loading skin", this.toString());

        sprites = new ArrayList<Rect[]>();

        int bmwidth = gv.getSprites().getSheet(Sprites.PLAYERSHEET).getWidth()/numFrames[currAction];
        int bmheight = gv.getSprites().getSheet(Sprites.PLAYERSHEET).getHeight()/numFrames.length;

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

    public void update() {
        //update position
        updatePosition();
        checkTileMapCollision();
        setPosition(xtemp, ytemp);

        //fireball timer
        long fireBallEndTimer = System.currentTimeMillis() - fireBallStartTimer;

        if (firing > -1 && fireBallEndTimer >= FIREBALLTIMER) {
            fireBallStartTimer = System.currentTimeMillis();
            createFireBall(currentDirection);
        }

        //update fireballs
        for (int i = 0; i < fireBalls.size(); i++) {
            fireBalls.get(i).update();
            if (fireBalls.get(i).shouldRemove()) {
                fireBalls.remove(i);
                i--;
            }
        }

        //check flinching
        if (flinching) {
            long elapsed = System.currentTimeMillis() - flinchTimer;
            if (elapsed > 1000) flinching = false;
        }

        //set animation
        if (dx != 0 || dy != 0){
            if (firing > -1) {
                if (currAction != FIRING) {
                    currAction = FIRING;
                    animation.setFrames(sprites.get(FIRING));
                }
            }
        }
        else
        {
            if(currAction != IDLE) {
                currAction = IDLE;
                animation.setFrames(sprites.get(IDLE));
            }
        }
        //if curr action = dead, play dead animation

        animation.update();
        //set direction
    }

    private void createFireBall(int currDirection)
    {
        FireBall fb = new FireBall(gv, currDirection);
        fb.setPosition(x, y);
        fireBalls.add(fb);
    }

    public void checkAttack(ArrayList<Enemy> enemies)
    {
         //loop through enemies
        for(int i = 0; i < enemies.size(); i++)
        {
            Enemy e = enemies.get(i);
            //fireballs
            for(int j = 0; j < fireBalls.size(); j++)
            {
                if(e.intersects(fireBalls.get(j)))
                {
                    e.hit(1);
                    fireBalls.get(j).setHit();
                }
            }

            //check enemy collision
            if (this.intersects(e)) hit(e.isDead());
        }

    }

    public void hit(boolean dead)
    {
        if(flinching || dead || invic) return;

        lives--;
        if(lives <= 0)
        {
            lives = 0;
            dead = true;
        }
        flinching = true;
        flinchTimer = System.currentTimeMillis();
    }

    public void deselect()
    {
        left = right = up = down = tl = tr = bl = br = false;
    }

    private void updatePosition()
    {
        if(right) {
            facingRight = true;
            currentDirection = RIGHT;
            setFiring(currentDirection);
            dx += moveSpeed;
            if(dx > maxSpeed)
                dx = maxSpeed;
        }
        else if(tr) {
            facingRight = true;
            currentDirection = TR;
            setFiring(currentDirection);
            dx += moveSpeed/Math.sqrt(2);
            dy -= moveSpeed/Math.sqrt(2);
            if(Math.sqrt(dx*dx+dy*dy) > maxSpeed) {
                dx = maxSpeed/Math.sqrt(2);
                dy = -maxSpeed/Math.sqrt(2);
            }
        }
        else if (br) {
            facingRight = true;
            currentDirection = BR;
            setFiring(currentDirection);
            dx += moveSpeed/Math.sqrt(2);
            dy += moveSpeed/Math.sqrt(2);
            if(Math.sqrt(dx*dx+dy*dy) > maxSpeed) {
                dx = maxSpeed/Math.sqrt(2);
                dy = maxSpeed/Math.sqrt(2);
            }
        }
        else if (left) {
            facingRight = false;
            currentDirection = LEFT;
            setFiring(currentDirection);
            dx -= moveSpeed;
            if(dx < -maxSpeed) dx = -maxSpeed;
        }
        else if (tl) {
            facingRight = false;
            currentDirection = TL;
            setFiring(currentDirection);
            dx -= moveSpeed/Math.sqrt(2);
            dy -= moveSpeed/Math.sqrt(2);
            if(Math.sqrt(dx*dx+dy*dy) > maxSpeed) dx = dy = -maxSpeed/Math.sqrt(2);
        }
        else if (bl) {
            facingRight = false;
            currentDirection = BL;
            setFiring(currentDirection);
            dx -= moveSpeed/Math.sqrt(2);
            dy += moveSpeed/Math.sqrt(2);
            if(Math.sqrt(dx*dx+dy*dy) > maxSpeed)
            {
                dx = -maxSpeed/Math.sqrt(2);
                dy = maxSpeed/Math.sqrt(2);
            }
        }
        else if (up) {
            currentDirection = UP;
            setFiring(currentDirection);
            dy -= moveSpeed;
            if (dy < -maxSpeed) dy = -maxSpeed;
        }
        else if (down) {
            currentDirection = DOWN;
            setFiring(currentDirection);
            dy += moveSpeed;
            if (dy > maxSpeed) dy = maxSpeed;
        }

        else {
            currentDirection = -1;
            setFiring(-1);
            if (dx > 0) {
                dx -= stopSpeed;
                if (dx < 0) dx = 0;
            }
            if (dx < 0) {
                dx += stopSpeed;
                if (dx > 0) dx = 0;
            }
            if (dy > 0) {
                dy -= stopSpeed;
                if (dy < 0) dy = 0;
            }
            if (dy < 0) {
                dy += stopSpeed;
                if (dy > 0) dy = 0;
            }
        }
    }

    public void draw(boolean paused)
    {
        //draw fireballs
        for(int i = 0; i < fireBalls.size(); i++) {
            fireBalls.get(i).draw();
        }

        //draw flinching
        if(flinching && !paused)
        {
            long elapsed = System.currentTimeMillis() - flinchTimer;
            if(elapsed/100%2 == 0) return;
        }

        super.draw();

    }

    public int getLives() { return lives; }
    public int getKillCount() { return killCount; }

    private  void setFiring(int i) { firing = i;}
    public void plusKillCount() { killCount++; }
    public void plusLives() { lives++; }

    public void destroy() {}

}
