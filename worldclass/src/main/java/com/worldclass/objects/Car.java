package com.worldclass.objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import com.worldclass.activities.MainActivity;

/**
 * Created by erz on 2/19/14.
 */
public class Car {

    public float x, y;
    public float radius;
    public float diameter;
    public boolean alive;
    //private float veloX = 0;
    public RectF carRect;
    private float upScale = 1;
    private Paint paint;
    private float speedX = -5;
    private float speedY = 0;

    public float speed;

    private float rotate = 180;

    final int NOT_TURNING = 101;
    final int TURNING_LEFT = 202;
    final int TURNING_RIGHT = 303;

    int turning = NOT_TURNING;


    public Car(float x, float y, float radius, boolean alive, float speed){
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.alive = alive;
        this.diameter = radius * 2;
        this.speed = speed;
        this.speedX = -speed;
        //this.speedx = speedx;

        carRect = new RectF();

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.WHITE);
    }

    private void update(int width, int height){

        speedX = (float)(speed*(Math.cos(Math.toRadians(rotate%360))));
        speedY = (float)(speed*(Math.sin(Math.toRadians(rotate%360))));

        x += speedX;

        y += speedY;

        if(x<0){
            x = 0;
        }

        if(x>width-diameter){
            x = width-diameter;
        }

//        if(y<0){
//            y = 0;
//        }
//
//        if(y>height-diameter){
//            y = height-diameter;
//        }

        switch (turning){
            case NOT_TURNING:
                break;
            case TURNING_LEFT:
                rotate -= 5;
                break;
            case TURNING_RIGHT:
                rotate += 5;
                break;
        }
    }

    public void draw(Canvas canvas){
        update(canvas.getWidth(), canvas.getHeight());

        carRect.set(x, y, x + diameter, y + diameter);
        canvas.rotate(rotate, carRect.centerX(), carRect.centerY());
        canvas.drawRect(carRect, paint);
    }

    public void turnLeft(){
        turning = TURNING_LEFT;
    }

    public void turnRight(){
        turning = TURNING_RIGHT;
    }

    public void changeColor(int color){
        paint.setColor(color);
    }

    public void notTurning() {
        turning = NOT_TURNING;
    }
}
