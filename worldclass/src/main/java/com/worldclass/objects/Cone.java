package com.worldclass.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

import java.util.Random;

/**
 * Created by erz on 2/25/14.
 */
public class Cone {
    public float x,y,size;
    private Bitmap bitmap;
    RectF rect;
    Random random;
    public Cone(float x, float y, float size,  Bitmap bitmap){
        this.x = x;
        this.y = y;
        this.size = size;
        this.bitmap = bitmap;

        rect = new RectF();
        random = new Random();
    }

    public void update(float veloY, int w, int h){
        y += veloY;
        if(y>h+size){
            y = 0-size;

            x = random.nextInt(w);
            if(x+size > w){
                x = w-size;
            }
        }
    }

    public void draw(Canvas canvas){
        rect.set(x, y, x + size, y + size);
        canvas.drawBitmap(bitmap, null, rect, null);
    }
}
