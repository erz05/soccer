package com.worldclass.listeners;

/**
 * Created by erz on 2/23/14.
 */
public interface GameListener {
    public void onGameOver();
    public boolean isMoving();
    public void playSound(int sound);
    public boolean getIsGameOver();
}
