package com.worldclass.utils;

import android.graphics.Canvas;

import com.worldclass.views.MyGLSurfaceView;

/**
 * Created by erz on 2/20/14.
 */
public class GameLoop extends Thread {
    static final long FPS = 60;
    private MyGLSurfaceView myGLSurfaceView;
    private boolean running = false;

    public GameLoop(MyGLSurfaceView myGLSurfaceView){
        this.myGLSurfaceView = myGLSurfaceView;
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
                //c = myGLSurfaceView.getHolder().lockCanvas();
                synchronized (myGLSurfaceView.getHolder()) {
                    //myGLSurfaceView.onDraw(c);
                    myGLSurfaceView.update();
                }
            } finally {
                if (c != null) {
                    myGLSurfaceView.getHolder().unlockCanvasAndPost(c);
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
