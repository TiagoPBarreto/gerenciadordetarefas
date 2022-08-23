package com.estudo.gerenciadordetarefas;
//02
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM tasks ORDER BY UPPER(descriptionTask)ASC")
    LiveData<List<Task>> loadAllTasks();

    @Query("SELECT *FROM tasks WHERE mIsDeleted = 0 ORDER BY UPPER(descriptionTask)ASC")
    LiveData<List<Task>> loadActivityTasks();

    @Query("SELECT *FROM tasks WHERE mIsDeleted = 1 ORDER BY UPPER(descriptionTask)ASC")
    LiveData<List<Task>> loadInactiveTasks();

    @Query("SELECT * FROM tasks ORDER BY UPPER(descriptionTask) DESC")
    LiveData<List<Task>> loadAllTasksDescending();

    @Query("SELECT * FROM tasks WHERE descriptionTask LIKE :search")
    LiveData<List<Task>> findTasksSpecifiedPattern(String search);

    @Query("SELECT * FROM tasks WHERE descriptionTask MATCH :search")
    LiveData<List<Task>> findTasksMatch(String search);

    @Query("SELECT COUNT(*) FROM tasks")
    int totalTasks();

    @Query("SELECT * FROM tasks ORDER BY RANDOM() LIMIT 1")
    LiveData<List<Task>>  loadRandomTask();

    @Insert
    void insertTask(Task task);

    @Delete
    void deletTask(Task task);

    @Update
    void updateTask(Task task);

    @Query("DELETE FROM tasks")
    void deleteAllTasks();

}
