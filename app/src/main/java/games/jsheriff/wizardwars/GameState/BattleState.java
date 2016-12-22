package games.jsheriff.wizardwars.GameState;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.MediaPlayer;

import java.util.ArrayList;
import java.util.Random;

import games.jsheriff.wizardwars.Entity.Background;
import games.jsheriff.wizardwars.Entity.Enemies.BossFBExplosion;
import games.jsheriff.wizardwars.Entity.Enemies.BossFireBall;
import games.jsheriff.wizardwars.Entity.Enemies.Enemy;
import games.jsheriff.wizardwars.Entity.Enemies.Ninja;
import games.jsheriff.wizardwars.Entity.Explosion;
import games.jsheriff.wizardwars.Entity.Enemies.NinjaExplosion;
import games.jsheriff.wizardwars.Entity.HUD;
import games.jsheriff.wizardwars.Entity.Player;
import games.jsheriff.wizardwars.GameView;
import games.jsheriff.wizardwars.R;

public class BattleState extends GameState{

    public boolean paused;
    private Bitmap pausedScreen;
    BitmapFactory.Options options;

    //entites
    Player player;
    public ArrayList<Enemy> enemies;
    public ArrayList<Explosion> explosions;

    GameView gv;
    boolean oneUpbool;
    public static final int WALL = 100;
    //HUD
    HUD hud;

    public BattleState(GameStateManager gsm)
    {
        this.gsm = gsm;
        this.gv = gsm.gv;

        init();
    }

    @Override
    public void init()
    {
        isinit = false;

        //Set player
        player = new Player(gv);
        player.setPosition(gv.getWidth() / 2 - player.getWidth() / 2, gv.getHeight() / 2 - player.getHeight() / 2);

        //enemies
        enemies = new ArrayList<Enemy>();

        //explosions
        explosions = new ArrayList<Explosion>();

        //HUD
        hud = new HUD(this, gv, player);

        paused = false;
        options = new BitmapFactory.Options();
        options.inScaled = false;
        pausedScreen = BitmapFactory.decodeResource(gv.getResources(), R.drawable.battlebg, options);
        oneUpbool = true;

        isinit = true;
    }

    public void update()
    {
        if(paused) return;

        //update player
        if(player == null) return;
        player.update();
        if(player.getLives() == 0) eventDead();

        //attack enemies
        player.checkAttack(enemies);

        //update enemies
        for(int i = 0; i < enemies.size(); i++)
        {
            Enemy e = enemies.get(i);
            e.update();
            if(e.isDead())
            {
                enemies.remove(i);
                i--;
                //one up
                if(e instanceof BossFireBall)
                    explosions.add(new BossFBExplosion(gv, e.getx(), e.gety()));
                else {
                    explosions.add(new NinjaExplosion(gv, e.getx(), e.gety()));
                    player.plusKillCount();

                    if(player.getKillCount() > 0 &&
                            player.getKillCount() % Player.ONEUPCOUNT == 0 &&
                            player.getLives() < Player.MAXLIVES &&
                            oneUpbool) {
                        oneUpbool = false;
                        player.plusLives();
                    }
                    else if (player.getKillCount() % Player.ONEUPCOUNT != 0)
                        oneUpbool = true;
                }
            }
        }

        //update explosions
        for(int i = 0; i < explosions.size(); i++)
        {
            explosions.get(i).update();
            if(explosions.get(i).shouldRemove())
            {
                explosions.remove(i);
                i--;
            }
        }
    }

    private void eventDead()
    {
        for(Enemy e : enemies) e.setDead(true);

        //player.setframes(sprites.get(DEAD));
        if(explosions.size() == 0) //&& player.animation.hasplayedonce
        {
            gv.updateStats(1, player.getKillCount(), 0, 0);
            gsm.setState(GameStateManager.DEATHSTATE);
            gsm.getState(GameStateManager.DEATHSTATE).setPlayer(this.player);
        }
    }

    @Override
    public void setPlayer(Player p)
    {
        this.player = p;
        hud = new HUD(this, gv, player);
    }

    public void draw()
    {
        if(paused)
        {
            Rect BGsrc = new Rect(0, 0, pausedScreen.getWidth(), pausedScreen.getHeight());
            Rect BGdst = new Rect(0, 0, gv.getWidth(), gv.getHeight());
            gv.canvas.drawBitmap(pausedScreen, BGsrc, BGdst, gv.paint);
            gv.paint.setTextSize(100);
            gv.paint.setColor(Color.WHITE);
            gv.paint.setTypeface(Typeface.createFromAsset(gv.getCx().getAssets(), "Minecraft.ttf"));
            gv.canvas.drawText("PAUSED", gv.getWidth()/2-150, gv.getHeight()/2, gv.paint);
            return;
        }
        //draw player
        player.draw(paused);

        //draw enemies
        for(int i = 0; i < enemies.size(); i++) enemies.get(i).draw();

        //draw explosions
        for(int i = 0; i < explosions.size(); i++) explosions.get(i).draw();

        //hud
        hud.draw();


    }

    @Override
    public void select(int Px, int Py)
    {
        hud.select(Px, Py);
    }

    @Override
    public void hold(int Px, int Py) {
        int playerLeft = player.getx() - player.getCWidth() / 2;
        int playerTop = player.gety() - player.getCHeight() / 2;
        int playerRight = player.getx() + player.getCWidth() / 2;
        int playerBottom = player.gety() + player.getCHeight() / 2;

        deselect();
        if (Px > 0 && Px < playerLeft && Py > 0 && Py < playerTop)
            player.setTL(true);
        else if (Px > playerLeft && Px < playerRight && Py > 0 && Py < playerTop)
            player.setUp(true);
        else if (Px > playerRight && Px < gv.getWidth() && Py > 0 && Py < playerTop)
            player.setTR(true);
        else if (Px > 0 && Px < playerLeft && Py > playerTop && Py < playerBottom)
            player.setLeft(true);
        else if (Px > playerRight && Px < gv.getWidth() && Py > playerTop && Py < playerBottom)
            player.setRight(true);
        else if (Px > 0 && Px < playerLeft && Py > playerBottom && Py < gv.getHeight())
            player.setBL(true);
        else if (Px > playerLeft && Px < playerRight && Py > playerBottom && Py < gv.getHeight())
            player.setDown(true);
        else if (Px > playerRight && Px < gv.getWidth() && Py > playerBottom && Py < gv.getHeight())
            player.setBR(true);
    }

    public void deselect()
    {
        player.deselect();
    }

    public void back()
    {
        gv.updateStats(1, player.getKillCount(), 0, 0);
        gsm.setState(GameStateManager.MENUSTATE);
    }

    public boolean isPaused() { return paused; }
    public void setPaused(boolean b) { paused = b; }

    public int generateEnemyX(Enemy e)
    {
        Random r = new Random();
        int x = r.nextInt(gv.getWidth() - e.getWidth() + 1) + e.getWidth();
        if(x > player.getx() - player.getWidth() && x < player.getx() + player.getWidth()) x = generateEnemyX(e);
        else if (x < WALL || x > gv.getWidth() - WALL) x = generateEnemyX(e);
        return x;
    }

    public int generateEnemyY(Enemy e)
    {
        Random r = new Random();
        int y = r.nextInt(gv.getHeight() - e.getHeight() + 1) + e.getHeight();
        if(y > player.gety() - player.getHeight() && y < player.gety() + player.getHeight()) y = generateEnemyY(e);
        else if (y < WALL || y > gv.getHeight() - WALL) y = generateEnemyY(e);
        return y;
    }

    public boolean isInit() { return isinit; }
}
