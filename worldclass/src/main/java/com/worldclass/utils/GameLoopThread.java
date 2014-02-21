package com.worldclass.utils;

import android.graphics.Canvas;

import com.worldclass.views.Game;

/**
 * Created by erz on 2/19/14.
 */
public class GameLoopThread extends Thread {
    static final long FPS = 60;
    private Game game;
    private boolean running = false;

    public GameLoopThread(Game game){
        this.game = game;
    }

    public void setRunning(boolean run){
        running = run;
    }

    public boolean isRunning(){
        return running;
    }

    @Override
    public void run() {
        long ticksPS = 1000 / FPS;
        long startTime;
        long sleepTime;
        while (running) {
            Canvas c = null;
            startTime = System.currentTimeMillis();
            try {
                c = game.getHolder().lockCanvas();
                synchronized (game.getHolder()) {
                    game.onDraw(c);
                }
            } finally {
                if (c != null) {
                    game.getHolder().unlockCanvasAndPost(c);
                }
            }
            sleepTime = ticksPS-(System.currentTimeMillis() - startTime);
            try {
                if (sleepTime > 0)
                    sleep(sleepTime);
                else
                    sleep(10);
            } catch (Exception e) {}
        }
    }
}
