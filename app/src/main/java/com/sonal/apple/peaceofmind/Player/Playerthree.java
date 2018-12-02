package com.sonal.apple.peaceofmind.Player;

import android.app.Activity;
import android.media.MediaPlayer;

import static com.sonal.apple.peaceofmind.MainActivity.player3;


public class Playerthree {
    Activity activity;
    int song;

    public Playerthree(Activity activity, int songid) {
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
            if (player3 != null) {
                player3.release();
                player3 = null;
            }
            player3 = new MediaPlayer();
            player3 = MediaPlayer.create(activity, song);
            player3.setLooping(true);
            player3.start();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    private void stopplayer() {
        if (player3 != null) {
            player3.release();
            player3 = null;
        }
    }
}
