package com.sonal.apple.peaceofmind.adaptor;

import android.content.Context;
import android.content.Intent;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sonal.apple.peaceofmind.MainActivity;
import com.sonal.apple.peaceofmind.Musiclist;
import com.sonal.apple.peaceofmind.R;
import com.sonal.apple.peaceofmind.model.playlistmodel;
import com.sonal.apple.peaceofmind.view.CircleImageView;

import java.util.ArrayList;

/**
 * Created by apple on 02/03/18.
 */

public class RecyclerViewDataAdapter extends RecyclerView.Adapter<RecyclerViewDataAdapter.ItemRowHolder> {

    ArrayList<playlistmodel> amusiclist = new ArrayList<>();
    private Context mContext;

    public RecyclerViewDataAdapter(Context context, ArrayList<playlistmodel> musiclist) {
        mContext = context;
        this.amusiclist = musiclist;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_song, null);
        ItemRowHolder mh = new ItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(final ItemRowHolder itemRowHolder, final int i) {
        itemRowHolder.onbind(i);
    }

    @Override
    public int getItemCount() {
        return (null != amusiclist ? amusiclist.size() : 0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View itemview;
        private TextView tv;


        public ItemRowHolder(View itemView) {
            super(itemView);
            this.itemview = itemView;
            tv = (TextView) itemView.findViewById(R.id.tv_music);
            itemView.setOnClickListener(this);
        }


        public void onbind(int i) {
            itemView.setTag(i);
            tv.setText("Releax Music " + i);
        }

        @Override
        public void onClick(View v) {
            int tag = (int) v.getTag();
            if (v == itemView) {
                mContext.startActivity(new Intent(mContext, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("song", amusiclist.get(tag)).putExtra("index", String.valueOf(tag)));

            }
        }


    }
}

