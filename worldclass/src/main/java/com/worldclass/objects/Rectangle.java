package com.worldclass.objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.Random;

/**
 * Created by erz on 2/19/14.
 */
public class Rectangle {

    public float x,y,width,height;
    private Paint paint;
    private Random random;
    private int yard;
    private boolean print;
    private float veloY = 0;

    public Rectangle(float x, float y, float width, float height, boolean print, int yard){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.print = print;
        this.yard = yard;

        paint = new Paint();
        paint.setStrokeWidth(2);
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setTextSize(50);
        random = new Random();
    }

    public void update(int w, int h){
        y += veloY;
        if(y>h+height){
            y = 0;
            yard += 10;
        }

        veloY -= .1;
        if(veloY < 0){
            veloY = 0;
        }
    }

    public void draw(Canvas canvas){
        update(canvas.getWidth(), canvas.getHeight());
        //RectF rect = new RectF(x,y,x+width,y+height);
        //canvas.drawRect(rect,paint);
        canvas.drawLine(0,y,canvas.getWidth(),y,paint);

        if(print){
            canvas.drawText(""+yard,15, y-height/2, paint);
        }
    }

    public void fling(){
        veloY += 3;
        if(veloY > 30){
            veloY = 30;
        }
    }
}
