package games.jsheriff.wizardwars;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.ArrayList;

import games.jsheriff.wizardwars.GameState.SettingsState;

/**
 * Created by jaafe on 1/24/2016.
 */
public class Sounds {

    GameView gv;
    SoundPool sp;
    ArrayList<Integer> sounds;

    public static final int BG = 0;
    public static final int SHUTTER = 1;
    public static final int PLAYERFB = 2;

    public Sounds(GameView gv)
    {
        this.gv = gv;
        sp = new SoundPool(100, AudioManager.STREAM_MUSIC, 0); //100 sounds at once
        sounds = new ArrayList<Integer>();

        init();
    }

    private void init()
    {
        sounds.add(sp.load(gv.getCx(), R.raw.bg, 1));
        sounds.add(sp.load(gv.getCx(), R.raw.shutter, 1));
        sounds.add(sp.load(gv.getCx(), R.raw.fb, 1));
    }

    public void playSound(int src, float volume, int loop)
    {
        sp.play(sounds.get(src), volume, volume, 1, loop, 1);
        //id, left vol, right, vol, priority, loop mode, playback rate
    }

    public void destroy()
    {
        sp.release();
    }
}
