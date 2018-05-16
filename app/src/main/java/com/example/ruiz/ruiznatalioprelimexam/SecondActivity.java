package com.example.ruiz.ruiznatalioprelimexam;

import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Random;

import static com.example.ruiz.ruiznatalioprelimexam.MainActivity.btn_Play;
import static com.example.ruiz.ruiznatalioprelimexam.MainActivity.currDuration;
import static com.example.ruiz.ruiznatalioprelimexam.MainActivity.currentid;
import static com.example.ruiz.ruiznatalioprelimexam.MainActivity.isclick;
import static com.example.ruiz.ruiznatalioprelimexam.MainActivity.lblMusicName;
import static com.example.ruiz.ruiznatalioprelimexam.MainActivity.maxDuration;
import static com.example.ruiz.ruiznatalioprelimexam.MainActivity.mediaPlayer;
import static com.example.ruiz.ruiznatalioprelimexam.MainActivity.musicInfoList;
import static com.example.ruiz.ruiznatalioprelimexam.MainActivity.musicSelected;
import static com.example.ruiz.ruiznatalioprelimexam.MainActivity.seekBar;
import static com.example.ruiz.ruiznatalioprelimexam.MainActivity.seekHandler;
import static com.example.ruiz.ruiznatalioprelimexam.MainActivity.start;
import static com.example.ruiz.ruiznatalioprelimexam.MainActivity.stop;
import static com.example.ruiz.ruiznatalioprelimexam.R.id.btnback;

public class SecondActivity extends AppCompatActivity {

   public static TextView txtStart , txtStop, lblmusicName;
    public static ToggleButton buttonPlay;
    Button btn_Prev , btn_Nextt, btn_Shuffle, btn_Repeat;
    SeekBar sBar;

    String lblMName;
    Cursor cursor;
    Intent dataIntent;
    int sekkbar;
    String lblStart , lblStop;
   public static boolean isShuffle;

   public static int countershuffle , counterRepeat , repeatcount;
   public static int shuffleid, repeatid;
   public static int currentSongindex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        lblMName = lblMusicName.getText().toString();
        sekkbar = seekBar.getProgress();
        lblStart = start;
        lblStop = stop;
        txtStart = (TextView)findViewById(R.id.lblStart);

        txtStop = (TextView)findViewById(R.id.lblstop);
        txtStop.setText(stop);
        lblmusicName = (TextView)findViewById(R.id.lblMusic);
        lblmusicName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        lblmusicName.setSelected(true);
        lblmusicName.setSingleLine(true);
        lblmusicName.setText(lblMName);

        buttonPlay = (ToggleButton)findViewById(R.id.btnplay);

        btn_Prev = (Button)findViewById(btnback);
        btn_Prev.setOnClickListener(new ButtonEvent());
        btn_Nextt = (Button)findViewById(R.id.btnForward);
        btn_Nextt.setOnClickListener(new ButtonEvent());

        sBar = (SeekBar)findViewById(R.id.seekbar);
        sBar.setOnTouchListener(new OnTouchListener());

        btn_Shuffle = (Button)findViewById(R.id.btnshuffle);
        btn_Shuffle.setOnClickListener(new ShuffleEvent());
        btn_Repeat = (Button)findViewById(R.id.btnRepeat);
        btn_Repeat.setOnClickListener(new RepeatEvent());

        if(mediaPlayer != null){
            seekUpdate();
        }
        dataIntent = getIntent();
        int sur = dataIntent.getIntExtra("pos" , 1);
        currentid = sur;
        initPlayPause();

        if(isclick == true){
            buttonPlay.setChecked(true);
        }else
            buttonPlay.setChecked(false);

        if(isShuffle == true){
            btn_Shuffle.setBackgroundResource(R.drawable.shuffleon);

        }else if(isShuffle == false){
            btn_Shuffle.setBackgroundResource(R.drawable.shuffleoff);

        }
        if(repeatcount == 1){
            btn_Repeat.setBackgroundResource(R.drawable.repeatonce);

        }else if(repeatcount ==2){
            btn_Repeat.setBackgroundResource(R.drawable.repeatall);

        }else {
            btn_Repeat.setBackgroundResource(R.drawable.repeatoff);

        }

    }
    public class ShuffleEvent implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            countershuffle++;
            if(countershuffle == 3){
                countershuffle = 1;
            }
            if(countershuffle == 1){
                Toast.makeText(getApplicationContext(), "Shuffle is ON", Toast.LENGTH_SHORT).show();
                btn_Shuffle.setBackgroundResource(R.drawable.shuffleon);
                //PlayShuffleSong(shuffleid);
                //isShuffle = true;
                // btn_Repeat.setClickable(false);

            }else if(countershuffle == 2){
                Toast.makeText(getApplicationContext(), "Shuffle is OFF", Toast.LENGTH_SHORT).show();
                btn_Shuffle.setBackgroundResource(R.drawable.shuffleoff);
               //Shuffle = false;
               // btn_Repeat.setClickable(true);
            }
        }
    }
    public  class RepeatEvent implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            counterRepeat++;
            if(counterRepeat == 3){
                counterRepeat =0;
            }
            if(counterRepeat == 0){
                Toast.makeText(getApplicationContext(), "Repeat OFF", Toast.LENGTH_SHORT).show();
                btn_Repeat.setBackgroundResource(R.drawable.repeatoff);
                repeatcount = 0;
              // btn_Shuffle.setClickable(true);
            }else if(counterRepeat == 1){
                Toast.makeText(getApplicationContext(), "Repeat this song", Toast.LENGTH_SHORT).show();
                btn_Repeat.setBackgroundResource(R.drawable.repeatonce);
                repeatcount=1;
                currentSongindex = currentid;
               // btn_Shuffle.setClickable(false);
            }else if(counterRepeat ==2){
                Toast.makeText(getApplicationContext(), "Repeat ALL", Toast.LENGTH_SHORT).show();
                btn_Repeat.setBackgroundResource(R.drawable.repeatall);
                repeatcount = 2;
               // btn_Shuffle.setClickable(false);
            }
        }
    }
    private void seekUpdate(){

        sBar.setMax(maxDuration);
        sBar.setProgress(currDuration);
        int getcur1 = mediaPlayer.getCurrentPosition();
        txtStart.setText(convertDuration(getcur1));
        seekHandler.postDelayed(run , 1000);
    }
    Runnable run = new Runnable() {
        @Override
        public void run() {

            if(mediaPlayer != null ){
                if(maxDuration == currDuration){
                    PlayNextSong();
                }
            }
            seekUpdate();
        }
    };
    public  class OnTouchListener implements View.OnTouchListener{
        @Override
        public boolean onTouch(View view, MotionEvent Event) {

            seekChange(view);
            return false;
        }
    }
    private void seekChange(View v){
        if(mediaPlayer != null && mediaPlayer.isPlaying()){
            SeekBar sb =  (SeekBar)v;
            mediaPlayer.seekTo(sb.getProgress());
        }
    }

    private void initPlayPause(){
        buttonPlay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(mediaPlayer != null){
                    if(isChecked) {
                        mediaPlayer.start();
                        isclick = true;
                    }
                    else {
                        mediaPlayer.pause();
                        isclick = false;
                    }
                }
            }
        });
    }
    public class ButtonEvent implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            if(view == btn_Nextt){
                PlayNextSong();
            }else if(view == btn_Prev){
                PlayPrevSong();
            }
        }
    }
    public void PlayNextSong(){
        try {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer = new MediaPlayer();
            try {
                if(isShuffle == true) {
                    Random rand = new Random();
                    shuffleid= rand.nextInt((musicInfoList.size() - 1) - 0 + 1) + 0;
                    currentid = shuffleid;

                }else if(isShuffle == false) {
                    currentid = currentid + 1;

                }
                if(repeatcount == 1){
                    currentid = currentSongindex;
                }else if(repeatcount == 2) {
                    if(currentid == musicInfoList.size()){
                        currentid = 0;
                    }
                }
                musicSelected = musicInfoList.get(currentid);
                mediaPlayer.setDataSource(musicSelected.getFullPath());
                mediaPlayer.prepare();
                sBar.setMax(musicSelected.getDuration());
                seekBar.setMax(musicSelected.getDuration());
                lblmusicName.setText(musicSelected.getMusicName());
                lblMusicName.setText(musicSelected.getMusicName());
                mediaPlayer.start();
                buttonPlay.setChecked(true);
                btn_Play.setChecked(true);
                seekUpdate();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void PlayPrevSong(){
        try {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer = new MediaPlayer();
            try {
                if(currentid == 0)
                {
                    currentid = 0;
                    currentSongindex  = currentid;
                }
                if(currentid != 0) {
                    currentid = currentid - 1;
                    currentSongindex = currentid;
                }
                musicSelected = musicInfoList.get(currentid);
                mediaPlayer.setDataSource(musicSelected.getFullPath());
                mediaPlayer.prepare();
                sBar.setMax(musicSelected.getDuration());
                seekBar.setMax(musicSelected.getDuration());
                lblmusicName.setText(musicSelected.getMusicName());
                lblMusicName.setText(musicSelected.getMusicName());
                mediaPlayer.start();
                buttonPlay.setChecked(true);
                btn_Play.setChecked(true);
                seekUpdate();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public String convertDuration(long duration) {
        String out = null;
        long hours=0;
        try {
            hours = (duration / 3600000);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return out;
        }
        long remaining_minutes = (duration - (hours * 3600000)) / 60000;
        String minutes = String.valueOf(remaining_minutes);
        if (minutes.equals(0)) {
            minutes = "00";
        }
        long remaining_seconds = (duration - (hours * 3600000) - (remaining_minutes * 60000));
        String seconds = String.valueOf(remaining_seconds);
        if (seconds.length() < 2) {
            seconds = "00";
        } else {
            seconds = seconds.substring(0, 2);
        }

        if (hours > 0) {
            out = hours + ":" + minutes + ":" + seconds;
        } else {
            out = minutes + ":" + seconds;
        }
        return out;
    }
}
