package com.sonal.apple.peaceofmind.adaptor;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.sonal.apple.peaceofmind.MainActivity;
import com.sonal.apple.peaceofmind.Player.Playerone;
import com.sonal.apple.peaceofmind.Player.Playertwo;
import com.sonal.apple.peaceofmind.Player.Playerthree;
import com.sonal.apple.peaceofmind.R;
import com.sonal.apple.peaceofmind.model.playlistmodel;

import java.util.ArrayList;

/**
 * Created by apple on 02/03/18.
 */

public class Playlistadaptor extends RecyclerView.Adapter<Playlistadaptor.ItemRowHolder> {


    ArrayList<playlistmodel> playlist;
    private Activity mContext;

    public Playlistadaptor(Activity context, ArrayList<playlistmodel> playlists) {
        mContext = context;
        this.playlist = playlists;
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_playlist, null);
        ItemRowHolder mh = new ItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(final ItemRowHolder itemRowHolder, final int i) {
        itemRowHolder.onbind(i);
    }

    @Override
    public int getItemCount() {
        return (null != playlist ? playlist.size() : 0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
        private ImageView iv;
        private SeekBar seekbarMain;
        private ImageView ivClose;


        public ItemRowHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.iv);
            seekbarMain = (SeekBar) itemView.findViewById(R.id.seekbar_main);
            ivClose = (ImageView) itemView.findViewById(R.id.iv_close);
            ivClose.setOnClickListener(this);
            seekbarMain.setOnSeekBarChangeListener(this);

        }


        public void onbind(final int i) {
            iv.setImageResource(playlist.get(i).getImageid());
            ivClose.setTag(i);
            seekbarMain.setTag(i);

            Log.e("i", "" + i);
            Log.e("volume", "" + playlist.get(i).getVolume());


            seekbarMain.setProgress(playlist.get(i).getVolume());

            float vol = (float) playlist.get(i).getVolume();
            if (i == 0) {
                if (MainActivity.player1 == null) {
//                    new Playerone(mContext, playlist.get(i).getSongid());
//                    if (MainActivity.player1 != null) {
//                        MainActivity.player1.setVolume(vol, vol);
//                    }

                }
            } else if (i == 1) {
                if (MainActivity.player2 == null) {
                    new Playertwo(mContext, playlist.get(i).getSongid());
                    if (MainActivity.player2 != null) {
                        MainActivity.player2.setVolume(vol, vol);
                    }

                }
            } else if (i == 2) {
                if (MainActivity.player3 == null) {
                    new Playerthree(mContext, playlist.get(i).getSongid());
                    if (MainActivity.player3 != null) {
                        MainActivity.player3.setVolume(vol, vol);
                    }

                }
            }
        }

        @Override
        public void onClick(View v) {
            int tag = (int) v.getTag();
            if (v == ivClose) {
                if (tag == 0) {
                    new Playerone(mContext, -1);
                } else if (tag == 1) {
                    new Playertwo(mContext, -1);
                } else if (tag == 2) {
                    new Playerthree(mContext, -1);
                }
                playlist.remove(tag);
                notifyDataSetChanged();
            }
        }


        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            int tag = (int) seekBar.getTag();
//            float val = (float) progress;
            float val = (float) progress / 100;
//            mp1.setVolume(volume1F, volume1F);
            if (tag == 0) {
                playlistmodel pm = new playlistmodel(playlist.get(0).getTag(), playlist.get(0).getImageid(), playlist.get(0).getSongid(), progress);
                playlist.set(0, pm);
                if (MainActivity.player1 != null) {
                    MainActivity.player1.setVolume(val, val);
                }
            } else if (tag == 1) {
                playlistmodel pm = new playlistmodel(playlist.get(1).getTag(), playlist.get(1).getImageid(), playlist.get(1).getSongid(), progress);
                playlist.set(1, pm);
                if (MainActivity.player2 != null) {
                    MainActivity.player2.setVolume(val, val);

                }
            } else if (tag == 2) {
                playlistmodel pm = new playlistmodel(playlist.get(2).getTag(), playlist.get(2).getImageid(), playlist.get(2).getSongid(), progress);
                playlist.set(2, pm);
                if (MainActivity.player3 != null) {
                    MainActivity.player3.setVolume(val, val);
                }
            }
            Log.e("seekbar", "" + (float) progress);

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            notifyDataSetChanged();
        }
    }
}
