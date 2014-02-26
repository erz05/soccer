package com.worldclass.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.LinkedList;

/**
 * Created by erz on 2/25/14.
 */
public class Cones {

    private LinkedList<Cone> coneList;
    private boolean updateVelo = true;
    private float veloY = 0;

    public Cones(int w, int h, int size, Bitmap bitmap){

        coneList = new LinkedList<Cone>();
        Cone cone;
        int posx = 100;
        int posy = 0;
        for(int i=0; i<2; i++){
            cone = new Cone(posx,posy-size, size, bitmap);
            coneList.add(cone);
            posx += 400;
            posy -= 500;
        }

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
            cone.draw(canvas);
        }
    }

    public void fling(){
        if(updateVelo){
            veloY += 3;
            if(veloY > 30)
                veloY = 30;
            updateVelo = false;
        }
    }
}
