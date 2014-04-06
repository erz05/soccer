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
import com.worldclass.listeners.GameListener;
import com.worldclass.listeners.SoundListener;
import com.worldclass.objects.Ball;
import com.worldclass.objects.ConeList;
import com.worldclass.objects.Floor;

/**
 * Created by erz on 2/19/14.
 */
public class Game extends MyView implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener, SoundListener {

    private Ball ball;
    private ConeList coneList;
    private GestureDetector detector;
    private GameListener gameListener;
    private Floor floor;
    public boolean startMoving = false;
    private Paint messagePaint;
    private int countdown = 3;
    private int time = 0;
    private boolean gameStarted = false;

    private float initY;

    public final static int MOVE_LEFT = 0;
    public final static int MOVE_RIGHT = 1;

    private boolean invertControls;

    public Game(Context context) {
        super(context);
        detector = new GestureDetector(context, this);
        messagePaint = new Paint();
        messagePaint.setColor(Color.WHITE);
        messagePaint.setStrokeWidth(2);
        messagePaint.setTextSize(100);
        messagePaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void update(){
        if(gameStarted){
            if(countdown > -1){
                if(time > 30){
                    countdown -= 1;
                    time = 0;
                }else {
                    time += 1;
                }
            }
        }
    }

    @Override
    public void onDraw(Canvas canvas){
        if(canvas != null){
            update();
            canvas.drawColor(Color.parseColor("#009966"));

            if(gameStarted){
                if(floor != null){
                    floor.draw(canvas, startMoving);
                }

                if(countdown > -1){
                    if(countdown > 0){
                        canvas.drawText(""+countdown, canvas.getWidth()/2 - ((messagePaint.measureText(""+countdown))/2), canvas.getHeight()/2, messagePaint);
                    }else {
                        canvas.drawText("GO!", canvas.getWidth()/2 - ((messagePaint.measureText("GO!"))/2), canvas.getHeight()/2, messagePaint);
                    }
                }else {
                    startMoving = true;
                }
            }

            if(coneList != null)
                coneList.draw(canvas, startMoving);
            if(ball != null)
                ball.draw(canvas);

            if(coneList != null && ball != null && ball.getUpScale() == 1){
                if(coneList.checkCollision(ball.getBounds())){
                    if(gameListener != null){
                        gameListener.onGameOver(floor.getYards());
                    }
                    ball.changeColor(Color.RED);
                }
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
        coneList = new ConeList(getWidth(), getHeight(), coneSize, coneBitmap, topSpeed);
        floor = new Floor(radius, topSpeed);
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
        if(floor != null)
        floor.reset();
    }

    public int getScore(){
        if(floor != null)
            return floor.getYards();
        return 0;
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