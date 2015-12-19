package games.jsheriff.wizardwars;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


import games.jsheriff.wizardwars.GameState.GameStateManager;

public class GameView extends SurfaceView implements Runnable {
    //Game stuff
    Thread gameThread = null;
    volatile boolean isRunning;
    GameStateManager gsm;

    //draw stuff
    SurfaceHolder ourHolder;
    public Canvas canvas;
    public Paint paint;

    //Fps stuff
    long fps;
    private long timeThisFrame;

    //user stuff
    public float Px = 0;
    public float Py = 0;

    Context cx;

    public GameView(Context context) {
        super(context);
        cx = context;
        ourHolder = getHolder();
        paint = new Paint();
    }

    private void init()
    {
        isRunning = true;
        gsm = new GameStateManager(this);
    }

    //Game engine
    @Override
    public void run() {

        init();

        while (isRunning) {
            long startTime = System.currentTimeMillis();

            update();
            draw();

            //fps
            timeThisFrame = System.currentTimeMillis() - startTime;
            if (timeThisFrame > 0) fps = 1000 / timeThisFrame;
        }
    }


    //Update virtual world
    public void update()
    {
        gsm.update();
    }

    //Draw virtual world
    public void draw() {
        //make sure our surface is valid
        if (!ourHolder.getSurface().isValid()) return;
        //Lock canvas to draw, make drawing surface our canvas
        canvas = ourHolder.lockCanvas();

        //Draw background
        canvas.drawColor(Color.argb(255, 26, 128, 182));

        gsm.draw();

        //Text
        paint.setColor(Color.argb(255, 249, 129, 0));
        paint.setTextSize(45);
        canvas.drawText("FPS: " + fps, 20, 50, paint);
        canvas.drawText("X: " + String.format("%.2f", Px), 20, 100, paint);
        canvas.drawText("Y: " + String.format("%.2f", Py), 20, 150, paint);

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
        //save?
    }

    public void resume() {
        isRunning = true;
        gameThread = new Thread(this);
        gameThread.start();

    }

    //TouchEvents overriden from surfaceview
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            //user touched the screen
            case MotionEvent.ACTION_MOVE:
                Px = motionEvent.getX();
                Py = motionEvent.getY();
                gsm.select(Px, Py);
                break;

            //user released the screen
            case MotionEvent.ACTION_UP:
                gsm.deselect();
                break;
        }
        return true;
    }
}