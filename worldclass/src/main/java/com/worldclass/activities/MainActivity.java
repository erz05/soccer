package com.worldclass.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.worldclass.R;
import com.worldclass.listeners.GameListener;
import com.worldclass.listeners.MenuListener;
import com.worldclass.utils.MusicPlayer;
import com.worldclass.views.Game;
import com.worldclass.views.Menu;

/*TODO
1. use different way to calculate score, use time?
2. have background stay, dont add or remove, just call reset when need too?
3. game reset instaed of adding and removing everytime
4. fix menu's
5. show touches maybe
 */

public class MainActivity extends Activity implements MenuListener, GameListener {
    private Game game;
    private Menu menu;
    private boolean paused;
    private final String PREFS = "preferencesFile";
    private MusicPlayer musicPlayer;
    private boolean playSounds;
    private LinearLayout pauseMenu;
    private static final int MENU_PAUSE = 0;
    private static final int MENU_END = 1;
    private static final int MENU_CLOSE = 2;
    private boolean isGameOver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize paused to true
        paused = true;

        FrameLayout gameFrame = (FrameLayout) findViewById(R.id.gameFrame);
        menu = new Menu(this);
        menu.setListener(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        menu.setLayoutParams(params);
        gameFrame.addView(menu);

        pauseMenu = (LinearLayout) findViewById(R.id.pauseMenu);
        Button quit = (Button) findViewById(R.id.quitButton);
        Button resume = (Button) findViewById(R.id.resumeButton);
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onGameMenu();
            }
        });
        resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isGameOver)
                    restartGame();
                else
                    onGameResume();
            }
        });

        SharedPreferences settings = getSharedPreferences(PREFS,0);
        playSounds = settings.getBoolean("sound", false);
        musicPlayer = new MusicPlayer(this);
    }

    @Override
    public void onBackPressed(){
        if(paused){
            if(musicPlayer != null)
                musicPlayer.stopALL();
            super.onBackPressed();
        }else {
            if(game != null){
                game.pause();
                paused = true;
                menuShower(MENU_PAUSE, game.getScore(), 0);
            }
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        game = null;
        menu = null;
    }

    public void menuShower(int i, int score, int highScore){
        if(pauseMenu != null){
            TextView title = (TextView) findViewById(R.id.title);
            TextView scoreView = (TextView) findViewById(R.id.score);
            TextView highView = (TextView) findViewById(R.id.highScore);
            Button resume = (Button) findViewById(R.id.resumeButton);
            switch (i){
                case MENU_PAUSE:
                    pauseMenu.setVisibility(View.VISIBLE);
                    scoreView.setText("Score: " + game.getScore());
                    break;
                case MENU_END:
                    pauseMenu.setVisibility(View.VISIBLE);
                    title.setText("Game Over");
                    scoreView.setText("Score: "+score);
                    highView.setText("Highscore: "+highScore);
                    highView.setVisibility(View.VISIBLE);
                    resume.setText("Restart");
                    break;
                case MENU_CLOSE:
                    pauseMenu.setVisibility(View.GONE);
                    title.setText("Game Paused");
                    highView.setVisibility(View.GONE);
                    resume.setText("Resume");
                    break;
            }
        }
    }

    @Override
    public boolean  onTouchEvent (MotionEvent event){
        if(!paused && game != null)
            game.onTouchEvent(event);
        return false;
    }

    @Override
    public void onPlay() {
        isGameOver = false;
        SharedPreferences settings = getSharedPreferences(PREFS,0);
        playSounds = settings.getBoolean("sound", false);
        game = new Game(this);
        game.setGameListener(this);
        game.setOptions(settings.getBoolean("inverted",false));
        if(game != null){
            paused = false;
            FrameLayout gameFrame = (FrameLayout) findViewById(R.id.gameFrame);
            gameFrame.removeView(menu);
            gameFrame.addView(game);
            game.start();
            menu = null;
        }
    }

    public void restartGame() {
        isGameOver = false;
        menuShower(MENU_CLOSE,0,0);
        FrameLayout gameFrame = (FrameLayout) findViewById(R.id.gameFrame);
        if(game != null){
            gameFrame.removeView(game);
        }
        SharedPreferences settings = getSharedPreferences(PREFS,0);
        playSounds = settings.getBoolean("sound", false);
        game = new Game(this);
        game.setGameListener(this);
        game.setOptions(settings.getBoolean("inverted",false));
        if(game != null){
            paused = false;
            gameFrame.addView(game);
            game.start();
        }
    }

    @Override
    public void onGameOver(final int score) {
        playSound(MusicPlayer.SOUND_HIT);
        isGameOver = true;
        if(game != null){
            game.gameStarted = false;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(!paused){
                    paused = true;

                    SharedPreferences settings = getSharedPreferences(PREFS,0);
                    int highScore = settings.getInt("highScore", 0);
                    menuShower(MENU_END, score, highScore);

                    if(score > highScore){
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putInt("highScore", score);
                        editor.commit();
                    }
                }
            }
        });
    }

    public void onGameResume() {
        if(game != null){
            game.resume();
            paused = false;
            menuShower(MENU_CLOSE,0,0);
        }
    }

    public void onGameMenu() {
        if(menu == null) {
            menu = new Menu(this);
            menu.setListener(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            menu.setLayoutParams(params);
        }
        menuShower(MENU_CLOSE,0,0);
        FrameLayout gameFrame = (FrameLayout) findViewById(R.id.gameFrame);
        gameFrame.removeView(game);
        gameFrame.addView(menu);
        game = null;
    }

    @Override
    public void playSound(int sound) {
        if(!isGameOver && playSounds && musicPlayer != null){
            musicPlayer.playSound(sound);
        }
    }

    @Override
    public boolean getIsGameOver() {
        return isGameOver;
    }
}