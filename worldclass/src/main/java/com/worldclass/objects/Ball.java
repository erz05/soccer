package com.worldclass.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

/**
 * Created by erz on 2/19/14.
 */
public class Ball {

    public float x;
    public float y;
    public int radius;
    public int diameter;
    public boolean alive;
    private float gravity = 9.81f;
    private float weight = 2;
    private float accY = gravity;
    private float veloY = 0;
    private float dt = 0.03f;
    private float t = 0;
    private boolean falling = true;
    private Bitmap ballBitmap;
    private RectF ballRect;
    private int rotate = 0;

    public Ball(float x, float y, int radius, boolean alive, Bitmap ballBitmap){
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.alive = alive;
        this.ballBitmap = ballBitmap;
        this.diameter = radius * 2;

        ballRect = new RectF();
    }

    private void update(int height){
        t=t+dt;
        veloY = veloY+accY*dt*weight;
        y += veloY;
        if(y > height-diameter-50){
            y = height-diameter-50;
        }

        rotate += 3;
        rotate = rotate % 360;
    }

    public void draw(Canvas canvas){
        update(canvas.getHeight());
        ballRect.set(x, y, x + diameter, y + diameter);
        canvas.rotate(rotate,ballRect.centerX(),ballRect.centerY());
        canvas.drawBitmap(ballBitmap, null, ballRect, null);
    }

    public void jump(){
        t = 0;
        veloY = -10;
    }
}
