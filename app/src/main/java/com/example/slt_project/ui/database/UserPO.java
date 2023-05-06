package com.example.slt_project.ui.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class UserPO {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "username")
    private String username;

    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "signtext")
    private String signtext;

    @ColumnInfo(name = "videopath")
    private String videopath;

    public String getSigntext() {
        return signtext;
    }
    public void setSigntext(String signtext) {
        this.signtext = signtext;
    }

    public String getVideopath() {
        return videopath;
    }
    public void setVideopath(String videopath) {
        this.videopath = videopath;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
