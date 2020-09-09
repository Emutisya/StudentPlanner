package com.example.studentplanner;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {
    private TextView textView, tvDebug, tvDate, tvDay;
    private final String url_get_timetable = "";
    private RecyclerView recyclerView;

    private TaskAdapter myAdapter;
    private List<Task> taskList;

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

        taskList = new ArrayList<Task>();
        Task newTask = new Task("IAP Class 1","STC","Group work","2 hours");
        taskList.add(newTask);
        Task newTask1 = new Task("IAP Class 2","STC","Group work","2 hours");
        taskList.add(newTask1);
        Task newTask2 = new Task("IAP Class 3","STC","Group work","2 hours");
        taskList.add(newTask2);
        Task newTask3 = new Task("IAP Class 4","STC","Group work","2 hours");
        taskList.add(newTask3);

//        Passing out data to the adapter
        myAdapter = new TaskAdapter(taskList, getActivity());
        recyclerView.setAdapter(myAdapter);
//        loadFragmentData();
        return root;
    }

    private void loadFragmentData() {
        Log.d("Recycler View", "Data presented start");
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

//        Display some data here using Volley and our adapter
//        Generating my own array here - should use API response
//            Task newTask = new Task("IAP Class","STC","Group work","2 hours");
//            taskList.add(newTask);
//            Toast.makeText(getActivity(), "TaskList:"+newTask, Toast.LENGTH_LONG).show();
//
////        Passing out data to the adapter
//        myAdapter = new TaskAdapter(taskList, getActivity());
//        recyclerView.setAdapter(myAdapter);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
        progressDialog.dismiss();
        Log.d("Recycler View", "Data presented end");

    };
}
