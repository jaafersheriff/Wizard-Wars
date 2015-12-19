package games.jsheriff.wizardwars.GameState;

import games.jsheriff.wizardwars.Entity.Player;

/**
 * Created by jaafe on 12/16/2015.
 */
public abstract class GameState
{
    protected GameStateManager gsm;

    public abstract void init();
    public abstract void update();
    public abstract void setPlayer(Player p);
    public abstract void draw();
    public abstract void select(float Px, float Py);
    public abstract void deselect();
}
