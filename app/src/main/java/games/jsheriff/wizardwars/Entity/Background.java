package games.jsheriff.wizardwars.Entity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import games.jsheriff.wizardwars.GameView;

/**
 * Created by jaafe on 12/24/2015.
 */
public class Background
{
    private GameView gv;
    private Bitmap image;

    private double x, y, dx, dy;
    private double movescale;

    public Background(GameView gv, int source, int ms)
    {
        this.gv = gv;
        this.movescale = ms;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        image = BitmapFactory.decodeResource(gv.getResources(), source, options);
    }

    public void setPosition(double x, double y)
    {
        this.x = (x*movescale) % gv.getWidth();
        this.y = (y*movescale) % gv.getHeight();
    }

    public void setVector(double dx, double dy)
    {
        this.dx = dx; this.dy = dy;
    }

    public void update()
    {
        x += dx * 0.2; y += dy;
    }

    public void draw()
    {
        Rect src = new Rect(0, 0, image.getWidth(), image.getHeight());
        Rect dst = new Rect((int)x, 0, (int)x + gv.getWidth(), gv.getHeight());
        gv.canvas.drawBitmap(image, src, dst, gv.paint);

        if (x < 0)
        {
            dst = new Rect((int) x+ gv.getWidth(), 0, (int)x+gv.getWidth()+gv.getHeight(), (int)y);
            gv.canvas.drawBitmap(image, src, dst, gv.paint);
        }
        if (x > 0)
        {
            dst = new Rect((int) x-gv.getWidth(), 0, (int) x, (int)y);
            gv.canvas.drawBitmap(image, src, dst, gv.paint);
        }
    }

}
