package com.onramp.android.takehome.data.database;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import android.content.Context;

import com.onramp.android.takehome.model.Task;

@Database(entities = {Task.class}, version = 2, exportSchema = false)
public abstract class TaskDatabase extends RoomDatabase {

    private static TaskDatabase INSTANCE;

    private final static String DATABASE_NAME = "TaskDatabase.db";

    public static TaskDatabase getAppDatabase(Context context){
        if (INSTANCE == null){
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), TaskDatabase.class, DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
        }
        return INSTANCE;
    }

    /*static final Migration MIGRATION_1_2 = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE tasks"
                            + " ADD COLUMN taskTime VARCHAR(255) ");
        }
    };*/


    public static void destroyInstance(){
        INSTANCE = null;
    }

    public abstract TaskDao taskDao();
}
