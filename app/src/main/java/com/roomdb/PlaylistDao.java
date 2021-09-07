package com.roomdb;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PlaylistDao{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Playlist playlist);
    @Update
    void update(Playlist playlist);
    @Delete
    void delete(Playlist playlist);
    @Query("select * from Playlist where Name <> \"favourites\" ")
    LiveData<List<Playlist>> getallPlaylists();
    @Query("select Exists(select Name from playlist where Name =:name)")
    Boolean isPlaylistExist(String name);
}
