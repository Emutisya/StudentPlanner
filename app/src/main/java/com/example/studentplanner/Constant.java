package com.example.studentplanner;
//class to store URLs
public class Constant {

    public static final String URL ="http://7e04c6d9cf2f.ngrok.io/";
    public static final String HOME =URL+"api";
    public static final String LOGIN =HOME+"/login";
    public static final String REGISTER =HOME+"/register";
    public static final String SAVE_USER_INFO =HOME+"/save_user_info";
    public static final String JOURNAL = HOME+"/journal";
    public static final String ADD_JOURNAL = JOURNAL+"/entry";
    public static final String UPDATE_JOURNAL = JOURNAL+"/update";
    public static final String DELETE_JOURNAL = JOURNAL+"/delete";
    public static final String LIKE_JOURNAL = JOURNAL+"/like";
    public static final String COMMENTS = JOURNAL+"/comment";

    public static final String TASK = HOME+"/timetable";
    public static final String ADD_TASK = TASK+"/create";
    public static final String GET_TASKS = HOME+"/timetables";

}
