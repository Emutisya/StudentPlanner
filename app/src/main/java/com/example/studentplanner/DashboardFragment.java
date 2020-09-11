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
//        Task newTask = new Task("IAP Class 1","STC","Group work","2 hours");
//        taskList.add(newTask);
//        Task newTask1 = new Task("IAP Class 2","STC","Group work","2 hours");
//        taskList.add(newTask1);
//        Task newTask2 = new Task("IAP Class 3","STC","Group work","2 hours");
//        taskList.add(newTask2);
//        Task newTask3 = new Task("IAP Class 4","STC","Group work","2 hours");
//        taskList.add(newTask3);

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
                Toast.makeText(getContext(), "Tasks loaded", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("timetables"));
                    tv_testing.setText(jsonArray.toString());

                    for (int i=0; i < jsonArray.length(); i++){
                        JSONObject task = jsonArray.getJSONObject(i);
                        Task tasknew = new Task();

                        tasknew.setTaskName(task.getString("timetable_name"));
                        tasknew.setTaskDate(task.getString("date"));
                        tasknew.setTaskStartTime(task.getString("start_time"));
                        tasknew.setTaskEndTime(task.getString("end_time"));
                        tasknew.setTaskDescription(task.getString("description"));

                        taskList.add(tasknew);
                    }

//                    myAdapter = new TaskAdapter(taskList, getContext());
//                    recyclerView.setAdapter(myAdapter);

                } catch (JSONException e) {
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
