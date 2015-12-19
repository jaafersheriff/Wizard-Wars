package games.jsheriff.wizardwars.GameState;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import games.jsheriff.wizardwars.Entity.Player;
import games.jsheriff.wizardwars.GameView;
import games.jsheriff.wizardwars.R;

public class BattleState extends GameState{

    Bitmap[] BG;
    int currFrame = 0;
    long currFrameTime;
    Player player;

    GameView gv;
    Canvas canvas;
    Paint paint;

    //HUD
    Rect[] controlBounds = new Rect[9];
    Bitmap[] controlBits = new Bitmap[9];
    String[] controlString = {"TL", "UP", "TR", "LEFT", "", "RIGHT", "BL", "DOWN", "BR"};

    public BattleState(GameStateManager gsm)
    {
        this.gsm = gsm;
        this.gv = gsm.gv;
        init();
    }

    @Override
    public void init()
    {
        currFrameTime = System.currentTimeMillis();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        BG = new Bitmap[]{
                BitmapFactory.decodeResource(gv.getResources(), R.drawable.menubg, options),
                BitmapFactory.decodeResource(gv.getResources(), R.drawable.menubg1, options),
                BitmapFactory.decodeResource(gv.getResources(), R.drawable.menubg2, options),
                BitmapFactory.decodeResource(gv.getResources(), R.drawable.menubg3, options) };
        player = new Player(gv);
        player.setPosition(100, 100);
        createBounds();
        //createBitmaps();
    }

    public void update()
    {
        player.update();

        long nextFrameTime = System.currentTimeMillis();
        if(nextFrameTime - currFrameTime >= 100)
        {
            currFrame = ++currFrame % 4;
            currFrameTime = nextFrameTime;
        }
    }

    @Override
    public void setPlayer(Player p) {

    }

    public void draw()
    {
        canvas = gv.canvas; paint = gv.paint;
        //draw BG
        Rect bgBounds = new Rect(0, 0, BG[currFrame].getWidth(), BG[currFrame].getHeight());
        Rect gvBounds = new Rect(0, 0, gv.getWidth(), gv.getHeight());
        gv.canvas.drawBitmap(BG[currFrame], bgBounds, gvBounds, gv.paint);

        //hud
        paint.setColor(Color.WHITE);
        paint.setAlpha(55);
        canvas.drawRect(0, gv.getHeight()/3*2, gv.getWidth(), gv.getHeight(), paint);
        paint.setAlpha(255);

        //draw controls
        int[] cols = {Color.WHITE, Color.BLUE, Color.CYAN, Color.GRAY, Color.GREEN, Color.MAGENTA, Color.YELLOW, Color.RED, Color.LTGRAY};
        for(int i = 0; i < controlBounds.length; i++)
        {
            if(i == 4 || controlBounds[i] == null) continue;
            paint.setColor(cols[i]);
            canvas.drawRect(controlBounds[i], paint);
            //Rect ctSrc = new Rect(0, 0, controlBits[i].getWidth(), controlBits[i].getHeight());
            //canvas.drawBitmap(controlBits[i], ctSrc, controlBounds[i], paint);
            paint.setColor(Color.BLACK);
            canvas.drawText(controlString[i], controlBounds[i].left, controlBounds[i].bottom, paint);
        }
        //draw player
        player.draw(gsm.gv);

        paint.setColor(Color.argb(255, 249, 129, 0));
        paint.setTextSize(45);
        canvas.drawText("Wx: " + player.getx(), 20, 200, paint);
        canvas.drawText("Wy: " + player.gety(), 20, 250, paint);
    }

    @Override
    public void select(float Px, float Py)
    {
        for(int i = 0; i < controlBounds.length; i++)
        {
            if(i == 4) continue;
            if(controlBounds[i].contains((int)Px, (int) Py))
            {
                if(i == 0)
                {
                    player.setUp(true); player.setLeft(true);
                    player.setTL();
                }
                if(i == 1) player.setUp(true);
                if(i == 2)
                {
                    player.setUp(true); player.setRight(true);
                    player.setTR();
                }
                if(i == 3) player.setLeft(true);
                if(i == 5) player.setRight(true);
                if(i == 6)
                {
                    player.setDown(true); player.setLeft(true);
                    player.setBL();
                }
                if(i == 7)
                {
                    player.setDown(true);
                }
                if(i == 8)
                {
                    player.setDown(true); player.setRight(true);
                    player.setBR();
                }
            }
        }
    }

    public void deselect()
    {
        player.setLeft(false);
        player.setRight(false);
        player.setUp(false);
        player.setDown(false);
        player.setBL(); player.setBR();
        player.setTL(); player.setTR();
    }


    private void createBounds()
    {
        int leftB = gv.getWidth()/2;
        int topB = gv.getHeight()/3*2;
        int bSize = 150;
        for(int i = 0; i < controlBounds.length; i++)
        {
            if(i == 4)
            {
                controlBounds[i] = null;
                continue;
            }
            int row = 0;
            int col = 0;
            controlBounds[i] = new Rect(
                    leftB + (i%3)*bSize,
                    topB + (i/3)*bSize,
                    leftB + (i%3+1)*bSize,
                    topB + (i/3+1)*bSize);
        }
    }

    private void createBitmaps()
    {
        controlBits[0] = BitmapFactory.decodeResource(gsm.gv.getResources(), R.drawable.tl);
        controlBits[1] = BitmapFactory.decodeResource(gsm.gv.getResources(), R.drawable.up);
        controlBits[2] = BitmapFactory.decodeResource(gsm.gv.getResources(), R.drawable.tr);
        controlBits[3] = BitmapFactory.decodeResource(gsm.gv.getResources(), R.drawable.left);
        controlBits[4] = null;
        controlBits[5] = BitmapFactory.decodeResource(gsm.gv.getResources(), R.drawable.right);
        controlBits[6] = BitmapFactory.decodeResource(gsm.gv.getResources(), R.drawable.bl);
        controlBits[7] = BitmapFactory.decodeResource(gsm.gv.getResources(), R.drawable.down);
        controlBits[8] = BitmapFactory.decodeResource(gsm.gv.getResources(), R.drawable.br);
    }

}
