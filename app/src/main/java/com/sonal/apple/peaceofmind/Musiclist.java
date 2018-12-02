package com.sonal.apple.peaceofmind;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sonal.apple.peaceofmind.adaptor.RecyclerViewDataAdapter;
import com.sonal.apple.peaceofmind.model.playlistmodel;
import com.sonal.apple.peaceofmind.view.CircleImageView;

import java.util.ArrayList;

public class Musiclist extends AppCompatActivity {
    private final int[] songlist = {R.raw.perfect_storm, R.raw.rain_on_window, R.raw.rain_on_leaves, R.raw.light_rain,
            R.raw.lake_rain, R.raw.rain_on_roof, R.raw.rain_on_sidewalk, R.raw.beach_main,
            R.raw.peaceful_water, R.raw.tent, R.raw.ocean_rain, R.raw.fp_fire, R.raw.th_main};
    private final int[] images = {R.drawable.circle_perfect_storm, R.drawable.circle_rain_on_window, R.drawable.circle_leaves,
            R.drawable.circle_begging_of_the_rain, R.drawable.circle_lake, R.drawable.circle_rain_on_roof,
            R.drawable.circle_sidewalk, R.drawable.circle_beach, R.drawable.circle_peaceful_water, R.drawable.circle_rain_on_tent,
            R.drawable.bg_ocean_rain, R.drawable.circle_fireplace, R.drawable.circle_thunderstorm};
    RecyclerView rv;
    ArrayList<playlistmodel> musiclist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musiclist);
        for (int i = 0; i < songlist.length; i++) {
            playlistmodel playlistmodel = new playlistmodel("mainmusiclist", images[i], songlist[i], 50);
            musiclist.add(playlistmodel);

        }
        rv = (RecyclerView) findViewById(R.id.rv);
        RecyclerViewDataAdapter itemListDataAdapter = new RecyclerViewDataAdapter(getApplicationContext(), musiclist);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        rv.setAdapter(itemListDataAdapter);
    }
}