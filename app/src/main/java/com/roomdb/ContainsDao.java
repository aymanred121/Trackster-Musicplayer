package com.roomdb;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RewriteQueriesToDropUnusedColumns;
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
    @RewriteQueriesToDropUnusedColumns
    @Query("select * from track as t join contains as c on c.Track_ID = t.ID where c.Playlist_Name = :playlistName order by `index` asc")
    LiveData<List<Track>> getPlaylistTracks(String playlistName);
    @Query("select * from Track where Track.ID in (select Track_ID from contains where contains.Playlist_Name = :playlistName)")
    List<Track> getListPlaylistTracks(String playlistName);
    @Query("select Exists(select Track_ID from contains where Track_ID =:trackId and Playlist_Name=:playlistName)")
    Boolean isExist(int trackId,String playlistName);
    @Query("select count( Track_ID )from contains where contains.Playlist_Name = :playlistName")
    int playlistCount(String playlistName);
    @Query("select count(`index`) from contains where Playlist_Name = :playlistName")
    int getLastIndex(String playlistName);
    @Query("delete from Contains where Playlist_Name =:playlistName ")
    void deleteAll(String playlistName);
}
