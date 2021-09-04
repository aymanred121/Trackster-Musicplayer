package com.roomdb;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity
public class Playlist {
    @NotNull
    @PrimaryKey
    private String Name;

    public Playlist(String Name) {
        this.Name = Name;
    }

    public String getName() {
        return Name;
    }


}
