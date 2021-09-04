package com.trackster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.material.button.MaterialButton;

import hiennguyen.me.circleseekbar.CircleSeekBar;

import static com.trackster.PlayingState.Repeat;
import static com.trackster.PlayingState.RepeatOnce;
import static com.trackster.PlayingState.Shuffle;

public class MainActivity extends UI {

    // Views
    private MaterialButton vOpenFavouritesButton;
    private MaterialButton vOpenPlaylistsButton;
    private EditText vSearchSongEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitializingViews();


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

        vOpenPlaylistsButton.setOnClickListener(lOpenPlaylists);
        vOpenFavouritesButton.setOnClickListener(lOpenFavourites);


    }

    @Override
    protected void onResume() {
        super.onResume();
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

    }
    private void openFavourites() {
        Intent intent = new Intent(this, Favourites_ui.class);
        startActivity(intent);
    }
    private void openPlaylists() {
        Intent intent = new Intent(this, Playlists_ui.class);
        startActivity(intent);
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


}