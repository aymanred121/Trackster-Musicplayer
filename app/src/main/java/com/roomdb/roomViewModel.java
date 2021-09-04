package com.roomdb;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class roomViewModel extends AndroidViewModel {
    private roomRepo repo;
    private LiveData<List<Track>> alltracks;
    private LiveData<List<Playlist>> allPlaylists;
    private LiveData<String> artistName;
    public roomViewModel(@NonNull Application application) {
        super(application);
        repo= new roomRepo(application);
        alltracks=repo.getAllTracks();
        allPlaylists=repo.getAllPlaylists();
    }
    //Track
    public void insert(Track obj){
        repo.insert(obj);
    }
    public void delete(Track obj){
        repo.delete(obj);
    }
    public void update(Track obj){
        repo.update(obj);
    }
    public LiveData<List<Track>> getAlltracks() {
        return alltracks;
    }
    public LiveData<String> getArtistName() {
        return artistName;
    }


    //Playlist
    public void insert(Playlist obj){
        repo.insert(obj);
    }
    public void delete(Playlist obj){
        repo.delete(obj);
    }
    public void update(Playlist obj){
        repo.update(obj);
    }
    public LiveData<List<Playlist>> getAllPlaylists() {
        return allPlaylists;
    }

    //contains
    public void insert(Contains obj){
        repo.insert(obj);
    }
    public void delete(Contains obj){
        repo.delete(obj);
    }
    public void update(Contains obj){
        repo.update(obj);
    }
    public LiveData<List<Track>> getAlltracksfromplaylist(String playlistName) {
        return repo.getAllTracksFromPlaylist(playlistName);
    }

}
