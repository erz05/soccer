package com.worldclass.objects;

import java.util.Random;

/**
 * Created by erz on 3/1/14.
 */
public class Star {
    public float x,y,size;
    private Random random;
    public boolean visible = true;
    public Star(float x, float y, float size){
        this.x = x;
        this.y = y;
        this.size = size;
        random = new Random();
    }

    public void update(float topspeed, int w, int h){
        y += topspeed;
        if(y>h+size){
            y = 0-size*8;
            visible = true;

            x = random.nextInt(w);
            if(x+size > w){
                x = w-size;
            }
        }
    }
}
