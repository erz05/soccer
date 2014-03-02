package com.worldclass.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

import java.util.LinkedList;

/**
 * Created by erz on 2/25/14.
 */
public class Cones {

    private LinkedList<Cone> coneList;
    private boolean updateVelo = true;
    private float veloY = 0;
    private int topSpeed;
    private RectF currentRect;
    private float currentX, currentY, size;
    private Bitmap coneBitmap;

    public Cones(int w, int h, int size, Bitmap bitmap, int topSpeed){
        this.topSpeed = topSpeed;
        this.size = size;
        this.coneBitmap = bitmap;
        coneList = new LinkedList<Cone>();
        Cone cone;
        int posx = 100;
        int posy = 0;
        for(int i=0; i<2; i++){
            cone = new Cone(posx,posy-size, size);
            coneList.add(cone);
            posx += 400;
            posy -= 500;
        }
        currentRect = new RectF();
    }

    public void update(int w, int h){
        for (Cone cone: coneList){
            cone.update(veloY, w, h);
        }

        veloY -= .1;
        if(veloY < 0){
            veloY = 0;
        }

        updateVelo = true;
    }

    public void draw(Canvas canvas){
        update(canvas.getWidth(), canvas.getHeight());
        for(Cone cone: coneList){
            currentX = cone.x;
            currentY = cone.y;
            currentRect.set(currentX, currentY, currentX+size, currentY+size);
            canvas.drawBitmap(coneBitmap, null, currentRect, null);
        }
    }

    public void fling(){
        if(updateVelo){
            veloY += 3;
            if(veloY > topSpeed)
                veloY = topSpeed;
            updateVelo = false;
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
