package com.example.studentplanner;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Adapters.DayAdapter;
import com.example.Models.Day;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DayTasksActivity extends AppCompatActivity {
    private TextView date;
    private TextView title;

    private RecyclerView recyclerView;
    private ProgressDialog dialog;

    private TaskAdapter myAdapter;
    private List<Task> taskList;

    private SharedPreferences sharedPreferences;

    private String currentDay;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.day_tasks);

        date = findViewById(R.id.tv_showDate);
        title = findViewById(R.id.tv_dayTitle);

        sharedPreferences=this.getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);

        Intent mIntent = getIntent();
        currentDay = mIntent.getStringExtra(DayAdapter.KEY_DAY);
        title.setText(currentDay);
        Toast.makeText(this, currentDay, Toast.LENGTH_SHORT).show();

//        Showing the date
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

//        Setting recyclerView
        recyclerView = (RecyclerView) findViewById(R.id.day_tasks_recycler);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

//        Load tasks for the given day
//        taskList = new ArrayList<Task>();
//        Task newTask = new Task("Math III","10:00","11:00","Class", "2020-08-08");
//        taskList.add(newTask);
//        Task newTask1 = new Task("Material Science","12:00","14:00","Class", "2020-08-09");
//        taskList.add(newTask1);
//        Task newTask2 = new Task("CAT - Logic","14:00","15:00","CAT", "2020-08-10");
//        taskList.add(newTask2);
//        Task newTask3 = new Task("Training","19:00","20:00","Meeting", "2020-08-11");
//        taskList.add(newTask3);
//
////        Passing out data to the adapter
//        myAdapter = new TaskAdapter(taskList, this);
//        recyclerView.setAdapter(myAdapter);
        getTasks(currentDay);
    }

    private void getTasks(String daySelected) {
        Toast.makeText(DayTasksActivity.this, "Loading fragment data", Toast.LENGTH_SHORT).show();
        ProgressDialog progressDialog = new ProgressDialog(DayTasksActivity.this);
        progressDialog.setMessage("Loading tasks");
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.GET, Constant.GET_TASKS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Toast.makeText(DayTasksActivity.this, "Tasks Loaded!", Toast.LENGTH_SHORT).show();
                try {
                    List<Task> taskLists = new ArrayList<Task>();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray timetables = new JSONArray(jsonObject.getString("timetables"));

                    for (int i=0; i< timetables.length(); i++){
                        Task task1 = new Task();
                        JSONObject timetables1 = (JSONObject) timetables.get(i);


                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date startDate = dateFormat.parse(timetables1.getString("date"));
                        Calendar c = Calendar.getInstance();
                        c.setTime(startDate);
                        int WeekOfYear = c.get(Calendar.WEEK_OF_YEAR);
                        String WeekString = String.valueOf(WeekOfYear);

                        Date d1 = Calendar.getInstance().getTime();
                        Calendar c1 = Calendar.getInstance();
                        c1.setTime(d1);
                        int CurrentWeek = c1.get(Calendar.WEEK_OF_YEAR);
                        String CurrentWeekString = String.valueOf(CurrentWeek);

//                        date.setText(WeekString+" "+CurrentWeekString);

//                        Shows tasks only if it is for the Current Week
                        if (CurrentWeekString.equals(WeekString)){
//                            Checking if task date == corresponding day
                            Calendar c2 = Calendar.getInstance();
                            c2.set(Calendar.WEEK_OF_YEAR, CurrentWeek);
                            int day = c2.get(Calendar.DAY_OF_WEEK);
                            switch (daySelected) {
                                case "Sunday":
                                    day = Calendar.SUNDAY;
                                    break;
                                case "Monday":
                                    day = Calendar.MONDAY;
                                    break;
                                case "Tuesday":
                                    day = Calendar.TUESDAY;
                                    break;
                                case "Wednesday":
                                    day = Calendar.WEDNESDAY;
                                    break;
                                case "Thursday":
                                    day = Calendar.THURSDAY;
                                    break;
                                case "Friday":
                                    day = Calendar.FRIDAY;
                                    break;
                                case "Saturday":
                                    day = Calendar.SATURDAY;
                                    break;
                            }
                            c2.set(Calendar.DAY_OF_WEEK, day);
                            DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
                            Date date2 = c2.getTime();
                            String daySelectedDate = (String) android.text.format.DateFormat.format("yyyy-MM-dd", date2);
                            Date startDate1 = dateFormat1.parse(timetables1.getString("date"));
                            String taskDate = (String) android.text.format.DateFormat.format("yyyy-MM-dd", startDate1);
                            date.setText(daySelectedDate);

                            if (taskDate.equals(daySelectedDate)){
                                task1.setTaskName(timetables1.getString("timetable_title"));
                                task1.setTaskStartTime(timetables1.getString("start_time"));
                                task1.setTaskEndTime(timetables1.getString("end_time"));
                                task1.setTaskDescription(timetables1.getString("description"));

//                        Formatting the date
//                            DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
//                            Date startDate1 = dateFormat1.parse(timetables1.getString("date"));
                                String days = (String) android.text.format.DateFormat.format("EEEE", startDate1);
                                task1.setTaskDate(days);

                                taskLists.add(task1);
                            }else{
//                                Show no tasks today photo
                            }
                        };
                    }

                    myAdapter = new TaskAdapter(taskLists, DayTasksActivity.this);
                    recyclerView.setAdapter(myAdapter);
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = sharedPreferences.getString("token","");
                HashMap<String,String> map=new HashMap<>();
                map.put("Authorization","Bearer"+token);
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(DayTasksActivity.this);
        queue.add(request);
    }
}
