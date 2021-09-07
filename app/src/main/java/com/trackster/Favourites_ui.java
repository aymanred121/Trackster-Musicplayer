package com.trackster;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Adapters.SongAdapter;
import com.google.android.material.button.MaterialButton;
import com.roomdb.Track;
import com.roomdb.roomViewModel;

import java.util.List;

public class Favourites_ui extends UI {

    //Views
    private MaterialButton vBackButton;
    private RecyclerView vSongsRecyclerView;


    //Variable
    private RecyclerView.LayoutManager mSongsLayoutManager;
    private SongAdapter mSongAdapter;
    private roomViewModel mViewModel;
    private List<Track> mTrackList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favourites_ui);
        InitializingViews();
        setupRecyclerView();


        vBackButton.setOnClickListener(lBackButton);
        mSongAdapter.setOnItemClickListener(lSongAdapter);


        vPlayingNowBackButton.setOnClickListener(lPlayingNowBackButton);
        vPlayingNowBar.setOnClickListener(lPlayingNowBar);
        vPlayButton.setOnClickListener(lPlayButton);
        vPlayToggle.setOnClickListener(lPlayToggle);
        vSongSeekBar.setSeekBarChangeListener(lSongSeekBar);
        vPlayingState.setOnClickListener(lPlayingState);
        vAddToPlaylist.setOnClickListener(lAddToPlaylist);
        vFavouritesToggle.setOnClickListener(lFavouritesToggle);
        vForwardButton.setOnClickListener(lForwardButton);
        vBackwardButton.setOnClickListener(lBackwardButton);
        vSong.setOnScrollChangeListener(lSong);

    }

    @Override
    public void onBackPressed() {
        if (isBarOpened) {
            goBack();
        } else
            close();
    }

    // Functions
    private void InitializingViews() {

        vBackButton = findViewById(R.id.Favourites_back_btn);
        vSongsRecyclerView = findViewById(R.id.Favourites_recyclerview);


        vMain = findViewById(R.id.Favourites);
        vPlayingNowBar = findViewById(R.id.Favourites_playing_now_bar);
        vPlayingNowBackButton = findViewById(R.id.Favourites_playing_back_btn);
        vPlayButton = findViewById(R.id.Favourites_playing_play_pause_btn);
        vPlayToggle = findViewById(R.id.Favourites_play_pause_btn);
        vSongSeekBar = findViewById(R.id.Favourites_playing_song_time);
        vSongProgressBar = findViewById(R.id.Favourites_song_time);
        vPlayingState = findViewById(R.id.Favourites_playing_play_state);
        vFavouritesToggle = findViewById(R.id.Favourites_playing_add_to_favourite);
        vAddToPlaylist = findViewById(R.id.Favourites_playing_add_to_playlist);
        vForwardButton = findViewById(R.id.Favourites_playing_forward_btn);
        vBackwardButton = findViewById(R.id.Favourites_playing_backward_btn);
        vToCover = findViewById(R.id.Favourites_playing_to_cover);
        vToLyrics = findViewById(R.id.Favourites_playing_to_lyrics);
        vSong = findViewById(R.id.Favourites_playing_scroll);
        vBarSongName = findViewById(R.id.Favourites_song_name);
        vBarArtistName = findViewById(R.id.Favourites_artist_name);
        vBarSongCover = findViewById(R.id.Favourites_song_cover);
        vSongName = findViewById(R.id.Favourites_playing_song_name);
        vArtistName = findViewById(R.id.Favourites_playing_artist_name);
        vSongCover = findViewById(R.id.Favourites_playing_song_cover);
        vSongLyrics = findViewById(R.id.Favourites_playing_lyrics);


        if (isExist) {
            sync();
            rSongTimer.run();
            vMain.setTransition(R.id.open_withbar_transition);
        } else
            vMain.setTransition(R.id.open_withoutbar_transition);

        vMain.transitionToStart();
    }

    private void setupRecyclerView() {
        vSongsRecyclerView.setHasFixedSize(true);
        mSongsLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mSongAdapter = new SongAdapter(R.layout.home_song_item);
        vSongsRecyclerView.setLayoutManager(mSongsLayoutManager);
        vSongsRecyclerView.setAdapter(mSongAdapter);
        vSongsRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mViewModel = new ViewModelProvider(this).get(roomViewModel.class);
        mViewModel.getAlltracksfromplaylist("favourites").observe(this, new Observer<List<Track>>() {
            @Override
            public void onChanged(@Nullable List<Track> tracks) {
                mSongAdapter.setSongsList(tracks);
                mCurrentList = tracks;
            }
        });
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
            mQueue = mTrackList;
            mPlayingNow = mTrackList.get(position);
            openSong();
        }
    };


}