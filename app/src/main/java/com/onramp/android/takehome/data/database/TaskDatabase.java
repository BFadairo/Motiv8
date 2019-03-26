package com.onramp.android.takehome.data.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.onramp.android.takehome.model.Task;

@Database(entities = {Task.class}, version = 1, exportSchema = false)
public abstract class TaskDatabase extends RoomDatabase {

    private static TaskDatabase INSTANCE;

    private final static String DATABASE_NAME = "TaskDatabase.db";

    public static TaskDatabase getAppDatabase(Context context){
        if (INSTANCE == null){
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), TaskDatabase.class, DATABASE_NAME)
                            .build();
        }
        return INSTANCE;
    }


    public static void destroyInstance(){
        INSTANCE = null;
    }

    public abstract TaskDao taskDao();
}
