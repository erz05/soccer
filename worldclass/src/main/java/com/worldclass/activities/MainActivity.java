package com.worldclass.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.worldclass.R;
import com.worldclass.views.Game;
import com.worldclass.views.MyGLSurfaceView;

public class MainActivity extends Activity {
    private Game game;
    private MyGLSurfaceView myGLSurfaceView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        game = new Game(this);
        myGLSurfaceView = new MyGLSurfaceView(this);

        //mainLayout.addView(myGLSurfaceView);
        mainLayout.addView(game);
    }

    @Override
    public boolean  onTouchEvent (MotionEvent event){
        if(game != null)
            game.onTouchEvent(event);
        return false;
    }
}