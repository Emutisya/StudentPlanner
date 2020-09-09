package com.example.studentplanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

//To display tasks fromm the listview
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private List<Task> tasks;
    private Context mContext;

//    Incase we need to pass this data to another intent
    public static final String KEY_NAME = "name";
    public static final String KEY_DURATION = "duration";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_NOTES = "notes";

    public TaskAdapter(List<Task> tasks, Context context){
        this.tasks = tasks;
        this.mContext = context;
    }

    @NonNull
    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.ViewHolder holder, int position) {

        final Task currentTask = tasks.get(position);

        holder.taskNotes.setText(currentTask.getTaskNotes());
        holder.taskDuration.setText(currentTask.getTaskDuration());
        holder.taskName.setText(currentTask.getTaskName());
        holder.taskLocation.setText(currentTask.getTaskLocation());

        holder.optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(mContext, "TextView clicked", Toast.LENGTH_SHORT).show();
                PopupMenu popupMenu = new PopupMenu(mContext, holder.optionsButton);
                popupMenu.inflate(R.menu.task_options_menu);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getItemId()){
                            case R.id.edit_btn:
                                Toast.makeText(mContext, "Edit button pressed "+currentTask.getTaskName(), Toast.LENGTH_SHORT).show();
                                break;

                            case R.id.delete_btn:
                                Toast.makeText(mContext, "Delete button pressed "+currentTask.getTaskName(), Toast.LENGTH_SHORT).show();
                                break;
                                
                        }
                        return false;
                    }
                });

                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView taskName;
        public TextView taskDuration;
        public TextView taskLocation;
        public TextView taskNotes;
        public TextView optionsButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            taskName = (TextView) itemView.findViewById(R.id.task_name);
            taskDuration = (TextView) itemView.findViewById(R.id.task_duration);
            taskLocation = (TextView) itemView.findViewById(R.id.task_location);
            taskNotes = (TextView) itemView.findViewById(R.id.task_notes);
            optionsButton = (TextView) itemView.findViewById(R.id.textViewOptions);
        }
    }
}
