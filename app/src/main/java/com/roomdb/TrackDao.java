package com.roomdb;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TrackDao{
    @Insert
    void insert(Track track);
    @Update
    void update(Track track);
    @Delete
    void delete(Track track);
    @Query("select * from Track")
    LiveData<List<Track>> getallTracks();
    @Query("select * from Track where name= :trackName")
    Track getTrackByName(String trackName);
    @Query("select * from Track where ID= :trackID")
    Track getTrackByID(int trackID);
    @Query("select EXISTS(select * from Track where ID= :trackID)")
    Boolean isTrackExist(int trackID );
    @Query("update track set lyrics = :lyrics where ID = :ID")
    void updateLyrics(String lyrics,int ID);


}
