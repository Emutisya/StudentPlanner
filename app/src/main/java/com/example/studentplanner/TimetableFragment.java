package com.example.studentplanner;

import android.graphics.RectF;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Adapters.DayAdapter;
import com.example.Models.Day;
import com.example.Models.ExampleDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TimetableFragment extends Fragment implements ExampleDialog.ExampleDialogListener {
    private Button addBtn;

    private static final int TYPE_DAY_VIEW = 1;
    private static final int TYPE_THREE_DAY_VIEW = 2;
    private static final int TYPE_WEEK_VIEW = 3;
    private int mWeekViewType = TYPE_THREE_DAY_VIEW;

    private TextView startTime1;
    private TextView endTime1;

    private RecyclerView recyclerView;

    private List<Day> days;
    private DayAdapter dayAdapter;

    public TimetableFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_timetable, container, false);
        addBtn = root.findViewById(R.id.add_btn);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

//        Recyclerview section
        recyclerView = (RecyclerView) root.findViewById(R.id.timetable_recyclerview);
        recyclerView.setHasFixedSize(true);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        days = new ArrayList<Day>();
        Day Monday = new Day("Monday");
        days.add(Monday);
        Day Tuesday = new Day("Tuesday");
        days.add(Tuesday);
        Day Wednesday = new Day("Wednesday");
        days.add(Wednesday);
        Day Thursday = new Day("Thursday");
        days.add(Thursday);
        Day Friday = new Day("Friday");
        days.add(Friday);
        Day Saturday = new Day("Saturday");
        days.add(Saturday);

        dayAdapter = new DayAdapter(days, getActivity());
        recyclerView.setAdapter(dayAdapter);

        return root;
    }

    private void openDialog() {
        ExampleDialog exampleDialog = new ExampleDialog();
        Log.i("msg3", "Opening fragment");
        exampleDialog.show(getActivity().getSupportFragmentManager(), "Example Dialog");
    }

    public void applyTexts(String name, String startTime, String endTime, String startDate, String priority) {
        startTime1.setText(startTime);
        endTime1.setText(endTime);
    }

}
