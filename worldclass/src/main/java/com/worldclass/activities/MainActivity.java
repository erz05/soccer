package com.worldclass.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.worldclass.R;
import com.worldclass.listeners.BackgroundListener;
import com.worldclass.listeners.GameListener;
import com.worldclass.listeners.MenuListener;
import com.worldclass.utils.MusicPlayer;
import com.worldclass.utils.MyGLRenderer;
import com.worldclass.utils.MyGLSurfaceView;
import com.worldclass.views.Background;
import com.worldclass.views.Game;
import com.worldclass.views.Menu;

/*TODO
1. use different way to calculate score, use time?
2. have background stay, dont add or remove, just call reset when need too?
3. game reset instaed of adding and removing everytime
4. fix menu's
5. show touches maybe
 */

public class MainActivity extends Activity implements MenuListener, GameListener, BackgroundListener {
    private Game game;
    private Background background;
    private Menu menu;
    private boolean paused;
    private final String PREFS = "preferencesFile";
    private MusicPlayer musicPlayer;
    private boolean playSounds;

    private MyGLSurfaceView myGLSurfaceView;

    private boolean isGameOver = false;

    static  {
        System.loadLibrary("MyLib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize paused to true
        paused = true;

        FrameLayout gameFrame = (FrameLayout) findViewById(R.id.gameFrame);
        menu = new Menu(this);
        menu.setListener(this);
        background = new Background(this);
        background.setBackgroundListener(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        menu.setLayoutParams(params);
        gameFrame.addView(background);
        background.start();
        gameFrame.addView(menu);

        Button quit = (Button) findViewById(R.id.quitButton);
        Button resume = (Button) findViewById(R.id.resumeButton);
        Button end = (Button) findViewById(R.id.endButton);
        Button restart = (Button) findViewById(R.id.restartButton);
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //onGameQuit();
                onGameMenu();
            }
        });
        resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onGameResume();
            }
        });
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //onGameQuit();
                onGameMenu();
            }
        });
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restartGame();
            }
        });

        SharedPreferences settings = getSharedPreferences(PREFS,0);
        playSounds = settings.getBoolean("sound", false);
        musicPlayer = new MusicPlayer(this);

        //Ndk test
        Log.v("DELETE_THIS", getStringFromNative());

        myGLSurfaceView = new MyGLSurfaceView(this);
        myGLSurfaceView.setEGLContextClientVersion(2);
        myGLSurfaceView.setRenderer(new MyGLRenderer());

        gameFrame.addView(myGLSurfaceView);
    }

    @Override
    public void onResume(){
        super.onResume();
        if(playSounds && musicPlayer != null)
            musicPlayer.playMusic();
        myGLSurfaceView.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
        if(musicPlayer != null)
            musicPlayer.pauseMusic();
        myGLSurfaceView.onPause();
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
                background.gamePaused();
                paused = true;
                LinearLayout pauseLayout = (LinearLayout) findViewById(R.id.pauseMenu);
                pauseLayout.setVisibility(View.VISIBLE);
                TextView scoreView = (TextView) findViewById(R.id.score);
                scoreView.setText("Score: " + background.getScore());
            }
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        game = null;
        menu = null;
        background = null;
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
        }
        if(background != null){
            background.reset();
            background.gameResume();
        }
    }

    @Override
    public void playMusic(boolean on) {
        if(musicPlayer != null){
            if(on){
                musicPlayer.playMusic();
            }else {
                musicPlayer.pauseMusic();
            }
        }
    }

    public void restartGame() {
        isGameOver = false;
        LinearLayout endMenu = (LinearLayout) findViewById(R.id.endMenu);
        endMenu.setVisibility(View.GONE);
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
        if(background != null){
            background.reset();
            background.gameResume();
        }
    }

    @Override
    public void onGameOver(){
        int score = 0;
        if(background != null){
            score = background.getScore();
        }
        onGameOver(score);
    }

    @Override
    public void onGameOver(final int score) {
        playSound(MusicPlayer.SOUND_HIT);
        isGameOver = true;
        if(background != null){
            background.reset();
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(!paused){
                    paused = true;
                    LinearLayout endMenu = (LinearLayout) findViewById(R.id.endMenu);
                    TextView scoreView = (TextView) findViewById(R.id.endScore);
                    TextView highView = (TextView) findViewById(R.id.highScore);

                    scoreView.setText("Score: "+score);

                    if(endMenu.getVisibility() == View.GONE)
                        endMenu.setVisibility(View.VISIBLE);

                    SharedPreferences settings = getSharedPreferences(PREFS,0);
                    int highScore = settings.getInt("highScore", 0);

                    highView.setText("Highscore: "+highScore);

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
            if(background != null){
                background.gameResume();
            }
            paused = false;
            LinearLayout pauseLayout = (LinearLayout) findViewById(R.id.pauseMenu);
            LinearLayout endLayout = (LinearLayout) findViewById(R.id.endMenu);
            pauseLayout.setVisibility(View.GONE);
            endLayout.setVisibility(View.GONE);
        }
    }

    public void onGameMenu() {
        if(menu != null){
            LinearLayout pauseLayout = (LinearLayout) findViewById(R.id.pauseMenu);
            LinearLayout endLayout = (LinearLayout) findViewById(R.id.endMenu);
            pauseLayout.setVisibility(View.GONE);
            endLayout.setVisibility(View.GONE);
            FrameLayout gameFrame = (FrameLayout) findViewById(R.id.gameFrame);
            gameFrame.removeView(game);
            gameFrame.addView(menu);
        }
    }

    @Override
    public void addPower() {
        if(background != null)
            background.addPower();
    }

    @Override
    public boolean isMoving() {
        if(background != null)
            return background.startMoving;
        return false;
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

    public native String getStringFromNative();
}