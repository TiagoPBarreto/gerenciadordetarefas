package com.estudo.gerenciadordetarefas;
//04


import android.app.Application;
import android.os.AsyncTask;
import android.telephony.TelephonyCallback;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TaskRepository {

    private DbRoomDatabase mDbRoomDatabase;
    private TaskDao mTaskDao;
    private LiveData<List<Task>>mAllTasks;
    private LiveData<List<Task>> mActiveTasks;

    public TaskRepository(Application application){
        mDbRoomDatabase = DbRoomDatabase.getDatabase(application);
        mTaskDao = mDbRoomDatabase.mTaskDao();
        mAllTasks = mTaskDao.loadAllTasks();
        mActiveTasks = mTaskDao.loadActivityTasks();

    }

    private static class InsertAsyncTask extends AsyncTask<Task,Void,Void>{
        private TaskDao mAsyncTaskDao;
        InsertAsyncTask(TaskDao dao){mAsyncTaskDao = dao;}

        @Override
        protected Void doInBackground(final Task...params){
            mAsyncTaskDao.insertTask(params[0]);
            return null;
        }

    }

    private static class UpdateAsyncTask extends AsyncTask<Task,Void,Void>{
        private TaskDao mAsyncTaskDao;
        UpdateAsyncTask(TaskDao dao){mAsyncTaskDao = dao;}

        @Override
        protected Void doInBackground(final Task...params){
            mAsyncTaskDao.updateTask(params[0]);
            return null;
        }

    }

    private static class DeletAsyncTask extends AsyncTask<Task,Void,Void>{
        private TaskDao mAsyncTaskDao;
        DeletAsyncTask(TaskDao dao){mAsyncTaskDao = dao;}

        @Override
        protected Void doInBackground(final Task...params){
            mAsyncTaskDao.deletTask(params[0]);
            return null;
        }
    }

    private static class DeleteAllAsyncTask extends AsyncTask<Void,Void,Void>{
        private TaskDao mAsyncTaskDao;
        DeleteAllAsyncTask(TaskDao dao){mAsyncTaskDao = dao;}

        @Override
        protected Void doInBackground(Void...voids){
            mAsyncTaskDao.deleteAllTasks();
            return null;
        }
    }

    public  void insert(Task task){
        new InsertAsyncTask(mTaskDao).execute(task);
    }
    public void update(Task task){
        new UpdateAsyncTask(mTaskDao).execute(task);
    }
    public void delete(Task task){
        new DeletAsyncTask(mTaskDao).execute(task);
    }
    public void deleteAll(){
        new DeleteAllAsyncTask(mTaskDao).execute();
    }

    LiveData<List<Task>> getAllTasks(){return mAllTasks;}

    LiveData<List<Task>> getActiveTasks(){return mActiveTasks;}

}
