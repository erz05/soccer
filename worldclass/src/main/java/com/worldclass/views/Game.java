package com.worldclass.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.worldclass.R;
import com.worldclass.listeners.GameListener;
import com.worldclass.objects.Ball;
import com.worldclass.objects.Cards;
import com.worldclass.objects.Floor;
import com.worldclass.utils.GameLoopThread;

/**
 * Created by erz on 2/19/14.
 */
public class Game extends SurfaceView implements GestureDetector.OnGestureListener {

    private SurfaceHolder holder;
    private GameLoopThread gameLoopThread;
    private Ball ball;
    private Floor floor;
    private Cards cards;
    private GestureDetector detector;
    private GameListener gameListener;
    private int canvasH, canvasW;

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
                int jumpHeight = getHeight()/44;

                Log.v("DELETE_THIS", "jump = "+jumpHeight);

                Bitmap ballBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.soccer_ball);
                ball = new Ball(startX,startY,radius,true,ballBitmap,jumpHeight);
                floor = new Floor(radius*2);
                cards = new Cards(getWidth(), getHeight(), radius, radius*2);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
            }
        });

        detector = new GestureDetector(context, this);
        canvasH = getHeight();
        canvasW = getWidth();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onDraw(Canvas canvas){
        if(canvas != null){
            canvas.drawColor(0, PorterDuff.Mode.CLEAR);
            //if(floor != null)
            //    floor.draw(canvas);
            if(cards != null)
                cards.draw(canvas);
            if(ball != null)
                ball.draw(canvas);
        }
    }

    public void setGameListener(GameListener gameListener){
        this.gameListener = gameListener;
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
        Log.v("DELETE","sigleTap = "+getHeight());
        if(gameListener != null)
            ball.jump(gameListener.getAngle(),getHeight());
        else
            ball.jump(0,getHeight());
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

    public void start(){
        if(gameLoopThread == null){
            gameLoopThread = new GameLoopThread(this);
        }
        if(gameLoopThread != null && !gameLoopThread.isRunning()){
            gameLoopThread.setRunning(true);
            gameLoopThread.start();
        }
    }

    public void pause() {
        if(gameLoopThread != null && gameLoopThread.isRunning()){
            gameLoopThread.setRunning(false);
            boolean retry = true;
            while (retry) {
                try {
                    gameLoopThread.join();
                    retry = false;
                } catch (InterruptedException e) {
                }
            }
            gameLoopThread = null;
        }
    }

    public void resume(){
        if(gameLoopThread == null){
            gameLoopThread = new GameLoopThread(this);
        }
        if(gameLoopThread != null && !gameLoopThread.isRunning()){
            gameLoopThread.setRunning(true);
            gameLoopThread.start();
        }
    }
}
