package com.worldclass.objects;

import java.util.Random;

/**
 * Created by erz on 4/6/2014.
 */
public class Net {
    public int x,y,sizeX, sizeY;
    public boolean visible;

    public Net(int x, int y, int sizeX, int sizeY){
        this.x = x;
        this.y = y;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public void update(int topspeed, int h){
        y += topspeed;
        if(y>h+sizeY){
            visible = false;
        }
    }
}
