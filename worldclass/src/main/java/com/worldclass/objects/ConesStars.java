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

    private boolean updateVelo = true;
    private float veloY = 0;
    private int topSpeed, createCount=0, patternCount=0;
    private RectF currentRect;
    private float currentX, currentY, size, spacing;
    private Bitmap coneBitmap, starBitmap;
    private Random random;

    /*
    1-2-3-4-5-6-7-8-9-10-11-12-13-14-15-16-17
    1-0-2-0-1-0-2-0-1-0-2-0-1-0-2-0-1-0-2-0-1
    ----------------1---------------1
    --------1---------------1--------
    1---------------1----------------

    1 = position for cones
     */

    private final int PATTERN_LINE = 0;
    private final int PATTERN_DOUBLE = 1;
    private final int PATTERN_INOUT = 2;
    private int currentPattern = PATTERN_LINE;

    private int[] linePos = {4, 8, 12};
    private int[][] doublePos = {{0,8},{4,12},{8,16}};
    private int[][] inoutPos = {{0,8,4},{4,12,8},{8,16,12}};

    LinkedList<Cone> list;

    public ConesStars(int w, int h, int size, Bitmap coneBitmap, Bitmap starBitmap, int topSpeed){
        this.topSpeed = topSpeed;
        this.size = size;
        this.coneBitmap = coneBitmap;
        this.starBitmap = starBitmap;
        random  = new Random();
        spacing = size * 4;

        list = new LinkedList<Cone>();

        Cone cone = new Cone();
        for(int i=0; i<12; i++){
            list.add(cone);
        }

        currentRect = new RectF();

        setPattern();
    }

    public void setPattern(){
        switch (currentPattern){
            case PATTERN_LINE:
                currentX = linePos[random.nextInt(3)];
                currentY = 0;
                createCount = 0;
                for(Cone cone: list){
                    if(!cone.visible){
                        cone.visible = true;
                        cone.x = currentX;
                        cone.y = currentY;
                        currentY += spacing;
                        createCount += 1;
                    }
                    if(createCount > 3){
                        break;
                    }
                }
                break;
            case PATTERN_DOUBLE:
                break;
            case PATTERN_INOUT:
                break;
        }
    }

    public void update(int w, int h){

        for (Cone cone: list){
            if(cone.visible){
                cone.y += veloY;
                if(cone.y > h + size){
                    cone.visible = false;
                }
            }
        }

        veloY -= .1;
        if(veloY < 0){
            veloY = 0;
        }

        updateVelo = true;

        switch (currentPattern){
            case PATTERN_LINE:
                patternCount += 1;
                if(patternCount>3){
                    patternCount = 0;
                    setPattern();
                }
                break;
            case PATTERN_DOUBLE:
                break;
            case PATTERN_INOUT:
                break;
        }
    }

    public void draw(Canvas canvas){
        update(canvas.getWidth(), canvas.getHeight());
        for(Cone cone: list){
            if(cone.visible){
                currentX = cone.x;
                currentY = cone.y;
                currentRect.set(currentX, currentY, currentX+size, currentY+size);
                if(cone.star)
                    canvas.drawBitmap(starBitmap, null, currentRect, null);
                else
                    canvas.drawBitmap(coneBitmap, null, currentRect, null);
            }
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
        for(Cone cone: list){
            if(cone.visible){
                currentX = cone.x;
                currentY = cone.y;
                currentRect.set(currentX, currentY, currentX + size, currentY+size);
                if(currentRect.intersect(ballRect)){
                    return true;
                }
            }
        }
        return false;
    }
}
