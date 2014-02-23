package com.worldclass.utils;

import android.graphics.Canvas;

import com.worldclass.views.Background;

/**
 * Created by erz on 2/22/14.
 */
public class BackgroundLoopThread extends Thread {
    static final long FPS = 60;
    private Background background;
    private boolean running = false;

    public BackgroundLoopThread(Background background){
        this.background = background;
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
                c = background.getHolder().lockCanvas();
                synchronized (background.getHolder()) {
                    background.onDraw(c);
                }
            } finally {
                if (c != null) {
                    background.getHolder().unlockCanvasAndPost(c);
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
