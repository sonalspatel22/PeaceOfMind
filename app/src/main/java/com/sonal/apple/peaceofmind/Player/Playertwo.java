package com.sonal.apple.peaceofmind.Player;

import android.app.Activity;
import android.media.MediaPlayer;

import static com.sonal.apple.peaceofmind.MainActivity.player2;


public class Playertwo {
    Activity activity;
    int song;

    public Playertwo(Activity activity, int songid) {
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
            if (player2 != null) {
                player2.release();
                player2 = null;
            }
            player2 = new MediaPlayer();
            player2 = MediaPlayer.create(activity, song);
            player2.setLooping(true);
            player2.start();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    private void stopplayer() {
        if (player2 != null) {
            player2.release();
            player2 = null;
        }
    }
}
