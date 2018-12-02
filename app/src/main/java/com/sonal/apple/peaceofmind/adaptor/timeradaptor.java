package com.sonal.apple.peaceofmind.adaptor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sonal.apple.peaceofmind.Event.TimerEvent;
import com.sonal.apple.peaceofmind.R;

import de.greenrobot.event.EventBus;

public class timeradaptor extends RecyclerView.Adapter<timeradaptor.ItemRowHolder> {


    private Context mContext;


    private String[] soundlist;

    public timeradaptor(Context context, String[] list) {
        mContext = context;
        this.soundlist = list;

    }

    @Override
    public timeradaptor.ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_timer, null);
        timeradaptor.ItemRowHolder mh = new timeradaptor.ItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(final timeradaptor.ItemRowHolder itemRowHolder, final int i) {
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
        private TextView textView;

        public ItemRowHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_timer);
            textView.setOnClickListener(this);
        }

        public void onbind(final int i) {
            textView.setTag(i);
            textView.setText(soundlist[i]);
        }

        @Override
        public void onClick(View v) {
            int tag = (int) v.getTag();
            if (v == textView) {
                Log.e("Time", "" + soundlist[tag]);
                EventBus.getDefault().postSticky(new TimerEvent(tag, soundlist[tag]));
            }
        }
    }
}
