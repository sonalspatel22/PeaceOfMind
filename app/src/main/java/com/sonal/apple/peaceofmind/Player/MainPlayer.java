package com.sonal.apple.peaceofmind.Player;

import android.app.Activity;
import android.media.MediaPlayer;

import static com.sonal.apple.peaceofmind.MainActivity.mainplayer;

/**
 * Created by apple on 02/03/18.
 */

public class MainPlayer {
    Activity activity;
    int song;

    public MainPlayer(Activity activity, int songid) {
        this.activity = activity;
        this.song = songid;
        if (songid == -1) {
            stopplayer();
        } else {
            startplayer();
        }
    }

    public void startplayer() {
        try {
            if (mainplayer != null) {
                mainplayer.release();
                mainplayer = null;
            }
            mainplayer = new MediaPlayer();
            mainplayer = MediaPlayer.create(activity, song);
            mainplayer.setLooping(true);
            mainplayer.start();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    private void stopplayer() {
        if (mainplayer != null) {
            mainplayer.release();
            mainplayer = null;
        }
    }
}
