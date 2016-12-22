package games.jsheriff.wizardwars;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import games.jsheriff.wizardwars.Entity.Player;
import games.jsheriff.wizardwars.GameState.GameStateManager;
import games.jsheriff.wizardwars.GameState.SettingsState;

public class GameView extends SurfaceView implements Runnable {

    //Game stuff
    Thread gameThread = null;
    volatile boolean isRunning;
    GameStateManager gsm;
    File statsFile;
    public static final String STATSFILE = "stats.txt";
    static Context cx;

    //draw stuff
    SurfaceHolder ourHolder;
    public Canvas canvas;
    public Paint paint;
    Sprites sprites;
    Typeface defaultfont;

    //Fps stuff
    ArrayList<Long> points;
    long fps;
    private long timeThisFrame;

    //user stuff
    public float Px = 0;
    public float Py = 0;

    SettingsState settings;

    Sounds sounds;

    public GameView(Context context) {
        super(context);
        cx = context;
        init();
    }

    private void init()
    {
        ourHolder = getHolder();
        paint = new Paint();
        defaultfont = paint.getTypeface();

        isRunning = true;

        points = new ArrayList<Long>();

        sprites = new Sprites(this);

        createFile();

        gsm = new GameStateManager(this);
        settings = (SettingsState) gsm.getState(GameStateManager.SETTINGSSTATE);
        gsm.init();

        sounds = new Sounds(this);
        //sounds.playSound(Sounds.BG, 1, -1);
        Log.d("Init finished", "GameView");
    }

    //Game engine
    @Override
    public void run() {

        while (isRunning) {
            long startTime = System.currentTimeMillis();

            update();
            draw();
            //fps
            timeThisFrame = System.currentTimeMillis() - startTime;
            if (timeThisFrame > 0)
            {
                fps = 1000 / timeThisFrame;
            }
        }

    }

    //Update virtual world
    public void update()
    {
        gsm.update();

        //play bg?
    }

    //Draw virtual world
    public void draw() {
        //make sure our surface is valid
        if (!ourHolder.getSurface().isValid()) return;

        //Lock canvas to draw, make drawing surface our canvas
        canvas = ourHolder.lockCanvas();

        //Draw background
        canvas.drawColor(Color.argb(255, 102, 128, 102));

        //draw game
        gsm.draw();

        //Text
        boolean showFps;
        if(this.getSettings() == null) showFps = false;
        else showFps = this.getSettings().getSetting(SettingsState.FPS);
        if(showFps) {
            points.add(fps);
            if (points.size() > 100) points.remove(0);

            int highcount = 0, lowcount = 0, medcount = 0;
            for (int i = 0; i < points.size(); i++)
            {
                if(points.get(i) >= 55)
                    highcount++;
                else if (points.get(i) < 55 && points.get(i) >= 30)
                    medcount++;
                else
                    lowcount++;
            }
            paint.setColor(Color.GREEN);
            canvas.drawRect(this.getWidth()-80-highcount*2, 5, this.getWidth()-80, 23, paint);

            paint.setColor(Color.YELLOW);
            canvas.drawRect(this.getWidth()-80-medcount*2, 23, this.getWidth()-80, 41, paint);

            paint.setColor(Color.RED);
            canvas.drawRect(this.getWidth()-80-lowcount*2, 41, this.getWidth()-80, 60, paint);

            paint.setTextSize(55);
            if (fps >= 55)
                paint.setColor(Color.GREEN);
            if (fps < 55 && fps >= 30)
                paint.setColor(Color.YELLOW);
            if (fps < 30)
                paint.setColor(Color.RED);
            paint.setTypeface(defaultfont);
            canvas.drawText(""+fps, this.getWidth() - 75, 55, paint);
        }
        else if (points.size() != 0) points.clear();

        //draw everything to the screen
        ourHolder.unlockCanvasAndPost(canvas);
    }


    //if game engine is paused/stopped
    public void pause() {
        isRunning = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error: ", "joining thread");
        }
    }

    public void resume() {
        isRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void destroy()
    {
        sprites.destroy();
        sounds.destroy();
    }

    //TouchEvents overriden from surfaceview
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                Px = motionEvent.getX();
                Py = motionEvent.getY();
                gsm.select((int)Px, (int)Py);
                break;
            //user touched the screen
            case MotionEvent.ACTION_MOVE:
                Px = motionEvent.getX();
                Py = motionEvent.getY();
                gsm.hold((int)Px, (int)Py);
                break;
            //user released the screen
            case MotionEvent.ACTION_UP:
                gsm.deselect();
                break;
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(event.getAction() == KeyEvent.ACTION_DOWN)
        {
            if(keyCode == KeyEvent.KEYCODE_BACK) gsm.back();
        }
        return true;
    }

    private void createFile()
    {
        if(!isExternalStorageReadable() || !isExternalStorageWritable())
            return;

        File stats = new File(getCx().getExternalFilesDir(null), STATSFILE);
        Log.d("Stats found", stats.getAbsolutePath());
        if(stats.exists()) {
            return;
        }

        try{
            OutputStream writer = new FileOutputStream(stats);
            writer.write("0\n0\n0\n0\n0\n0\n".getBytes());
            writer.close();
            Log.d("Stats file", stats.getAbsolutePath());
        }
        catch(IOException e){
            Log.d("Stats file", "failed to create");
        }
    }

    public void updateStats(int newGame, int killCount, int bossFight, int bossWin)
    {
        if(!isExternalStorageReadable() || !isExternalStorageWritable())
            return;

        ArrayList<String> data = getFile();
        data.set(0, (Integer.parseInt(data.get(0)) + newGame) + "\n");    //Total Games
        data.set(1, (Integer.parseInt(data.get(1)) + killCount) + "\n"); //Total Score
        data.set(2, String.format("%.1f\n", Double.parseDouble(data.get(1)) / Double.parseDouble(data.get(0)))); //Avg Score
        data.set(3, (Integer.parseInt(data.get(3)) + bossFight) +"\n"); //Boss fights
        data.set(4, (Integer.parseInt(data.get(4)) + bossWin) + "\n"); //Boss wins
        int oldHighScore = Integer.parseInt(data.get(5));   //Highscore
        if(killCount > oldHighScore)
            data.set(5, killCount + "\n");
        else data.set(5, oldHighScore + "\n");


        File stats = new File(getCx().getExternalFilesDir(null), STATSFILE);
        try{
            OutputStream writer = new FileOutputStream(stats);
            for(int i = 0; i < data.size(); i++)
                writer.write(data.get(i).getBytes());
            writer.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public static ArrayList<String> getFile()
    {
        ArrayList<String> data = new ArrayList<String>();
        File stats = new File(getCx().getExternalFilesDir(null), STATSFILE);
        try
        {
            Log.d("Attempting to read file", stats.getAbsolutePath());
            InputStream is = new FileInputStream(stats);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = "";
            while((line = br.readLine()) != null)
                data.add(line);
            Log.d("Stats retrieved", "");
        }
        catch(FileNotFoundException e) {
            Log.d("Stats file found", stats.getAbsolutePath());
        }
        catch (IOException e) {
            Log.d("Stats file", "failed to read");
        }
        return data;
    }

    public static Context getCx(){ return cx; }
    public File getStats() { return statsFile; }
    public Sprites getSprites(){ return sprites; }
    public SettingsState getSettings() { return  settings; }
    public Sounds getSounds() { return sounds; }


    public boolean isExternalStorageWritable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return(state.equals(Environment.MEDIA_MOUNTED) ||
                state.equals(Environment.MEDIA_MOUNTED_READ_ONLY));
    }
}