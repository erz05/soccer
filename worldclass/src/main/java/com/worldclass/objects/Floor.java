package com.worldclass.objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by erz on 2/21/14.
 */
public class Floor {

    private Paint paint;

    public Floor(){
        paint = new Paint();
        paint.setStrokeWidth(1);
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
    }

    public void update(){

    }

    public void draw(Canvas canvas){
        update();
        canvas.drawLine(0, canvas.getHeight()-100, canvas.getWidth(), canvas.getHeight()-100, paint);
    }
}
