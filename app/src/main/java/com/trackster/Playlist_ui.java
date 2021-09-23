package com.trackster;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Adapters.SongAdapter;
import com.google.android.material.button.MaterialButton;
import com.roomdb.Contains;
import com.roomdb.Track;
import com.roomdb.TracksterRoomDb;
import com.roomdb.roomViewModel;

import java.util.Collections;
import java.util.List;

public class Playlist_ui extends UI {

    //Views
    private MaterialButton vBackButton;
    private RecyclerView vSongsRecyclerView;
    private TextView vPlaylistName;


    //Variable
    private RecyclerView.LayoutManager mSongsLayoutManager;
    private SongAdapter mSongAdapter;
    private roomViewModel mViewModel;
    private List<Track> mTrackList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist_ui);
        InitializingViews();
        setupRecyclerView();

        vBackButton.setOnClickListener(lBackButton);
        mSongAdapter.setOnItemClickListener(lSongAdapter);


        setupListeners();

    }

    @Override
    public void onBackPressed() {
        if (isBarOpened) {
            goBack();
        } else
            close();
        TracksterRoomDb.getInstance(getApplicationContext()).containsDao().deleteAll(mPlaylistName);
        for(int i=0;i<mTrackList.size();i++)
            TracksterRoomDb.getInstance(getApplicationContext()).containsDao().insert(new Contains(mPlaylistName,mTrackList.get(i).getID(),i));

    }

    // Functions
    private void InitializingViews() {

        vBackButton = findViewById(R.id.Playlist_back_btn);
        vSongsRecyclerView = findViewById(R.id.Playlist_recyclerview);
        vPlaylistName = findViewById(R.id.Playlist_name);

        vMain = findViewById(R.id.Playlist);
        vPlayingNowBar = findViewById(R.id.Playlist_playing_now_bar);
        vPlayingNowBackButton = findViewById(R.id.Playlist_playing_back_btn);
        vPlayButton = findViewById(R.id.Playlist_playing_play_pause_btn);
        vPlayToggle = findViewById(R.id.Playlist_play_pause_btn);
        vSongSeekBar = findViewById(R.id.Playlist_playing_song_time);
        vSongProgressBar = findViewById(R.id.Playlist_song_time);
        vPlayingState = findViewById(R.id.Playlist_playing_play_state);
        vFavouritesToggle = findViewById(R.id.Playlist_playing_add_to_favourite);
        vAddToPlaylist = findViewById(R.id.Playlist_playing_add_to_playlist);
        vForwardButton = findViewById(R.id.Playlist_playing_forward_btn);
        vBackwardButton = findViewById(R.id.Playlist_playing_backward_btn);
        vToCover = findViewById(R.id.Playlist_playing_to_cover);
        vToLyrics = findViewById(R.id.Playlist_playing_to_lyrics);
        vSong = findViewById(R.id.Playlist_playing_scroll);
        vBarSongName = findViewById(R.id.Playlist_song_name);
        vBarArtistName = findViewById(R.id.Playlist_artist_name);
        vBarSongCover = findViewById(R.id.Playlist_song_cover);
        vSongName = findViewById(R.id.Playlist_playing_song_name);
        vArtistName = findViewById(R.id.Playlist_playing_artist_name);
        vSongCover = findViewById(R.id.Playlist_playing_song_cover);
        vSongLyrics = findViewById(R.id.Playlist_playing_lyrics);


        if (mPlayingNow!=null) {
            sync();
            rSongTimer.run();
            vMain.setTransition(R.id.open_withbar_transition);
        } else
            vMain.setTransition(R.id.open_withoutbar_transition);

        vMain.transitionToStart();

        vPlaylistName.setText(mPlaylistName);


    }

    private void setupRecyclerView() {
        vSongsRecyclerView.setHasFixedSize(true);
        mSongsLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mSongAdapter = new SongAdapter(R.layout.song_item);
        vSongsRecyclerView.setLayoutManager(mSongsLayoutManager);
        vSongsRecyclerView.setAdapter(mSongAdapter);
        vSongsRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mViewModel = new ViewModelProvider(this).get(roomViewModel.class);
        mViewModel.getAllTracksFromPlaylist(mPlaylistName).observe(this, new Observer<List<Track>>() {
            @Override
            public void onChanged(@Nullable List<Track> tracks) {
                mSongAdapter.setSongsList(tracks);
                mTrackList = tracks;
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(vSongsRecyclerView);

    }


    // Listeners
    private View.OnClickListener lBackButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            close();
        }
    };
    private SongAdapter.OnItemClickListener lSongAdapter = new SongAdapter.OnItemClickListener() {
        @Override
        public void onSongClick(int position) {
            if (!isBarOpened) {
                mQueue = mTrackList;
                state = queueState.playlist;
                trackPosition = position;
                mPlayingNow = mTrackList.get(position);
                openBar();
                openSong();
            }
        }
    };

    // touch helper
    private ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END,0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();

            Collections.swap(mTrackList,fromPosition,toPosition);
            recyclerView.getAdapter().notifyItemMoved(fromPosition,toPosition);
            
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };


}