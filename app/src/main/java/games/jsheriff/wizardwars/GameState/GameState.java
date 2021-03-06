package games.jsheriff.wizardwars.GameState;

import games.jsheriff.wizardwars.Entity.Player;

/**
 * Created by jaafe on 12/16/2015.
 */
public abstract class GameState
{
    protected GameStateManager gsm;
    protected boolean isinit = false;

    public abstract void init();
    public abstract boolean isInit();
    public abstract void update();
    public abstract void setPlayer(Player p);
    public abstract void draw();
    public abstract void select(int Px, int Py);
    public abstract void hold(int Px, int Py);
    public abstract void deselect();
    public abstract void back();
}
