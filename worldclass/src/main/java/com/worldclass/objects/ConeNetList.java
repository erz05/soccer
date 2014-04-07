package com.worldclass.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.worldclass.listeners.BallPosListener;

import java.util.LinkedList;
import java.util.Random;

/**
 * Created by erz on 2/25/14.
 */
public class ConeNetList {

    private LinkedList<Cone> coneList;
    private int topSpeed, currentX, currentY, size, posX, posY;
    private Rect currentRect;
    private Bitmap coneBitmap;
    private Bitmap netBitmap;
    private Net net;
    private Random random;
    private int spawnY = 0;
    private BallPosListener listener;

    private final static int SINGLE = 0;
    private final static int DOUBLE = 1;
    private final static int TRIPPLE = 2;
    private final static int NET = 3;

    public ConeNetList(int w, int h, int size, Bitmap coneBitmap, int topSpeed, Bitmap netBitmap){
        this.topSpeed = topSpeed;
        this.size = size;
        this.coneBitmap = coneBitmap;
        this.netBitmap = netBitmap;
        spawnY = -size;

        random = new Random();

        coneList = new LinkedList<Cone>();
        Cone cone;
        posX = random.nextInt(w - size);
        posY = 0;
        currentRect = new Rect();
        for(int i=0; i<12; i++){
            cone = new Cone(0, 0, size);
            if(i == 0){
                cone.x = posX;
                cone.y = posY-size;
                cone.visible = true;
            }
            coneList.add(cone);
        }
        net = new Net(posX, -size, size*3, size);
    }

    public void update(int w, int h){
        for (Cone cone: coneList){
            if(cone.visible) {
                cone.update(topSpeed, h);
            }
        }
        if(net.visible) {
            net.update(topSpeed, h);
        }

        spawnY += topSpeed;
        if(spawnY > size * 8){
            spawnY = -size;
            if(net.visible)
                coneManger(random.nextInt(3),w);
            else
                coneManger(random.nextInt(4), w);
        }
    }

    public void draw(Canvas canvas, boolean startMoving){
        if(startMoving)
            update(canvas.getWidth(), canvas.getHeight());
        for(Cone cone: coneList){
            if(cone.visible) {
                currentX = cone.x;
                currentY = cone.y;
                currentRect.set(currentX, currentY, currentX + size, currentY + size);
                canvas.drawBitmap(coneBitmap, null, currentRect, null);
            }
        }
        if(net.visible){
            currentRect.set(net.x, net.y, net.x+(size*3), net.y+size);
            canvas.drawBitmap(netBitmap, null, currentRect, null);
        }
    }

    public boolean checkCollision(Rect ballRect){
        for(Cone cone: coneList){
            if(cone.visible) {
                currentX = cone.x;
                currentY = cone.y;
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
                for(Cone cone: coneList){
                    if(!cone.visible){
                        cone.visible = true;
                        cone.y = -size;
                        if(listener != null)
                            posX = listener.getBallX();
                        if(posX > 0)
                            cone.x = posX;
                        else
                            cone.x = random.nextInt(w - size);
                        break;
                    }
                }
                break;
            case DOUBLE:
                for(Cone cone: coneList){
                    if(!cone.visible){
                        cone.visible = true;
                        cone.y = -size;
                        if(count == 0)
                            posX = random.nextInt(w-size);
                        else
                            posX += size *3;
                        if(posX+size>w)
                            posX = 0;
                        cone.x = posX;
                        count += 1;
                    }
                    if(count == 2) break;
                }
                break;
            case TRIPPLE:
                for(Cone cone: coneList){
                    if(!cone.visible){
                        cone.visible = true;
                        cone.y = -size;
                        if(count == 0)
                            posX = random.nextInt(w-size);
                        else
                            posX += size *3;
                        if(posX+size>w)
                            posX = 0;
                        cone.x = posX;
                        count += 1;
                    }
                    if(count == 3) break;
                }
                break;
            case NET:
                net.visible = true;
                net.y = -size;
                net.x = random.nextInt(w-size*3);
                break;
        }
    }

    public void setListener(BallPosListener listener){
        this.listener = listener;
    }
}
