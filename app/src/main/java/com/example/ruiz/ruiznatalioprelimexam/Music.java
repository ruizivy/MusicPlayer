package com.example.ruiz.ruiznatalioprelimexam;

/**
 * Created by Ruiz on 8/21/2017.
 */

public class Music {
    private int musicID;
    private String musicName;
    private String fullPath;
    private String album;
    private String artistName;
    private  int duration;
    private int fileSize;

    public Music() {
    }

    public void setMusicID(int musicID) {
        this.musicID = musicID;
    }

    public int getMusicID() {
        return musicID;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getAlbum() {
        return album;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public int getFileSize() {
        return fileSize;
    }
}
