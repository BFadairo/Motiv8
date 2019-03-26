package com.onramp.android.takehome.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.onramp.android.takehome.R;
import com.onramp.android.takehome.model.Task;
import com.onramp.android.takehome.view.Fragments.TaskFragment;
import com.onramp.android.takehome.viewmodel.TaskViewModel;

import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements TaskFragment.OnListFragmentInteractionListener{

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    //@BindView(R.id.navigation)
    //BottomNavigationView navigation;
    private SectionsPageAdapter mSectionsPagerAdapter;
    @BindView(R.id.container)
    ViewPager mViewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    private TaskViewModel taskViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        setupViewPager();
    }


    private void setupViewPager(){
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Log.v(LOG_TAG, "Options ID: " + id);

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_settings:
                Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
            case R.id.action_delete_tasks:
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListFragmentInteraction(Task item) {
        Bundle viewTaskExtras = new Bundle();
        Intent viewTaskActivity = new Intent(MainActivity.this, AddTaskActivity.class);
        viewTaskExtras.putParcelable("TASK", item);
        viewTaskActivity.putExtra("BUNDLE", viewTaskExtras);
        startActivity(viewTaskActivity);
        Log.v(LOG_TAG, item.getTitle());
        Log.v(LOG_TAG, "Item ID: " + item.getId());
    }
}
