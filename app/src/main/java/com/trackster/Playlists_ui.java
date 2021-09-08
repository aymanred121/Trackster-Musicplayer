package com.trackster;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.Adapters.PlaylistAdapter;
import com.Dialogs.AddPlaylistDialog;
import com.google.android.material.button.MaterialButton;
import com.roomdb.Playlist;
import com.roomdb.roomViewModel;

import java.util.List;

public class Playlists_ui extends UI {

    //Views
    private MaterialButton vAddPlaylistButton;
    private MaterialButton vBackButton;
    private RecyclerView vPlaylistsRecyclerView;

    //Variables
    private RecyclerView.LayoutManager mPlaylistsLayoutManager;
    private PlaylistAdapter mPlaylistAdapter;
    private roomViewModel mViewModel;
    private List<Playlist> mPlaylistsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlists_ui);

        InitializingViews();
        setupRecyclerView();


        vBackButton.setOnClickListener(lBackButton);
        vAddPlaylistButton.setOnClickListener(lAddPlaylistButton);
        mPlaylistAdapter.setOnItemClickListener(lPlaylists);

        setupListeners();
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

        vBackButton = findViewById(R.id.Playlists_back_btn);
        vPlaylistsRecyclerView = findViewById(R.id.Playlists_recyclerview);
        vAddPlaylistButton = findViewById(R.id.Playlists_add_playlist);

        vMain = findViewById(R.id.Playlists);
        vPlayingNowBar = findViewById(R.id.Playlists_playing_now_bar);
        vPlayingNowBackButton = findViewById(R.id.Playlists_playing_back_btn);
        vPlayButton = findViewById(R.id.Playlists_playing_play_pause_btn);
        vPlayToggle = findViewById(R.id.Playlists_play_pause_btn);
        vSongSeekBar = findViewById(R.id.Playlists_playing_song_time);
        vSongProgressBar = findViewById(R.id.Playlists_song_time);
        vPlayingState = findViewById(R.id.Playlists_playing_play_state);
        vFavouritesToggle = findViewById(R.id.Playlists_playing_add_to_favourite);
        vAddToPlaylist = findViewById(R.id.Playlists_playing_add_to_playlist);
        vForwardButton = findViewById(R.id.Playlists_playing_forward_btn);
        vBackwardButton = findViewById(R.id.Playlists_playing_backward_btn);
        vToCover = findViewById(R.id.Playlists_playing_to_cover);
        vToLyrics = findViewById(R.id.Playlists_playing_to_lyrics);
        vSong = findViewById(R.id.Playlists_playing_scroll);
        vBarSongName = findViewById(R.id.Playlists_song_name);
        vBarArtistName = findViewById(R.id.Playlists_artist_name);
        vBarSongCover = findViewById(R.id.Playlists_song_cover);
        vSongName = findViewById(R.id.Playlists_playing_song_name);
        vArtistName = findViewById(R.id.Playlists_playing_artist_name);
        vSongCover = findViewById(R.id.Playlists_playing_song_cover);
        vSongLyrics = findViewById(R.id.Playlists_playing_lyrics);


        if (isExist) {
            sync();
            rSongTimer.run();
            vMain.setTransition(R.id.open_withbar_transition);
        } else
            vMain.setTransition(R.id.open_withoutbar_transition);

        vMain.transitionToStart();


    }

    private void setupRecyclerView() {
        vPlaylistsRecyclerView.setHasFixedSize(true);
        mPlaylistsLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mPlaylistAdapter = new PlaylistAdapter();
        vPlaylistsRecyclerView.setLayoutManager(mPlaylistsLayoutManager);
        vPlaylistsRecyclerView.setAdapter(mPlaylistAdapter);
        vPlaylistsRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mViewModel = new ViewModelProvider(this).get(roomViewModel.class);
        mViewModel.getAllPlaylists().observe(this, new Observer<List<Playlist>>() {
            @Override
            public void onChanged(@Nullable List<Playlist> playlists) {
                mPlaylistAdapter.setPlaylistsList(playlists);
                mPlaylistsList = playlists;
            }
        });
        // ToDO doesn't load playlists when opened
    }

    private void openPlaylist() {
        Intent intent = new Intent(this, Playlist_ui.class);
        startActivity(intent);
    }

    public void openAddPlaylistDialog() {
        AddPlaylistDialog dialog = new AddPlaylistDialog();
        dialog.show(getSupportFragmentManager(), "");
    }

    // Listeners
    private View.OnClickListener lBackButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            close();
        }
    };
    private PlaylistAdapter.OnItemClickListener lPlaylists = new PlaylistAdapter.OnItemClickListener() {
        @Override
        public void onPlaylistClick(int position) {
            mPlaylistName = mPlaylistsList.get(position).getName();
            openPlaylist();

        }
    };
    private View.OnClickListener lAddPlaylistButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openAddPlaylistDialog();
        }
    };
}