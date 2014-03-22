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

    private boolean gameStarted = false;

    int mLineWidth;
    float mMinStep;
    float mMaxStep;

    boolean mInitialized;
    final MovingPoint mPoint1 = new MovingPoint();
    final MovingPoint mPoint2 = new MovingPoint();

    static final int NUM_OLD = 100;
    int mNumOld = 0;
    final float[] mOld = new float[NUM_OLD*4];
    final int[] mOldColor = new int[NUM_OLD];
    int mBrightLine = 0;

    // X is red, Y is blue.
    final MovingPoint mColor = new MovingPoint();

    final Paint mBackground = new Paint();
    final Paint mForeground = new Paint();

    private BackgroundListener backgroundListener;

    public Background(Context context) {
        super(context);

        messagePaint = new Paint();
        messagePaint.setStrokeWidth(5);
        messagePaint.setAntiAlias(true);
        messagePaint.setColor(Color.WHITE);
        messagePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        messagePaint.setTextSize(100);

        mLineWidth = (int)(getResources().getDisplayMetrics().density * 1.5);
        if (mLineWidth < 1) mLineWidth = 1;
        mMinStep = mLineWidth * 2;
        mMaxStep = mMinStep * 3;

        mBackground.setColor(0xff000000);
        mForeground.setColor(0xff00ffff);
        mForeground.setAntiAlias(false);
        mForeground.setStrokeWidth(mLineWidth);
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

    private void update(int width, int height){
        if(gameStarted){
            if(countdown > -1){
                if(time > 30){
                    countdown -= 1;
                    time = 0;
                }else {
                    time += 1;
                }
            }
        }

        if (!mInitialized) {
            mInitialized = true;
            mPoint1.init(width, height, mMinStep);
            mPoint2.init(width, height, mMinStep);
            mColor.init(127, 127, 1);
        } else {
            mPoint1.step(width, height,
                    mMinStep, mMaxStep);
            mPoint2.step(width, height,
                    mMinStep, mMaxStep);
            mColor.step(127, 127, 1, 3);
        }
        mBrightLine+=2;
        if (mBrightLine > (NUM_OLD*2)) {
            mBrightLine = -2;
        }
    }

    int makeGreen(int index) {
        int dist = Math.abs(mBrightLine-index);
        if (dist > 10) return 0;
        return (255-(dist*(255/10))) << 8;
    }

    @Override
    public void onDraw(Canvas canvas){
        if(canvas != null){
            update(canvas.getWidth(), canvas.getHeight());
            canvas.drawColor(Color.BLACK);

            for (int i=mNumOld-1; i>=0; i--) {
                mForeground.setColor(mOldColor[i] | makeGreen(i));
                mForeground.setAlpha(((NUM_OLD-i) * 255) / NUM_OLD);
                int p = i*4;
                canvas.drawLine(mOld[p], mOld[p+1], mOld[p+2], mOld[p+3], mForeground);
            }

            // Draw new line.
            int red = (int)mColor.x + 128;
            if (red > 255) red = 255;
            int blue = (int)mColor.y + 128;
            if (blue > 255) blue = 255;
            int color = 0xff000000 | (red<<16) | blue;
            mForeground.setColor(color | makeGreen(-2));
            canvas.drawLine(mPoint1.x, mPoint1.y, mPoint2.x, mPoint2.y, mForeground);

            // Add in the new line.
            if (mNumOld > 1) {
                System.arraycopy(mOld, 0, mOld, 4, (mNumOld-1)*4);
                System.arraycopy(mOldColor, 0, mOldColor, 1, mNumOld-1);
            }
            if (mNumOld < NUM_OLD) mNumOld++;
            mOld[0] = mPoint1.x;
            mOld[1] = mPoint1.y;
            mOld[2] = mPoint2.x;
            mOld[3] = mPoint2.y;
            mOldColor[0] = color;

            if(gameStarted){
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
    }

    public void reset(){
        startMoving = false;
        countdown = 3;
        time = 0;
        gameStarted = false;
        floor.reset();
        powerBar.reset();
    }

    public int getScore(){
        if(floor != null)
            return floor.getYards();
        return 0;
    }

    public void addPower() {
        if(powerBar != null)
            powerBar.addPower();
    }

    public void setGameStarted(boolean gameStarted){
        this.gameStarted = gameStarted;
    }

    public void gamePaused(){
        this.gameStarted = false;
    }

    public void gameResume(){
        this.gameStarted = true;
    }

    static final class MovingPoint {
        float x, y, dx, dy;

        void init(int width, int height, float minStep) {
            x = (float)((width-1)*Math.random());
            y = (float)((height-1)*Math.random());
            dx = (float)(Math.random()*minStep*2) + 1;
            dy = (float)(Math.random()*minStep*2) + 1;
        }

        float adjDelta(float cur, float minStep, float maxStep) {
            cur += (Math.random()*minStep) - (minStep/2);
            if (cur < 0 && cur > -minStep) cur = -minStep;
            if (cur >= 0 && cur < minStep) cur = minStep;
            if (cur > maxStep) cur = maxStep;
            if (cur < -maxStep) cur = -maxStep;
            return cur;
        }

        void step(int width, int height, float minStep, float maxStep) {
            x += dx;
            if (x <= 0 || x >= (width-1)) {
                if (x <= 0) x = 0;
                else if (x >= (width-1)) x = width-1;
                dx = adjDelta(-dx, minStep, maxStep);
            }
            y += dy;
            if (y <= 0 || y >= (height-1)) {
                if (y <= 0) y = 0;
                else if (y >= (height-1)) y = height-1;
                dy = adjDelta(-dy, minStep, maxStep);
            }
        }
    }
}
