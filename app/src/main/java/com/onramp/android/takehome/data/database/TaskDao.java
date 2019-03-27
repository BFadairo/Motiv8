package com.onramp.android.takehome.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.onramp.android.takehome.model.Task;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    void insertTask(Task task);

    @Query("SELECT * FROM tasks")
    LiveData<List<Task>> getAllTasks();

    @Query("DELETE FROM tasks")
    void deleteAllTasks();

    @Update
    void updateTask(Task... tasks);

    @Delete
    void deleteTask(Task task);
}
