package com.onramp.android.takehome.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.onramp.android.takehome.R;
import com.onramp.android.takehome.model.Task;
import com.onramp.android.takehome.viewmodel.TaskViewModel;

public class AddTaskActivity extends AppCompatActivity {

    @BindView(R.id.enter_task_title)
    EditText mTaskTitle;
    @BindView(R.id.task_description)
    EditText mTaskDescription;
    @BindView(R.id.priority_group)
    RadioGroup priortyRadioGroup;
    @BindView(R.id.priority_color)
    View priorityColor;
    private final String LOG_TAG = AddTaskActivity.class.getSimpleName();
    private RadioButton priortyRadioButton;
    private TaskViewModel viewModel;
    private Task currentTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        ButterKnife.bind(this);
        setupOnClickRadioListener();
        Intent intent = getIntent();
        if (intent.getExtras() != null){
            Bundle bundle = intent.getExtras().getBundle("BUNDLE");
            if (bundle != null) {
                setTitle("Edit Task");
                currentTask = bundle.getParcelable("TASK");
                Log.v(LOG_TAG, mTaskTitle.toString());
                mTaskTitle.setText(currentTask.getTitle());
                mTaskDescription.setText(currentTask.getDescription());
            }
        }

        viewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_save:
                if (currentTask == null) {
                    addTask();
                    Toast.makeText(this, "Task has been added", Toast.LENGTH_SHORT).show();
                } else {
                    updateTask();
                    Toast.makeText(this, "Task has been updated", Toast.LENGTH_SHORT).show();
                }
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupOnClickRadioListener(){
        priortyRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                priortyRadioButton = findViewById(i);
                Log.v(LOG_TAG, priortyRadioButton.getText().toString());
            }
        });
    }

    private void addTask(){
        String title = mTaskTitle.getText().toString();
        String description = mTaskDescription.getText().toString();
        if (title.trim().isEmpty() || description.trim().isEmpty()){
            Toast.makeText(this, "A title and description are required", Toast.LENGTH_SHORT).show();
            return;
        }
        Task task = new Task(title, description, priortyRadioButton.getText().toString());
        viewModel.insert(task);
    }

    private void updateTask(){
        String title = mTaskTitle.getText().toString();
        String description = mTaskDescription.getText().toString();
        if (title.trim().isEmpty() || description.trim().isEmpty()){
            Toast.makeText(this, "A title and description are required", Toast.LENGTH_SHORT).show();
            return;
        }
        Task task = new Task(title, description, priortyRadioButton.getText().toString());
        task.setId(currentTask.getId());
        viewModel.update(task);
    }

}
