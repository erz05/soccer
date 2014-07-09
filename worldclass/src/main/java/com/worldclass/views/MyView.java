package com.worldclass.views;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.worldclass.utils.LoopThread;

/**
 * Created by erz on 3/20/14.
 */
public abstract class MyView extends SurfaceView implements SurfaceHolder.Callback {

    private LoopThread loopThread;

    public MyView(Context context) {
        super(context);
        loopThread = new LoopThread(this);
        SurfaceHolder holder = getHolder();
        if(holder != null)
            holder.addCallback(this);
    }

    public abstract void createObjects();

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        createObjects();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if(loopThread != null){
            boolean retry = true;
            loopThread.setRunning(false);
            while (retry) {
                try {
                    loopThread.join();
                    retry = false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void start(){
        if(loopThread == null){
            loopThread = new LoopThread(this);
        }
        if(!loopThread.isRunning()){
            loopThread.setRunning(true);
            loopThread.start();
        }
    }

    public void pause() {
        if(loopThread != null && loopThread.isRunning()){
            loopThread.setRunning(false);
            boolean retry = true;
            while (retry) {
                try {
                    loopThread.join();
                    retry = false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            loopThread = null;
        }
    }

    public void resume(){
        if(loopThread == null){
            loopThread = new LoopThread(this);
        }else {
            loopThread = null;
            loopThread = new LoopThread(this);
        }
        if(!loopThread.isRunning()){
            loopThread.setRunning(true);
            loopThread.start();
        }
    }
}
