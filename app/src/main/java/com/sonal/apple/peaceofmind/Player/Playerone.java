package com.sonal.apple.peaceofmind.Player;

import android.app.Activity;
import android.media.MediaPlayer;

import static com.sonal.apple.peaceofmind.MainActivity.player1;

/**
 * Created by apple on 02/03/18.
 */

public class Playerone {
    Activity activity;
    int song;

    public Playerone(Activity activity, int songid) {
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
            if (player1 != null) {
                player1.release();
                player1 = null;
            }
            player1 = new MediaPlayer();
            player1 = MediaPlayer.create(activity, song);
            player1.setLooping(true);
            player1.start();


        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    private void stopplayer() {
        if (player1 != null) {
            player1.release();
            player1 = null;
        }
    }

}
