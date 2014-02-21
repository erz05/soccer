package com.worldclass.objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

/**
 * Created by erz on 2/19/14.
 */
public class Ball {

    public float x;
    public float y;
    public int radius;
    public boolean alive;
    private Paint ballPaint;
    private Paint floorPaint;
    private float gravity = 9.81f;
    private float weight = 2;
    private float accY = gravity;
    private float veloY = 0;
    private float dt = 0.03f;
    private float t = 0;
    private boolean falling = true;

    public Ball(float x, float y, int radius, boolean alive){
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.alive = alive;

        ballPaint = new Paint();
        ballPaint.setStrokeWidth(1);
        ballPaint.setAntiAlias(true);
        ballPaint.setColor(Color.RED);
        ballPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        floorPaint = new Paint();
        floorPaint.setStrokeWidth(1);
        floorPaint.setAntiAlias(true);
        floorPaint.setColor(Color.WHITE);
        floorPaint.setStyle(Paint.Style.STROKE);
    }

    private void update(int height){
        t=t+dt;
        veloY = veloY+accY*dt*weight;
        y += veloY;
        if(y > height-radius-50){
            y = height-radius-50;
        }

    }

    public void draw(Canvas canvas){
        update(canvas.getHeight());
        canvas.drawCircle(x, y, radius, ballPaint);
        canvas.drawLine(0, canvas.getHeight()-50, canvas.getWidth(), canvas.getHeight()-50, floorPaint);
    }

    public void jump(){
        t = 0;
        veloY = -10;
        Log.v("DELETE_THIS", "jump");
    }
}
