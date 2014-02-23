package com.worldclass.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.worldclass.R;
import com.worldclass.listeners.MenuListener;
import com.worldclass.views.Game;
import com.worldclass.views.Menu;

public class MainActivity extends Activity implements MenuListener {
    private Game game;
    private Menu menu;
    private boolean pause = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        game = new Game(this);
        menu = new Menu(this);
        menu.setListener(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        menu.setLayoutParams(params);
        mainLayout.addView(menu);
    }

    @Override
    public boolean  onTouchEvent (MotionEvent event){
        if(!pause && game != null)
            game.onTouchEvent(event);
        return false;
    }

    @Override
    public void onPlay() {
        pause = false;
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        mainLayout.removeView(menu);
        mainLayout.addView(game);
    }
}