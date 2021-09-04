package com.trackster;

import android.os.Bundle;
import android.view.View;

import androidx.constraintlayout.motion.widget.MotionLayout;

import com.google.android.material.button.MaterialButton;

import hiennguyen.me.circleseekbar.CircleSeekBar;

public class Playlist_ui extends UI {

    //Views
    private MaterialButton vBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist_ui);
        InitializingViews();


        vBackButton.setOnClickListener(lBackButton);


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

        vBackButton = findViewById(R.id.Playlist_back_btn);


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

        sync();


    }
    private void close() {
        vMain.setTransition(R.id.close_transition);
        vMain.transitionToEnd();
        vMain.setTransitionListener(lMain);
    }

    // Listeners
    private View.OnClickListener lBackButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            close();
        }
    };
    private MotionLayout.TransitionListener lMain = new MotionLayout.TransitionListener() {
        @Override
        public void onTransitionStarted(MotionLayout motionLayout, int startId, int endId) {

        }

        @Override
        public void onTransitionChange(MotionLayout motionLayout, int startId, int endId, float progress) {

        }

        @Override
        public void onTransitionCompleted(MotionLayout motionLayout, int currentId) {
            Playlist_ui.super.onBackPressed();
        }

        @Override
        public void onTransitionTrigger(MotionLayout motionLayout, int triggerId, boolean positive, float progress) {

        }
    };

}