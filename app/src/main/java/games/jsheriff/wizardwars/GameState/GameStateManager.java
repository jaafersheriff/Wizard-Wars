package games.jsheriff.wizardwars.GameState;

import android.util.Log;

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

    public static final int SETTINGSSTATE = 0;
    public static final int MENUSTATE = 1;
    public static final int STATSSTATE = 2;
    public static final int LEVEL1STATE = 3;
    public static final int BOSSSTATE = 4;
    public static final int LEVEL2STATE = 5;
    public static final int DEATHSTATE = 6;

    public GameStateManager(GameView gv)
    {
        gameStates = new ArrayList<GameState>();
        this.gv = gv;
        gameStates.add(new SettingsState(this));
        Log.d("Settings created", gameStates.get(SETTINGSSTATE).toString());
    }

    public void init()
    {
        Log.d("Creating gameStates", "...");
        gameStates.add(new MainMenuState(this));
        Log.d("Menu created", gameStates.get(MENUSTATE).toString());
        gameStates.add(new StatsState(this));
        Log.d("Stats created", gameStates.get(STATSSTATE).toString());
        gameStates.add(new Level1State(this));
        Log.d("Level 1 created", gameStates.get(LEVEL1STATE).toString());
        gameStates.add(new BossState(this));
        Log.d("Boss State created", gameStates.get(BOSSSTATE).toString());
        gameStates.add(null);
        //level2 state
        gameStates.add(new DeathState(this));
        Log.d("Death state created", gameStates.get(DEATHSTATE).toString());

        currState = MENUSTATE;
        gameStates.get(currState).init();
    }

    public void setState(int state)
    {
        currState = state;
        gameStates.get(currState).init();
        Log.d("Set State", gameStates.get(currState).toString());
    }

    public GameState getState(int state)
    {
        return gameStates.get(state);
    }

    public GameState getCurrState() { return gameStates.get(currState); }

    public void update()
    {
        if(gameStates.get(currState).isInit())
            gameStates.get(currState).update();
    }

    public void draw()
    {
        if(gameStates.get(currState).isInit())
            gameStates.get(currState).draw();
    }

    public void select(int Px, int Py) { gameStates.get(currState).select(Px, Py); }

    public void hold(int Px, int Py) { gameStates.get(currState).hold(Px, Py); }

    public void deselect() { gameStates.get(currState).deselect(); }

    public void back() { gameStates.get(currState).back(); }
}
