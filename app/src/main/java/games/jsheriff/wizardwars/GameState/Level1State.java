package games.jsheriff.wizardwars.GameState;

import java.util.Random;

import games.jsheriff.wizardwars.Entity.Button;
import games.jsheriff.wizardwars.Entity.Enemies.Enemy;
import games.jsheriff.wizardwars.Entity.Enemies.Ninja;

/**
 * Created by jaafe on 12/17/2015.
 */
public class Level1State extends BattleState{

    //enemies
    private static long spawnStartTimer;
    private final int SPAWNTIME = 450;
    private final int MAXENEMIES = 10;
    boolean generateEnemy;

    //boss
    private final int BOSSKILLCOUNT = 30;

    public Level1State(GameStateManager gsm)
    {
        super(gsm);
    }

    public void init()
    {
        super.init();

        spawnStartTimer = System.currentTimeMillis();
        generateEnemy = gv.getSettings().getSetting(SettingsState.ENEMY);
    }

    public void update()
    {
        super.update();
        if(paused) return;

        if(player.getLives() <= 0) generateEnemy = false;

        //create enemies
        long elapsed = System.currentTimeMillis() - spawnStartTimer;
        if(elapsed >= SPAWNTIME && enemies.size() < MAXENEMIES && generateEnemy)
        {
            spawnStartTimer = System.currentTimeMillis();
            addEnemy();
        }

        //boss
        if(player.getKillCount() % BOSSKILLCOUNT == 0 && player.getKillCount() > 0) {
            generateEnemy = false;
            for (Enemy e : enemies)
                e.setDead(true);
        }
        if(explosions.size() == 0 && !generateEnemy && player.getKillCount() % BOSSKILLCOUNT > 0) {
            player.deselect();
            gsm.setState(GameStateManager.BOSSSTATE);
            gsm.getState(GameStateManager.BOSSSTATE).setPlayer(player);
        }
    }

    public void draw()
    {
        super.draw();
    }

    public void addEnemy()
    {
        Ninja w = new Ninja(gv, player);
        w.setPosition(generateEnemyX(w), generateEnemyY(w));
        enemies.add(w);
    }
}
