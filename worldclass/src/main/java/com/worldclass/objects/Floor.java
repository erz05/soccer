package com.worldclass.objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by erz on 2/21/14.
 */
public class Floor {

    private int height;
    private Paint paint;

    public Floor(int height){
        this.height = height;
        paint = new Paint();
        paint.setStrokeWidth(1);
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#A4C739"));
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public void update(){

    }

    public void draw(Canvas canvas){
        update();
        canvas.drawRect(0,canvas.getHeight()-height,canvas.getWidth(),canvas.getHeight(),paint);
    }
}
