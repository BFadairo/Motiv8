package com.onramp.android.takehome.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.onramp.android.takehome.R;
import com.onramp.android.takehome.model.Task;
import com.onramp.android.takehome.viewmodel.TaskViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class AddTaskActivity extends AppCompatActivity {

    @BindView(R.id.enter_task_title)
    EditText mTaskTitle;
    @BindView(R.id.task_description)
    EditText mTaskDescription;
    @BindView(R.id.priority_group)
    RadioGroup priorityRadioGroup;
    @BindViews({R.id.low_priority_radio, R.id.medium_priority_radio, R.id.high_priority_radio})
    List<RadioButton> radioButtons;
    @BindView(R.id.task_time_picker)
    TimePicker taskTimePicker;
    private final String TASK_BUNDLE = "task_bundle";
    private final String TASK_LABEL = "task";
    private final String LOG_TAG = AddTaskActivity.class.getSimpleName();
    private RadioButton priorityRadioButton;
    private TaskViewModel viewModel;
    private Task currentTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        ButterKnife.bind(this);
        setupOnClickRadioListener();
        //Grab the Intent that started the activity
        Intent intent = getIntent();
        if (intent.getExtras() != null){
            //If the bundle is not null
            //That means the user wants to Edit/Delete this task
            Bundle bundle = intent.getExtras().getBundle(TASK_BUNDLE);
            if (bundle != null) {
                setTitle("Edit Task");
                //Retrieve the Task from the Bundle and populate the TextFields
                currentTask = bundle.getParcelable(TASK_LABEL);
                Log.v(LOG_TAG, mTaskTitle.toString());
                mTaskTitle.setText(currentTask.getTitle());
                mTaskDescription.setText(currentTask.getDescription());
                setTimePickerHands();
                Log.v(LOG_TAG, currentTask.getTime());
                setPriorityStatusOnUpdate();
            }
        }

        viewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
    }

    private void setupOnClickRadioListener(){
        priorityRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                priorityRadioButton = findViewById(i);
                Log.v(LOG_TAG, priorityRadioButton.getText().toString());
            }
        });
    }

    private void addTask(){
        String title = mTaskTitle.getText().toString();
        String description = mTaskDescription.getText().toString();
        //Check if the title or description fields are empty
        checkIfTitleOrDescriptionIsEmpty(title, description);
        String priority = "";
        if (priorityRadioButton != null){
            priority = priorityRadioButton.getText().toString();
        } else {
            Toast.makeText(this, "A priority level must be chosen", Toast.LENGTH_SHORT).show();
            return;
        }
        Task task = new Task(title, description, priority, getTimeFromPicker());
        viewModel.insert(task);
        Toast.makeText(this, getResources().getString(R.string.toast_added_task), Toast.LENGTH_SHORT).show();
        finish();
    }

    private void updateTask(){
        String title = mTaskTitle.getText().toString();
        String description = mTaskDescription.getText().toString();
        //Checks if the title and description fields are empty
        checkIfTitleOrDescriptionIsEmpty(title, description);
        Task task = new Task(title, description, priorityRadioButton.getText().toString(), getTimeFromPicker());
        task.setId(currentTask.getId());
        Toast.makeText(this, getResources().getString(R.string.toast_updated_task), Toast.LENGTH_SHORT).show();
        finish();
        viewModel.update(task);
    }

    private void deleteTask(){
        Toast.makeText(this, getResources().getString(R.string.toast_deleted_task), Toast.LENGTH_SHORT).show();
        viewModel.delete(currentTask);
    }

    private void checkIfTitleOrDescriptionIsEmpty(String title, String description){
        //Check if the title or description fields are empty
        //If they are set error on their respective EditText views
        if (TextUtils.isEmpty(title)){
            mTaskTitle.setError("A title is required");
        }

        if (TextUtils.isEmpty(description)){
            mTaskDescription.setError("A description is required");
        }
    }

    private String getTimeFromPicker(){
        int hour = taskTimePicker.getHour();
        int mins = taskTimePicker.getMinute();
        String formattedTime = String.valueOf(hour) + ":" + String.valueOf(mins);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("H:mm");
        Date date;
        try {
            date = simpleDateFormat.parse(formattedTime);
            formattedTime = new SimpleDateFormat("h:mm a").format(date);
        } catch (ParseException e){
            e.printStackTrace();
        }
        return formattedTime;
    }

    private void setTimePickerHands(){
        String retrievedTime = currentTask.getTime();
        Log.v(LOG_TAG, "Retrieved Time: " + retrievedTime);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
        Date date;
        try {
            //Sure there's a better way to do this
            date = simpleDateFormat.parse(retrievedTime);
            retrievedTime = new SimpleDateFormat("HH:mm").format(date);
            String[] hourAndMinutes = retrievedTime.split(":");
            taskTimePicker.setHour(Integer.parseInt(hourAndMinutes[0]));
            taskTimePicker.setMinute(Integer.parseInt(hourAndMinutes[1]));
        } catch (ParseException e){
            e.printStackTrace();
        }
    }

    private void setPriorityStatusOnUpdate(){
        String currentPriority = currentTask.getPriority();

        switch (currentPriority){
            case "Low":
                radioButtons.get(0).setChecked(true);
                break;
            case "Medium":
                radioButtons.get(1).setChecked(true);
                break;
            case "High":
                radioButtons.get(2).setChecked(true);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_menu, menu);
        if (currentTask == null) {
            menu.findItem(R.id.action_delete).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_save:
                if (currentTask == null) {
                    addTask();
                } else {
                    updateTask();
                }
                return true;
            case R.id.action_delete:
                if (currentTask != null) {
                    deleteTask();
                    finish();
                }
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }

}
