package com.example.studentplanner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Adapters.DayAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class DayTasksActivity extends AppCompatActivity {
    private TextView date;
    private TextView title;

    private RecyclerView recyclerView;
    private ProgressDialog dialog;

    private TaskAdapter myAdapter;
    private List<Task> taskList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.day_tasks);

        date = findViewById(R.id.tv_showDate);
        title = findViewById(R.id.tv_dayTitle);

        Intent mIntent = getIntent();
        String currentDay = mIntent.getStringExtra(DayAdapter.KEY_DAY);
        title.setText(currentDay);
        Toast.makeText(this, currentDay, Toast.LENGTH_SHORT).show();

//        Setting recyclerView
        recyclerView = (RecyclerView) findViewById(R.id.day_tasks_recycler);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

//        Load tasks for the given day
        taskList = new ArrayList<Task>();
        Task newTask = new Task("Classwork 1", "10am", "3pm", "API Dev", "2020/25/04");
        taskList.add(newTask);
        Task newTask1 = new Task("Classwork 2", "10am", "3pm", "API Dev", "2020/25/04");
        taskList.add(newTask1);
        Task newTask2 = new Task("Classwork 3", "10am", "3pm", "API Dev", "2020/25/04");
        taskList.add(newTask2);
        Task newTask3 = new Task("Classwork 4", "10am", "3pm", "API Dev", "2020/25/04");
        taskList.add(newTask3);

//        Passing out data to the adapter
        myAdapter = new TaskAdapter(taskList, this);
        recyclerView.setAdapter(myAdapter);
    }
}
