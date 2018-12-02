package com.sonal.apple.peaceofmind;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.test.suitebuilder.annotation.Suppress;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.sonal.apple.peaceofmind.Event.AddEvent;
import com.sonal.apple.peaceofmind.Event.TimerEvent;
import com.sonal.apple.peaceofmind.Player.MainPlayer;
import com.sonal.apple.peaceofmind.Player.Playerone;
import com.sonal.apple.peaceofmind.Player.Playerthree;
import com.sonal.apple.peaceofmind.Player.Playertwo;
import com.sonal.apple.peaceofmind.adaptor.Playlistadaptor;
import com.sonal.apple.peaceofmind.adaptor.gridadaptor;
import com.sonal.apple.peaceofmind.adaptor.timeradaptor;
import com.sonal.apple.peaceofmind.model.playlistmodel;
import com.sonal.apple.peaceofmind.prefrence.MyPrefrence;
import com.sonal.apple.peaceofmind.service.StreamService;
import com.sonal.apple.peaceofmind.ui.MainPresenter;
import com.sonal.apple.peaceofmind.view.CircleImageView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.ThreadMode;

import static android.os.Build.VERSION.SDK_INT;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String Broadcast_PLAY_NEW_AUDIO = "com.valdioveliu.valdio.audioplayer.PlayNewAudio";
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private final static String TAG = MainActivity.class.getSimpleName();
    public static int flag = 0;
    public static Button btn_Add;
    public static MediaPlayer mainplayer = null;
    public static MediaPlayer player1 = null;
    public static MediaPlayer player2 = null;
    public static MediaPlayer player3 = null;
    public static playlistmodel pm = null;
    public static ArrayList<playlistmodel> playlist;
    public static ArrayList<playlistmodel> musicplaylist;
    public static int[] sound_music = {
            R.raw.birds,
            R.raw.beach_seagulls,
            R.raw.thunder_light_2,
            R.raw.thunder_light_3,
            R.raw.thunder_light_4,
            R.raw.fp_fire,
            R.raw.piano_main,
            R.raw.piano_smoother_move,
            R.raw.piano_awaiting_return,

            R.raw.wind,
            R.raw.gong_bell,
            R.raw.string_background_music,
            R.raw.rain_on_sidewalk,
            R.raw.stones,
            R.raw.guitar,
            R.raw.guitar,
            R.raw.peaceful_water,

            R.raw.rain_on_roof,
            R.raw.rain_on_window,
            R.raw.rain_on_leaves,};
    public int[] images_music = {
            R.drawable.ic_bird,
            R.drawable.ic_bird1,
            R.drawable.ic_cloud,
            R.drawable.ic_cloud2,
            R.drawable.ic_cloud3,
            R.drawable.ic_fire,
            R.drawable.ic_piano1,
            R.drawable.ic_piano2,
            R.drawable.ic_piano3,

            R.drawable.ic_desert,
            R.drawable.ic_frog,
            R.drawable.ic_poven,
            R.drawable.ic_shower,
            R.drawable.ic_tornado,
            R.drawable.ic_violin,
            R.drawable.ic_violin1,
            R.drawable.ic_water,

            R.drawable.ic_roofonrain,
            R.drawable.ic_leavesonrain,
            R.drawable.ic_windowonrain};
    MainPresenter presenter;
    AudioManager audioManager;
    int max;
    Drawable play;
    Drawable pause;
    AudioManager audio;
    int currentVolume;
    Playlistadaptor itemListDataAdapter;
    String[] time = {"10", "30", "1", "2", "3", "4", "5", "7"};
    Dialog dialog;
    private TextView tv;
    private RecyclerView rvSubmusiclist;
    private RecyclerView rvplaylsit;
    //    private ImageView pause1;
    private SeekBar seekbar;
    private SeekBar seekbarplayer1;
    private ImageView playpause;
    private ImageView timer;
    private int state1 = 0;
    private Boolean boundToService = false;
    private StreamService streamService;
    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            Log.i(TAG, "onServiceConnected: successfully bound to service.");
            StreamService.StreamBinder binder = (StreamService.StreamBinder) service;
            streamService = binder.getService();
//            currentStream = streamService.getPlayingStream();
//            if (currentStream != null) {
//                presenter.restoreUI(currentStream, streamService.getState() == StreamService.State.PLAYING);
//            } else {
//                int last = preferences.getInt(LAST_STREAM_IDENTIFIER, 0);
//                currentStream = streams.get(last);
//                presenter.restoreUI(currentStream, false);
//            }
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.i(TAG, "onServiceDisconnected: disconnected from service.");
            streamService = null;
        }
    };
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            handleIntent(intent);
        }
    };

    private void findViews() {
        tv = (TextView) findViewById(R.id.tv);
        rvSubmusiclist = (RecyclerView) findViewById(R.id.rv_submusiclist);
        rvplaylsit = (RecyclerView) findViewById(R.id.rv_playlist);
        seekbar = (SeekBar) findViewById(R.id.seekbar);
        seekbarplayer1 = (SeekBar) findViewById(R.id.seekbarplayer1);
        playpause = (ImageView) findViewById(R.id.playpause);
        timer = (ImageView) findViewById(R.id.timer);
        playpause.setOnClickListener(this);
        timer.setOnClickListener(this);
    }

    public void setrvadaptor() {
        rvSubmusiclist.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
        gridadaptor madapter = new gridadaptor(this, images_music);
        rvSubmusiclist.setAdapter(madapter);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actviity_main);
        findViews();
        setrvadaptor();
        playlist = new ArrayList<playlistmodel>();
        startplaylist();
        audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        seekbar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        seekbar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

        Log.e("max", "" + max);

        if (checkAndRequestPermissions()) {

        }
        play = getResources().getDrawable(R.drawable.play);
        pause = getResources().getDrawable(R.drawable.pause);
        Intent i = getIntent();

        try {
            pm = (playlistmodel) i.getSerializableExtra("song");
            if (pm != null) {
                new MainPlayer(MainActivity.this, pm.getSongid());
                playpause.setImageDrawable(pause);
                state1 = 1;
            }
            tv.setText("Relax Music " + i.getStringExtra("index"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        makelist();
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbarplayer1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mainplayer != null) {
                    float val = (float) progress / 100;
                    mainplayer.setVolume(val, val);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void makelist() {
        musicplaylist = new ArrayList<>();
        for (int j = 0; j < images_music.length; j++) {
            musicplaylist.add(new playlistmodel("music", images_music[j], sound_music[j], 50));
        }
    }

    public void startplaylist() {
        MyPrefrence myPrefrence = new MyPrefrence(MainActivity.this);
        if (myPrefrence.getList() != null) {
            playlist.clear();
            playlist = myPrefrence.getList();
        }
        try {
            if (playlist != null) {
                for (int i = 0; i < playlist.size(); i++) {
                    if (i == 0) {
                        if (player1 == null) {
                            new Playerone(MainActivity.this, playlist.get(0).getSongid());
                        }
                    } else if (i == 1) {
                        if (player2 == null) {
                            new Playertwo(MainActivity.this, playlist.get(1).getSongid());
                        }
                    } else if (i == 2) {
                        if (player3 == null) {
                            new Playerthree(MainActivity.this, playlist.get(2).getSongid());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        if (v == playpause) {
            if (state1 == 0) {
                new MainPlayer(MainActivity.this, pm.getSongid());
                startplaylist();
                playpause.setImageDrawable(pause);
                state1 = 1;
            } else {
                new MainPlayer(MainActivity.this, -1);
                state1 = 0;
                if (player1 != null) {
                    new Playerone(MainActivity.this, -1);
                }
                if (player2 != null) {
                    new Playertwo(MainActivity.this, -1);
                }
                if (player3 != null) {
                    new Playerthree(MainActivity.this, -1);
                }
                playpause.setImageDrawable(play);
            }
        } else if (v == timer) {
            createdialog();
//            startActivity(new Intent(MainActivity.this,TimerActivity.class));
        }

    }

    public void Createdialog() {
        final Dialog dialog = new Dialog(MainActivity.this);
        // Include dialog.xml file
        dialog.setContentView(R.layout.dialog_addmusic);


        TextView tvMusic;
        RecyclerView rvMusic;
        TextView tvNature;
        RecyclerView rvNature;
        TextView tvAnimals;
        RecyclerView rvAnimals;
        LinearLayout llclose;

        TextView close;


        llclose = (LinearLayout) dialog.findViewById(R.id.llclose);
        close = (TextView) dialog.findViewById(R.id.close);
        tvMusic = (TextView) dialog.findViewById(R.id.tv_music);
        rvMusic = (RecyclerView) dialog.findViewById(R.id.rv_music);
        tvNature = (TextView) dialog.findViewById(R.id.tv_nature);
        rvNature = (RecyclerView) dialog.findViewById(R.id.rv_nature);
        tvAnimals = (TextView) dialog.findViewById(R.id.tv_animals);
        rvAnimals = (RecyclerView) dialog.findViewById(R.id.rv_animals);
        dialog.show();

        int numberOfColumns = 4;


        llclose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                dialog.dismiss();
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Createsonglistdialog(pm);
            }
        });
    }

    public void Createsonglistdialog(playlistmodel pm) {
        final Dialog dialog = new Dialog(MainActivity.this);
        // Include dialog.xml file
        dialog.setContentView(R.layout.playlist);
        RecyclerView rl1;
        CircleImageView iv;
        SeekBar seekbar_Main;
        SeekBar seekbar_system;

        rl1 = (RecyclerView) dialog.findViewById(R.id.rl1);
        iv = (CircleImageView) dialog.findViewById(R.id.iv);
        seekbar_Main = (SeekBar) dialog.findViewById(R.id.seekbar_mainplayer);
        seekbar_system = (SeekBar) dialog.findViewById(R.id.seekbar_system);
        btn_Add = (Button) dialog.findViewById(R.id.btn_add);

        seekbar_Main.setMax(15);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            seekbar_Main.setMin(1);
        }
        seekbar_Main.setProgress(7);

        dialog.show();
        if (playlist.size() > 2) {
            btn_Add.setVisibility(View.GONE);
        } else {
            btn_Add.setVisibility(View.VISIBLE);
        }


        iv.setImageResource(pm.getImageid());

        seekbar_system.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        seekbar_system.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

        seekbar_system.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekbarMain, int i, boolean b) {
                seekbar.setProgress(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekbarMain) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekbarMain) {

            }
        });
        seekbar_Main.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mainplayer != null) {
                    mainplayer.setVolume((float) progress, (float) progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        flag = 0;
        btn_Add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Createdialog();
                flag = 1;
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (flag == 0) {
                    ArrayList<playlistmodel> listofsongs = new ArrayList<>();
                    if (playlist != null) {
                        for (int i = 0; i < playlist.size(); i++) {
                            playlistmodel pm = new playlistmodel(playlist.get(i).getTag(), playlist.get(i).getImageid(), playlist.get(i).getSongid(), playlist.get(i).getVolume());
                            listofsongs.add(pm);
                        }
                        MyPrefrence myPrefrence = new MyPrefrence(MainActivity.this);
                        myPrefrence.setlist(listofsongs);
                    }
                }
            }
        });

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_DOWN) {
                    audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
                    int volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    if (volume < max) {
                        int lastvolume = seekbar.getProgress();
                        volume = lastvolume + 1;
                        seekbar.setProgress(volume);
                    }
                    Log.e("volumeinc", "" + volume);

                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                    audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
                    int volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    Log.e("volumedec", "" + volume);
                    if (volume > 0) {
                        int lastvolume = seekbar.getProgress();
                        volume = lastvolume - 1;
                        seekbar.setProgress(volume);
                    }
                    seekbar.setProgress(volume);
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(AddEvent event) {
        if (event.getActionAdd() == AddEvent.ActionAdd) {
            if (playlist.size() < 3) {
                playlistmodel pm = event.getplmodel();
                playlist.add(new playlistmodel(pm.getTag(), pm.getImageid(), pm.getSongid(), pm.getVolume()));
                itemListDataAdapter = new Playlistadaptor(MainActivity.this, playlist);
                rvplaylsit.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
                rvplaylsit.setAdapter(itemListDataAdapter);
                Toast.makeText(this, "Hey, my message" + event.getActionAdd(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Delete any one music" + event.getActionAdd(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onEvent(TimerEvent event) {
        dialog.dismiss();
        if (event.getActionAdd() == TimerEvent.TimeSelected) {
            String time = event.gettimer();
            Log.e("timer", "" + time);
//            presenter.setSleepTimer(Integer.parseInt(time));
            startService();


//            if (Log.isLoggable(TAG, Log.VERBOSE)) {
//                Log.v(TAG, "Starting and binding service");
//            }
//            startservice();
//            if (time.equals("10 min")) {
//                timerService.endTime = 600;
//                Toast.makeText(MainActivity.this, "600", Toast.LENGTH_SHORT).show();
//            } else if (time.equals("30 min")) {
//                timerService.endTime = 1800;
//                Toast.makeText(MainActivity.this, "600", Toast.LENGTH_SHORT).show();
//
//            } else if (time.equals("1 hr")) {
//                timerService.endTime = 3600;
//                Toast.makeText(MainActivity.this, "600", Toast.LENGTH_SHORT).show();
//
//            } else if (time.equals("2 hr")) {
//                timerService.endTime = 7200;
//            } else if (time.equals("3 hr")) {
//                timerService.endTime = 9000;
//            } else if (time.equals("4 hr")) {
//                timerService.endTime = 10800;
//            } else if (time.equals("5 hr")) {
//                timerService.endTime = 18000;
//            } else if (time.equals("7 hr")) {
//                timerService.endTime = 25200;
//            }

        }
    }

    public void startService() {

        Intent intent = new Intent(MainActivity.this, StreamService.class);
        if (!isServiceAlreadyRunning()) {
            Log.i("MainActivity.this", "onStart: service not running, starting service.");
            startService(intent);
        }

        if (!boundToService) {
            Log.i("MainActivity.this", "onStart: binding to service.");
            boundToService = bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        }
        registerBroadcastReceiver();
    }

    /**
     * See if the StreamService is already running in the background.
     *
     * @return boolean indicating if the service runs
     */
    private boolean isServiceAlreadyRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (StreamService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void registerBroadcastReceiver() {

        Log.i("MainActivity.this", "onStart: registering broadcast receiver.");
        IntentFilter broadcastIntentFilter = new IntentFilter();
        broadcastIntentFilter.addAction(StreamService.STREAM_DONE_LOADING_INTENT);
        broadcastIntentFilter.addAction(StreamService.TIMER_DONE_INTENT);
        broadcastIntentFilter.addAction(StreamService.TIMER_UPDATE_INTENT);
        LocalBroadcastManager.getInstance(this).registerReceiver((broadcastReceiver), broadcastIntentFilter);
    }

    public void createdialog() {
        dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.activity_timer); //layout for dialog
        dialog.setTitle("Add a new friend");
        dialog.setCancelable(false); //none-dismiss when touching outside Dialog
        // set the custom dialog components - texts and image
        RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.rv_timer);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
        timeradaptor madapter = new timeradaptor(this, time);
        recyclerView.setAdapter(madapter);
        dialog.show();

    }


    public void stopplayer() {
        if (mainplayer != null) {
            mainplayer.release();
            mainplayer = null;
        }
        if (player1 != null) {
            player1.release();
            player1 = null;
        }
        if (player2 != null) {
            player2.release();
            player2 = null;
        }
        if (player3 != null) {
            player3.release();
            player3 = null;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        String TAG = "LOG_PERMISSION";
        Log.d(TAG, "Permission callback called-------");
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(android.Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions

                    if (perms.get(android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            ) {
                        Log.d(TAG, "Phone state and storage permissions granted");
                        // process the normal flow
                        //else any one or both the permissions are not granted

                    } else {
                        Log.d(TAG, "Some permissions are not granted ask again ");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                      //shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
                            showDialogOK("Phone state and storage permissions required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
                                    .show();
                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }

    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    private boolean checkAndRequestPermissions() {
        if (SDK_INT >= Build.VERSION_CODES.M) {
            int permissionReadPhoneState = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
            int permissionStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            List<String> listPermissionsNeeded = new ArrayList<>();

            if (permissionReadPhoneState != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
            }

            if (permissionStorage != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }

            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

}
