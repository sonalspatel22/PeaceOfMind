package com.sonal.apple.peaceofmind.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;


import com.sonal.apple.peaceofmind.MainActivity;
import com.sonal.apple.peaceofmind.R;
import com.sonal.apple.peaceofmind.model.Stream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
public class StreamService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    public static final String STREAM_DONE_LOADING_INTENT = "stream_done_loading_intent";
    public static final String STREAM_DONE_LOADING_SUCCESS = "stream_done_loading_success";
    public static final String TIMER_UPDATE_INTENT = "timer_update_intent";
    public static final String TIMER_UPDATE_VALUE = "timer_update_value";
    public static final String TIMER_DONE_INTENT = "timer_done_intent";
    private static final String TAG = StreamService.class.getSimpleName();
    private static final int NOTIFY_ID = 1;
    private static final String ACTION_STOP = "action_stop";
    private final float MAX_VOLUME = 100.0f;
    private final IBinder streamBinder = new StreamBinder();
    private State state1 = State.STOPPED;
    private State state2 = State.STOPPED;
    private State state3 = State.STOPPED;
    private State state4 = State.STOPPED;
    private MediaPlayer player;
    private MediaPlayer player2;
    private MediaPlayer player3;
    private MediaPlayer player4;
    private ArrayList<Stream> currentStream;
    private LocalBroadcastManager broadcastManager;
    private CountDownTimer countDownTimer;

    public void onCreate() {
        super.onCreate();
        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player2 = new MediaPlayer();
        player2.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player3 = new MediaPlayer();
        player3.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player4 = new MediaPlayer();
        player4.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
        broadcastManager = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        toBackground();
        return streamBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        toBackground();
        super.onRebind(intent);
    }

    private void toBackground() {
        stopForeground(true);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (player.isPlaying() || state1 == State.PREPARING) {
            toForeground();
        } else if (player2.isPlaying() || state2 == State.PREPARING) {
            toForeground();
        } else if (player3.isPlaying() || state3 == State.PREPARING) {
            toForeground();
        } else if (player4.isPlaying() || state4 == State.PREPARING) {
            toForeground();
        }
        return true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        handleIntent(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * Run the service in the foreground
     * and show a notification
     */
    private void toForeground() {

        RemoteViews notificationView = new RemoteViews(getPackageName(),
                R.layout.notification);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notification)
                .setShowWhen(false)
                .setContent(notificationView);

        notificationView.setImageViewResource(R.id.streamIcon, R.drawable.buddhism);
        notificationView.setTextViewText(R.id.titleTxt, getString(R.string.app_name));
//        notificationView.setTextViewText(R.id.descTxt, "" + currentStream.size());
//        notificationView.setTextViewText(R.id.timeleft, "00:00");

        Intent closeIntent = new Intent(getApplicationContext(), StreamService.class);
        closeIntent.setAction(ACTION_STOP);
        PendingIntent pendingCloseIntent = PendingIntent.getService(getApplicationContext(), 1, closeIntent, 0);

        notificationView.setOnClickPendingIntent(R.id.closeStream, pendingCloseIntent);

        Intent resultIntent = new Intent(this, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);
        builder.setContentIntent(resultPendingIntent);

        Notification notification = builder.build();
        startForeground(NOTIFY_ID, notification);
    }

    /**
     * Handle intent from notification
     *
     * @param intent intent to add pending intent to
     */
    private void handleIntent(Intent intent) {
        if (intent == null || intent.getAction() == null)
            return;

        String action = intent.getAction();

        if (action.equalsIgnoreCase(ACTION_STOP)) {
            Log.i(TAG, "handleIntent: stopping stream from notification");
            stopStreaming();
            stopSleepTimer();
            toBackground();
            stopSelf();
        }
    }

    public State getState1() {
        return state1;
    }

    public State getState2() {
        return state2;
    }

    public State getState3() {
        return state3;
    }

    public State getState4() {
        return state4;
    }

    /**
     * Start play a stream
     */
    public void setvolume(int pos, float vol) {
        if (pos == 0) {
            player.setVolume(vol, vol);
        } else if (pos == 1) {
            player2.setVolume(vol, vol);
        } else if (pos == 2) {
            player3.setVolume(vol, vol);
        } else if (pos == 3) {
            player4.setVolume(vol, vol);
        }
    }

    public void playStream(ArrayList<Stream> stream) {
        if (!stream.isEmpty()) {
            // If a stream was already running stop it and reset
            if (player.isPlaying()) {
                player.stop();
            }
            if (player2.isPlaying()) {
                player2.stop();
            }
            if (player3.isPlaying()) {
                player3.stop();
            }
            if (player4.isPlaying()) {
                player4.stop();
            }

            prepareplayers(stream.size());
            startplayers(stream);
//            List<String> keys = Arrays.asList(getResources().getStringArray(R.array.api_keys));
//            String key = keys.get((new Random()).nextInt(keys.size()));


//            player = MediaPlayer.create(this, R.raw.light_rain);
//            player.setLooping(true);

            currentStream = stream;

        } else

        {
            stopStreaming();
//            if (player.isPlaying()) {
//                player.stop();
//                player2.stop();
//                player3.stop();
//                player4.stop();
//            }
//            currentStream = stream;
        }

    }

    public void prepareplayers(int streams) {
        Log.e("strems", "" + streams);
        if (streams == 1) {
            prepareplayer1();
        } else if (streams == 2) {
            prepareplayer1();
            prepareplayer2();
        } else if (streams == 3) {
            prepareplayer1();
            prepareplayer2();
            prepareplayer3();
        } else if (streams == 4) {
            prepareplayer1();
            prepareplayer2();
            prepareplayer3();
            prepareplayer4();
        } else {
            stopStreaming();
        }
    }

    public void prepareplayer1() {
        if (player != null) {
            player.reset();
            state1 = State.PREPARING;
        }
    }

    public void prepareplayer2() {
        if (player2 != null) {
            player2.reset();
            state2 = State.PREPARING;
        }
    }

    public void prepareplayer3() {
        if (player3 != null) {
            player3.reset();
            state3 = State.PREPARING;
        }
    }

    public void prepareplayer4() {
        if (player4 != null) {
            player4.reset();
            state4 = State.PREPARING;
        }
    }

    public void startplayers(ArrayList<Stream> streams) {
        Log.e("strems", "" + streams);
        if (streams.size() == 1) {
            startplayer1(streams.get(0));
        } else if (streams.size() == 2) {
            startplayer1(streams.get(0));
            startplayer2(streams.get(1));
        } else if (streams.size() == 3) {
            startplayer1(streams.get(0));
            startplayer2(streams.get(1));
            startplayer3(streams.get(2));
        } else if (streams.size() == 4) {
            startplayer1(streams.get(0));
            startplayer2(streams.get(1));
            startplayer3(streams.get(2));
            startplayer4(streams.get(3));
        } else {
            stopStreaming();
        }
    }

    public void startplayer1(Stream stream) {
        player = new MediaPlayer();
        player = MediaPlayer.create(this, stream.getSongid());
        player.setLooping(true);
        player.setVolume(MAX_VOLUME, MAX_VOLUME);
        notifyStreamLoaded(true);
        player.start();
        player.setVolume(stream.getVol(), stream.getVol());
        state1 = State.PLAYING;
    }

    public void startplayer2(Stream stream) {
        player2 = new MediaPlayer();
        player2 = MediaPlayer.create(this, stream.getSongid());
        player2.setLooping(true);
        player2.setVolume(MAX_VOLUME, MAX_VOLUME);
        notifyStreamLoaded(true);
        player2.start();
        player2.setVolume(stream.getVol(), stream.getVol());
        state2 = State.PLAYING;
    }

    public void startplayer3(Stream stream) {
        player3 = new MediaPlayer();
        player3 = MediaPlayer.create(this, stream.getSongid());
        player3.setLooping(true);
        player3.setVolume(MAX_VOLUME, MAX_VOLUME);
        notifyStreamLoaded(true);
        player3.start();
        player3.setVolume(stream.getVol(), stream.getVol());
        state3 = State.PLAYING;
    }

    public void startplayer4(Stream stream) {
        player4 = new MediaPlayer();
        player4 = MediaPlayer.create(this, stream.getSongid());
        player4.setLooping(true);
        player4.setVolume(MAX_VOLUME, MAX_VOLUME);
        notifyStreamLoaded(true);
        player4.start();
        player4.setVolume(stream.getVol(), stream.getVol());
        state4 = State.PLAYING;
    }

    public void pauseStream() {

        if (state1 == State.PLAYING) {
            player.pause();
            stopSleepTimer();
            state1 = State.PAUSED;
        }

        if (state2 == State.PLAYING) {
            player2.pause();
            stopSleepTimer();
            state2 = State.PAUSED;
        }

        if (state3 == State.PLAYING) {
            player3.pause();
            stopSleepTimer();
            state1 = State.PAUSED;
        }

        if (state4 == State.PLAYING) {
            player4.pause();
            stopSleepTimer();
            state1 = State.PAUSED;
        }
    }

    public void resumeStream() {

        if (state1 == State.PAUSED) {
            player.start();
            state1 = State.PLAYING;
        }
        if (state2 == State.PAUSED) {
            player2.start();
            state2 = State.PLAYING;
        }
        if (state3 == State.PAUSED) {
            player.start();
            state3 = State.PLAYING;
        }
        if (state4 == State.PAUSED) {
            player.start();
            state4 = State.PLAYING;
        }
    }

    /**
     * Stop the MediaPlayer if something is streaming
     */
    public void stopStreaming() {

        if (state1 == State.PLAYING || state1 == State.PAUSED) {
            player.stop();
            player.reset();
            state1 = State.STOPPED;
        }
        if (state2 == State.PLAYING || state2 == State.PAUSED) {
            player2.stop();
            player2.reset();
            state2 = State.STOPPED;
        }
        if (state3 == State.PLAYING || state3 == State.PAUSED) {
            player3.stop();
            player3.reset();
            state3 = State.STOPPED;
        }
        if (state4 == State.PLAYING || state4 == State.PAUSED) {
            player4.stop();
            player4.reset();
            state4 = State.STOPPED;
        }
    }

    /**
     * Get the stream that is playing right now, if any
     *
     * @return the playing stream or null
     */
    public ArrayList<Stream> getPlayingStream() {
        if (state1 == State.PLAYING || state1 == State.PAUSED) {
            return currentStream;
        }
        return null;
    }

    /**
     * Set a sleep timer
     *
     * @param milliseconds to wait before sleep
     */
    public void setSleepTimer(int milliseconds) {
        Log.i(TAG, "setSleepTimer: setting sleep timer for " + milliseconds + "ms");

        stopSleepTimer();

        if (milliseconds != 0) {

            countDownTimer = new CountDownTimer(milliseconds, 1000) {

                public void onTick(long millisUntilFinished) {
                    Intent intent = new Intent(TIMER_UPDATE_INTENT);
                    intent.putExtra(TIMER_UPDATE_VALUE, (int) millisUntilFinished);
                    broadcastManager.sendBroadcast(intent);
                    if (millisUntilFinished < TimeUnit.SECONDS.toMillis(30)) {
                        //lower the volume by respective step
                        lowerVolume((int) ((int) millisUntilFinished / TimeUnit.SECONDS.toMillis(1)));
                    }
                }

                public void onFinish() {
                    stopStreaming();
                    stopSleepTimer();
                    timerDoneBroadcast();
                    toBackground();
                }

            }.start();
        }
    }

    /**
     * Lowers the volume of the stream to a step
     *
     * @param step out of a max of 30
     */
    private void lowerVolume(int step) {

        float voulme = ((float) step) / 30f;
        player.setVolume(voulme, voulme);
    }

    /**
     * Stop the sleep timer and restore volume to max
     */
    private void stopSleepTimer() {

        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        player.setVolume(MAX_VOLUME, MAX_VOLUME);
    }

    private void timerDoneBroadcast() {
        Log.i(TAG, "setSleepTimer: sleep timer is done, notifying bindings.");

        Intent intent = new Intent(TIMER_DONE_INTENT);
        broadcastManager.sendBroadcast(intent);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.i(TAG, "onError: " + what + ", " + extra);

        mp.reset();
        notifyStreamLoaded(false);
        state1 = State.STOPPED;
        state2 = State.STOPPED;
        state3 = State.STOPPED;
        state4 = State.STOPPED;
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {


    }

    /**
     * Send out a broadcast indicating stream was started with success or couldn't be found
     *
     * @param success
     */
    private void notifyStreamLoaded(boolean success) {
        Intent intent = new Intent(STREAM_DONE_LOADING_INTENT);
        intent.putExtra(STREAM_DONE_LOADING_SUCCESS, success);
        broadcastManager.sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
    }

    public enum State {PAUSED, STOPPED, PLAYING, PREPARING}

    public class StreamBinder extends Binder {
        public StreamService getService() {
            return StreamService.this;
        }
    }
}