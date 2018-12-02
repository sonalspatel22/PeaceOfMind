package com.sonal.apple.peaceofmind.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by apple on 02/03/18.
 */

public class playlistmodel implements Serializable {
    int imageid;
    int volume;
    String tag;
    int songid;

    protected playlistmodel(Parcel in) {
        imageid = in.readInt();
        tag = in.readString();
        songid = in.readInt();
        volume = in.readInt();
    }

    public playlistmodel(String tags, int image, int song, int volume) {
        this.tag = tags;
        this.imageid = image;
        this.songid = song;
        this.volume = volume;
    }

    public int getVolume() {
        return volume;
    }

    public String getTag() {
        return tag;
    }

    public int getImageid() {
        return imageid;
    }

    public int getSongid() {
        return songid;
    }


}
