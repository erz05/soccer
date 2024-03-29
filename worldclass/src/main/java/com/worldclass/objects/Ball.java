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
    private float veloY = 10;
    private float veloX = 0;
    private float dt = 0.03f;
    private float t = 0;
    private Bitmap ballBitmap;
    private RectF ballRect;
    private int rotate = 0;
    private int jumpHeight;
    private Paint player;
    private int playerH, playerW;
    boolean goingup = false;

    public Ball(float x, float y, int radius, boolean alive, Bitmap ballBitmap, int jumpHeight){
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.alive = alive;
        this.ballBitmap = ballBitmap;
        this.diameter = radius * 2;
        this.jumpHeight = jumpHeight;

        ballRect = new RectF();

        player = new Paint();
        player.setStyle(Paint.Style.FILL_AND_STROKE);
        player.setStrokeWidth(1);
        player.setAntiAlias(true);
        player.setColor(Color.BLACK);

        playerH = diameter * 2;
        playerW = diameter;
    }

    private void update(int width, int height){
        if(!goingup){
            t=t+dt;
            veloY = veloY+(accY*dt*weight);
        }
        y += veloY;
        if(y > height-diameter-radius){
            y = height-diameter-radius;
            veloX = 0;
            goingup = false;
        }

        if(y < 0){
            y = 0;
            veloY = 10;
            t = 0;
            goingup = false;
        }

        x += veloX;
        if(x > width-diameter){
            x = width-diameter;
            veloX = - veloX;
            //veloY = 10;
            goingup = false;
        }

        if(x < 0){
            x = 0;
            //veloY = 10;
            veloX = -veloX;
            goingup = false;
        }

        rotate += 3;
        rotate = rotate % 360;
    }

    public void draw(Canvas canvas){
        update(canvas.getWidth(), canvas.getHeight());

        //canvas.drawRect(x,canvas.getHeight()-radius-playerH,x+playerW,canvas.getHeight()-radius,player);

        ballRect.set(x, y, x + diameter, y + diameter);
        canvas.rotate(rotate,ballRect.centerX(),ballRect.centerY());
        canvas.drawBitmap(ballBitmap, null, ballRect, null);
    }

    public void jump(int angle, int canvasH){
        Log.v("DELETE_THIS", "y = "+y+", canvasH = "+canvasH);
        //if(y>canvasH-radius-playerH){

        goingup = false;
            int ax = (angle + 180) % 360;

    //        int ax = 270;
            if(ax>180 && ax<360){
                veloX = 0;
            }else {
                veloX = -(float)Math.cos(Math.toRadians(ax)) * 10;
            }

            /*if(angle>90&&angle<270){
                ax = -ax;
            }*/

           // veloX = (float)Math.cos(Math.toRadians(ax));

            Log.v("DELETE_THIS", "angle = "+angle+", ax = "+ax+", velo = "+veloX);

            //veloX = +3;
            //t = 0;
            veloY = -10;
        //}
    }

    public void fling(int angle) {

//        if(angle>-135&&angle<-45){
//            veloY = -20;
//            goingup = true;
//        }

//        if(angle<-135 && angle>-180 || angle>135&& angle<180){
//            veloX = -20;
//            goingup = true;
//        }
//
////        if(angle<135 && angle > 45){
////            veloY = 20;
////            goingup = true;
////        }
//
//        if(angle>-45 && angle<0 || angle<45 && angle>0){
//            veloX = 20;
//            goingup = true;
//        }

        if(!goingup){
            if((angle>-180 && angle<-90) || (angle<180 && angle>90)){
                veloX = -30;
                goingup = true;
            }

            if((angle>-90 && angle<0) || (angle<90 && angle>0)){
                veloX = 30;
                goingup = true;
            }

            veloY = 0;
        }
        Log.v("DELETE_THIS", "****** = "+angle);
//        veloX = 10;
//        veloY = -20;
    }
}
