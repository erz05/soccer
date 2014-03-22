package com.worldclass.utils;

import android.content.Context;
import android.media.MediaPlayer;

import com.worldclass.R;

/**
 * Created by erz on 3/22/14.
 */
public class MusicPlayer {

    private MediaPlayer moveSound;
    private MediaPlayer hitSound;
    private MediaPlayer jumpSound;
    private MediaPlayer begginOneSound;
    private MediaPlayer begginTwoSound;
    private MediaPlayer erzSound;
    public final static int SOUND_MOVE = 101;
    public final static int SOUND_HIT = 102;
    public final static int SOUND_JUMP = 103;

    public MusicPlayer(Context context){
        moveSound = MediaPlayer.create(context, R.raw.swosh);
        hitSound = MediaPlayer.create(context, R.raw.hit);
        jumpSound = MediaPlayer.create(context, R.raw.jump);
        //begginOneSound = MediaPlayer.create(this, R.raw.beggining_one);
        //begginTwoSound = MediaPlayer.create(this, R.raw.beggining_two);
        erzSound = MediaPlayer.create(context, R.raw.erz);
        erzSound.setLooping(true);
    }

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

    public void playMusic() {
        erzSound.start();
    }

    public void stopALL(){
        erzSound.stop();
        hitSound.stop();
        moveSound.stop();
        jumpSound.stop();
    }

    public void pauseMusic(){
        erzSound.pause();
    }
}
