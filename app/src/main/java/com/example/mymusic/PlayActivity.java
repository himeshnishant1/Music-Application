package com.example.mymusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

public class PlayActivity extends AppCompatActivity {

    private TextView tvSongName, tvTimePlayed, tvTotalTime;
    private ImageButton ibPlayPause, ibFastRewind, ibSkipPrevious, ibSkipNext, ibFastForward;

    private ImageView ivLogo;

    private SeekBar sbMusic;
    private ArrayList<MusicModel> songs;
    private String songName;
    private int position;

    private static MediaPlayer mediaPlayer;
    private int PlayPauseState = 0;

    Thread seekbarThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        getSupportActionBar().hide();

        setIDs();

        getIntentData();

        setSong(1);

        ButtonListener();

        configureSeekBar();
    }

    private void configureSeekBar() {
        seekbarThread = new Thread(){
            @Override
            public void run() {
                super.run();
                int totalDuration = mediaPlayer.getDuration();
                int currentPosition = 0;
                while(currentPosition < totalDuration){
                    try {
                        sleep(500);
                        if(mediaPlayer != null) currentPosition = mediaPlayer.getCurrentPosition();
                        sbMusic.setProgress(currentPosition);
                        if(mediaPlayer != null) sbMusic.setMax(mediaPlayer.getDuration());
                    }
                    catch (InterruptedException | IllegalStateException e){
                        e.printStackTrace();
                    }
                }
            }
        };
        if(mediaPlayer != null) sbMusic.setMax(mediaPlayer.getDuration());
        seekbarThread.start();
        sbMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tvTimePlayed.setText(createTime(mediaPlayer.getCurrentPosition()));
                handler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    private void setIDs(){
        tvSongName = findViewById(R.id.tvSongName);
        ibFastRewind = findViewById(R.id.ibFastRewind);
        ibSkipPrevious = findViewById(R.id.ibSkipPrevious);

        ibPlayPause = findViewById(R.id.ibPlayPause);
        ibPlayPause.setImageResource(R.drawable.baseline_pause_24);

        ibSkipNext = findViewById(R.id.ibSkipNext);
        ibFastForward = findViewById(R.id.ibFastForward);
        tvTimePlayed = findViewById(R.id.tvTimePlayed);
        tvTotalTime = findViewById(R.id.tvTotalTime);
        sbMusic = findViewById(R.id.sbMusic);
    }
    private void getIntentData(){
        Intent getData = getIntent();
        songs = (ArrayList<MusicModel>) getData.getSerializableExtra("Songs");
        songName = getData.getStringExtra("SongName");
        position = getData.getIntExtra("pos", -1);
    }

    private void ButtonListener(){
        ibPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(PlayPauseState == 0) {
                    mediaPlayer.pause();
                    ibPlayPause.setImageResource(R.drawable.baseline_play_arrow_24);
                    PlayPauseState = 1;
                }else{
                    mediaPlayer.start();
                    ibPlayPause.setImageResource(R.drawable.baseline_pause_24);
                    PlayPauseState = 0;
                }
            }
        });

        ibSkipNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position >= songs.size() - 1)    position = 0;
                else    position++;
                setSong(1);
            }
        });

        ibSkipPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position <= 0)    position = songs.size() - 1;
                else    position--;
                setSong(-1);
            }
        });

        ibFastForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.getCurrentPosition() / 1000 < mediaPlayer.getDuration() / 1000 - 10) mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 10000);
                sbMusic.setProgress(mediaPlayer.getCurrentPosition());
            }
        });

        ibFastRewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.getCurrentPosition() / 1000  > 10) mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 10000);
                sbMusic.setProgress(mediaPlayer.getCurrentPosition());
            }
        });
    }

    private void setSong(int var){
        if(mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(songs.get(position).getPath()));
        songName = songs.get(position).getName();
        tvSongName.setText(songName.substring(0, songName.lastIndexOf('.')));
        if(mediaPlayer != null) mediaPlayer.start();
        else{
            position += var;
            setSong(var);
        }

        tvTotalTime.setText(createTime(mediaPlayer.getDuration()));

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                System.out.println("Completed");
                ibSkipNext.callOnClick();
            }
        });

        PlayPauseState = 0;
        ibPlayPause.setImageResource(R.drawable.baseline_pause_24);

        configureSeekBar();
    }

    private String createTime(int duration){
        String time = "";
        int min = duration / 1000 / 60;
        int sec = duration / 1000 % 60;

        time = time + min + ":";
        if(sec < 10)    time += "0";
        time = time + sec;

        return time;
    }
}