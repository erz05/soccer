package com.worldclass.objects;

import android.graphics.Canvas;
import android.graphics.Color;

import java.util.LinkedList;
import java.util.Random;

/**
 * Created by erz on 2/21/14.
 */
public class Cards {
    private final int TYPE_RED = 0;
    private final int TYPE_YELLOW = 1;
    private LinkedList<Rectangle> rectangles;

    public Cards(int w, int h, int cardW, int cardH){
        rectangles = new LinkedList<Rectangle>();
        Random random = new Random();
        int spacing, y;
        Rectangle rectangle;
//        for(int i=0; i<3; i++){
//            spacing = random.nextInt(w-100)+100;
//            y = random.nextInt((h-cardH));
//            rectangle = new Rectangle(w+spacing,y,cardW,cardH, Color.RED, TYPE_RED);
//            rectangles.add(rectangle);
//            spacing = random.nextInt(w-100)+100;
//            y = random.nextInt((h-cardH));
//            rectangle = new Rectangle(w+spacing,y,cardW,cardH, Color.YELLOW, TYPE_YELLOW);
//            rectangles.add(rectangle);
//        }
    }

    public void draw(Canvas canvas){
        for(Rectangle rectangle: rectangles){
            rectangle.draw(canvas);
        }
    }
}
