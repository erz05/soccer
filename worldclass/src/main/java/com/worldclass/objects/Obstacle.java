package com.worldclass.objects;

/**
 * Created by erz on 2/25/14.
 */
public class Obstacle {
    public int x,y,size;
    public boolean visible = false;

    public Obstacle(int x, int y, int size){
        this.x = x;
        this.y = y;
        this.size = size;
    }

    public void update(float topspeed, int h) {
        y += topspeed;

        if (y > h + size) {
            visible = false;
        }
    }
}
