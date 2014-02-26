package com.worldclass.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.worldclass.R;
import com.worldclass.listeners.GameListener;
import com.worldclass.listeners.MenuListener;
import com.worldclass.listeners.PauseListener;
import com.worldclass.views.Game;
import com.worldclass.views.Menu;
import com.worldclass.views.Pause;

public class MainActivity extends Activity implements MenuListener, GameListener, PauseListener {
    private Game game;
    private Menu menu;
    private Pause pauseView;
    private boolean paused = true;
    private OrientationEventListener orientationEventListener;
    private int orientation = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        game = new Game(this);
        game.setGameListener(this);
        menu = new Menu(this);
        menu.setListener(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        menu.setLayoutParams(params);
        mainLayout.addView(menu);

        orientationEventListener = new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int i) {
                orientation = i;
                //Log.v("DELETE_THIS", "i = "+i);
            }
        };

        pauseView = new Pause(this);
        pauseView.setListener(this);
    }

    @Override
    public void onBackPressed(){
        if(paused){
            super.onBackPressed();
        }else {
            if(game != null){
                game.pause();
                paused = true;
                LinearLayout mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
                if(pauseView != null){
                    mainLayout.addView(pauseView);
                }
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
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        mainLayout.removeView(menu);
        mainLayout.addView(game);
        game.start();
    }

    @Override
    public int getAngle() {
        return orientation;
    }

    @Override
    public void onGameResume() {
        if(game != null){
            game.resume();
        }
        if(pauseView != null){
            LinearLayout mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
            mainLayout.removeView(pauseView);
        }
    }

    @Override
    public void onGameQuit() {
        onBackPressed();
    }
}