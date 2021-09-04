package com.roomdb;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public  class Track  {
    @PrimaryKey(autoGenerate = true)
    private int ID;
    private String location;
    private String name;

    private String lyrics;
    private String ArtistName;
    private String AlbumName;
    private int duration;


    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] Cover;

    public Track( String location, String name, String lyrics, String ArtistName, String AlbumName,byte[] Cover,int duration) {
        this.location = location;
        this.name = name;
        this.lyrics = lyrics;
        this.ArtistName = ArtistName;
        this.AlbumName = AlbumName;
        this.Cover=Cover;
        this.duration=duration;
    }


    public void setID(int ID) {
        this.ID = ID;
    }

    public String getLyrics() {
        return lyrics;
    }

    public int getID() {
        return ID;
    }

    public String getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public String getArtistName() {
        return ArtistName;
    }

    public byte[] getCover() {
        return Cover;
    }

    public String getAlbumName() {
        return AlbumName;
    }

    public int getDuration() {
        return duration;
    }
}

