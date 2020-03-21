package com.example.eggtimer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    SeekBar timerSeekBar;
    TextView timerTextView;
    MediaPlayer mediaPlayer;

    boolean timerActive;
    boolean timerIsRunning;
    int currentTime;
    CountDownTimer countDownTimer;

    public void reset() {
        if (timerIsRunning) {
            countDownTimer.cancel();
        }

        timerActive = false;
        timerIsRunning = false;
    }

    public String readableTime(int seconds) {
        int minutes = seconds / 60;
        int newSeconds = seconds % 60;
        if (minutes >= 1) {
            if (newSeconds < 10) {
                String convertedSeconds = "0" + newSeconds;
                return minutes + ":" + convertedSeconds;
            } else {
                return minutes + ":" + newSeconds;
            }
        } else {
            return String.valueOf(newSeconds);
        }
    }

    public void startCounting(View view) {
        timerActive = true;
        currentTime = timerSeekBar.getProgress();

        long currentTimeInMillisecond = currentTime * 1000;

        if (timerActive && !timerIsRunning) {
            countDownTimer = new CountDownTimer(currentTimeInMillisecond, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    timerIsRunning = true;
                    currentTime--;
                    timerSeekBar.setProgress(currentTime);
                    timerTextView.setText(readableTime(timerSeekBar.getProgress()));
                }

                @Override
                public void onFinish() {
                    // setting the alarm clip
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.airhorn);
                    mediaPlayer.start();

                    timerActive = false;
                    timerIsRunning = false;
                }
            }.start();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerActive = false;
        timerIsRunning = false;

        // setting the default max to 36000 secs and current in 15000 secs
        timerSeekBar = (SeekBar) findViewById(R.id.timerSeekBar);
        timerSeekBar.setMax(600);
        timerSeekBar.setProgress(90);

        // handling timerTextView
        timerTextView = (TextView) findViewById(R.id.timerTextView);
        timerTextView.setText(readableTime(timerSeekBar.getProgress()));

        // handling timerSeekBar on progress
        timerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    reset();
                    timerTextView.setText(readableTime(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                reset();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
}
