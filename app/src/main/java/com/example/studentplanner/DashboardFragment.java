package com.example.studentplanner;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
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

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {
    private TextView textView, tvDebug;
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

        recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));

        taskList = new ArrayList<>();
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadFragmentData();
        return root;
    }

    private void loadFragmentData() {
        Log.d("Recycler View", "Data presented start");
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

//        Display some data here using Volley and our adapter
//        Generating my own array here - should use API response
            Task newTask = new Task("IAP Class","STC","Group work","2 hours");
            taskList.add(newTask);
            tvDebug.setText(newTask.toString());
            Toast.makeText(getActivity(), "TaskList:"+newTask, Toast.LENGTH_LONG).show();

//        Passing out data to the adapter
        myAdapter = new TaskAdapter(taskList, getActivity());
        recyclerView.setAdapter(myAdapter);
        progressDialog.dismiss();
        Log.d("Recycler View", "Data presented end");

    };
}
