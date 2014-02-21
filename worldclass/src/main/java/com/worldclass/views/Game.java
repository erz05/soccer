package com.worldclass.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.worldclass.R;
import com.worldclass.objects.Ball;
import com.worldclass.objects.Floor;
import com.worldclass.objects.Obsticles;
import com.worldclass.utils.GameLoopThread;

/**
 * Created by erz on 2/19/14.
 */
public class Game extends SurfaceView implements GestureDetector.OnGestureListener {

    private SurfaceHolder holder;
    private GameLoopThread gameLoopThread;
    private Ball ball;
    private Obsticles obsticles;
    private Floor floor;
    private GestureDetector detector;

    public Game(Context context) {
        super(context);
        setZOrderOnTop(true);
        getHolder().setFormat(PixelFormat.TRANSPARENT);
        gameLoopThread = new GameLoopThread(this);
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if(gameLoopThread != null){
                    boolean retry = true;
                    gameLoopThread.setRunning(false);
                    while (retry) {
                        try {
                            gameLoopThread.join();
                            retry = false;
                        } catch (InterruptedException e) {
                        }
                    }
                }
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                //create stuff here
                int radius = getHeight()/20;
                int startX = getWidth()/10;
                int startY = getHeight()/20;

                Bitmap ballBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.soccer_ball);
                ball = new Ball(startX,startY,radius,true,ballBitmap);
                obsticles = new Obsticles(getWidth(), getHeight());
                floor = new Floor(radius*2);

                if(gameLoopThread != null && !gameLoopThread.isRunning()){
                    gameLoopThread.setRunning(true);
                    gameLoopThread.start();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
            }
        });

        detector = new GestureDetector(context, this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onDraw(Canvas canvas){
        if(canvas != null){
            canvas.drawColor(0, PorterDuff.Mode.CLEAR);
            if(floor != null)
                floor.draw(canvas);
            //if(obsticles != null)
            //    obsticles.draw(canvas);
            if(ball != null)
                ball.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        detector.onTouchEvent(event);
        return false;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        ball.jump();
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float v, float v2) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float v, float v2) {
        return false;
    }
}
