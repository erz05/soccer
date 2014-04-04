package com.worldclass.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

import java.util.LinkedList;
import java.util.Random;

/**
 * Created by erz on 2/25/14.
 */
public class ConeList {

    private LinkedList<Cone> coneList;
    private int topSpeed;
    private RectF currentRect;
    private float currentX, currentY, size;
    private Bitmap coneBitmap;

    public ConeList(int w, int h, int size, Bitmap coneBitmap, int topSpeed){
        this.topSpeed = topSpeed;
        this.size = size;
        this.coneBitmap = coneBitmap;

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
    }

    public void update(int w, int h){
        for (Cone cone: coneList){
            cone.update(topSpeed, w, h);
        }
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
}
