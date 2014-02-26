package com.worldclass.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.worldclass.R;
import com.worldclass.listeners.PauseListener;

/**
 * Created by erz on 2/25/14.
 */
public class Pause extends LinearLayout {

    private PauseListener listener;

    public Pause(Context context) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.pause, this, true);

        Button resume = (Button) findViewById(R.id.resume);
        Button quit = (Button) findViewById(R.id.quit);

        resume.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null){
                    listener.onGameResume();
                }
            }
        });

        quit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null){
                    listener.onGameQuit();
                }
            }
        });
    }

    public void setListener(PauseListener listener){
        this.listener = listener;
    }
}
