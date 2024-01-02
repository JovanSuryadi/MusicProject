package com.example.musicproject.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SongModel {
    @Expose
    @SerializedName("song")
    String song;

    //make sure kalau nama songnya sama sesuai dengan yang di anotate untuk apinya

    @Expose
    @SerializedName("url")
    String url;
    @Expose
    @SerializedName("cover_image")
    String cover_image;
    @Expose
    @SerializedName("artist")
    String artist;

   // getter and constructor


    public SongModel(String song, String url, String cover_image, String artist) {
        this.song = song;
        this.url = url;
        this.cover_image = cover_image;
        this.artist = artist;
    }

    public String getSong() {
        return song;
    }

    public String getUrl() {
        return url;
    }

    public String getCover_image() {
        return cover_image;
    }

    public String getArtist() {
        return artist;
    }
}
