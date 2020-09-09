package com.example.Models;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.studentplanner.R;

public class ExampleDialog extends AppCompatDialogFragment {
    private EditText startTime, endTime, name, startDate, endDate, priority;
    private ExampleDialogListener listener;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

        name = view.findViewById(R.id.et_name);
        startTime = view.findViewById(R.id.et_starttime);
        endTime = view.findViewById(R.id.et_endtime);
        startDate = view.findViewById(R.id.et_startdate);
        endDate = view.findViewById(R.id.et_enddate);
        priority = view.findViewById(R.id.et_priority);

        builder.setView(view)
                .setTitle("Set Time")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mstartTime = startTime.getText().toString();
                        String mendTime = endTime.getText().toString();
                        String mname = name.getText().toString();
                        String mendDate = endDate.getText().toString();
                        String mstartDate = startDate.getText().toString();
                        String mpriority = priority.getText().toString();

                        listener.applyTexts(mname,mstartTime, mendTime,mstartDate,mendDate,mpriority);
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (ExampleDialogListener) context;
        }
        catch(ClassCastException e){
            throw new ClassCastException(context.toString() + " Must implement example dialog listener");
        }
    }

    public interface ExampleDialogListener{
        void applyTexts(String name, String startTime, String endTime, String startDate, String endDate, String priority);
    }
}
