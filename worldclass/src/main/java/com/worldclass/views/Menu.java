package com.worldclass.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.worldclass.R;
import com.worldclass.listeners.MenuListener;

/**
 * Created by erz on 2/22/14.
 */
public class Menu extends LinearLayout {

    private Context context;
    private MenuListener listener;
    private final String PREFS = "preferencesFile";
    private boolean sound, inverted;

    public Menu(Context context) {
        super(context);

        this.context = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.menu, this, true);

        final LinearLayout optionsMenu = (LinearLayout) findViewById(R.id.optionsMenu);
        final CheckBox soundCheck = (CheckBox) findViewById(R.id.soundCheck);
        final CheckBox invertCheck = (CheckBox) findViewById(R.id.invertCheck);

        SharedPreferences settings = context.getSharedPreferences(PREFS, 0);
        sound = settings.getBoolean("sound", false);
        inverted = settings.getBoolean("inverted", false);

        soundCheck.setChecked(sound);
        invertCheck.setChecked(inverted);

        soundCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setSound(b);
            }
        });

        invertCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setInverted(b);
            }
        });

        ImageView play = (ImageView) findViewById(R.id.play);
        play.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    optionsMenu.setVisibility(GONE);
                    listener.onPlay();
                }
            }
        });

        ImageView options = (ImageView) findViewById(R.id.options);
        options.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                int open = optionsMenu.getVisibility();
                if(open == GONE)
                    optionsMenu.setVisibility(VISIBLE);
                else if(open == VISIBLE)
                    optionsMenu.setVisibility(GONE);
            }
        });
    }

    public void setSound(boolean b){
        SharedPreferences settings = context.getSharedPreferences(PREFS, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("sound", b);
        editor.commit();
    }

    public void setInverted(boolean b){
        SharedPreferences settings = context.getSharedPreferences(PREFS, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("inverted", b);
        editor.commit();
    }

    public void setListener(MenuListener listener){
        this.listener = listener;
    }
}
