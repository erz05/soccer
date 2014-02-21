package com.worldclass.objects;

import android.graphics.Canvas;

import java.util.LinkedList;

/**
 * Created by erz on 2/21/14.
 */
public class Cards {

    private LinkedList<Rectangle> rectangles;

    public Cards(){
        for(int i=0; i<5; i++){

        }
    }

    public void draw(Canvas canvas){
        for(Rectangle rectangle: rectangles){
            rectangle.draw(canvas);
        }
    }
}
