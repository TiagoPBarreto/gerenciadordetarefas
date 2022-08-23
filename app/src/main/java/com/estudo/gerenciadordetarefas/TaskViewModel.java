package com.estudo.gerenciadordetarefas;
//05


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {

    private TaskRepository mTaskRepository;
    private LiveData<List<Task>>mAllTasks;
    private LiveData<List<Task>>mActiveTasks;


    public TaskViewModel(@NonNull Application application) {
        super(application);
        mTaskRepository = new TaskRepository(application);
        mAllTasks = mTaskRepository.getAllTasks();
        mActiveTasks = mTaskRepository.getActiveTasks();

    }

    LiveData<List<Task>>getFullTasks(){
        return mAllTasks;
    }
    LiveData<List<Task>>getActiveTasks(){
        return mActiveTasks;
    }

    public void insert(Task task){mTaskRepository.insert(task);}

    public void update(Task task){mTaskRepository.update(task);}

    public void delete(Task task){mTaskRepository.delete(task);}

    public void deleteAll(){mTaskRepository.deleteAll();}
}
