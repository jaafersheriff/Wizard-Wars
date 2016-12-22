package games.jsheriff.wizardwars.GameState;

import android.graphics.Color;
import android.graphics.Typeface;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import games.jsheriff.wizardwars.Entity.Background;
import games.jsheriff.wizardwars.Entity.Player;
import games.jsheriff.wizardwars.R;

public class StatsState extends GameState{

    Background bg;

    ArrayList<String> labels;
    ArrayList<String> scores;

    public StatsState(GameStateManager gsm)
    {
        this.gsm = gsm;
    }

    @Override
    public void init()
    {
        isinit = false;

        bg = new Background(gsm.gv, R.drawable.menubg10, 2);

        scores = new ArrayList<String>();
        scores = gsm.gv.getFile();

        labels = new ArrayList<String>();
        labels.add("Total Games:");
        labels.add("Total Score:");
        labels.add("Average Score:");
        labels.add("Boss Fights:");
        labels.add("Boss Wins:");
        labels.add("Highest Score:");

        isinit = true;
    }

    @Override
    public void update()
    {
        bg.update();
    }

    @Override
    public void setPlayer(Player p) {

    }

    @Override
    public void draw()
    {
        gsm.gv.paint.setTypeface(Typeface.createFromAsset(gsm.gv.getCx().getAssets(), "Minecraft.ttf"));
        gsm.gv.paint.setColor(Color.BLACK);
        gsm.gv.paint.setTextSize(50);
        for(int i = 0; i < labels.size(); i++) {
            gsm.gv.paint.setColor(Color.BLACK);
            gsm.gv.canvas.drawText(labels.get(i), gsm.gv.getWidth() / 2 - 300, 300 + 100 * i, gsm.gv.paint);
            gsm.gv.paint.setColor(Color.WHITE);
            gsm.gv.canvas.drawText(labels.get(i), gsm.gv.getWidth() / 2 - 300, 300 + 100 * i - 7, gsm.gv.paint);
        }
        for(int i = 0; i < scores.size(); i++) {
            gsm.gv.paint.setColor(Color.BLACK);
            gsm.gv.canvas.drawText(scores.get(i), gsm.gv.getWidth() / 2 + 200, 300 + 100 * i, gsm.gv.paint);
            gsm.gv.paint.setColor(Color.WHITE);
            gsm.gv.canvas.drawText(scores.get(i), gsm.gv.getWidth() / 2 + 200, 300 + 100 * i - 7, gsm.gv.paint);
        }
    }

    @Override
    public void select(int Px, int Py) {
        gsm.setState(GameStateManager.MENUSTATE);
    }

    @Override
    public void hold(int Px, int Py)
    {

    }

    @Override
    public void deselect() {

    }

    @Override
    public void back() {

    }

    public boolean isInit() { return isinit; }
}
