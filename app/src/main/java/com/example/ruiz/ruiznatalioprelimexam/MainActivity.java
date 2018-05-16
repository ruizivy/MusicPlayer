package com.example.ruiz.ruiznatalioprelimexam;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Random;

import static com.example.ruiz.ruiznatalioprelimexam.SecondActivity.buttonPlay;
import static com.example.ruiz.ruiznatalioprelimexam.SecondActivity.currentSongindex;
import static com.example.ruiz.ruiznatalioprelimexam.SecondActivity.isShuffle;
import static com.example.ruiz.ruiznatalioprelimexam.SecondActivity.lblmusicName;
import static com.example.ruiz.ruiznatalioprelimexam.SecondActivity.repeatcount;
import static com.example.ruiz.ruiznatalioprelimexam.SecondActivity.shuffleid;

public class MainActivity extends AppCompatActivity {

    ListView lst_music;

    private CustimListAdapter adapter;
    Intent data_Intent;
    public static ArrayList<Music> musicInfoList = new ArrayList<Music>();

     public static   MediaPlayer mediaPlayer;
    Cursor cursor;

    AudioManager audioManager;
    public static Music musicSelected;
    public static android.os.Handler seekHandler = new android.os.Handler();

    public  static  int currDuration;
    public static int maxDuration;
    DialogInterface.OnClickListener dialoglistener;

    public  static ToggleButton btn_Play;
    Button btn_prev , btn_next, btn_Music;
    public static TextView lblMusicName;
    public static SeekBar seekBar;
    public static Integer currentid = null;
    public static boolean isclick;
    int pos;
    String title;
    public static String start , stop;
    LinearLayout pnl_header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lblMusicName = (TextView)findViewById(R.id.txtMusicName);
        lblMusicName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        lblMusicName.setSelected(true);
        lblMusicName.setSingleLine(true);

        seekBar = (SeekBar)findViewById(R.id.skBar);
        seekBar.setOnTouchListener(new OnTouchListener());

        btn_prev = (Button)findViewById(R.id.btnprev);
        btn_prev.setOnClickListener(new MyButtonEventHandler());
        btn_next = (Button)findViewById(R.id.btnNext);
        btn_next.setOnClickListener(new MyButtonEventHandler());
        btn_Music = (Button)findViewById(R.id.btnMusic);
        btn_Play = (ToggleButton)findViewById(R.id.buttonPlay);

        lst_music = (ListView)findViewById(R.id.lstMusic);
        lst_music.setOnItemClickListener(new ItemClickListener());

        adapter = new CustimListAdapter(this ,musicInfoList);
        lst_music.setAdapter(adapter);

        pnl_header= (LinearLayout)findViewById(R.id.pnl_header);
        pnl_header.setOnClickListener(new ButtonEvent());

        LoadMusic();
        if(mediaPlayer != null){
            seekUpdate();
        }
        initPlayPause();
        play();
        if(isclick == true){
           btn_Play.setChecked(true);
        }else
           btn_Play.setChecked(false);

    }
    public class ButtonEvent implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this , SecondActivity.class);
            intent.putExtra("pos", pos);
            intent.putExtra("title" ,title );
            startActivityForResult(intent , 1);
        }
    }
    public  void  LoadMusic(){
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String songList = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        cursor = managedQuery(songUri , new String[]{"*"} , songList , null, null);

        Music music = null;

        if(cursor !=null){
            while(cursor.moveToNext()){
                music = new Music();
                music.setMusicID(cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
                music.setMusicName(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)));
                music.setFullPath(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
                music.setAlbum(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
                music.setArtistName(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
                music.setDuration(cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
                music.setFileSize(cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE)));
                musicInfoList.add(music);
            }
            lst_music.setAdapter(adapter);
          // cursor.close();
        }
    }
    public class ItemClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if(mediaPlayer != null && mediaPlayer.isPlaying()){

                mediaPlayer.stop();
            }
            mediaPlayer = new MediaPlayer();
            try
            {
                musicSelected = musicInfoList.get(i);
                currentid = i;
                pos = i;
                mediaPlayer.setDataSource(musicSelected.getFullPath());
                mediaPlayer.prepare();
                seekBar.setMax(musicSelected.getDuration());
                mediaPlayer.start();
                lblMusicName.setText(musicSelected.getMusicName());
                btn_Play.setChecked(true);
                buttonPlay.setChecked(true);
                title = musicSelected.getMusicName();
                stop = convertDuration(musicSelected.getDuration());
                seekUpdate();
            }catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    public class MyButtonEventHandler implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            if(view == btn_prev){
                PlayPrevSong();
            }else if(view == btn_next){
                PlayNextSong();
            }
        }
    }

    public void play(){
        mediaPlayer = new MediaPlayer();
        try
        {
            /*musicSelected = musicInfoList.get(0);
            currentid = 0;
            pos = 0;*/
            Uri soundURI = Uri.parse("android.resource://com.example.ruiz.testmusic/" + R.raw.mostgirls);

            mediaPlayer.setDataSource(getApplicationContext() , soundURI);
            mediaPlayer.prepare();
            seekBar.setMax(musicSelected.getDuration());
            lblMusicName.setText(musicSelected.getMusicName());
            title = musicSelected.getMusicName();
            mediaPlayer.pause();
            btn_Play.setChecked(false);
            stop = convertDuration(musicSelected.getDuration());
            seekUpdate();

        }catch (Exception ex) {
            ex.printStackTrace();
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
                pos = currentid;
                musicSelected = musicInfoList.get(currentid);
                mediaPlayer.setDataSource(musicSelected.getFullPath());
                mediaPlayer.prepare();
                seekBar.setMax(musicSelected.getDuration());
                lblMusicName.setText(musicSelected.getMusicName());
                lblmusicName.setText(musicSelected.getMusicName());
                mediaPlayer.start();
                title = musicSelected.getMusicName();
                btn_Play.setChecked(true);
                buttonPlay.setChecked(true);
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
                }
                if(currentid != 0) {
                    currentid = currentid - 1;
                    pos = currentid;
                }
                musicSelected = musicInfoList.get(currentid);
                mediaPlayer.setDataSource(musicSelected.getFullPath());
                mediaPlayer.prepare();
                seekBar.setMax(musicSelected.getDuration());
                lblMusicName.setText(musicSelected.getMusicName());
                lblmusicName.setText(musicSelected.getMusicName());
                mediaPlayer.start();
                title = musicSelected.getMusicName();
                btn_Play.setChecked(true);
                buttonPlay.setChecked(true);
                seekUpdate();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private void seekUpdate(){

        seekBar.setProgress(mediaPlayer.getCurrentPosition());
        maxDuration = musicSelected.getDuration();
        currDuration = mediaPlayer.getCurrentPosition();
        seekHandler.postDelayed(run , 1000);

    }
    Runnable run = new Runnable() {
        @Override
        public void run() {

            int  getcur1 = mediaPlayer.getCurrentPosition();
            start = convertDuration(getcur1);
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
        btn_Play.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(mediaPlayer != null){
                    if(isChecked) {
                        mediaPlayer.start();
                        isclick =true;
                    }
                    else {
                        mediaPlayer.pause();
                        isclick = false;
                    }
                }
            }
        });
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
