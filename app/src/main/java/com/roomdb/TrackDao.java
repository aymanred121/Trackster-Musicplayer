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
public interface TrackDao{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Track track);
    @Update
    void update(Track track);
    @Delete
    void delete(Track track);
    @Query("select * from Track order by track.AlbumName")
    LiveData<List<Track>> getallTracks();
    @Query("select * from Track where name= :arg0")
    Track getTrackByName(String arg0);
    @Query("select * from Track where ID= :trackID")
    Track getTrackByID(int trackID);
    @Query("select EXISTS(select * from Track where ID= :trackID)")
    Boolean isTrackExist(int trackID );
    @Query("update track set lyrics = :lyrics where ID = :ID")
    void updateLyrics(String lyrics,int ID);
    @Query("select lyrics from track where track.ID = :ID")
    String getLyricsByID(int ID);


}
