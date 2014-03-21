package com.worldclass.utils;

import android.graphics.Canvas;

import com.worldclass.views.MyView;

/**
 * Created by erz on 3/20/14.
 */
public class LoopThread extends Thread {
    static final long FPS = 60;
    private boolean running = false;
    private MyView myView;

    public LoopThread(MyView myView){
        this.myView = myView;
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
                c = myView.getHolder().lockCanvas();
                synchronized (myView.getHolder()) {
                    myView.onDraw(c);
                }
            } finally {
                if (c != null) {
                    myView.getHolder().unlockCanvasAndPost(c);
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
