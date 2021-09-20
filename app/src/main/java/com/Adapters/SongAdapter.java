package com.Adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.gauravk.audiovisualizer.visualizer.BarVisualizer;
import com.roomdb.Track;
import com.trackster.AudioModelDB;
import com.trackster.R;

import java.util.ArrayList;
import java.util.List;

import static com.trackster.UI.ALBUMART;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ExampleViewHolder> {

    private List<Track> mSongsList = new ArrayList<>();
    private SongAdapter.OnItemClickListener mListener;
    private int mItemLayout;
    private Context context;

    public void filterList(List<Track> searchResult) {
        mSongsList=searchResult;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onSongClick(int position);
    }

    public void setOnItemClickListener(SongAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public ImageView mSongImage;
        public TextView mSongName;
        public TextView mArtistName;
        public TextView mSongTime;

        public ExampleViewHolder(@NonNull View itemView, final SongAdapter.OnItemClickListener listener, int itemLayout) {
            super(itemView);
            if(itemLayout == R.layout.song_item)
            {
                mSongTime = itemView.findViewById(R.id.song_item_song_time);
                mSongImage = itemView.findViewById(R.id.song_item_song_cover);
                mSongName = itemView.findViewById(R.id.song_item_song_name);
                mArtistName = itemView.findViewById(R.id.song_item_song_artist_name);
            }
            else{
                mSongImage = itemView.findViewById(R.id.home_song_item_song_cover);
                mSongName = itemView.findViewById(R.id.home_song_item_song_name);
                mArtistName = itemView.findViewById(R.id.home_song_item_artist_name);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onSongClick(position);
                        }
                    }
                }
            });


        }
    }

    public SongAdapter( int itemLayout) {

        mItemLayout = itemLayout;

    }

    public void setSongsList(List<Track> mSongsList) {
        this.mSongsList = mSongsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SongAdapter.ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context=parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(mItemLayout, parent, false);
        SongAdapter.ExampleViewHolder evh = new SongAdapter.ExampleViewHolder(v, mListener, mItemLayout);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull SongAdapter.ExampleViewHolder holder, int position) {
        Track currentSong = mSongsList.get(position);
//        File file=new File(currentSong.getLocation());
//        if(file.exists())
//            trackinfo= new Trackinfo(context,file);
//        else
//            Toast.makeText(context,"file not found",Toast.LENGTH_LONG).show();

        holder.mSongName.setText(currentSong.getName());
        holder.mArtistName.setText(currentSong.getArtistName());
        if(currentSong.getName().length() > 20)
            holder.mSongName.setSelected(true);
        if(currentSong.getArtistName().length() > 20)
            holder.mArtistName.setSelected(true);

        Glide.with(holder.mSongImage.getContext())
                .load(ALBUMART+currentSong.getID()+".jpg")
                .placeholder(R.drawable.music_note)
                .error(R.drawable.music_note)
                .dontAnimate()
                .into(holder.mSongImage);

        if (mItemLayout == R.layout.song_item) {
            holder.mSongTime.setText(getTime(currentSong.getDuration()/1000));
        }

    }
    private String getTime(int duration){
        int time = duration;
        int seconds = 0;
        int mins = 0;
        String sTime;
        seconds = time % 60;
        time /= 60;
        mins = time;

        sTime = mins + ":";
        if (seconds<10)
            sTime+="0";
        sTime+=seconds;
        return sTime;
    }

    @Override
    public int getItemCount() {
        return mSongsList.size();
    }
}