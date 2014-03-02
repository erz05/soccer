package com.worldclass.objects;

import android.graphics.RectF;

import java.util.Random;

/**
 * Created by erz on 2/25/14.
 */
public class Cone {
    public float x,y,size;
    Random random;
    public Cone(float x, float y, float size){
        this.x = x;
        this.y = y;
        this.size = size;

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
}
