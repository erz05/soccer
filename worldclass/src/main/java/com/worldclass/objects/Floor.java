package com.worldclass.objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by erz on 2/21/14.
 */
public class Floor {

    private float veloY = 0;
    private float yardHeight;
    private Paint paint;

    private float y, spacing;
    private int yards;

    private int topSpeed;

    public Floor(int height, int width, float textSize, int topSpeed){
        yardHeight = height/10;
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
        //y += veloY;
        y += 20;

        if(y > h){
            y = 0;
            yards += 1;
        }

        veloY -= .1;
        if(veloY<0){
            veloY = 0;
        }
    }

    public void draw(Canvas canvas){
        update(canvas.getHeight());
        //if(yards != 0 && (yards % 10)==0)
        //canvas.drawLine(0,y,canvas.getWidth(),y,paint);
        canvas.drawText("Score: "+yards,spacing,spacing*2,paint);
    }

    public void fling(){
        veloY += 3;
        if(veloY > topSpeed){
            veloY = topSpeed;
        }
    }

    public int getYards(){
        return yards;
    }
}
