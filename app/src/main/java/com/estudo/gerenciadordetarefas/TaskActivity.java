package com.estudo.gerenciadordetarefas;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.SearchView;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class TaskActivity extends AppCompatActivity {

    public static final int ADD_TASK = 1;
    public static final int EDIT_TASK = 2;

    public String TEXT_SEARCH_RESULT = "texto de pesquisa";

    private FloatingActionButton mFabAddNewTask;

    private TaskViewModel mTaskViewModel;
    private TaskAdapter mTaskAdapter;

    private long mInsertionDate;

    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            mInsertionDate = mTaskAdapter.getTaskAt(position).getInsertionDate();
            Intent intent = new Intent(TaskActivity.this, TaskAddEditActivity.class);
            intent.putExtra("EXTRA_ID", mTaskAdapter.getTaskAt(position).getId());
            intent.putExtra("EXTRA_DESCRIPTION", mTaskAdapter.getTaskAt(position).getDescription());
            intent.putExtra("EXTRA_NOTE", mTaskAdapter.getTaskAt(position).getNote());
            intent.putExtra("EXTRA_ESTIMATED_DATE", mTaskAdapter.getTaskAt(position).getEstimatedDate());
            intent.putExtra("EXTRA_PRIORITY_LEVEL", mTaskAdapter.getTaskAt(position).getPriorityLevel());
            intent.putExtra("EXTRA_TASK_FINISHED", mTaskAdapter.getTaskAt(position).getIsFinished());
            startActivityForResult(intent, EDIT_TASK);
        }
    };

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_tasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        mTaskAdapter = new TaskAdapter(this);
        recyclerView.setAdapter(mTaskAdapter);

        mTaskViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(TaskViewModel.class);

        mTaskViewModel.getFullTasks().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                mTaskAdapter.setTaskList(tasks);

            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                mTaskViewModel.delete(mTaskAdapter.getTaskAt(viewHolder.getAdapterPosition()));

            }
        }).attachToRecyclerView(recyclerView);
        mTaskAdapter.setOnItemClickListener(onItemClickListener);

    }

    private void addNewTask() {
        Intent intent = new Intent(TaskActivity.this, TaskAddEditActivity.class);
        startActivityForResult(intent, ADD_TASK);
    }

    public class ClickAddNewTask implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            addNewTask();
        }
    }

    @Override
    protected void onCreate(Bundle saveInstancedState) {
        super.onCreate(saveInstancedState);
        setContentView(R.layout.activity_tasks);

        setupRecyclerView();

        mFabAddNewTask = findViewById(R.id.fab_add_new_task);
        mFabAddNewTask.setOnClickListener(new ClickAddNewTask());
    }

    public boolean executeMyTransaction(Intent data, int code) {

        int id = data.getIntExtra("EXTRA_ID", -1);
        if (id == -1 && code == EDIT_TASK) {
            return false;
        }

        String description = data.getStringExtra("EXTRA_DESCRIPTION");
        String note = data.getStringExtra("EXTRA_NOTE");

        long estimatedDate = data.getLongExtra("EXTRA_ESTIMATED_DATE", 0);

        float priority = data.getFloatExtra("EXTRA_PRIORITY_LEVEL", 0.0f);

        int finished = data.getIntExtra("EXTRA_TASK_FINISHED", 0);

        Task task = new Task(description, note, priority, estimatedDate, mInsertionDate, System.currentTimeMillis(), finished, 0);

        if (code == EDIT_TASK) {

            task.setId(id);
            mTaskViewModel.update(task);

        } else {

            task.setInsertionDate(System.currentTimeMillis());
            mTaskViewModel.insert(task);

        }
        return true;

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_TASK && resultCode == RESULT_OK) {

            if (executeMyTransaction(data, ADD_TASK)) {

                Toast.makeText(this, "save_success", Toast.LENGTH_SHORT).show();

            } else {

                Toast.makeText(this, "save_fail", Toast.LENGTH_SHORT).show();

            }

        } else if (requestCode == EDIT_TASK && resultCode == RESULT_OK) {

            if (executeMyTransaction(data, EDIT_TASK)) {

                Toast.makeText(this, "update_success", Toast.LENGTH_SHORT).show();

            } else {

                Toast.makeText(this, "update_fail", Toast.LENGTH_SHORT).show();
            }


        } else {

            if (resultCode != RESULT_CANCELED) {

                Toast.makeText(this, "failure_has_occurred", Toast.LENGTH_SHORT).show();

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        final SearchView searchView = (SearchView) menu.findItem(R.id.action_main_menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                TEXT_SEARCH_RESULT = query;
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mTaskAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_main_menu_add:
                addNewTask();
                return true;
            case R.id.action_main_menu_delete_all:
                mTaskViewModel.deleteAll();
                Toast.makeText(this, "Todas as tarefas excluidas", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
