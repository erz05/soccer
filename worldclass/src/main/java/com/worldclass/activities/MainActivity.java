package com.worldclass.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.worldclass.R;
import com.worldclass.listeners.BackgroundListener;
import com.worldclass.listeners.GameListener;
import com.worldclass.listeners.MenuListener;
import com.worldclass.views.Background;
import com.worldclass.views.Game;
import com.worldclass.views.Menu;

public class MainActivity extends Activity implements MenuListener, GameListener, BackgroundListener {
    private Game game;
    private Background background;
    private Menu menu;
    private boolean paused;
    private final String PREFS = "preferencesFile";
    public final static int SOUND_MOVE = 101;
    public final static int SOUND_HIT = 102;
    public final static int SOUND_JUMP = 103;

    private MediaPlayer moveSound;
    private MediaPlayer hitSound;
    private MediaPlayer jumpSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize paused to true
        paused = true;

        FrameLayout gameFrame = (FrameLayout) findViewById(R.id.gameFrame);
        menu = new Menu(this);
        menu.setListener(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        menu.setLayoutParams(params);
        gameFrame.addView(menu);

        Button quit = (Button) findViewById(R.id.quitButton);
        Button resume = (Button) findViewById(R.id.resumeButton);
        Button end = (Button) findViewById(R.id.endButton);
        Button restart = (Button) findViewById(R.id.restartButton);
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //onGameQuit();
                onGameMenu();
            }
        });
        resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onGameResume();
            }
        });
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //onGameQuit();
                onGameMenu();
            }
        });
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restartGame();
            }
        });

        moveSound = MediaPlayer.create(this, R.raw.swosh);
        hitSound = MediaPlayer.create(this, R.raw.hit);
        jumpSound = MediaPlayer.create(this, R.raw.jump);
    }

    @Override
    public void onBackPressed(){
        if(paused){
            super.onBackPressed();
        }else {
            if(game != null){
                game.pause();
                background.pause();
                paused = true;
                LinearLayout pauseLayout = (LinearLayout) findViewById(R.id.pauseMenu);
                pauseLayout.setVisibility(View.VISIBLE);
                TextView scoreView = (TextView) findViewById(R.id.score);
                scoreView.setText("Score: " + background.getScore());
            }
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        game = null;
        menu = null;
        background = null;
    }

    @Override
    public boolean  onTouchEvent (MotionEvent event){
        if(!paused && game != null)
            game.onTouchEvent(event);
        return false;
    }

    @Override
    public void onPlay() {
        SharedPreferences settings = getSharedPreferences(PREFS,0);
        game = new Game(this);
        game.setGameListener(this);
        game.setOptions(settings.getBoolean("sound", false), settings.getBoolean("inverted",false));
        background = new Background(this);
        background.setBackgroundListener(this);
        if(game != null){
            paused = false;
            FrameLayout gameFrame = (FrameLayout) findViewById(R.id.gameFrame);
            gameFrame.removeView(menu);
            if(background != null){
                gameFrame.addView(background);
                background.start();
            }
            gameFrame.addView(game);
            game.start();
        }
    }

    public void restartGame() {
        LinearLayout endMenu = (LinearLayout) findViewById(R.id.endMenu);
        endMenu.setVisibility(View.GONE);
        FrameLayout gameFrame = (FrameLayout) findViewById(R.id.gameFrame);
        if(game != null){
            gameFrame.removeView(game);
        }
        if(background != null){
            gameFrame.removeView(background);
        }
        SharedPreferences settings = getSharedPreferences(PREFS,0);
        game = new Game(this);
        game.setGameListener(this);
        game.setOptions(settings.getBoolean("sound", false), settings.getBoolean("inverted",false));
        background = new Background(this);
        background.setBackgroundListener(this);
        if(background != null){
            gameFrame.addView(background);
            background.start();
        }
        if(game != null){
            paused = false;
            gameFrame.addView(game);
            game.start();
        }
    }

    @Override
    public void onGameOver(final int score) {
        playSound(SOUND_HIT);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(!paused){
                    paused = true;
                    LinearLayout endMenu = (LinearLayout) findViewById(R.id.endMenu);
                    TextView scoreView = (TextView) findViewById(R.id.endScore);
                    TextView highView = (TextView) findViewById(R.id.highScore);

                    scoreView.setText("Score: "+score);

                    if(endMenu.getVisibility() == View.GONE)
                        endMenu.setVisibility(View.VISIBLE);

                    SharedPreferences settings = getSharedPreferences(PREFS,0);
                    int highScore = settings.getInt("highScore", 0);

                    highView.setText("Highscore: "+highScore);

                    if(score > highScore){
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putInt("highScore", score);
                        editor.commit();
                    }
                }
            }
        });
    }

    public void onGameResume() {
        if(game != null){
            game.resume();
            if(background != null){
                background.resume();
            }
            paused = false;
            LinearLayout pauseLayout = (LinearLayout) findViewById(R.id.pauseMenu);
            LinearLayout endLayout = (LinearLayout) findViewById(R.id.endMenu);
            pauseLayout.setVisibility(View.GONE);
            endLayout.setVisibility(View.GONE);
        }
    }

    public void onGameMenu() {
        if(menu != null){
            LinearLayout pauseLayout = (LinearLayout) findViewById(R.id.pauseMenu);
            LinearLayout endLayout = (LinearLayout) findViewById(R.id.endMenu);
            pauseLayout.setVisibility(View.GONE);
            endLayout.setVisibility(View.GONE);
            FrameLayout gameFrame = (FrameLayout) findViewById(R.id.gameFrame);
            gameFrame.removeView(game);
            gameFrame.removeView(background);
            gameFrame.addView(menu);
        }
    }

    @Override
    public void onGameOver() {
        playSound(SOUND_HIT);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(!paused){
                    paused = true;
                    LinearLayout endMenu = (LinearLayout) findViewById(R.id.endMenu);
                    TextView scoreView = (TextView) findViewById(R.id.endScore);
                    TextView highView = (TextView) findViewById(R.id.highScore);

                    int score = 0;
                    if(background != null){
                        score = background.getScore();
                    }

                    scoreView.setText("Score: "+score);

                    if(endMenu.getVisibility() == View.GONE)
                        endMenu.setVisibility(View.VISIBLE);

                    SharedPreferences settings = getSharedPreferences(PREFS,0);
                    int highScore = settings.getInt("highScore", 0);

                    highView.setText("Highscore: "+highScore);

                    if(score > highScore){
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putInt("highScore", score);
                        editor.commit();
                    }
                }
            }
        });
    }

    @Override
    public void addPower() {

    }

    @Override
    public boolean isMoving() {
        if(background != null)
            return background.startMoving;
        return false;
    }

    @Override
    public void playSound(int sound) {
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