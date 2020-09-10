package com.example.studentplanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Adapters.CommentsAdapter;
import com.example.Models.Comment;
import com.example.Models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommentActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Comment> list;
    private CommentsAdapter adapter;
    private int journalId=0;
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        init();
    }

    private void init() {

        preferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);

        recyclerView = findViewById(R.id.recyclerComments);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        journalId=getIntent().getIntExtra("journalId",0);

        getComments();


    }

    private void getComments() {

        list=new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.POST,Constant.COMMENTS,res->{

            try {
                JSONObject object = new JSONObject(res);
                if(object.getBoolean("success")){
                    JSONArray comments = new JSONArray(object.getString("comment"));
                    for (int i =0; i<comments.length();i++){
                        JSONObject comment = comments.getJSONObject(i);
                        JSONObject user = comment.getJSONObject("user");

                        User mUser= new User();
                        mUser.setId(user.getInt("id"));
                        mUser.setPhoto(Constant.URL+"storage/profiles/"+user.getString("photo"));
                        mUser.setUserName(user.getString("name"));



                        Comment mComment = new Comment();
                        mComment.setId(comment.getInt("id"));
                        mComment.setUser(mUser);
                        mComment.setDate(comment.getString("created_at"));
                        mComment.getComment(comment.getString("comment"));
                        list.add(mComment);




                    }
                }

                adapter= new CommentsAdapter(this,list);
                recyclerView.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        },error->{

            error.printStackTrace();

        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = preferences.getString("token","");
                HashMap<String,String>map = new HashMap<>();
                map.put("Authorization","Bearer"+token);
                return map;

            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String>map = new HashMap<>();
                map.put("id",journalId+"");
                return map;
            }
        };


        RequestQueue queue = Volley.newRequestQueue(CommentActivity.this);
        queue.add(request);

    }

    public void goBack(View view) {
        super.onBackPressed();
    }
}