package com.roomdb;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;

public class roomRepo {
    private final ContainsDao containsDao;
    private final PlaylistDao playlistDao;
    private final TrackDao trackDao;
    private final LiveData<List<Track>> AllTracks;
    private final LiveData<List<Playlist>> AllPlaylists;

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
         insertTrack(trackDao,track);

    }



    public void insert(Contains contains) {
         insertContains(containsDao,contains);
    }

    public void insert(Playlist playlist) {
         insertPlaylist(playlistDao,playlist);

    }


    //Delete
    public void delete(Track track) {
         deleteTrack(trackDao,track);

    }


    public void delete(Contains contains) {
         deletecontains(containsDao,contains);
    }

    public void delete(Playlist playlist) {
         deletePlaylist(playlistDao,playlist);

    }


    //update
    public void update(Track track) {
         updateTrack(trackDao,track);

    }


    public void update(Contains contains) {
         updateContains(containsDao,contains);
    }

    public void update(Playlist playlist) {
         updatePlaylist(playlistDao,playlist);

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
    private void insertTrack(TrackDao trackDao,Track track){
        new Thread(
                () -> trackDao.insert(track));

    }
//    private static class insertTrack extends AsyncTask<Track,Void,Void>{
//        TrackDao objDao;
//
//        public insertTrack(TrackDao trackDao) {
//            this.objDao = trackDao;
//        }
//
//        @Override
//        protected Void doInBackground(Track... tracks) {
//            objDao.insert(tracks[0]);
//            return null;
//        }
//    }
    private void insertPlaylist(PlaylistDao Dao,Playlist obj){
    new Thread(
            () -> Dao.insert(obj));

}
//    private static class insertPlaylist extends AsyncTask<Playlist,Void,Void>{
//        PlaylistDao objDao;
//
//        public insertPlaylist(PlaylistDao playlistDao) {
//            this.objDao = playlistDao;
//        }
//
//        @Override
//        protected Void doInBackground(Playlist... playlists) {
//            objDao.insert(playlists[0]);
//            return null;
//        }
//    }
private void insertContains(ContainsDao Dao,Contains obj){
    new Thread(
            () -> Dao.insert(obj));

}
//    private static class insertContains extends AsyncTask<Contains,Void,Void>{
//        ContainsDao objDao;
//
//        public insertContains(ContainsDao contains) {
//            this.objDao = contains;
//        }
//
//        @Override
//        protected Void doInBackground(Contains... contains) {
//            objDao.insert(contains[0]);
//            return null;
//        }
//    }
    //Asyncupdate
private void updateTrack(TrackDao Dao,Track obj){
    new Thread(
            () -> Dao.update(obj));

}
//    private static class updateTrack extends AsyncTask<Track,Void,Void>{
//        TrackDao objDao;
//
//        public updateTrack(TrackDao trackDao) {
//            this.objDao = trackDao;
//        }
//
//        @Override
//        protected Void doInBackground(Track... tracks) {
//            objDao.update(tracks[0]);
//            return null;
//        }
//    }
private void updatePlaylist(PlaylistDao Dao,Playlist obj){
    new Thread(
            () -> Dao.update(obj));

}
//    private static class updatePlaylist extends AsyncTask<Playlist,Void,Void>{
//        PlaylistDao objDao;
//
//        public updatePlaylist(PlaylistDao playlistDao) {
//            this.objDao = playlistDao;
//        }
//
//        @Override
//        protected Void doInBackground(Playlist... playlists) {
//            objDao.update(playlists[0]);
//            return null;
//        }
//    }
private void updateContains(ContainsDao Dao,Contains obj){
    new Thread(
            () -> Dao.update(obj));

}
//    private static class updatecontains extends AsyncTask<Contains,Void,Void>{
//        ContainsDao objDao;
//
//        public updatecontains(ContainsDao contains) {
//            this.objDao = contains;
//        }
//
//        @Override
//        protected Void doInBackground(Contains... contains) {
//            objDao.update(contains[0]);
//            return null;
//        }
//    }
    //deleteAsynctask
private void deleteTrack(TrackDao Dao,Track obj){
    new Thread(
            () -> Dao.delete(obj));

}
//    private static class deleteTrack extends AsyncTask<Track,Void,Void>{
//        TrackDao objDao;
//
//        public deleteTrack(TrackDao trackDao) {
//            this.objDao = trackDao;
//        }
//
//        @Override
//        protected Void doInBackground(Track... tracks) {
//            objDao.delete(tracks[0]);
//            return null;
//        }
//    }
private void deletePlaylist(PlaylistDao Dao,Playlist obj){
    new Thread(
            () -> Dao.delete(obj));

}
//    private static class deletePlaylist extends AsyncTask<Playlist,Void,Void>{
//        PlaylistDao objDao;
//
//        public deletePlaylist(PlaylistDao playlistDao) {
//            this.objDao = playlistDao;
//        }
//
//        @Override
//        protected Void doInBackground(Playlist... playlists) {
//            objDao.delete(playlists[0]);
//            return null;
//        }
//    }
private void deletecontains(ContainsDao Dao,Contains obj){
    new Thread(
            () -> Dao.delete(obj));

}
//    private static class deletecontains extends AsyncTask<Contains,Void,Void>{
//        ContainsDao objDao;
//
//        public deletecontains(ContainsDao contains) {
//            this.objDao = contains;
//        }
//
//        @Override
//        protected Void doInBackground(Contains... contains) {
//            objDao.delete(contains[0]);
//            return null;
//        }
//    }



}
