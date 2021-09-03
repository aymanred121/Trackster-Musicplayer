package com.trackster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;

public class Playlists_ui extends MainActivity {

    //Views
    private MaterialButton vBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlists_ui);

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

        vBackButton = findViewById(R.id.Playlists_back_btn);


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
            Playlists_ui.super.onBackPressed();
        }

        @Override
        public void onTransitionTrigger(MotionLayout motionLayout, int triggerId, boolean positive, float progress) {

        }
    };
}