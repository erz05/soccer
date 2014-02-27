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

    private float y1, y2;
    private int i, yard1, yard2;
    private float posY;

    private int topSpeed;

    public Floor(int height, int width, int textSize, int topSpeed){
        yardHeight = height/10;
        y1 = height;
        y2 = 0;
        this.topSpeed = topSpeed;

        yard1 = 10;
        yard2 = 20;

        paint = new Paint();
        paint.setStrokeWidth(2);
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setTextSize(textSize);
    }

    public void update(int h){
        y1 += veloY;
        y2 += veloY;

        if(y1 > h*2){
            y1 = 0;
            yard1 += 20;
        }

        if(y2 > h*2){
            y2 = 0;
            yard2 += 20;
        }

        veloY -= .1;
        if(veloY<0){
            veloY = 0;
        }
    }

    public void draw(Canvas canvas){

        update(canvas.getHeight());

        posY = y1 - yardHeight;

        for(i=0; i<10; i++){
            canvas.drawLine(0,posY,canvas.getWidth(),posY,paint);
            if(i == 9){
                canvas.drawText(""+yard1,15, posY+yardHeight/2, paint);
            }
            posY -= yardHeight;
        }

        posY = y2 - yardHeight;

        for(i=0; i<10; i++){
            canvas.drawLine(0,posY,canvas.getWidth(),posY,paint);
            if(i == 9){
                canvas.drawText(""+yard2,15, posY+yardHeight/2, paint);
            }
            posY -= yardHeight;
        }
    }

    public void fling(){
        veloY += 3;
        if(veloY > topSpeed){
            veloY = topSpeed;
        }
    }
}
