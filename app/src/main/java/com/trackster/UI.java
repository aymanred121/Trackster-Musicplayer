package com.trackster;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
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
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.roomdb.Contains;
import com.roomdb.Track;
import com.roomdb.TracksterRoomDb;

import java.io.IOException;
import java.util.List;
import java.util.Random;
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
    protected TextView vSongLyrics;



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
    public static final String PLAYLISTNAME = "PLAYLISTNAME";
    protected static int trackPosition;
    protected  enum  queueState{
        main,favourites,playlist
    };
    protected static queueState state;
    protected static String mPlaylistName;


    // variables
    protected boolean isBarOpened = false;
    protected AnimatedVectorDrawableCompat avdc;
    private MainActivity activity;
    protected AnimatedVectorDrawable avd;
    protected PlayingState playingState = Repeat;
    private  final RotateAnimation rotateAnimation = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    private  final RotateAnimation BarrotateAnimation = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);


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
        {
            mAudio.start();
            coverAnimation();
            vBarSongCover.startAnimation(rotateAnimation);
            vSongCover.startAnimation(BarrotateAnimation);

        }
        else
        {
            mAudio.pause();
            rotateAnimation.cancel();
            BarrotateAnimation.cancel();
        }
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
        updateLyrics();
        if(TracksterRoomDb.getInstance(getApplicationContext()).containsDao().isExist(mPlayingNow.getID(),"favourites")){
            vFavouritesToggle.setChecked(true);
        }else
            vFavouritesToggle.setChecked(false);

        Glide.with(vSongCover.getContext())
                .load(mPlayingNow.getCover())
                .placeholder(R.drawable.music_note)
                .error(R.drawable.music_note)
                .dontAnimate()
                .into(vSongCover);

        Glide.with(vBarSongCover.getContext())
                .load(mPlayingNow.getCover())
                .placeholder(R.drawable.music_note)
                .error(R.drawable.music_note)
                .dontAnimate()
                .into(vBarSongCover);

    }

    protected void updateLyrics() {
        if(mPlayingNow.getLyrics().trim().isEmpty())
            new Trackinfo(this,mPlayingNow);
        vSongLyrics.setText(mPlayingNow.getLyrics());
    }

    protected void updateLastPlayedSong() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHAREDPREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(ID, mPlayingNow.getID());
        editor.putInt(QUEUEPOS,trackPosition);
        editor.putInt(ORIGIN,state.ordinal());
        if(state==queueState.main)
        editor.putString(PLAYLISTNAME,"MAIN");
        else if(state == queueState.favourites)
            editor.putString(PLAYLISTNAME,"favourites");
        else
            editor.putString(PLAYLISTNAME,mPlaylistName);
        editor.apply();
    }

    protected   void openSong(){
        playing = true;
        vPlayToggle.setChecked(true);
        setPlayButton();
        openBar();
        mediaPlayerRelease();
        isExist = true;
        setupSong();
        updateLastPlayedSong();
        mAudio = MediaPlayer.create(this, Uri.parse(mPlayingNow.getLocation()));
        playSong();
    }
    protected   void setSong() {
        mediaPlayerRelease();
        mAudio=MediaPlayer.create(this, Uri.parse(mPlayingNow.getLocation()));
        mAudio.start();
        vPlayButton.setIcon(getResources().getDrawable(R.drawable.playing));
        vFavouritesToggle.setChecked(TracksterRoomDb.getInstance(this).containsDao().isExist(mPlayingNow.getID(),"favourites"));
        vPlayToggle.setChecked(true);

        Glide.with(vSongCover.getContext())
                .load(mPlayingNow.getCover())
                .placeholder(R.drawable.music_note)
                .error(R.drawable.music_note)
                .dontAnimate()
                .into(vSongCover);

        Glide.with(vBarSongCover.getContext())
                .load(mPlayingNow.getCover())
                .placeholder(R.drawable.music_note)
                .error(R.drawable.music_note)
                .dontAnimate()
                .into(vBarSongCover);

        vArtistName.setText(mPlayingNow.getArtistName());
        vSongName.setText(mPlayingNow.getName());
        vBarArtistName.setText(mPlayingNow.getArtistName());
        vBarSongName.setText(mPlayingNow.getName());

        updateLyrics();

    }
    protected   void backwardSong() {
        if (playingState == Shuffle)
            shuffle();
        else {
            trackPosition--;
            if (trackPosition == -1)
                trackPosition = mQueue.size() - 1;
            mPlayingNow = mQueue.get(trackPosition);
            updateLastPlayedSong();
            setSong();
        }
    }
    protected void forwardSong() {
        if (playingState == Shuffle) {
            shuffle();
        } else {
            trackPosition++;
            trackPosition %= mQueue.size();
            mPlayingNow = mQueue.get(trackPosition);
            updateLastPlayedSong();
            setSong();
        }

    }
    protected void shuffle() {
        Random rand = new Random();
        trackPosition = rand.nextInt(mQueue.size());
        mPlayingNow = mQueue.get(trackPosition);
        updateLastPlayedSong();
        setSong();

    }

    private void coverAnimation(){
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        rotateAnimation.setDuration(30000);
        BarrotateAnimation.setInterpolator(new LinearInterpolator());
        BarrotateAnimation.setRepeatCount(Animation.INFINITE);
        BarrotateAnimation.setDuration(30000);

    }
    protected void mediaPlayerRelease(){
        if (mAudio != null)
            mAudio.release();
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
            if(!vFavouritesToggle.isChecked()){
                TracksterRoomDb.getInstance(getApplicationContext()).containsDao().delete(new Contains("favourites",mPlayingNow.getID()));
            }else
                TracksterRoomDb.getInstance(getApplicationContext()).containsDao().insert(new Contains("favourites",mPlayingNow.getID()));
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
                progress = (int) (points * (float) mPlayingNow.getDuration() / 100);
                mAudio.seekTo(progress);
            }
            progress = points;
            vSongProgressBar.setProgress(progress);
        }

        @Override
        public void onStartTrackingTouch(CircleSeekBar circleSeekBar) {
        }

        @Override
        public void onStopTrackingTouch(CircleSeekBar circleSeekBar) {
        }
    };
    protected View.OnClickListener lForwardButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            forwardSong();
        }
    };
    protected View.OnClickListener lBackwardButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            backwardSong();
        }
    };
    protected MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            switch (playingState) {
                case Repeat:
                    forwardSong();
                    break;
                case RepeatOnce:
                    mAudio.start();
                    break;
                case Shuffle:
                    shuffle();
            }

        }
    };
//TODO Button OnlongclickListner

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
                if(mAudio.isPlaying())
                progress = mAudio.getCurrentPosition() * 100 / mPlayingNow.getDuration();
                vSongSeekBar.setProgressDisplayAndInvalidate(progress);
            }
            MainActivity.mHandler.postDelayed(this, 300);
        }
    };

}
