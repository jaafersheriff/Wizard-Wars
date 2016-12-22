package games.jsheriff.wizardwars.Entity.Enemies;

import games.jsheriff.wizardwars.Entity.MapObject;
import games.jsheriff.wizardwars.Entity.Player;
import games.jsheriff.wizardwars.GameView;

/**
 * Created by jaafe on 12/24/2015.
 */
public class Enemy extends MapObject{

    protected int health, maxhealth;
    protected boolean dead;
    protected int damage;

    protected Player p;
    public GameView gv;

    public Enemy(GameView gv)
    {
        super(gv);
    }

    public Enemy(GameView gv, Player p)
    {
        super(gv);
        this.gv = gv;
        this.p = p;
    }

    public boolean isDead() { return dead; }
    public int getDamage() { return damage;}

    public void setDead(boolean b) { dead = b; }

    public void update() {}

    public void hit(int damage)
    {
        if (isDead()) return;
        health -= damage;
        if(health <= 0)
        {
            health = 0;
            dead = true;

        }
    }
}
