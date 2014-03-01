package com.worldclass.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.worldclass.R;
import com.worldclass.listeners.GameListener;
import com.worldclass.objects.Ball;
import com.worldclass.objects.Cones;
import com.worldclass.objects.Floor;
import com.worldclass.utils.GameLoopThread;

/**
 * Created by erz on 2/19/14.
 */
public class Game extends SurfaceView implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    private SurfaceHolder holder;
    private GameLoopThread gameLoopThread;
    private Ball ball;
    private Floor floor;
    private Cones cones;
    private GestureDetector detector;
    private GameListener gameListener;
    private boolean isGameOver = false;

    private float initX, initY;

    public final static int MOVE_LEFT = 0;
    public final static int MOVE_RIGHT = 1;

    public Game(Context context) {
        super(context);
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

                int radius = getHeight()/30;
                int coneSize = getHeight()/20;
                int jumpHeight = getHeight()/44;
                int topSpeed = getHeight()/62;

                int newX = getWidth()/2 - radius;
                int newY = getHeight()/2 - radius;

                Bitmap ballBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.soccer_ball);
                ball = new Ball(newX,getHeight()-(radius*4),radius,true,ballBitmap,jumpHeight);
                floor = new Floor(getHeight(), getWidth(), radius, topSpeed);
                Bitmap coneBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cone);
                cones = new Cones(getWidth(), getHeight(), coneSize, coneBitmap, topSpeed);
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
            canvas.drawColor(Color.parseColor("#22B14C"));
            if(floor != null)
                floor.draw(canvas);
            if(cones != null)
                cones.draw(canvas);
            if(ball != null)
                ball.draw(canvas);

            if(cones != null && ball != null && ball.getUpScale() == 1){
                if(cones.checkCollision(ball.getBounds())){
                    gameOver();
                }
            }
        }
    }

    public void gameOver(){
        if(gameListener != null){
            isGameOver = true;
            gameListener.onGameOver(floor.getYards());
        }
    }

    public void setGameListener(GameListener gameListener){
        this.gameListener = gameListener;
    }

    public int getScore(){
        if(floor != null){
            return floor.getYards();
        }
        return 0;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(!isGameOver){
            detector.onTouchEvent(event);

            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    initX = event.getX();
                    initY = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:

                    float motionX = event.getX();
                    float motionY = event.getY();

                    if(motionY > initY){
                        float half = getWidth()/2;

                        if(motionX  > half){
                            ball.fling(MOVE_RIGHT, 0);
                        }else if(motionX < half){
                            ball.fling(MOVE_LEFT, 0);
                        }

                        floor.fling();
                        cones.fling();
                    }

                    break;
            }

            return true;
        }
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
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float xv, float yv) {
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
                    e.printStackTrace();
                }
            }
            gameLoopThread = null;
        }
    }

    public void resume(){
        if(gameLoopThread == null){
            gameLoopThread = new GameLoopThread(this);
        }else {
            gameLoopThread = null;
            gameLoopThread = new GameLoopThread(this);
        }
        if(gameLoopThread != null && !gameLoopThread.isRunning()){
            gameLoopThread.setRunning(true);
            gameLoopThread.start();
        }
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        if(ball != null)
            ball.jump();
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {
        return false;
    }
}
