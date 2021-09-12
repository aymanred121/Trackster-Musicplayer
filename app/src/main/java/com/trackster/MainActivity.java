package com.trackster;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Adapters.SongAdapter;
import com.google.android.material.button.MaterialButton;
import com.roomdb.Track;
import com.roomdb.TracksterRoomDb;
import com.roomdb.roomViewModel;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends UI {

    // Views
    private MaterialButton vOpenFavouritesButton;
    private MaterialButton vOpenPlaylistsButton;
    private EditText vSearchSongEdit;
    private RecyclerView vSongsRecyclerView;


    // Variables
    private RecyclerView.LayoutManager mSongsLayoutManager;
    private SongAdapter mSongAdapter;
    private roomViewModel mViewModel;
    private List<Track> mTrackList;
    public static Handler mHandler = new Handler();
    private static List<Track> searchResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setPermissionAndDB();
        InitializingViews();
        setupRecyclerView();

        vSearchSongEdit.addTextChangedListener(lSearchBar);
        vOpenPlaylistsButton.setOnClickListener(lOpenPlaylists);
        vOpenFavouritesButton.setOnClickListener(lOpenFavourites);
        mSongAdapter.setOnItemClickListener(lSongAdapter);
        setupListeners();


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPlayingNow == null)
            retrieveLastPlayedSong();
        if (mPlayingNow != null && mAudio != null)
            sync();

    }

    @Override
    public void onBackPressed() {
        if (isBarOpened) {
            goBack();
        } else
            super.onBackPressed();
    }

    // functions
    private void InitializingViews() {
        // for HomePage
        vOpenFavouritesButton = findViewById(R.id.HomePage_toFavourites);
        vOpenPlaylistsButton = findViewById(R.id.HomePage_toPlaylists);
        vSearchSongEdit = findViewById(R.id.HomePage_search_song);
        vSongsRecyclerView = findViewById(R.id.HomePage_recyclerView);
        // for playing bar
        vMain = findViewById(R.id.HomePage);
        vPlayingNowBar = findViewById(R.id.HomePage_playing_now_bar);
        vPlayingNowBackButton = findViewById(R.id.HomePage_playing_back_btn);
        vPlayButton = findViewById(R.id.HomePage_playing_play_pause_btn);
        vPlayToggle = findViewById(R.id.HomePage_play_pause_btn);
        vSongSeekBar = findViewById(R.id.HomePage_playing_song_time);
        vSongProgressBar = findViewById(R.id.HomePage_song_time);
        vPlayingState = findViewById(R.id.HomePage_playing_play_state);
        vFavouritesToggle = findViewById(R.id.HomePage_playing_add_to_favourite);
        vAddToPlaylist = findViewById(R.id.HomePage_playing_add_to_playlist);
        vForwardButton = findViewById(R.id.HomePage_playing_forward_btn);
        vBackwardButton = findViewById(R.id.HomePage_playing_backward_btn);
        vToCover = findViewById(R.id.HomePage_playing_to_cover);
        vToLyrics = findViewById(R.id.HomePage_playing_to_lyrics);
        vSong = findViewById(R.id.HomePage_playing_scroll);
        vBarSongName = findViewById(R.id.HomePage_song_name);
        vBarArtistName = findViewById(R.id.HomePage_artist_name);
        vBarSongCover = findViewById(R.id.HomePage_song_cover);
        vSongName = findViewById(R.id.HomePage_playing_song_name);
        vArtistName = findViewById(R.id.HomePage_playing_artist_name);
        vSongCover = findViewById(R.id.HomePage_playing_song_cover);
        vSongLyrics = findViewById(R.id.HomePage_playing_lyrics);



    }

    private void openFavourites() {
        Intent intent = new Intent(this, Favourites_ui.class);
        startActivity(intent);
    }

    private void openPlaylists() {
        Intent intent = new Intent(this, Playlists_ui.class);
        startActivity(intent);
    }

    private void setupRecyclerView() {
        vSongsRecyclerView.setHasFixedSize(true);
        mSongsLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mSongAdapter = new SongAdapter(R.layout.home_song_item);
        vSongsRecyclerView.setLayoutManager(mSongsLayoutManager);
        vSongsRecyclerView.setAdapter(mSongAdapter);
        vSongsRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mViewModel = new ViewModelProvider(this).get(roomViewModel.class);
        mViewModel.getAlltracks().observe(this, new Observer<List<Track>>() {
            @Override
            public void onChanged(@Nullable List<Track> tracks) {
                mSongAdapter.setSongsList(tracks);
                mTrackList = tracks;
                //To fill the Queue when restarting the App
                if(state == queueState.main)
                    mQueue = tracks;
            }
        });
    }

    private void retrieveLastPlayedSong() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHAREDPREF, MODE_PRIVATE);
        isExist = TracksterRoomDb.getInstance(this).trackDao().isTrackExist(sharedPreferences.getInt(ID, -1));
        vMain.setTransition(R.id.showSong_transition);
        if (isExist) {
            mPlayingNow = TracksterRoomDb.getInstance(this).trackDao().getTrackByID(sharedPreferences.getInt(ID, -1));
            mAudio = MediaPlayer.create(this, Uri.parse(mPlayingNow.getLocation()));
            vSongSeekBar.setMax(mAudio.getDuration());
            vSongProgressBar.setMax(mAudio.getDuration());
            setupSong();
            vMain.transitionToEnd();
            trackPosition=sharedPreferences.getInt(QUEUEPOS,-1);
            state=queueState.values()[sharedPreferences.getInt(ORIGIN,1)];
            if(state!=queueState.main)
            mQueue=TracksterRoomDb.getInstance(this).containsDao().getListPlaylistTracks(sharedPreferences.getString(PLAYLISTNAME,""));


        } else
            vMain.transitionToStart();


    }

    private void setPermissionAndDB() {
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
        while (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
        }

    }

    private void filter(String s) {
        searchResult = new ArrayList<>();
        for (Track t : mTrackList) {
            if (t.getName().toLowerCase().startsWith(s.toLowerCase())) {
                searchResult.add(t);
            }

        }
        //trackList=(searchResult);
        mSongAdapter.filterList(searchResult);


    }







    // listeners
    private View.OnClickListener lOpenFavourites = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openFavourites();
        }
    };
    private View.OnClickListener lOpenPlaylists = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openPlaylists();
        }
    };
    private SongAdapter.OnItemClickListener lSongAdapter = new SongAdapter.OnItemClickListener() {
        @Override
        public void onSongClick(int position) {
            if(!isBarOpened)
            {
                state=queueState.main;
                mQueue = mTrackList;
                trackPosition=position;
                if (vSearchSongEdit.getText().toString().trim().isEmpty())
                    mPlayingNow = mTrackList.get(position);
                else
                    mPlayingNow = searchResult.get(position);
                openSong();

            }
        }
    };
    private TextWatcher lSearchBar = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            filter(s.toString());
        }
    };
}