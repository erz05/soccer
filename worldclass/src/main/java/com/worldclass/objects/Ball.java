package com.worldclass.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.worldclass.listeners.SoundListener;
import com.worldclass.views.Game;

/**
 * Created by erz on 2/19/14.
 */
public class Ball {

    public float x;
    public float y;
    public int radius;
    public int diameter;
    public boolean alive;
    private float veloX = 0;
    private float speedx;
    private Bitmap ballBitmap;
    private RectF ballRect;
    private int rotate = 0;
    private int jumpHeight;
    private int rotateDirection = 0;
    private float upScale = 1;
    private boolean goingUp = false;
    private boolean onFloor = true;
    private int count = 0;
    private SoundListener listener;
    private Paint paint;

    public Ball(float x, float y, int radius, boolean alive, Bitmap ballBitmap, int jumpHeight, float speedx){
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.alive = alive;
        this.ballBitmap = ballBitmap;
        this.diameter = radius * 2;
        this.jumpHeight = jumpHeight;
        this.speedx = speedx;

        ballRect = new RectF();

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.BLACK);
    }

    private void update(int width, int height){

        x += veloX;

        if(x<0){
            x = 0;
            veloX = -veloX/2;
        }

        if(x>width-diameter){
            x = width-diameter;
            veloX = -veloX/2;
        }

        if(veloX > 0){
            veloX -= .1;
            if(veloX < 0){
                veloX = 0;
            }
        }

        if(veloX < 0){
            veloX += .1;
            if(veloX > 0){
                veloX = 0;
            }
        }

        rotate += rotateDirection;
        rotate = rotate % 360;

        if(goingUp){
            if(count < 20){
                upScale += .1;
                count ++;
            }else {
                goingUp = false;
                count = 0;
            }
        }else {
            if(upScale > 1){
                upScale -= .1;
            }
            if(upScale == 1){
                onFloor = true;
            }
        }
    }

    public void draw(Canvas canvas){
        update(canvas.getWidth(), canvas.getHeight());

        ballRect.set(x, y, x + diameter, y + diameter);
        canvas.rotate(rotate, ballRect.centerX(), ballRect.centerY());
        canvas.scale(upScale, upScale, x + radius, y + radius);
        canvas.drawCircle(x+radius,y+radius,radius,paint);
        canvas.drawBitmap(ballBitmap, null, ballRect, null);
    }

    public RectF getBounds(){
        return ballRect;
    }

    public float getUpScale(){
        return upScale;
    }

    public void jump(){
        if(!goingUp && onFloor){
            goingUp = true;
            onFloor = false;
            rotateDirection = 0;
            veloX = 0;
            if(listener != null){
                listener.playSound(Game.SOUND_JUMP);
            }
        }
    }

    public void fling(int direction, float v, boolean invert) {
        if(!goingUp){
//            if(listener != null){
//                listener.playSound(Game.SOUND_HIT);
//            }
            if(invert){
                switch (direction){
                    case Game.MOVE_LEFT:
                        veloX += speedx;
                        if(rotateDirection > 0)
                            rotateDirection = 0;
                        rotateDirection += 2;
                        break;
                    case Game.MOVE_RIGHT:
                        veloX -= speedx;
                        if(rotateDirection < 0)
                            rotateDirection = 0;
                        rotateDirection -= 2;
                        break;
                }
            }else {
                switch (direction){
                    case Game.MOVE_LEFT:
                        veloX -= speedx;
                        if(rotateDirection > 0)
                            rotateDirection = 0;
                        rotateDirection -= 2;
                    break;
                    case Game.MOVE_RIGHT:
                        veloX += speedx;
                        if(rotateDirection < 0)
                            rotateDirection = 0;
                        rotateDirection += 2;
                    break;
                }
            }
        }
    }

    public void setListener(SoundListener listener){
        this.listener = listener;
}
    
    public void changeColor(int color){
        paint.setColor(color);
    }
}
