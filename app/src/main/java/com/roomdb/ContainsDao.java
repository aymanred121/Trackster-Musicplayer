package com.roomdb;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContainsDao{
    @Insert
    void insert(Contains contains);
    @Update
    void update(Contains contains);
    @Delete
    void delete(Contains contains);
    @Query("select * from Track where Track.ID in (select Track_ID from contains where contains.Playlist_Name = :playlistName)")
    LiveData<List<Track>> getPlaylistTracks(String playlistName);
    @Query("select Exists(select Track_ID from contains where Track_ID =:trackId and Playlist_Name=:playlistName)")
    Boolean isExist(int trackId,String playlistName);
    @Query("select count( Track_ID )from contains where contains.Playlist_Name = :playlistName")
    int playlistCount(String playlistName);
}
