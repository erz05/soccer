package com.worldclass.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.worldclass.listeners.BallPosListener;
import com.worldclass.listeners.GameListener;
import com.worldclass.listeners.SoundListener;
import com.worldclass.objects.Car;

import java.util.LinkedList;

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
    int i;

    final int CHECK1 = 101;
    final int CHECK2 = 202;
    final int CHECK3 = 303;
    final int CHECK4 = 404;
    int laps = 0;
    int check = CHECK2;

    //Nascar ======================================
    RectF outerTrack, innerTrack;
    Paint trackPaint;

    float wEight, hFourth, width, height;
    RectF inner, outter, arc1, arc2, arc3, arc4, rcheck1, rcheck2, rcheck3, rcheck4;

    LinkedList<RectF> finishLine = new LinkedList<RectF>();

    private Paint finishPaint;

    float finishSize;
    float speed;

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

    private void update(float h){
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
            //update(height);
            canvas.drawColor(Color.BLACK);

            canvas.drawArc(arc1, 90, 180, false, trackPaint);
            canvas.drawArc(arc2, 270, 180, false, trackPaint);
            canvas.drawArc(arc3, 90, 180, false, trackPaint);
            canvas.drawArc(arc4, 270, 180, false, trackPaint);

            canvas.drawLine(arc1.centerX(), 0, arc2.centerX(), 0, trackPaint);
            canvas.drawLine(arc1.centerX(), height, arc2.centerX(), height, trackPaint);
            canvas.drawLine(arc1.centerX(), hFourth, arc2.centerX(), hFourth, trackPaint);
            canvas.drawLine(arc1.centerX(), height- hFourth, arc2.centerX(), height- hFourth, trackPaint);

            for(RectF rect: finishLine){
                canvas.drawRect(rect, finishPaint);
            }

            /*if(gameStarted){
                if(countdown > -1){
                    if(countdown > 0){
                        if(paint != null)
                            canvas.drawText(""+countdown, width/2 - ((paint.measureText(""+countdown))/2), height/2, paint);
                    }else {
                        if(paint != null)
                            canvas.drawText("GO!", width/2 - ((paint.measureText("GO!"))/2), height/2, paint);
                    }
                }else {
                    startMoving = true;
                }
            }*/

            if(paint != null)
                canvas.drawText("Laps: "+laps,width/2,height/2,paint);

            if(car != null) {
                car.draw(canvas);

                double xDif, xDif2, yDif, yDif2, distanceSquared, distanceSquared2;
                boolean collision, collision2, collision3 = false;
                xDif = car.carRect.centerX() - arc1.centerX();
                yDif = car.carRect.centerY() - arc1.centerY();
                distanceSquared = xDif * xDif + yDif * yDif;
                collision = distanceSquared < (car.size/2 + arc1.width()/2) * (car.size/2 + arc1.width()/2);

                xDif = car.carRect.centerX() - arc2.centerX();
                yDif = car.carRect.centerY() - arc2.centerY();
                distanceSquared = xDif * xDif + yDif * yDif;
                collision2 = distanceSquared < (car.size/2 + arc2.width()/2) * (car.size/2 + arc2.width()/2);

                xDif = car.carRect.centerX() - arc3.centerX();
                yDif = car.carRect.centerY() - arc3.centerY();
                distanceSquared = xDif * xDif + yDif * yDif;

                xDif2 = car.carRect.centerX() - arc4.centerX();
                yDif2 = car.carRect.centerY() - arc4.centerY();
                distanceSquared2 = xDif2 * xDif2 + yDif2 * yDif2;

                collision3 = (distanceSquared > (car.size/2 + (arc3.width()/2)-(car.size*2)) * (car.size/2 + (arc3.width()/2))-(car.size*2)) &&
                        (distanceSquared2 > (car.size/2 + (arc4.width()/2)-(car.size*2)) * (car.size/2 + (arc4.width()/2))-(car.size*2)) &&
                        !car.carRect.intersect(outter);

                if(collision || collision2 || collision3 || car.carRect.intersect(inner)){
                    car.speed = 0;
                    gameListener.onGameOver(laps);
                }

                boolean pass = false;
                switch (check){
                    case CHECK1:
                        pass = car.carRect.intersect(rcheck1);
                        if(pass) {
                            check = CHECK2;
                            laps += 1;
                            car.speed += .5;
                        }
                        break;
                    case CHECK2:
                        pass = car.carRect.intersect(rcheck2);
                        if(pass)
                            check = CHECK3;
                        break;
                    case CHECK3:
                        pass = car.carRect.intersect(rcheck3);
                        if(pass)
                            check = CHECK4;
                        break;
                    case CHECK4:
                        pass = car.carRect.intersect(rcheck4);
                        if(pass)
                            check = CHECK1;
                        break;
                }
            }
        }
    }

    @Override
    public void createObjects() {
        width = getWidth();
        height = getHeight();
        float size = height/20;

        wEight = width/8;
        hFourth = height/4;
        finishSize = hFourth/7;

        speed = height/185;

        float newX = width/2 + size + finishSize;

        car = new Car(newX,hFourth/2 - size/2 ,size,true, speed);

        spacing = size;

        paint = new Paint();
        paint.setStrokeWidth(getWidth()/400);
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setTextSize(getHeight()/20);
        paint.setTextAlign(Paint.Align.CENTER);

        trackPaint = new Paint();
        trackPaint.setStrokeWidth(getHeight()/100);
        trackPaint.setAntiAlias(true);
        trackPaint.setColor(Color.WHITE);
        trackPaint.setStyle(Paint.Style.STROKE);

        finishPaint = new Paint();
        finishPaint.setAntiAlias(true);
        finishPaint.setColor(Color.WHITE);
        finishPaint.setStyle(Paint.Style.FILL);

        outerTrack = new RectF(0,0,getWidth(),getHeight());

        arc1 = new RectF(wEight, hFourth, wEight+(hFourth*2), hFourth*3);
        arc2 = new RectF(width- wEight *3, hFourth, (width-wEight*3)+(hFourth*2), hFourth*3);
        arc3 = new RectF(0, 0, height, height);
        arc4 = new RectF(width- height, 0, width, height);
        inner = new RectF(arc1.centerX(), hFourth, arc2.centerX(), height-hFourth);
        outter = new RectF(arc1.centerX(), size, arc2.centerX(), height-size);

        float[] fx = {width/2 - finishSize, width/2, width/2 + finishSize};
        int count = 0;
        float tmp, fh = 0;
        for(int j=1; j<22; j++){
            if(j%2 == 1) {
                tmp = fx[count];
                finishLine.add(new RectF(tmp-finishSize/2, fh, tmp+finishSize/2, fh+finishSize));
            }
            count += 1;
            if (count > 2) {
                count = 0;
            }
            if(j%3 == 0)
                fh += finishSize;
        }

        rcheck1 = new RectF(width/2-size/2, 0, width/2+size/2, hFourth);
        rcheck2 = new RectF(0, height/2-size/2, wEight, height/2+size/2);
        rcheck3 = new RectF(width/2-size/2, hFourth*3, width/2+size/2, height);
        rcheck4 = new RectF(width-wEight, height/2-size/2, width, height/2+size/2);
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