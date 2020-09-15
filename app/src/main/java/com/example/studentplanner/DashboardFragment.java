package com.example.studentplanner;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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
import java.util.TimeZone;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {
    private TextView textView, tvDebug, tvDate, tvDay, tv_testing;
    private final String url_get_timetable = "";
    private RecyclerView recyclerView;

    private TaskAdapter myAdapter;
    private List<Task> taskList;

    private SharedPreferences sharedPreferences;

    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_dashboard, container, false);

        tvDebug = (TextView) root.findViewById(R.id.tv_debug);
        tvDate = (TextView) root.findViewById(R.id.tv_date);
        tvDay = (TextView) root.findViewById(R.id.tv_day);
        tv_testing = root.findViewById(R.id.tv_testing);

        sharedPreferences=getContext().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);

//        Display dates
        Date date = Calendar.getInstance(TimeZone.getDefault()).getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(date);
        tvDate.setText(strDate);

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        String dayName = "Monday - default";

        switch (day) {
            case Calendar.SUNDAY:
                dayName = "Sunday";
                break;
            case Calendar.MONDAY:
                dayName = "Monday";
                break;
            case Calendar.TUESDAY:
                dayName = "Tuesday";
                break;
            case Calendar.WEDNESDAY:
                dayName = "Wednesday";
                break;
            case Calendar.THURSDAY:
                dayName = "Thursday";
                break;
            case Calendar.FRIDAY:
                dayName = "Friday";
                break;
            case Calendar.SATURDAY:
                dayName = "Saturday";
                break;
        }

        tvDay.setText(dayName);

        recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

//        taskList = new ArrayList<Task>();
//        Task newTask = new Task("Math III","10:00","11:00","Class", "2020-08-08");
//        taskList.add(newTask);
//        Task newTask1 = new Task("Material Science","12:00","14:00","Class", "2020-08-09");
//        taskList.add(newTask1);
//        Task newTask2 = new Task("CAT - Logic","14:00","15:00","CAT", "2020-08-10");
//        taskList.add(newTask2);
//        Task newTask3 = new Task("Training","19:00","20:00","Meeting", "2020-08-11");
//        taskList.add(newTask3);
//        Task newTask4 = new Task("Math III","10:00","11:00","Class", "2020-08-08");
//        taskList.add(newTask4);
////
//        myAdapter = new TaskAdapter(taskList, getContext());
//        recyclerView.setAdapter(myAdapter);

//        Passing out data to the adapter
        loadFragmentData();
        return root;
    }

    private void loadFragmentData() {
        Toast.makeText(getContext(), "Loading fragment data", Toast.LENGTH_SHORT).show();
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading tasks");
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.GET, Constant.GET_TASKS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Tasks Loaded!", Toast.LENGTH_SHORT).show();
                try {
                    List<Task> taskLists = new ArrayList<Task>();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray timetables = new JSONArray(jsonObject.getString("timetables"));
                    tv_testing.setText(timetables.toString());

                    if (timetables.length() != 0){
                        for (int i=0; i< timetables.length(); i++){
                            Task task1 = new Task();
                            JSONObject timetables1 = (JSONObject) timetables.get(i);

                            task1.setId(timetables1.getString("id"));
                            task1.setTaskName(timetables1.getString("timetable_title"));
                            task1.setTaskStartTime(timetables1.getString("start_time"));
                            task1.setTaskEndTime(timetables1.getString("end_time"));
                            task1.setTaskDescription(timetables1.getString("description"));

//                      Getting Day from Date
                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date startDate = dateFormat.parse(timetables1.getString("date"));
                            String day = (String) android.text.format.DateFormat.format("EEEE", startDate);

//                      Getting current week from date
                            Calendar c = Calendar.getInstance();
                            c.setTime(startDate);
                            int WeekOfYear = c.get(Calendar.WEEK_OF_YEAR);
                            String WeekString = String.valueOf(WeekOfYear);

                            task1.setTaskDate(day);

                            taskLists.add(task1);
                        }
                    }
                    else{
                        Task task1 = new Task();
                        task1.setTaskName("No tasks. Add from timetable page");
                        taskLists.add(task1);
                    }

                    myAdapter = new TaskAdapter(taskLists, getContext());
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

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    };
}
