package com.worldclass.objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by erz on 3/2/14.
 */
public class PowerBar {

    float x, y, w, h;
    float power, minus;
    int count = 0;
    private Paint paint, borderPaint;

    public PowerBar(float x, float y, float w, float h){

        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.power = w;

        minus = w/100;

        paint = new Paint();
        paint.setColor(Color.YELLOW);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        borderPaint = new Paint();
        borderPaint.setStrokeWidth(5);
        borderPaint.setColor(Color.BLACK);
        borderPaint.setAntiAlias(true);
        borderPaint.setStyle(Paint.Style.STROKE);
    }

    public void update(){

        if(count > 10){
            power -= minus;
            if(power < 0){
                power = 0;
            }
            count = 0;
        }
        count += 1;
    }

    public void draw(Canvas canvas){
        update();
        canvas.drawRect(x,y,x+power,y+h,paint);
        canvas.drawRect(x,y,x+w,y+h,borderPaint);
    }

    public void addPower(){
        this.power += minus*2;
        if(this.power>w){
            this.power = w;
        }
    }

    public float getPower(){
        return power;
    }
}
