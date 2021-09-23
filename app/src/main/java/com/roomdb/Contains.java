package com.roomdb;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;


import org.jetbrains.annotations.NotNull;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = {
        @ForeignKey(entity = Playlist.class,
        parentColumns = "Name",
        childColumns = "Playlist_Name",
        onDelete = CASCADE,
        onUpdate = CASCADE),
        @ForeignKey(entity = Track.class,
                parentColumns = "ID",
                childColumns = "Track_ID",
                onDelete = CASCADE,
                onUpdate = CASCADE)
},
primaryKeys ={"Playlist_Name","Track_ID"}
)
public class Contains {
    @NotNull
    private String Playlist_Name;
    @ColumnInfo(index = true)
    private int Track_ID;
    private int index;
    @Ignore
    public Contains(String Playlist_Name, int Track_ID) {
        this.Playlist_Name = Playlist_Name;
        this.Track_ID = Track_ID;
    }
    public Contains(String Playlist_Name, int Track_ID,int index) {
        this.Playlist_Name = Playlist_Name;
        this.Track_ID = Track_ID;
        this.index = index;
    }

    public String getPlaylist_Name() {
        return Playlist_Name;
    }

    public int getTrack_ID() {
        return Track_ID;
    }

    public int getIndex() {
        return index;
    }
}

