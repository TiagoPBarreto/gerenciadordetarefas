package com.estudo.gerenciadordetarefas;
// 7
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class TaskAddEditActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        // Texto do botão
        mButtonEstimatedDate.setText(currentDateString);

        //variavel para receber o tempo em milisegundos
        mEstimatedDate = c.getTimeInMillis();

    }

    private EditText mEditDescription,mEditNote;
    private Button mButtonSave, mButtonEstimatedDate;
    private RatingBar mRatingPriorityLevel;
    private ToggleButton mToggleFinish;

    private long mEstimatedDate;
    private  int mFinished;

    private void displayEstimatedDate(){

        String estimatedDateString = java.text.DateFormat.getTimeInstance(DateFormat.FULL).format(new Date(mEstimatedDate));
        mButtonEstimatedDate.setText(estimatedDateString);
    }

    private boolean isRequered(){

        if (TextUtils.isEmpty(mEditDescription.getText()) || TextUtils.isEmpty(mEditNote.getText())){

            return true;

        } else{

            return false;
        }
    }

    private void saveTask(){

        if (isRequered()){

            Toast.makeText(this,"Informe descrição e/ou observação",Toast.LENGTH_SHORT).show();
            return;
        }

        Intent replyIntent = new Intent();

        int id = getIntent().getIntExtra("EXTRA_ID",-1);

        if (id != -1){

            replyIntent.putExtra("EXTRA_ID",id);

        }

        float priority = mRatingPriorityLevel.getRating();
        replyIntent.putExtra("EXTRA_PRIORITY_LEVEL",priority);

        mFinished = 0;
        if (mToggleFinish.isChecked()){
            mFinished = 1;
        }
        replyIntent.putExtra("EXTRA_TASK_FINISHED",mFinished);

        String description = mEditDescription.getText().toString().trim();
        replyIntent.putExtra("EXTRA_DESCRIPTION",description);

        String note = mEditNote.getText().toString().trim();
        replyIntent.putExtra("EXTRA_NOTE",note);

        replyIntent.putExtra("EXTRA_ESTIMATED_DATE",mEstimatedDate);
        setResult(RESULT_OK,replyIntent);
        finish();
    }

    public void showCalendar(){
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "definir data");
    }

    public class ClickButtomEstimatedDate implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            showCalendar();
        }
    }

    public class ClickButtomSave implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            saveTask();
        }
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        setContentView(R.layout.activity_add_edit_task);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        mEditDescription = findViewById(R.id.editText_task_description);
        mEditNote = findViewById(R.id.editText_task_note);

        mButtonEstimatedDate = findViewById(R.id.button_estimated_date_task);
        mButtonEstimatedDate.setOnClickListener(new ClickButtomEstimatedDate());

        mRatingPriorityLevel = findViewById(R.id.rating_task_priority_level);

        mToggleFinish = findViewById(R.id.toogle_task_finish);

        mButtonSave = findViewById(R.id.button_save_task);
        mButtonSave.setOnClickListener(new ClickButtomSave());

        Intent intent = getIntent();

        if ( intent.hasExtra("EXTRA_ID") ){

            setTitle("Editar a tarefa");
            mEditDescription.setText(intent.getStringExtra("EXTRA_DESCRIPTION"));
            mEditNote.setText(intent.getStringExtra("EXTRA_NOTE"));
            mEstimatedDate = intent.getLongExtra("EXTRA_ESTIMATED_DATE",0);
            displayEstimatedDate();
            mRatingPriorityLevel.setRating(intent.getFloatExtra("EXTRA_PRIORITY_LEVEL",0.0f));
            mFinished = intent.getIntExtra("EXTRA_TASK_FINISHED",0);
            if ( mFinished == 0 ){
                mToggleFinish.setChecked(false);
            }else{
                mToggleFinish.setChecked(true);
            }

        }else{

            setTitle("Nova Tarefa");
            mEstimatedDate = System.currentTimeMillis();
            displayEstimatedDate();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.save_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected (MenuItem item){

        switch ( item.getItemId() ){
            case R.id.action_menu_save:
                saveTask();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
