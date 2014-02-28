package com.worldclass.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.worldclass.R;
import com.worldclass.listeners.GameListener;
import com.worldclass.listeners.MenuListener;
import com.worldclass.views.Game;
import com.worldclass.views.Menu;

public class MainActivity extends Activity implements MenuListener, GameListener {
    private Game game;
    private Menu menu;
    private boolean paused = true;
    private OrientationEventListener orientationEventListener;
    private int orientation = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FrameLayout gameFrame = (FrameLayout) findViewById(R.id.gameFrame);
        game = new Game(this);
        game.setGameListener(this);
        menu = new Menu(this);
        menu.setListener(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        menu.setLayoutParams(params);
        gameFrame.addView(menu);

        orientationEventListener = new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int i) {
                orientation = i;
                //Log.v("DELETE_THIS", "i = "+i);
            }
        };

        Button quit = (Button) findViewById(R.id.quitButton);
        Button resume = (Button) findViewById(R.id.resumeButton);

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
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        orientationEventListener.enable();
    }

    @Override
    public void onPause(){
        super.onPause();
        orientationEventListener.disable();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        game = null;
        menu = null;
        orientationEventListener = null;
    }

    @Override
    public boolean  onTouchEvent (MotionEvent event){
        if(!paused && game != null)
            game.onTouchEvent(event);
        return false;
    }

    @Override
    public void onPlay() {
        paused = false;
        FrameLayout gameFrame = (FrameLayout) findViewById(R.id.gameFrame);
        gameFrame.removeView(menu);
        gameFrame.addView(game);
        game.start();
    }

    @Override
    public int getAngle() {
        return orientation;
    }

    public void onGameResume() {
        if(game != null){
            game.resume();
        }
        paused = false;
        LinearLayout pauseLayout = (LinearLayout) findViewById(R.id.pauseMenu);
        pauseLayout.setVisibility(View.GONE);
    }

    public void onGameQuit() {
        onBackPressed();
    }
}