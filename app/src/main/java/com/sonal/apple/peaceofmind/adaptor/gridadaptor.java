package com.sonal.apple.peaceofmind.adaptor;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sonal.apple.peaceofmind.Event.AddEvent;
import com.sonal.apple.peaceofmind.MainActivity;
import com.sonal.apple.peaceofmind.R;
import com.sonal.apple.peaceofmind.model.playlistmodel;

import de.greenrobot.event.EventBus;


public class gridadaptor extends RecyclerView.Adapter<gridadaptor.ItemRowHolder> {


    private Context mContext;

    private int[] soundlist;

    public gridadaptor(Context context, int[] list) {
        mContext = context;
        this.soundlist = list;

    }

    @Override
    public gridadaptor.ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_gridview, null);
        gridadaptor.ItemRowHolder mh = new gridadaptor.ItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(final gridadaptor.ItemRowHolder itemRowHolder, final int i) {
        itemRowHolder.onbind(i);
    }

    @Override
    public int getItemCount() {
        return (null != soundlist ? soundlist.length : 0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageView;

        public ItemRowHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageview);
            imageView.setOnClickListener(this);
        }

        public void onbind(final int i) {
            imageView.setTag(i);
            imageView.setImageResource(soundlist[i]);
        }

        @Override
        public void onClick(View v) {
            int tag = (int) v.getTag();
            if (v == imageView) {
                EventBus.getDefault().postSticky(new AddEvent(1, MainActivity.musicplaylist.get(tag)));
            }
        }
    }
}
