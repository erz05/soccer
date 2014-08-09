package com.worldclass.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;

import com.worldclass.listeners.GameListener;
import com.worldclass.listeners.SoundListener;
import com.worldclass.objects.Smile;

import java.util.LinkedList;

/**
 * Created by erz on 2/19/14.
 */
public class Game extends MyView implements SoundListener {

    private Smile smile;
    private GameListener gameListener;
    private Paint paint;

    float wEight, hFourth, width, height, speed, finishSize, strokeWidth;

    public Game(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void draw(Canvas canvas){
        if(canvas != null){
            canvas.drawColor(Color.BLACK);

            if(smile != null) {
                smile.draw(canvas);
            }
        }
    }

    @Override
    public void createObjects() {
        width = getWidth();
        height = getHeight();
        float size = height/15;
        strokeWidth = height/100;
        wEight = width/8;
        hFourth = height/4;
        finishSize = hFourth/7;

        speed = height/100;

        Log.v("DELETE_THIS", "speed = "+speed);

        float newX = width/2 + size + finishSize;

        smile = new Smile(width/2, height/2 ,size,true);

        paint = new Paint();
        paint.setStrokeWidth(getWidth()/400);
        paint.setAntiAlias(false);
        paint.setDither(false);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setTextSize(getHeight()/10);
        paint.setTextAlign(Paint.Align.CENTER);
    }

    public void setGameListener(GameListener gameListener){
        this.gameListener = gameListener;
    }

    public void setOptions(boolean invertControls){}

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(!gameListener.getIsGameOver()){
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    if(smile != null) {
                        smile.setPosition(event.getX(), event.getY());
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if(smile != null){
                        smile.setPosition(event.getX(), event.getY());
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if(smile != null){
                    }
                    break;
            }
            return true;
        }
        return false;
    }

    @Override
    public void start(){
        super.start();
        if(smile != null){
        }
    }

    public int getScore(){
        return 0;
    }

    @Override
    public void playSound(int sound) {
        gameListener.playSound(sound);
    }
}