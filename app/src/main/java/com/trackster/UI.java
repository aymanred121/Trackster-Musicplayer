package com.trackster;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import com.Adapters.SongAdapter;
import com.google.android.material.button.MaterialButton;
import com.roomdb.Track;

import java.util.List;
import java.util.logging.Handler;

import hiennguyen.me.circleseekbar.CircleSeekBar;

import static com.trackster.PlayingState.Repeat;
import static com.trackster.PlayingState.RepeatOnce;
import static com.trackster.PlayingState.Shuffle;

public class UI extends AppCompatActivity {

    // views
    protected MotionLayout vMain;
    protected View vPlayingNowBar;
    protected MaterialButton vPlayingNowBackButton;
    protected MaterialButton vPlayButton;
    protected MaterialButton vToCover;
    protected MaterialButton vToLyrics;
    protected ToggleButton vPlayToggle;
    protected ToggleButton vFavouritesToggle;
    protected CircleSeekBar vSongSeekBar;
    protected ProgressBar vSongProgressBar;
    protected ImageView vPlayingState;
    protected ImageView vAddToPlaylist;
    protected ImageView vForwardButton;
    protected ImageView vBackwardButton;
    protected HorizontalScrollView vSong;
    protected TextView vSongName;
    protected TextView vArtistName;
    protected ImageView vSongCover;
    protected TextView vBarSongName;
    protected TextView vBarArtistName;
    protected ImageView vBarSongCover;


    // globals
    protected static MediaPlayer mAudio;
    protected static Track mPlayingNow;
    protected static List<Track> mQueue;
    protected static List<Track> mCurrentList;
    protected static int progress = 0;
    protected static boolean playing = false;
    protected static boolean isExist = false;
    public static final String SHAREDPREF = "SHAREDPERF";
    public static final String ID = "ID";
    public static final String QUEUEPOS = "QUEUEPOS";
    public static final String ORIGIN = "ORIGIN";

    // variables
    protected boolean isBarOpened = false;
    protected AnimatedVectorDrawableCompat avdc;
    protected AnimatedVectorDrawable avd;
    protected PlayingState playingState = Repeat;

    // functions
    protected void setPlayButton() {
        if (vPlayToggle.isChecked()) {
            vPlayButton.setIcon(getResources().getDrawable(R.drawable.playing));
        } else {
            vPlayButton.setIcon(getResources().getDrawable(R.drawable.paused));
        }
    }

    protected void playSong() {
        if (playing)
            mAudio.start();
        else
            mAudio.pause();
        runOnUiThread(rSongTimer);
    }

    protected void close() {
        if (isExist)
            vMain.setTransition(R.id.withbar_close_transition);
        else
            vMain.setTransition(R.id.withoutbar_close_transition);
        vMain.transitionToEnd();
        vMain.setTransitionListener(lMain);
    }

    protected void goBack() {
        vMain.setTransition(R.id.back_transition);
        vMain.transitionToEnd();
        isBarOpened = false;
    }

    protected void PlayNPause() {
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
        }
        vPlayToggle.setChecked(!vPlayToggle.isChecked());
        playing = vPlayToggle.isChecked();
        playSong();

    }

    protected void ChangeState() {
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

    protected void sync() {
        vSongProgressBar.setProgress(progress);
        vSongSeekBar.setProgressDisplayAndInvalidate(progress);
        vPlayToggle.setChecked(playing);
        setPlayButton();
        setupSong();

    }

    protected void openBar() {
        if (!isBarOpened) {
            if (isExist)
                vMain.setTransition(R.id.open_song_transition);
            else
                vMain.setTransition(R.id.nobar_transition);

            vMain.transitionToEnd();
            isBarOpened = true;
        }

    }

    protected void setupSong() {
        vSongName.setText(mPlayingNow.getName());
        vBarSongName.setText(mPlayingNow.getName());
        vArtistName.setText(mPlayingNow.getArtistName());
        vBarArtistName.setText(mPlayingNow.getArtistName());
        if (mPlayingNow.getName().length() > 20)
            vSongName.setSelected(true);
        if (mPlayingNow.getArtistName().length() > 20)
            vArtistName.setSelected(true);
        if (mPlayingNow.getCover() != null) {
            vSongCover.setImageBitmap(BitmapFactory.decodeByteArray(mPlayingNow.getCover(), 0, mPlayingNow.getCover().length));
            vBarSongCover.setImageBitmap(BitmapFactory.decodeByteArray(mPlayingNow.getCover(), 0, mPlayingNow.getCover().length));
        }
    }

    protected void updateLastPlayedSong() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHAREDPREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(ID, mPlayingNow.getID());
//        editor.putInt(QUEUEPOS,trackPosition);
//        editor.putInt(ORIGIN,queueState);
        editor.apply();
    }

    protected void openSong(){
        playing = true;
        vPlayToggle.setChecked(true);
        setPlayButton();
        openBar();
        if (mAudio != null)
            mAudio.release();
        isExist = true;
        setupSong();
        updateLastPlayedSong();
        mAudio = MediaPlayer.create(MainActivity.mContext, Uri.parse(mPlayingNow.getLocation()));
        playSong();
    }

    // listeners
    protected View.OnClickListener lPlayingNowBackButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            goBack();
        }
    };
    protected View.OnClickListener lPlayingNowBar = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openBar();
        }
    };


    protected View.OnClickListener lPlayButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PlayNPause();
        }
    };
    protected View.OnClickListener lPlayToggle = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            playing = vPlayToggle.isChecked();
            setPlayButton();
            playSong();
        }
    };
    protected View.OnClickListener lFavouritesToggle = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
    protected View.OnClickListener lAddToPlaylist = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
    protected View.OnClickListener lPlayingState = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ChangeState();
        }
    };
    protected View.OnClickListener lForwardButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
    protected View.OnClickListener lBackwardButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
    protected View.OnScrollChangeListener lSong = new View.OnScrollChangeListener() {
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
    protected CircleSeekBar.OnSeekBarChangedListener lSongSeekBar = new CircleSeekBar.OnSeekBarChangedListener() {
        @Override
        public void onPointsChanged(CircleSeekBar circleSeekBar, int points, boolean fromUser) {
            if (fromUser) {
                progress = (int) (points * (float) mAudio.getDuration() / 100);
                mAudio.seekTo(progress);
            }
            progress = points;
            vSongProgressBar.setProgress(progress);
        }

        @Override
        public void onStartTrackingTouch(CircleSeekBar circleSeekBar) {
            mAudio.pause();
        }

        @Override
        public void onStopTrackingTouch(CircleSeekBar circleSeekBar) {
            mAudio.start();
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
            UI.super.onBackPressed();
        }

        @Override
        public void onTransitionTrigger(MotionLayout motionLayout, int triggerId, boolean positive, float progress) {

        }
    };
    protected Runnable rSongTimer = new Runnable() {
        @Override
        public void run() {
            if (mAudio != null) {
                // TODO getduration equals 0 if the audio hasn't been played
                progress = mAudio.getCurrentPosition() * 100 / mAudio.getDuration();
                vSongSeekBar.setProgressDisplayAndInvalidate(progress);
            }
            MainActivity.mHandler.postDelayed(this, 300);
        }
    };
}
