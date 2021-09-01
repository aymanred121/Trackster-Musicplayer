package com.trackster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
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

public class MainActivity extends AppCompatActivity {

    // Views
    private MotionLayout vMain;
    private View vPlayingNowBar;
    private MaterialButton vBackButton;
    private MaterialButton vPlayButton;
    private MaterialButton vToCover;
    private MaterialButton vToLyrics;
    private ToggleButton vPlayToggle;
    private ToggleButton vFavouritesToggle;
    private CircleSeekBar vSongSeekBar;
    private ProgressBar vSongProgressBar;
    private ImageView vPlayingState;
    private ImageView vAddToPlaylist;
    private ImageView vForwardButton;
    private ImageView vBackwardButton;
    private HorizontalScrollView vSong;


    // variables
    private boolean isBarOpened = false;
    private AnimatedVectorDrawableCompat avdc;
    private AnimatedVectorDrawable avd;
    private PlayingState playingState = Repeat;

    // overrides
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitializingViews();

        vBackButton.setOnClickListener(lBackButton);
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
            super.onBackPressed();
    }
    // functions
    private void InitializingViews() {
        vMain = findViewById(R.id.HomePage);
        vPlayingNowBar = findViewById(R.id.HomePage_playing_now_bar);
        vBackButton = findViewById(R.id.HomePage_playing_back_btn);
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
    private void goBack() {
        vMain.setTransition(R.id.back_transition);
        vMain.transitionToEnd();
        isBarOpened = false;
    }
    private void PlayNPause() {
        // animation
        if (vPlayToggle.isChecked()) {
            vPlayButton.setIcon(getResources().getDrawable(R.drawable.playing));
            Drawable drawable = vPlayButton.getIcon();
            if (drawable instanceof AnimatedVectorDrawableCompat) {
                avdc = (AnimatedVectorDrawableCompat) drawable;
                avdc.start();
            } else if (drawable instanceof AnimatedVectorDrawable) {
                avd = (AnimatedVectorDrawable) drawable;
                avd.start();
            }

            // pause music code
        }
        else {
            vPlayButton.setIcon(getResources().getDrawable(R.drawable.paused));
            Drawable drawable = vPlayButton.getIcon();
            if (drawable instanceof AnimatedVectorDrawableCompat) {
                avdc = (AnimatedVectorDrawableCompat) drawable;
                avdc.start();
            } else if (drawable instanceof AnimatedVectorDrawable) {
                avd = (AnimatedVectorDrawable) drawable;
                avd.start();
            }

            //play music code
        }
        vPlayToggle.setChecked(!vPlayToggle.isChecked());

    }
    private void ChangeState() {
        // repeat song
        if (playingState == Repeat) {
            vPlayingState.setImageResource(R.drawable.ic_repeat_one);
            Toast.makeText(this, "Current song is looped", Toast.LENGTH_SHORT).show();
            playingState = RepeatOnce;

        }
        // shuffle
        else if (playingState == RepeatOnce) {
            vPlayingState.setImageResource(R.drawable.ic_shuffle);
            Toast.makeText(this, "Playback is shuffled", Toast.LENGTH_SHORT).show();
            playingState = Shuffle;
        }
        // repeat playlist
        else {
            vPlayingState.setImageResource(R.drawable.ic_repeat);
            Toast.makeText(this, "Current playlist is looped", Toast.LENGTH_SHORT).show();
            playingState = Repeat;
        }
    }

    // listeners
    private View.OnClickListener lBackButton =  new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            goBack();
        }
    };
    private View.OnClickListener lPlayingNowBar = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(!isBarOpened) {
                vMain.setTransition(R.id.open_song_transition);
                vMain.transitionToEnd();
                isBarOpened = true;
            }
        }
    };
    private View.OnClickListener lPlayButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PlayNPause();
        }
    };
    private View.OnClickListener lPlayToggle = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(vPlayToggle.isChecked())
                vPlayButton.setIcon(getResources().getDrawable(R.drawable.playing));
            else
                vPlayButton.setIcon(getResources().getDrawable(R.drawable.paused));


        }
    };
    private View.OnClickListener lFavouritesToggle = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
    private View.OnClickListener lAddToPlaylist = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
    private View.OnClickListener lPlayingState = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ChangeState();
        }
    };
    private View.OnClickListener lForwardButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
    private View.OnClickListener lBackwardButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
    private  View.OnScrollChangeListener lSong = new View.OnScrollChangeListener() {
        @Override
        public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
            if (scrollX > oldScrollX) {
                vToCover.setEnabled(false);
                vToLyrics.setEnabled(true);
            } else if (oldScrollX > scrollX) {
                vToCover.setEnabled(true);
                vToLyrics.setEnabled(false);
            }
        }
    };
    private CircleSeekBar.OnSeekBarChangedListener lSongSeekBar = new CircleSeekBar.OnSeekBarChangedListener() {
        @Override
        public void onPointsChanged(CircleSeekBar circleSeekBar, int points, boolean fromUser) {
            vSongProgressBar.setProgress(points);
        }

        @Override
        public void onStartTrackingTouch(CircleSeekBar circleSeekBar) {

        }

        @Override
        public void onStopTrackingTouch(CircleSeekBar circleSeekBar) {

        }
    };

}