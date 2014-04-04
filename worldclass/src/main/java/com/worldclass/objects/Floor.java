package com.worldclass.objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by erz on 2/21/14.
 */
public class Floor {

    private Paint paint;

    private float y, spacing;
    private int yards;

    private int topSpeed;

    public Floor(float textSize, int topSpeed){
        y = 0;
        this.spacing = textSize;
        this.topSpeed = topSpeed;

        yards = 0;

        paint = new Paint();
        paint.setStrokeWidth(5);
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setTextSize(textSize);
    }

    public void update(int h){
        y += topSpeed;

        if(y > h){
            y = 0;
            yards += 1;
        }
    }

    public void draw(Canvas canvas, boolean startMoving){
        if(startMoving)
            update(canvas.getHeight());
        canvas.drawText("Score: "+yards,spacing,spacing*2,paint);
    }

    public int getYards(){
        return yards;
    }

    public void reset(){
        y = 0;
        yards = 0;
    }
}
