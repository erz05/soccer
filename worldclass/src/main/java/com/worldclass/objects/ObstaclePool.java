package com.worldclass.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.worldclass.listeners.BallPosListener;

import java.util.LinkedList;
import java.util.Random;

/**
 * Created by erz on 2/25/14.
 */
public class ObstaclePool {

    private LinkedList<Obstacle> sleeping, awake, deleteList;
    private int topSpeed, currentX, currentY, size, posX, posY;
    private Rect currentRect;
    private Rect bitmapRect;
    private Bitmap coneBitmap;
    private Random random;
    private int spawnY = 0;
    private BallPosListener listener;
    private int magicY;
    private int columns = 5;
    private int spriteSize;
    private Obstacle temp;
    Paint paint;

    private final static int SINGLE = 0;
    private final static int DOUBLE = 1;
//    private final static int TRIPPLE = 2;
//    private final static int NET = 3;

    public ObstaclePool(int w, int h, int size, Bitmap coneBitmap, int topSpeed){
        this.topSpeed = topSpeed;
        this.size = size*2;
        this.coneBitmap = coneBitmap;
        spawnY = -size;
        spriteSize = coneBitmap.getWidth()/columns;
        random = new Random();
        bitmapRect = new Rect();

        magicY = coneBitmap.getHeight()+(Math.abs(size-coneBitmap.getWidth()));

        sleeping = new LinkedList<Obstacle>();
        awake = new LinkedList<Obstacle>();
        deleteList = new LinkedList<Obstacle>();

        Obstacle obstacle;
        posX = random.nextInt(w - size);
        posY = 0;
        currentRect = new Rect();
        int countRow=0, countColumn=0;
        for(int i=0; i<10; i++){
            obstacle = new Obstacle(0, 0, size);
            if(i == 0){
                obstacle.x = posX;
                obstacle.y = posY-size*2;
            }
            obstacle.column = countColumn;
            obstacle.row= countRow;
            countColumn += 1;
            if(countColumn > 4){
                countRow += 1;
                countColumn = 0;
            }
            sleeping.add(obstacle);
        }

        paint = new Paint();
        paint.setAntiAlias(true);
    }

    public void update(int w, int h){
        for (Obstacle obstacle : awake){
            obstacle.y += topSpeed;

            if (obstacle.y > h + size) {
                deleteList.add(obstacle);
            }
        }

        sleeping.addAll(deleteList);
        awake.removeAll(deleteList);
        deleteList.clear();

        spawnY += topSpeed;
        if(spawnY > size * 8){
            spawnY = -size;
            coneManger(random.nextInt(2), w);
        }
    }

    public void draw(Canvas canvas, boolean startMoving){
        if(startMoving)
            update(canvas.getWidth(), canvas.getHeight());
        for(Obstacle obstacle : awake){
            currentX = obstacle.x;
            currentY = obstacle.y;
            currentRect.set(currentX, currentY, currentX + size, currentY + size);
            setBitmapRect(obstacle.row, obstacle.column);
            canvas.drawBitmap(coneBitmap, bitmapRect, currentRect, paint);
        }
    }

    public boolean checkCollision(Rect ballRect){
        boolean hit = false;
        for(Obstacle obstacle : awake){
            currentX = obstacle.x;
            currentY = obstacle.y;
            currentRect.set(currentX, currentY, currentX + size, currentY + size);
            if (currentRect.intersect(ballRect)) {
                deleteList.add(obstacle);
                hit = true;
            }
        }
        sleeping.addAll(deleteList);
        awake.removeAll(deleteList);
        deleteList.clear();
        return hit;
    }

    public void coneManger(int i, int w){
        int count = 0;
        posX = 0;
        switch (i){
            case SINGLE:
                temp = sleeping.poll();
                if(temp != null){
                    temp.y = -size;
                    if(listener != null)
                        posX = listener.getBallX();
                    if(posX > 0)
                        temp.x = posX;
                    else
                        temp.x = random.nextInt(w - size);

                    awake.add(temp);
                }
                break;
            case DOUBLE:
                int tX = -1;
                for(int j=0; j<2; j++){
                    temp = sleeping.poll();
                    if(temp != null){
                        temp.y = -size;
                        if(tX == -1)
                            tX = random.nextInt(w-size);
                        else
                            tX = tX + (size * 3);
                        if(tX > w-size){
                            tX = random.nextInt((w-(size*3))-size);
                        }
                        if(tX > 0)
                            temp.x = tX;
                        else
                            temp.x = random.nextInt(w - size);

                        awake.add(temp);
                    }
                }
                break;
        }
    }

    public void setListener(BallPosListener listener){
        this.listener = listener;
    }

    public void setBitmapRect(int r, int c){
        int left, top=0, right, bottom=0;

        if(r == 0){
            top = 0;
            bottom = spriteSize;
        }

        if(r == 1){
            top = spriteSize;
            bottom = spriteSize*2;
        }

        if(c == 0){
            left = 0;
            right = spriteSize;
        }else {
            left = c * spriteSize;
            right = (c * spriteSize) + spriteSize;
        }

        bitmapRect.set(left,top,right,bottom);
    }
}
