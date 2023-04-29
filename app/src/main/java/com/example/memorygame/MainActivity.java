package com.example.memorygame;

import androidx.appcompat.app.AppCompatActivity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    int levelnumber = 1;
    String actual_pattern = "";
    int totalVariations = 2;
    int eventCounter = 0;
    int pos=0;
    MediaPlayer media;
    boolean gameActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv = findViewById(R.id.level_number);
        tv.setText("Level 1");
        generatepattern();
    }

    public void generatepattern()
    {
        for(int i=0;i<totalVariations;i++) {
            int r_num = (int)(Math.random()*4);;
            actual_pattern += Integer.toString(r_num);
        }
        generateblink();
    }

    public void generateblink()
    {
        GridLayout gridLayout = findViewById(R.id.grid);
        ImageView img = (ImageView) gridLayout.getChildAt(actual_pattern.charAt(pos)-48);
        Animation animate = AnimationUtils.loadAnimation(this, R.anim.blink);
        img.startAnimation(animate);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pos++;
                if(pos == actual_pattern.length())
                {
                    gameActive = true;
                    return;
                }
                generateblink();
            }
        }, 350);
    }

    public void tapIn(View v)
    {
        TextView t = findViewById(R.id.display);
        ImageView counter = (ImageView) findViewById(v.getId());
        int tappedCounter = Integer.parseInt(counter.getTag().toString());
        if(gameActive)
        {
            playSound();
            Animation animate = AnimationUtils.loadAnimation(this, R.anim.blink);
            counter.startAnimation(animate);
            if(tappedCounter+48 != actual_pattern.charAt(eventCounter))
            {
                t.setText("Pattern Incorrect");
                Button retry = findViewById(R.id.button);
                retry.setText("Retry");
                retry.setVisibility(View.VISIBLE);
                gameActive = false;
            }
            eventCounter++;
            if(eventCounter == actual_pattern.length() && gameActive == true)
            {
                t.setText("Great!");
                Button nextLevel = findViewById(R.id.button);
                nextLevel.setText("Next Level");
                nextLevel.setVisibility(View.VISIBLE);
                gameActive=false;
            }
        }
    }

    public void button(View v)
    {
        Button bt = findViewById(v.getId());
        pos=0;
        bt.setVisibility(View.INVISIBLE);
        TextView t = findViewById(R.id.display);
        t.setText("");
        actual_pattern = "";
        eventCounter = 0;
        if(bt.getText().toString().equals("Next Level"))
        {
            levelnumber++;
            TextView tv = findViewById(R.id.level_number);
            String lvl_num = "Level ";
            lvl_num+=String.valueOf(levelnumber);
            tv.setText(lvl_num);
            if(totalVariations != 10)
                totalVariations++;
        }
        generatepattern();
    }

    public void playSound()
    {
        media= MediaPlayer.create(getApplicationContext(), R.raw.tap_sound);
        media.start();
        media.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            public void onCompletion(MediaPlayer mp) {
                mp.reset();
                mp.release();
                media = null;
            };
        });
    }
}
