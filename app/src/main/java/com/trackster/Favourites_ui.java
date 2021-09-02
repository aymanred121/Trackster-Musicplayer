package com.trackster;

import android.os.Bundle;
import android.view.View;

import androidx.constraintlayout.motion.widget.MotionLayout;

import com.google.android.material.button.MaterialButton;

public class Favourites_ui extends MainActivity {

    //Views
    private MaterialButton vBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favourites_ui);
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

    // Functions
    private void InitializingViews() {

        vBackButton = findViewById(R.id.Favourites_back_btn);


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
            Favourites_ui.super.onBackPressed();
        }

        @Override
        public void onTransitionTrigger(MotionLayout motionLayout, int triggerId, boolean positive, float progress) {

        }
    };

}