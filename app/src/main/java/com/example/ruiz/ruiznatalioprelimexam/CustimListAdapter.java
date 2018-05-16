package com.example.ruiz.ruiznatalioprelimexam;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Ruiz on 7/22/2017.
 */

public class CustimListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Music> musicList;
    public  ImageView img;
    MediaPlayer mediaPlayer;
    Cursor cursor;
    AudioManager audioManager;
    Music musicSelected;
    android.os.Handler seekHandler = new android.os.Handler();
    Button image;

    public  CustimListAdapter(Activity  a , List<Music> m){

        this.activity = a;
        this.musicList = m;
    }
    public  int getCount(){
        return  musicList.size();
    }
    public Object getItem(int pos){
        return  musicList.get(pos);
    }
    public long getItemId(int pos){
        return  pos;
    }
    public View getView(final int pos , View view , ViewGroup vGroup){
        if(inflater == null){
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if(view == null){
            view = inflater.inflate(R.layout.activity_list_item , null);
        }

        TextView title = (TextView) view.findViewById(R.id.lbltitle);
        TextView artist = (TextView) view.findViewById(R.id.lblartist);
        TextView album = (TextView) view.findViewById(R.id.lblalbum);
        TextView duration = (TextView) view.findViewById(R.id.lblduration);

        Music  m = musicList.get(pos);
        title.setText(m.getMusicName());
        artist.setText(m.getArtistName() + "Artist");
        album.setText(m.getAlbum());
        long d  = m.getDuration();
        String time = convertDuration(d);
        duration.setText(time);
        return view;
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
