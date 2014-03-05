package com.worldclass.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.worldclass.R;
import com.worldclass.listeners.GameListener;
import com.worldclass.listeners.MenuListener;
import com.worldclass.views.Game;
import com.worldclass.views.Menu;

public class MainActivity extends Activity implements MenuListener, GameListener {
    private Game game;
    private Menu menu;
    private boolean paused;
    private final String PREFS = "preferencesFile";

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
                onGameQuit();
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
                onGameQuit();
            }
        });
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restartGame();
            }
        });
    }

    @Override
    public void onBackPressed(){
        if(paused){
            super.onBackPressed();
        }else {
            if(game != null){
                game.pause();
                paused = true;
                LinearLayout pauseLayout = (LinearLayout) findViewById(R.id.pauseMenu);
                pauseLayout.setVisibility(View.VISIBLE);
                TextView scoreView = (TextView) findViewById(R.id.score);
                scoreView.setText("Score: "+game.getScore());
            }
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        game = null;
        menu = null;
    }

    @Override
    public boolean  onTouchEvent (MotionEvent event){
        if(!paused && game != null)
            game.onTouchEvent(event);
        return false;
    }

    @Override
    public void onPlay() {
        game = new Game(this);
        game.setGameListener(this);
        if(game != null){
            paused = false;
            FrameLayout gameFrame = (FrameLayout) findViewById(R.id.gameFrame);
            gameFrame.removeView(menu);
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
        game = new Game(this);
        game.setGameListener(this);
        if(game != null){
            paused = false;
            gameFrame.addView(game);
            game.start();
        }
    }

    @Override
    public void onGameOver(final int score) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(!paused){
                    paused = true;
                    LinearLayout endMenu = (LinearLayout) findViewById(R.id.endMenu);
                    TextView scoreView = (TextView) findViewById(R.id.endScore);
                    TextView highView = (TextView) findViewById(R.id.highScore);

                    scoreView.setText("Score: "+score);
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
            paused = false;
            LinearLayout pauseLayout = (LinearLayout) findViewById(R.id.pauseMenu);
            pauseLayout.setVisibility(View.GONE);
        }
    }

    public void onGameQuit() {
        onBackPressed();
    }
}