package com.worldclass.objects;

import android.graphics.Canvas;
import android.graphics.Color;

import java.util.LinkedList;

/**
 * Created by erz on 2/19/14.
 */
public class Obsticles {

    private int spacing = 400;
    private int spacingX = 800;
    private int w = 200;
    private LinkedList<Rectangle> rectangles;
    private int topHeights[] = {100, 300, 400, 200, 300};

    public Obsticles(int width, int height){
        rectangles = new LinkedList<Rectangle>();
        Rectangle rectangle;
        int start = width + spacingX;
        for(int i=0; i<5; i++){
            rectangle = new Rectangle(start, 0, w, topHeights[i], Color.WHITE);
            rectangles.add(rectangle);
            rectangle = new Rectangle(start, topHeights[i]+spacing, w, height-topHeights[i]-spacing-50, Color.WHITE);
            rectangles.add(rectangle);
            start += spacingX;
        }
    }

    public void draw(Canvas canvas){
        for(Rectangle rectangle: rectangles){
            rectangle.draw(canvas);
        }
    }
}
