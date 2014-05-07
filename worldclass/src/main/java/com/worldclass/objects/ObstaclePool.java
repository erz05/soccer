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

    private LinkedList<Obstacle> obstacleList;
    private int topSpeed, currentX, currentY, size, posX, posY;
    private Rect currentRect;
    private Rect bitmapRect;
    private Bitmap coneBitmap;
    private Random random;
    private int spawnY = 0;
    private BallPosListener listener;
    private int magicY;
    private Paint paint;
    private int columns = 5;
    private int spriteSize;

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

        obstacleList = new LinkedList<Obstacle>();
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
                obstacle.visible = true;
            }
            obstacle.column = countColumn;
            obstacle.row= countRow;
            countColumn += 1;
            if(countColumn > 4){
                countRow += 1;
                countColumn = 0;
            }
            obstacleList.add(obstacle);
        }

        paint = new Paint();
        paint.setAntiAlias(true);
    }

    public void update(int w, int h){
        for (Obstacle obstacle : obstacleList){
            if(obstacle.visible) {
                obstacle.update(topSpeed, h);
            }
        }

        spawnY += topSpeed;
        if(spawnY > size * 8){
            spawnY = -size;
            coneManger(random.nextInt(2), w);
        }
    }

    public void draw(Canvas canvas, boolean startMoving){
        if(startMoving)
            update(canvas.getWidth(), canvas.getHeight());
        for(Obstacle obstacle : obstacleList){
            if(obstacle.visible) {
                currentX = obstacle.x;
                currentY = obstacle.y;
                currentRect.set(currentX, currentY, currentX + size, currentY + size);
                setBitmapRect(obstacle.row, obstacle.column);
                canvas.drawBitmap(coneBitmap, bitmapRect, currentRect, paint);
                //canvas.drawBitmap(coneBitmap,cone.x,cone.y,null);
            }
        }
    }

    public boolean checkCollision(Rect ballRect){
        for(Obstacle obstacle : obstacleList){
            if(obstacle.visible) {
                currentX = obstacle.x;
                currentY = obstacle.y;
                currentRect.set(currentX, currentY, currentX + size, currentY + size);
                if (currentRect.intersect(ballRect)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void coneManger(int i, int w){
        int count = 0;
        posX = 0;
        switch (i){
            case SINGLE:
                for(Obstacle obstacle : obstacleList){
                    if(!obstacle.visible){
                        obstacle.visible = true;
                        obstacle.y = -size;
                        if(listener != null)
                            posX = listener.getBallX();
                        if(posX > 0)
                            obstacle.x = posX;
                        else
                            obstacle.x = random.nextInt(w - size);
                        break;
                    }
                }
                break;
            case DOUBLE:
                for(Obstacle obstacle : obstacleList){
                    if(!obstacle.visible){
                        obstacle.visible = true;
                        obstacle.y = -size;
                        if(count == 0)
                            posX = random.nextInt(w-size);
                        else
                            posX += size *3;
                        if(posX+size>w)
                            posX = 0;
                        obstacle.x = posX;
                        count += 1;
                    }
                    if(count == 2) break;
                }
                break;
//            case TRIPPLE:
//                for(Cone cone: coneList){
//                    if(!cone.visible){
//                        cone.visible = true;
//                        cone.y = -size;
//                        if(count == 0)
//                            posX = random.nextInt(w-size);
//                        else
//                            posX += size *3;
//                        if(posX+size>w)
//                            posX = 0;
//                        cone.x = posX;
//                        count += 1;
//                    }
//                    if(count == 3) break;
//                }
//                break;
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
