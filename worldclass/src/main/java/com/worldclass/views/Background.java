package com.worldclass.views;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.worldclass.utils.BackgroundLoopThread;

/**
 * Created by erz on 2/22/14.
 */
public class Background extends SurfaceView {

    private SurfaceHolder holder;
    private BackgroundLoopThread backgroundLoopThread;

    public Background(Context context) {
        super(context);
        backgroundLoopThread = new BackgroundLoopThread(this);
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if(backgroundLoopThread != null){
                    boolean retry = true;
                    backgroundLoopThread.setRunning(false);
                    while (retry) {
                        try {
                            backgroundLoopThread.join();
                            retry = false;
                        } catch (InterruptedException e) {
                        }
                    }
                }
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if(backgroundLoopThread != null && !backgroundLoopThread.isRunning()){
                    backgroundLoopThread.setRunning(true);
                    backgroundLoopThread.start();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
            }
        });
    }

    @Override
    public void onDraw(Canvas canvas){
        if(canvas != null){

        }
    }
}
