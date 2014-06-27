package com.worldclass.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.worldclass.R;
import com.worldclass.listeners.BallPosListener;
import com.worldclass.listeners.GameListener;
import com.worldclass.listeners.SoundListener;
import com.worldclass.objects.Car;

/**
 * Created by erz on 2/19/14.
 */
public class Game extends MyView implements SoundListener, BallPosListener {

    private Car car;
    private GameListener gameListener;
    public boolean startMoving = false;
    private int countdown = 3;
    private int time = 0;
    public boolean gameStarted = false;
    private Paint paint;
    private float spacing;


    //Nascar ======================================
    RectF outerTrack, innerTrack;
    Paint trackPaint;

    float wEight, hFourth, width;

    //---------------------------------------------

    private float initY;

    public final static int MOVE_LEFT = 0;
    public final static int MOVE_RIGHT = 1;

    private boolean invertControls;

    public Game(Context context) {
        super(context);
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
        }
    }

    @Override
    public void onDraw(Canvas canvas){
        if(canvas != null){
            update(canvas.getHeight());
            canvas.drawColor(Color.BLACK);

            //canvas.drawOval(outerTrack, trackPaint);

            //canvas.drawRect(wEight*2, hFourth, getWidth()-wEight*2, getHeight()-hFourth, trackPaint);
            canvas.drawArc(new RectF(wEight, hFourth, wEight *3, getHeight()- hFourth), 90, 180, false, trackPaint);
            canvas.drawArc(new RectF(getWidth()- wEight *3, hFourth, getWidth()- wEight, getHeight()- hFourth), 270, 180, false, trackPaint);

            canvas.drawArc(new RectF(0, 0, wEight *4, getHeight()), 90, 180, false, trackPaint);
            canvas.drawArc(new RectF(getWidth()- wEight *4, 0, getWidth(), getHeight()), 270, 180, false, trackPaint);

            canvas.drawLine(wEight *2, 0, getWidth()- wEight *2, 0, trackPaint);
            canvas.drawLine(wEight *2, getHeight(), getWidth()- wEight *2, getHeight(), trackPaint);
            canvas.drawLine(wEight *2, hFourth, getWidth()- wEight *2, hFourth, trackPaint);
            canvas.drawLine(wEight *2, getHeight()- hFourth, getWidth()- wEight *2, getHeight()- hFourth, trackPaint);


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

            if(paint != null)
                canvas.drawText("Laps: 0",spacing,spacing*2,paint);

            if(car != null) {
                car.draw(canvas);

                RectF oval = new RectF(wEight, hFourth, wEight *3, getHeight()- hFourth);

                double xDif = car.carRect.centerX() - oval.centerX();
                double yDif = car.carRect.centerY() - oval.centerY();
                double distanceSquared = xDif * xDif + yDif * yDif;
                boolean collision = distanceSquared < (car.diameter + oval.width()/2) * (car.diameter + oval.width()/2);

                if(collision || car.y < 0 || (car.y + car.diameter)>getHeight() || car.carRect.intersect(new RectF(wEight*2, hFourth, getWidth()-wEight*2, getHeight()-hFourth))){
                    car.speed = 0;
                    gameListener.onGameOver(0);
                }
            }
        }
    }

    @Override
    public void createObjects() {
        int radius = getHeight()/40;

        wEight = getWidth()/8;
        hFourth = getHeight()/4;
        width = getWidth();

        float speedX = getHeight()/185;

        int newX = getWidth()/2 - radius;

        car = new Car(newX,hFourth/2,radius,true, speedX);

        spacing = radius;

        paint = new Paint();
        paint.setStrokeWidth(getWidth()/300);
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setTextSize(getHeight()/25);

        trackPaint = new Paint();
        trackPaint.setStrokeWidth(getHeight()/100);
        trackPaint.setAntiAlias(true);
        trackPaint.setColor(Color.WHITE);
        trackPaint.setStyle(Paint.Style.STROKE);

        outerTrack = new RectF(0,0,getWidth(),getHeight());
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
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    if(car != null) {
                        if (event.getX() < width / 2)
                            car.turnLeft();
                        else
                            car.turnRight();
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if(car != null){
                        car.notTurning();
                    }
                    break;
            }
            return true;
        }
        return false;
    }

    @Override
    public void start(){
        super.start();
        if(car != null){
            car.changeColor(Color.BLACK);
        }
        startMoving = false;
        countdown = 3;
        time = 0;
        gameStarted = true;
    }

    public int getScore(){
        return 0;
    }

    @Override
    public void playSound(int sound) {
        gameListener.playSound(sound);
    }

    @Override
    public float getBallX() {
        if(car != null)
            return car.x;
        return -1;
    }
}