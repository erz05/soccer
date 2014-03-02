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
    private boolean updateVelo = true;
    private float veloY = 0;
    private int topSpeed;
    private RectF currentRect;
    private float currentX, currentY, size;
    private Bitmap coneBitmap, starBitmap;
    private Random random;

    public ConesStars(int w, int h, int size, Bitmap coneBitmap, Bitmap starBitmap, int topSpeed){
        this.topSpeed = topSpeed;
        this.size = size;
        this.coneBitmap = coneBitmap;
        this.starBitmap = starBitmap;

        random  = new Random();

        coneList = new LinkedList<Cone>();
        Cone cone;
        int posx = random.nextInt(w-size);
        int posy = 0;
        for(int i=0; i<3; i++){
            cone = new Cone(posx,posy-size, size);
            coneList.add(cone);
            posy -= size*8;
        }

        star = new Star(posx, posy, size);

        currentRect = new RectF();
    }

    public void update(int w, int h){
        for (Cone cone: coneList){
            cone.update(veloY, w, h);
        }

        star.update(veloY, w, h);

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

        if(star.visible){
            currentRect.set(star.x, star.y, star.x+size, star.y+size);
            canvas.drawBitmap(starBitmap, null, currentRect, null);
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

    public boolean checkStarCollision(RectF ballRect){
        currentRect.set(star.x, star.y, star.x+size, star.y+size);
        if(currentRect.intersect(ballRect)){
            star.visible = false;
            return true;
        }
        return false;
    }
}
