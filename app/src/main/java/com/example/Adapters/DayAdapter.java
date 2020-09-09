package com.example.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Models.Day;
import com.example.studentplanner.DayTasksActivity;
import com.example.studentplanner.R;
import com.example.studentplanner.TaskAdapter;

import java.util.List;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.ViewHolder>{
    private List<Day> days;
    private Context mContext;

    public static final String KEY_DAY = "day";

    public DayAdapter(List<Day> days, Context context) {
        this.days = days;
        this.mContext = context;
    }

    @NonNull
    @Override
    public DayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.dayview_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DayAdapter.ViewHolder holder, int position) {
        final Day currentDay = days.get(position);

        holder.dayName.setText(currentDay.getDayName());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Day day1 = days.get(position);
                Intent mIntent  = new Intent(v.getContext(), DayTasksActivity.class);
                mIntent.putExtra(KEY_DAY, day1.getDayName());

                v.getContext().startActivity(mIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView dayName;
        public RelativeLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dayName = itemView.findViewById(R.id.title_dayview);
            layout = itemView.findViewById(R.id.layout_dayView);
        }
    }
}
