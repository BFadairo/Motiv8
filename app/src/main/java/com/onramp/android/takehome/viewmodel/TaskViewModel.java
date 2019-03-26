package com.onramp.android.takehome.viewmodel;

import android.app.Application;

import com.onramp.android.takehome.data.TaskRepository;
import com.onramp.android.takehome.model.Task;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class TaskViewModel extends AndroidViewModel {

    private final String LOG_TAG = TaskViewModel.class.getSimpleName();
    private TaskRepository repository;
    private LiveData<List<Task>> tasks;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        repository = new TaskRepository(application);
        tasks = repository.getAllTasks();
    }

    public void insert(Task task) {
        repository.insert(task);
    }

    public void update(Task task) {
        repository.update(task);
    }

    public void delete(Task task) {
        repository.delete(task);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public LiveData<List<Task>> getAllTasks() {
        return tasks;
    }
}
