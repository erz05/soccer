package com.worldclass.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.worldclass.listeners.BackgroundListener;
import com.worldclass.objects.Floor;
import com.worldclass.objects.PowerBar;

/**
 * Created by erz on 3/20/14.
 */
public class Background extends MyView {

    private Floor floor;
    private PowerBar powerBar;
    public boolean startMoving = false;
    private Paint messagePaint;
    private int countdown = 3;
    private int time = 0;

    private BackgroundListener backgroundListener;

    public Background(Context context) {
        super(context);

        messagePaint = new Paint();
        messagePaint.setStrokeWidth(5);
        messagePaint.setAntiAlias(true);
        messagePaint.setColor(Color.WHITE);
        messagePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        messagePaint.setTextSize(100);
    }

    public void setBackgroundListener(BackgroundListener backgroundListener){
        this.backgroundListener = backgroundListener;
    }

    @Override
    public void createObjects() {
        int radius = getHeight()/30;
        int topSpeed = getHeight()/80;
        floor = new Floor(getHeight(), getWidth(), radius, topSpeed);
        powerBar = new PowerBar(getWidth()/2+radius, radius, getWidth()/2-radius*2, radius);
    }

    private void update(){
        if(countdown > -1){
            if(time > 30){
                countdown -= 1;
                time = 0;
            }else {
                time += 1;
            }
        }
    }

    @Override
    public void onDraw(Canvas canvas){
        if(canvas != null){
            update();
            canvas.drawColor(Color.parseColor("#22B14C"));
            if(floor != null)
                floor.draw(canvas, startMoving);

            if(countdown > -1){
                if(countdown > 0){
                    canvas.drawText(""+countdown, canvas.getWidth()/2 - ((messagePaint.measureText(""+countdown))/2), canvas.getHeight()/2, messagePaint);
                }else {
                    canvas.drawText("GO!", canvas.getWidth()/2 - ((messagePaint.measureText("GO!"))/2), canvas.getHeight()/2, messagePaint);
                }
            }else {
                startMoving = true;
            }

            if(powerBar != null){
                powerBar.draw(canvas, startMoving);
                if(powerBar.getPower() == 0){
                    if(backgroundListener != null){
                        int score = 0;
                        if(floor != null){
                            score = floor.getYards();
                        }
                        backgroundListener.onGameOver(score);
                    }
                }
            }
        }
    }

    public int getScore(){
        if(floor != null)
            return floor.getYards();
        return 0;
    }
}
