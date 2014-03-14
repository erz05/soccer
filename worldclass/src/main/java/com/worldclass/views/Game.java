package com.worldclass.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.worldclass.R;
import com.worldclass.listeners.GameListener;
import com.worldclass.listeners.SoundListener;
import com.worldclass.objects.Ball;
import com.worldclass.objects.ConesStars;
import com.worldclass.objects.Floor;
import com.worldclass.objects.PowerBar;
import com.worldclass.utils.GameLoopThread;

/**
 * Created by erz on 2/19/14.
 */
public class Game extends SurfaceView implements SurfaceHolder.Callback, GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener, SoundListener {

    private GameLoopThread gameLoopThread;
    private Ball ball;
    private Floor floor;
    private PowerBar powerBar;
    private ConesStars conesStars;
    private GestureDetector detector;
    private GameListener gameListener;
    private boolean isGameOver = false;

    private float initY;

    public final static int MOVE_LEFT = 0;
    public final static int MOVE_RIGHT = 1;
    public final static int SOUND_MOVE = 101;
    public final static int SOUND_HIT = 102;
    public final static int SOUND_JUMP = 103;

    private MediaPlayer moveSound;
    private MediaPlayer hitSound;
    private MediaPlayer jumpSound;

    private boolean playSound, invertControls;

    //count down
    private Paint messagePaint;
    private int countdown = 3;
    private int time = 0;
    private boolean startMoving = false;

    public Game(Context context) {
        super(context);

        moveSound = MediaPlayer.create(context, R.raw.swosh);
        hitSound = MediaPlayer.create(context, R.raw.hit);
        jumpSound = MediaPlayer.create(context, R.raw.jump);

        gameLoopThread = new GameLoopThread(this);
        SurfaceHolder holder = getHolder();
        if(holder != null)
            holder.addCallback(this);
        detector = new GestureDetector(context, this);

        messagePaint = new Paint();
        messagePaint.setStrokeWidth(5);
        messagePaint.setAntiAlias(true);
        messagePaint.setColor(Color.WHITE);
        messagePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        messagePaint.setTextSize(100);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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
                    gameOver();
                }
            }

            if(conesStars != null)
                conesStars.draw(canvas, startMoving);
            if(ball != null)
                ball.draw(canvas);

            if(conesStars != null && ball != null && ball.getUpScale() == 1){
                if(conesStars.checkCollision(ball.getBounds())){
                    gameOver();
                    ball.changeColor(Color.RED);
                }
                if(conesStars.checkStarCollision(ball.getBounds()))
                    powerBar.addPower();
            }
        }
    }

    public void gameOver(){
        playSound(SOUND_HIT);
        if(gameListener != null){
            isGameOver = true;
            gameListener.onGameOver(floor.getYards());
        }
    }

    public void setGameListener(GameListener gameListener){
        this.gameListener = gameListener;
    }

    public void setOptions(boolean playSound, boolean invertControls){
        this.playSound = playSound;
        this.invertControls = invertControls;
    }

    public int getScore(){
        if(floor != null)
            return floor.getYards();
        return 0;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(!isGameOver){
            detector.onTouchEvent(event);

            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    initY = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    float motionX = event.getX();
                    if(event.getY() > initY){
                        if(ball != null){
                            float half = getWidth()/2;
                            if(motionX  > half){
                                ball.fling(MOVE_RIGHT, 0, invertControls);
                            }else if(motionX < half){
                                ball.fling(MOVE_LEFT, 0, invertControls);
                            }
                        }
                    }
                    break;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float v, float v2) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float xv, float yv) {
        return false;
    }

    public void start(){
        if(gameLoopThread == null){
            gameLoopThread = new GameLoopThread(this);
        }
        if(!gameLoopThread.isRunning()){
            gameLoopThread.setRunning(true);
            gameLoopThread.start();
        }

        if(ball != null){
            ball.changeColor(Color.BLACK);
        }
    }

    public void pause() {
        if(gameLoopThread != null && gameLoopThread.isRunning()){
            gameLoopThread.setRunning(false);
            boolean retry = true;
            while (retry) {
                try {
                    gameLoopThread.join();
                    retry = false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            gameLoopThread = null;
        }
    }

    public void resume(){
        if(gameLoopThread == null){
            gameLoopThread = new GameLoopThread(this);
        }else {
            gameLoopThread = null;
            gameLoopThread = new GameLoopThread(this);
        }
        if(!gameLoopThread.isRunning()){
            gameLoopThread.setRunning(true);
            gameLoopThread.start();
        }
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        if(ball != null)
            ball.jump();
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {
        return false;
    }

    @Override
    public void playSound(int sound) {
        if(!isGameOver && playSound){
            switch (sound){
                case SOUND_MOVE:
                    moveSound.start();
                    break;
                case SOUND_HIT:
                    hitSound.start();
                    break;
                case SOUND_JUMP:
                    jumpSound.start();
                    break;
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        int radius = getHeight()/30;
        int coneSize = getHeight()/20;
        int jumpHeight = getHeight()/44;
        int topSpeed = getHeight()/80;

        float speedX = getWidth()/185;

        int newX = getWidth()/2 - radius;

        powerBar = new PowerBar(getWidth()/2+radius, radius, getWidth()/2-radius*2, radius);
        Bitmap ballBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.soccer_ball);
        ball = new Ball(newX,getHeight()-(radius*4),radius,true,ballBitmap,jumpHeight, speedX);
        ball.setListener(this);
        floor = new Floor(getHeight(), getWidth(), radius, topSpeed);
        Bitmap coneBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cone);
        Bitmap starBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.star);
        conesStars = new ConesStars(getWidth(), getHeight(), coneSize, coneBitmap, starBitmap, topSpeed);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if(gameLoopThread != null){
            boolean retry = true;
            gameLoopThread.setRunning(false);
            while (retry) {
                try {
                    gameLoopThread.join();
                    retry = false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}