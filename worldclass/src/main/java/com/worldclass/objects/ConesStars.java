package com.worldclass.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

import java.util.LinkedList;
import java.util.Random;

/**
 * Created by erz on 2/25/14.
 */
public class ConesStars {

    private LinkedList<Cone> coneList;
    //private LinkedList<Star> starList;
    private Star star;
    private int topSpeed;
    private RectF currentRect;
    private float currentX, currentY, size;
    private Bitmap coneBitmap, starBitmap;

    public ConesStars(int w, int h, int size, Bitmap coneBitmap, Bitmap starBitmap, int topSpeed){
        this.topSpeed = topSpeed;
        this.size = size;
        this.coneBitmap = coneBitmap;
        this.starBitmap = starBitmap;

        Random random = new Random();

        coneList = new LinkedList<Cone>();
        Cone cone;
        int posx = random.nextInt(w - size);
        int posy = 0;
        currentRect = new RectF();
        for(int i=0; i<4; i++){
            cone = new Cone(posx,posy-size, size);
            coneList.add(cone);
            if(i != 1)
                posy -= size*8;
            posx = random.nextInt(w - size);
        }

        star = new Star(posx, posy, size);
    }

    public void update(int w, int h){
        for (Cone cone: coneList){
            cone.update(topSpeed, w, h);
        }

        star.update(topSpeed, w, h);
    }

    public void draw(Canvas canvas, boolean startMoving){
        if(startMoving)
            update(canvas.getWidth(), canvas.getHeight());
        for(Cone cone: coneList){
            currentX = cone.x;
            currentY = cone.y;
            currentRect.set(currentX, currentY, currentX+size, currentY+size);
            canvas.drawBitmap(coneBitmap, null, currentRect, null);
        }

        if(star.visible){
            currentRect.set(star.x, star.y, star.x+size, star.y+size);
            canvas.drawBitmap(starBitmap, null, currentRect, null);
        }
    }

    public boolean checkCollision(RectF ballRect){
        for(Cone cone: coneList){
            currentX = cone.x;
            currentY = cone.y;
            currentRect.set(currentX, currentY, currentX + size, currentY+size);
            if(currentRect.intersect(ballRect)){
                return true;
            }
        }
        return false;
    }

    public boolean checkStarCollision(RectF ballRect){
        currentRect.set(star.x, star.y, star.x+size, star.y+size);
        if(currentRect.intersect(ballRect)){
            star.visible = false;
            return true;
        }
        return false;
    }
}
