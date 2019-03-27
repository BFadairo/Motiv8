package com.onramp.android.takehome.data;

import android.app.Application;
import androidx.lifecycle.LiveData;
import android.os.AsyncTask;

import com.onramp.android.takehome.data.database.TaskDao;
import com.onramp.android.takehome.data.database.TaskDatabase;
import com.onramp.android.takehome.model.Task;

import java.util.List;

public class TaskRepository {

    private final String LOG_TAG = TaskRepository.class.getSimpleName();
    private TaskDao taskDao;
    private LiveData<List<Task>> tasks;
    private Task retrievedTask;

    public TaskRepository(Application application){
        TaskDatabase database = TaskDatabase.getAppDatabase(application);
        taskDao = database.taskDao();
        tasks = taskDao.getAllTasks();
    }

    public LiveData<List<Task>> getAllTasks(){
        return tasks;
    }

    public void insert(Task task){
        new InsertTaskAsyncTask(taskDao).execute(task);
    }

    public void update(Task task){
        new UpdateTaskAsyncTask(taskDao).execute(task);
    }

    public void delete(Task task){
        new DeleteTaskAsyncTask(taskDao).execute(task);
    }

    public void deleteAll(){
        new DeleteAllAsyncTask(taskDao).execute();
    }

    private static class InsertTaskAsyncTask extends AsyncTask<Task, Void, Void>{
        private TaskDao taskDao;

        private InsertTaskAsyncTask(TaskDao taskDao){
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.insertTask(tasks[0]);
            return null;
        }
    }

    private static class UpdateTaskAsyncTask extends AsyncTask<Task, Void, Void>{
        private TaskDao taskDao;

        private UpdateTaskAsyncTask(TaskDao taskDao){
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.updateTask(tasks[0]);
            return null;
        }
    }

    private static class DeleteTaskAsyncTask extends AsyncTask<Task, Void, Void>{
        private TaskDao taskDao;

        private DeleteTaskAsyncTask(TaskDao taskDao){
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.deleteTask(tasks[0]);
            return null;
        }
    }

    private static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void>{
        private TaskDao taskDao;

        private DeleteAllAsyncTask(TaskDao taskDao){
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            taskDao.deleteAllTasks();
            return null;
        }
    }
}
