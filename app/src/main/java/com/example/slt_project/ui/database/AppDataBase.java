package com.example.slt_project.ui.database;

import android.content.Context;
import android.os.Environment;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {UserPO.class}, version = 1, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {
    public abstract UserDao userDao();
    private static final String DATABASE_NAME = "/mydatabase.db";
    // 单例模式
    private static volatile AppDataBase INSTANCE;

    public static AppDataBase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDataBase.class) {
                if (INSTANCE == null) {
//                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
//                                    AppDataBase.class, Environment.getExternalStorageDirectory() + "/DCIM/Data/" + DATABASE_NAME)
//                            .build();
//                }
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDataBase.class, context.getFilesDir() + DATABASE_NAME)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

