package com.worldclass.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.worldclass.R;
import com.worldclass.listeners.MenuListener;

/**
 * Created by erz on 2/22/14.
 */
public class Menu extends LinearLayout {

    private MenuListener listener;

    public Menu(Context context) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.menu, this, true);

        ImageView play = (ImageView) findViewById(R.id.play);
        play.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    listener.onPlay();
                }
            }
        });
    }

    public void setListener(MenuListener listener){
        this.listener = listener;
    }
}
