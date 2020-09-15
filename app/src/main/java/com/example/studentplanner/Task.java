package com.example.studentplanner;


import java.sql.Time;

//Task object
public class Task {
    public String id;
    private String taskName;
    private String taskDate;
    private String taskStartTime;
    private String taskEndTime;
    private String taskDescription;

    public Task(String taskName, String taskStartTime, String taskEndTime, String taskDescription, String taskDate){
        this.taskStartTime = taskStartTime;
        this.taskEndTime = taskEndTime;
        this.taskDate = taskDate;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
    }

    public Task(){
//        Empty constructor
    }

    public String getId() {
        return id;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskDate() {
        return taskDate;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public String getTaskEndTime() {
        return taskEndTime;
    }

    public String getTaskStartTime() {
        return taskStartTime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public void setTaskEndTime(String taskEndTime) {
        this.taskEndTime = taskEndTime;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setTaskStartTime(String taskStartTime) {
        this.taskStartTime = taskStartTime;
    }
}
