package com.worldclass.listeners;

/**
 * Created by erz on 2/23/14.
 */
public interface GameListener {
    public void onGameOver(int score);
    public void playSound(int sound);
    public boolean getIsGameOver();
}
