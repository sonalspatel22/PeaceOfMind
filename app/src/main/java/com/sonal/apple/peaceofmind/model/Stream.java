package com.sonal.apple.peaceofmind.model;

import java.io.Serializable;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
public class Stream implements Serializable {

    public float vol;
    private int id;
    private int songid;
    private String title;
    private String desc;
    private int bigImgRes;
    private int smallImgRes;

    public Stream(int id, float vol, int url, String title, String desc, int bigImgRes, int smallImgRes) {
        this.id = id;
        this.vol = vol;
        this.songid = url;
        this.title = title;
        this.desc = desc;
        this.bigImgRes = bigImgRes;
        this.smallImgRes = smallImgRes;
    }

    public int getSongid() {
        return songid;
    }

    public void setSongid(int songid) {
        this.songid = songid;
    }

    public float getVol() {
        return vol;
    }

    public void setVol(float vol) {
        this.vol = vol;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getBigImgRes() {
        return bigImgRes;
    }

    public void setBigImgRes(int bigImgRes) {
        this.bigImgRes = bigImgRes;
    }

    public int getSmallImgRes() {
        return smallImgRes;
    }

    public void setSmallImgRes(int smallImgRes) {
        this.smallImgRes = smallImgRes;
    }
}
