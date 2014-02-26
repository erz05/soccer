package com.worldclass.objects;

import android.graphics.Canvas;

import java.util.LinkedList;

/**
 * Created by erz on 2/21/14.
 */
public class Floor {

    private int height;
    private int width;
    private LinkedList<Rectangle> yards;
    private float veloY = 0;
    private boolean updateVelo = true;

    public Floor(int height, int width){
        this.height = height;
        this.width = width;

        yards = new LinkedList<Rectangle>();

        int yardHeight = height/10;
        Rectangle rectangle;
        int posy = height;
        for(int i=0; i<11; i++){
            if(i == 9){
                rectangle = new Rectangle(0,posy,width,yardHeight,true,i+1);
            }else {
                rectangle = new Rectangle(0,posy,width,yardHeight,false,i+1);
            }
            yards.add(rectangle);
            posy -= yardHeight;
        }
    }

    public void update(int h){
        for(Rectangle rectangle: yards){
            rectangle.update(veloY, h);
        }

        veloY -= .1;
        if(veloY < 0){
            veloY = 0;
        }

        updateVelo = true;
    }

    public void draw(Canvas canvas){
        update(canvas.getHeight());

        for(Rectangle rectangle: yards){
            rectangle.draw(canvas);
        }
    }

    public void fling(){
        if(updateVelo){
            veloY += 3;
            if(veloY > 30){
                veloY = 30;
            }

            updateVelo = false;
        }
    }
}
