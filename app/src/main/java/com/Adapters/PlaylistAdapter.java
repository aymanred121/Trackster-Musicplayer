package com.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.roomdb.Playlist;
import com.roomdb.TracksterRoomDb;
import com.trackster.R;

import java.util.ArrayList;
import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ExampleViewHolder> {


    private List<Playlist> mPlaylistLists = new ArrayList<>();
    private OnItemClickListener mListener;
    private boolean mIsMenuShown = true;
    private Context context;

    public PlaylistAdapter(boolean mIsMenuShown) {
        this.mIsMenuShown = mIsMenuShown;
    }

    public interface OnItemClickListener {
        void onPlaylistClick(int position);

        void onMenuClick(int position, View view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView playlist_name;
        public TextView playlist_songs_number;
        public ImageButton playlist_menu;

        public ExampleViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            playlist_name = itemView.findViewById(R.id.playlist_item_name);
            playlist_songs_number = itemView.findViewById(R.id.playlist_item_songs_number);
            playlist_menu = itemView.findViewById(R.id.playlist_item_menu);
            playlist_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onMenuClick(position, playlist_menu);
                        }
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onPlaylistClick(position);
                        }
                    }
                }
            });
        }


    }


    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_item, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {

        Playlist currentPlaylist = mPlaylistLists.get(position);
        if (mIsMenuShown)
            holder.playlist_menu.setVisibility(View.VISIBLE);
        else
            holder.playlist_menu.setVisibility(View.GONE);
        holder.playlist_name.setText(currentPlaylist.getName());
        holder.playlist_songs_number.setText(TracksterRoomDb.getInstance(context).containsDao().playlistCount(currentPlaylist.getName()) + " Songs");

    }

    @Override
    public int getItemCount() {

        return mPlaylistLists.size();
    }

    public void setPlaylistsList(List<Playlist> playlistsList) {
        mPlaylistLists = playlistsList;
        notifyDataSetChanged();
    }


}
