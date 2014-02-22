package com.worldclass.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

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
    private int jumpHeight;

    public Ball(float x, float y, int radius, boolean alive, Bitmap ballBitmap, int jumpHeight){
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.alive = alive;
        this.ballBitmap = ballBitmap;
        this.diameter = radius * 2;
        this.jumpHeight = jumpHeight;

        ballRect = new RectF();
    }

    private void update(int height){
        t=t+dt;
        veloY = veloY+accY*dt*weight;
        y += veloY;
        if(y > height-diameter-radius){
            y = height-diameter-radius;
        }

        if(y < 0){
            y = 0;
            veloY = 0;
            t = 0;
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
        veloY = -jumpHeight;
    }
}
