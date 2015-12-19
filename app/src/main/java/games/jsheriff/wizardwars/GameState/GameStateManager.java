package games.jsheriff.wizardwars.GameState;

import java.util.ArrayList;

import games.jsheriff.wizardwars.GameView;

/**
 * Created by jaafe on 12/16/2015.
 */
public class GameStateManager
{
    public GameView gv;
    private ArrayList<GameState> gameStates;
    private int currState;

    public static final int MENUSTATE = 0;
    public static final int ABOUTSTATE = 1;
    public static final int LEVEL1STATE = 2;
    public static final int BOSS1STATE = 3;
    public static final int LEVEL2STATE = 4;

    public GameStateManager(GameView gv)
    {
        gameStates = new ArrayList<GameState>();
        this.gv = gv;
        init();
    }

    private void init()
    {
        gameStates.clear();

        currState = MENUSTATE;

        gameStates.add(new MainMenuState(this));
        gameStates.add(new AboutState(this));
        gameStates.add(new Level1State(this));
    }

    public void setState(int state)
    {
        currState = state;
        gameStates.get(currState).init();
    }

    public GameState getState(int state)
    {
        return gameStates.get(state);
    }

    public void update()
    {
        gameStates.get(currState).update();
    }

    public void draw()
    {
        gameStates.get(currState).draw();
    }

    public void select(float Px, float Py) { gameStates.get(currState).select(Px, Py); }

    public void deselect() { gameStates.get(currState).deselect(); }
}
