package com.roomdb;

import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import java.util.List;

public class roomRepo {
    private ContainsDao containsDao;
    private PlaylistDao playlistDao;
    private TrackDao trackDao;
    private LiveData<List<Track>> AllTracks;
    private LiveData<List<Playlist>> AllPlaylists;

    public roomRepo(Application app) {
        TracksterRoomDb db = TracksterRoomDb.getInstance(app);
        containsDao = db.containsDao();
        playlistDao = db.playlistDao();
        trackDao = db.trackDao();
        AllTracks = trackDao.getallTracks();
        AllPlaylists= playlistDao.getallPlaylists();
    }


    //Insert
    public void insert(Track track) {
        new insertTrack(trackDao).execute(track);

    }



    public void insert(Contains contains) {
        new insertcontains(containsDao).execute(contains);
    }

    public void insert(Playlist playlist) {
        new insertPlaylist(playlistDao).execute(playlist);

    }


    //Delete
    public void delete(Track track) {
        new deleteTrack(trackDao).execute(track);

    }


    public void delete(Contains contains) {
        new deletecontains(containsDao).execute(contains);
    }

    public void delete(Playlist playlist) {
        new deletePlaylist(playlistDao).execute(playlist);

    }


    //update
    public void update(Track track) {
        new updateTrack(trackDao).execute(track);

    }


    public void update(Contains contains) {
        new updatecontains(containsDao).execute(contains);
    }

    public void update(Playlist playlist) {
        new updatePlaylist(playlistDao).execute(playlist);

    }

    //getalltracks
    public LiveData<List<Track>> getAllTracks() {
        return AllTracks;
    }

    public LiveData<List<Track>> getAllTracksFromPlaylist(String playlistName) {
        return containsDao.getPlaylistTracks(playlistName);
    }

    public LiveData<List<Playlist>> getAllPlaylists() {
        return AllPlaylists;
    }

    //Asynctask insert classes
    private static class insertTrack extends AsyncTask<Track,Void,Void>{
        TrackDao objDao;

        public insertTrack(TrackDao trackDao) {
            this.objDao = trackDao;
        }

        @Override
        protected Void doInBackground(Track... tracks) {
            objDao.insert(tracks[0]);
            return null;
        }
    }

    private static class insertPlaylist extends AsyncTask<Playlist,Void,Void>{
        PlaylistDao objDao;

        public insertPlaylist(PlaylistDao playlistDao) {
            this.objDao = playlistDao;
        }

        @Override
        protected Void doInBackground(Playlist... playlists) {
            objDao.insert(playlists[0]);
            return null;
        }
    }
    private static class insertcontains extends AsyncTask<Contains,Void,Void>{
        ContainsDao objDao;

        public insertcontains(ContainsDao contains) {
            this.objDao = contains;
        }

        @Override
        protected Void doInBackground(Contains... contains) {
            objDao.insert(contains[0]);
            return null;
        }
    }
    //Asyncupdate
    private static class updateTrack extends AsyncTask<Track,Void,Void>{
        TrackDao objDao;

        public updateTrack(TrackDao trackDao) {
            this.objDao = trackDao;
        }

        @Override
        protected Void doInBackground(Track... tracks) {
            objDao.update(tracks[0]);
            return null;
        }
    }

    private static class updatePlaylist extends AsyncTask<Playlist,Void,Void>{
        PlaylistDao objDao;

        public updatePlaylist(PlaylistDao playlistDao) {
            this.objDao = playlistDao;
        }

        @Override
        protected Void doInBackground(Playlist... playlists) {
            objDao.update(playlists[0]);
            return null;
        }
    }
    private static class updatecontains extends AsyncTask<Contains,Void,Void>{
        ContainsDao objDao;

        public updatecontains(ContainsDao contains) {
            this.objDao = contains;
        }

        @Override
        protected Void doInBackground(Contains... contains) {
            objDao.update(contains[0]);
            return null;
        }
    }
    //deleteAsynctask
    private static class deleteTrack extends AsyncTask<Track,Void,Void>{
        TrackDao objDao;

        public deleteTrack(TrackDao trackDao) {
            this.objDao = trackDao;
        }

        @Override
        protected Void doInBackground(Track... tracks) {
            objDao.delete(tracks[0]);
            return null;
        }
    }

    private static class deletePlaylist extends AsyncTask<Playlist,Void,Void>{
        PlaylistDao objDao;

        public deletePlaylist(PlaylistDao playlistDao) {
            this.objDao = playlistDao;
        }

        @Override
        protected Void doInBackground(Playlist... playlists) {
            objDao.delete(playlists[0]);
            return null;
        }
    }
    private static class deletecontains extends AsyncTask<Contains,Void,Void>{
        ContainsDao objDao;

        public deletecontains(ContainsDao contains) {
            this.objDao = contains;
        }

        @Override
        protected Void doInBackground(Contains... contains) {
            objDao.delete(contains[0]);
            return null;
        }
    }



}
