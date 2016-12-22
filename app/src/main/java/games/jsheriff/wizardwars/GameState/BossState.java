package games.jsheriff.wizardwars.GameState;

import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;

import java.util.ArrayList;

import games.jsheriff.wizardwars.Entity.Enemies.Boss;
import games.jsheriff.wizardwars.Entity.Enemies.BossFBExplosion;
import games.jsheriff.wizardwars.Entity.Enemies.BossFireBall;
import games.jsheriff.wizardwars.Entity.Enemies.Enemy;
import games.jsheriff.wizardwars.Entity.HUD;
import games.jsheriff.wizardwars.Entity.Player;

public class BossState extends BattleState{

    private Boss boss;

    private static long fireTimer;
    private final int FIRETIMER = 600;

    public ArrayList<BossFireBall> BossFB;
    public ArrayList<BossFBExplosion> BossFBE;

    public BossState(GameStateManager gsm)
    {
        super(gsm);
    }

    public void init()
    {
        isinit = false;
        super.init();

        fireTimer = System.currentTimeMillis();
        gv.updateStats(0, 0, 1, 0);

        enemies = new ArrayList<Enemy>();
        BossFB = new ArrayList<BossFireBall>();
        BossFBE = new ArrayList<BossFBExplosion>();

        isinit = true;
    }

    @Override
    public void setPlayer(Player p)
    {
        this.player = p;
        boss = new Boss(gv, player);
        boss.setPosition(generateEnemyX(boss), generateEnemyY(boss));
        Log.d("Boss created", boss.getx() + ", " + boss.gety());
        enemies.add(boss);

        hud = new HUD(this, gv, player);
    }

    public void update()
    {
        super.update();
        if(paused) return;

        //shoot fireball
        long elapsed = System.currentTimeMillis() - fireTimer;
        if(elapsed >= FIRETIMER && !boss.isDead())
        {
            fireTimer = System.currentTimeMillis();
            BossFireBall bfb = new BossFireBall(gv, player, boss, boss.getx(), boss.gety());
            enemies.add(bfb);
        }

        if(boss.isDead() && explosions.size() == 0)
        {
            player.plusKillCount();
            gv.updateStats(0, 0, 0, 1);
            gsm.setState(GameStateManager.LEVEL1STATE);
            gsm.getState(GameStateManager.LEVEL1STATE).setPlayer(player);
        }
    }

    public void draw()
    {
        //draw boss health
        Rect bar = new Rect(50, 130, 50 + (int) ((gv.getWidth()- 100)*((double)boss.getHealth()/boss.getMaxHealth())), 180);
        gv.paint.setColor(Color.RED);;
        gv.canvas.drawRect(bar, gv.paint);
        gv.paint.setColor(Color.WHITE);
        gv.paint.setTextSize(45);
        gv.paint.setTypeface(Typeface.createFromAsset(gv.getCx().getAssets(), "Minecraft.ttf"));
        gv.canvas.drawText("" + boss.getHealth(), 50 + (int) ((gv.getWidth()-100)*((double)boss.getHealth()/boss.getMaxHealth()))/2, 175, gv.paint);


        super.draw();

    }
}
