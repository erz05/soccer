package com.worldclass.objects;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by erz on 2/19/14.
 */
public class Rectangle {

    public float x,y,width,height;
    public int color, type;
    private Paint paint;

    public Rectangle(float x, float y, float width, float height, int color, int type){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.type = type;

        paint = new Paint();
        paint.setStrokeWidth(1);
        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public void update(int width){
        x -= 10;
        if(x < -width*2){
            x = width*2;
        }
    }

    public void draw(Canvas canvas){
        update(canvas.getWidth());
        RectF rect = new RectF(x,y,x+width,y+height);
        canvas.drawRect(rect,paint);
    }
}
