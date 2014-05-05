package com.worldclass.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.worldclass.R;
import com.worldclass.listeners.BallPosListener;
import com.worldclass.listeners.GameListener;
import com.worldclass.listeners.SoundListener;
import com.worldclass.objects.Ball;
import com.worldclass.objects.ObstaclePool;

/**
 * Created by erz on 2/19/14.
 */
public class Game extends MyView implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener, SoundListener, BallPosListener {

    private Ball ball;
    private ObstaclePool obstaclePool;
    private GestureDetector detector;
    private GameListener gameListener;
    public boolean startMoving = false;
    private int countdown = 3;
    private int time = 0;
    public boolean gameStarted = false;
    private Paint paint;
    private float y, spacing;
    private int yards;

    private int topSpeed;

    private float initY;

    public final static int MOVE_LEFT = 0;
    public final static int MOVE_RIGHT = 1;

    private boolean invertControls;

    public Game(Context context) {
        super(context);
        detector = new GestureDetector(context, this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void update(int h){
        if(gameStarted){
            if(countdown > -1){
                if(time > 30){
                    countdown -= 1;
                    time = 0;
                }else {
                    time += 1;
                }
            }

            if(startMoving){
                y += topSpeed;

                if(y > h){
                    y = 0;
                    yards += 1;
                }
            }
        }
    }

    @Override
    public void onDraw(Canvas canvas){
        if(canvas != null){
            update(canvas.getHeight());
            canvas.drawColor(Color.parseColor("#187E37"));

            if(gameStarted){
                if(countdown > -1){
                    if(countdown > 0){
                        if(paint != null)
                            canvas.drawText(""+countdown, canvas.getWidth()/2 - ((paint.measureText(""+countdown))/2), canvas.getHeight()/2, paint);
                    }else {
                        if(paint != null)
                            canvas.drawText("GO!", canvas.getWidth()/2 - ((paint.measureText("GO!"))/2), canvas.getHeight()/2, paint);
                    }
                }else {
                    startMoving = true;
                }
            }

            canvas.drawText("Score: "+yards,spacing,spacing*2,paint);

            if(obstaclePool != null)
                obstaclePool.draw(canvas, startMoving);
            if(ball != null)
                ball.draw(canvas);

            if(obstaclePool != null && ball != null && ball.getUpScale() == 1){
                if(obstaclePool.checkCollision(ball.getBounds())){
                    if(gameListener != null){
                        gameListener.onGameOver(yards);
                    }
                    ball.changeColor(Color.RED);
                }
            }
        }
    }

    @Override
    public void createObjects() {
        int radius = getHeight()/30;
        int coneSize = getHeight()/10;
        int jumpHeight = getHeight()/44;
        topSpeed = getHeight()/80;

        float speedX = getWidth()/185;

        int newX = getWidth()/2 - radius;

        Bitmap ballBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
        ball = new Ball(newX,getHeight()-(radius*4),radius,true,ballBitmap,jumpHeight, speedX);
        ball.setListener(this);
        Bitmap coneBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.jersey);
        obstaclePool = new ObstaclePool(getWidth(), getHeight(), radius, coneBitmap, topSpeed);
        obstaclePool.setListener(this);

        spacing = radius;
        yards = 0;
        y = 0;
        paint = new Paint();
        paint.setStrokeWidth(getWidth()/200);
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setTextSize(getHeight()/25);
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
                                ball.fling(MOVE_RIGHT, invertControls);
                            }else if(motionX < half){
                                ball.fling(MOVE_LEFT, invertControls);
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
        startMoving = false;
        countdown = 3;
        time = 0;
        gameStarted = true;
        y = 0;
        yards = 0;
    }

    public int getScore(){
        return yards;
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

    @Override
    public int getBallX() {
        if(ball != null)
            return ball.x;
        return -1;
    }
}