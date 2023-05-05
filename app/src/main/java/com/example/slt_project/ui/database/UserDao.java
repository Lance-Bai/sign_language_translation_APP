package com.example.slt_project.ui.database;

import androidx.room.Dao;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserDao {
    @Insert
    void insert(UserPO userPO);

    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    UserPO findUser(String username, String password);

    @Query("SELECT id FROM users WHERE username=:username")
   //wyt add here
    UserPO getUserByUsername(String username);
    // 其他操作
    // ...
}
