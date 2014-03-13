package com.worldclass.objects;

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

    public void update(float topspeed, int w, int h){
        y += topspeed;

        if(y>h+size){
            y = 0-size*8;

            x = random.nextInt(w);
            if(x+size > w){
                x = w-size;
            }
        }
    }
}
