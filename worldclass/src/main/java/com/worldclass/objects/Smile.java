package com.worldclass.objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by erz on 2/19/14.
 */
public class Smile {

    public float x, y, size;
    public boolean alive;
    public RectF carRect;
    private Paint paint;
    private RectF smileRect;
    public final static int TYPE_HAPPY = 0;
    public final static int TYPE_MILD = 1;
    public final static int TYPE_ANGRY = 2;
    int type = TYPE_HAPPY;

    public Smile(float x, float y, float size, boolean alive){
        this.x = x;
        this.y = y;
        this.size = size;
        this.alive = alive;

        carRect = new RectF();

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.YELLOW);

        smileRect = new RectF();
    }

    private void update(int width, int height){
        if(x-size<0) {
            x = size;
            switchType();
        }
        if(x+size>width) {
            x=width-size;
            switchType();
        }
        if(y-size<0) {
            y = size;
            switchType();
        }
        if(y+size>height) {
            y=height-size;
            switchType();
        }
    }

    public void draw(Canvas canvas){
        update(canvas.getWidth(), canvas.getHeight());

        switch (type){
            case TYPE_HAPPY:
                paint.setColor(Color.YELLOW);
                break;
            case TYPE_MILD:
                paint.setColor(Color.parseColor("#FFA500"));
                break;
            case TYPE_ANGRY:
                paint.setColor(Color.RED);
                break;
        }
        //body
        carRect.set(x - size / 2, y - size / 2, x + size / 2, y + size / 2);
        canvas.drawCircle(x, y, size, paint);

        //eyes
        paint.setColor(Color.BLACK);
        canvas.drawCircle(x - size + size / 3, y - size / 8, size / 8, paint);
        canvas.drawCircle(x+size-size/3, y-size/8, size/8, paint);

        //smile
        smileRect.set(x-size/2, y+size/8, x+size/2, y+size/2);
        switch (type){
            case TYPE_HAPPY:
                canvas.drawArc(smileRect, 0, 180, true, paint);
                break;
            case TYPE_MILD:
                canvas.drawArc(smileRect, 0, 360, true, paint);
                break;
            case TYPE_ANGRY:
                canvas.drawArc(smileRect, 0, -180, true, paint);
                break;
        }
    }

    public void setPosition(float x, float y){
        this.x = x;
        this.y = y;
    }

    public void switchType(){
        type ++;
        if(type > 2) type = 0;
    }
}
