package com.roomdb;

import androidx.room.Entity;
import androidx.room.ForeignKey;


import org.jetbrains.annotations.NotNull;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = {
        @ForeignKey(entity = Playlist.class,
        parentColumns = "Name",
        childColumns = "Playlist_Name",
        onDelete = CASCADE),
        @ForeignKey(entity = Track.class,
                parentColumns = "ID",
                childColumns = "Track_ID",
                onDelete = CASCADE)
},
primaryKeys ={"Playlist_Name","Track_ID"}
)
public class Contains {
    @NotNull
    private String Playlist_Name;
    private int Track_ID;

    public Contains(String Playlist_Name, int Track_ID) {
        this.Playlist_Name = Playlist_Name;
        this.Track_ID = Track_ID;
    }

    public String getPlaylist_Name() {
        return Playlist_Name;
    }

    public int getTrack_ID() {
        return Track_ID;
    }

}