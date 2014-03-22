package com.worldclass.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.worldclass.R;
import com.worldclass.listeners.GameListener;
import com.worldclass.listeners.SoundListener;
import com.worldclass.objects.Ball;
import com.worldclass.objects.ConesStars;

/**
 * Created by erz on 2/19/14.
 */
public class Game extends MyView implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener, SoundListener {

    private Ball ball;
    private ConesStars conesStars;
    private GestureDetector detector;
    private GameListener gameListener;

    private float initY;

    public final static int MOVE_LEFT = 0;
    public final static int MOVE_RIGHT = 1;

    private boolean invertControls;

    public Game(Context context) {
        super(context);
        setZOrderOnTop(true);
        SurfaceHolder holder = getHolder();
        if(holder != null)
            holder.setFormat(PixelFormat.TRANSPARENT);
        detector = new GestureDetector(context, this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onDraw(Canvas canvas){
        if(canvas != null){
            //canvas.drawColor(Color.parseColor("#22B14C"));
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

            if(conesStars != null)
                conesStars.draw(canvas, gameListener.isMoving());
            if(ball != null)
                ball.draw(canvas);

            if(conesStars != null && ball != null && ball.getUpScale() == 1){
                if(conesStars.checkCollision(ball.getBounds())){
                    if(gameListener != null){
                        gameListener.onGameOver();
                    }
                    ball.changeColor(Color.RED);
                }
                if(conesStars.checkStarCollision(ball.getBounds()))
                    gameListener.addPower();
            }
        }
    }

    @Override
    public void createObjects() {
        int radius = getHeight()/30;
        int coneSize = getHeight()/20;
        int jumpHeight = getHeight()/44;
        int topSpeed = getHeight()/80;

        float speedX = getWidth()/185;

        int newX = getWidth()/2 - radius;

        Bitmap ballBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.soccer_ball);
        ball = new Ball(newX,getHeight()-(radius*4),radius,true,ballBitmap,jumpHeight, speedX);
        ball.setListener(this);
        Bitmap coneBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cone);
        Bitmap starBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.star);
        conesStars = new ConesStars(getWidth(), getHeight(), coneSize, coneBitmap, starBitmap, topSpeed);
    }

    public void setGameListener(GameListener gameListener){
        this.gameListener = gameListener;
    }

    public void setOptions(boolean invertControls){
        this.invertControls = invertControls;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(!gameListener.getIsGameOver()){
            detector.onTouchEvent(event);

            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    initY = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    float motionX = event.getX();
                    if(event.getY() > initY){
                        if(ball != null){
                            float half = getWidth()/2;
                            if(motionX  > half){
                                ball.fling(MOVE_RIGHT, 0, invertControls);
                            }else if(motionX < half){
                                ball.fling(MOVE_LEFT, 0, invertControls);
                            }
                        }
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

    @Override
    public void start(){
        super.start();

        if(ball != null){
            ball.changeColor(Color.BLACK);
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

    @Override
    public void playSound(int sound) {
        gameListener.playSound(sound);
    }
}