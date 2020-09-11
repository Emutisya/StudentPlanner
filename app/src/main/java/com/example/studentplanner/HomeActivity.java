package com.example.studentplanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Models.ExampleDialog;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements ExampleDialog.ExampleDialogListener {
    private Fragment fragment;
    private FragmentManager fragmentManager;
    private FloatingActionButton fab;
    private static final int GALLERY_ADD_POST=2;

    private TextView startTime, endTime;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        View navHost = findViewById(R.id.fragment);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        fragmentManager = getSupportFragmentManager();
        NavigationUI.setupWithNavController(bottomNavigationView, navHostFragment.getNavController());
        init();

        sharedPreferences=this.getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);


//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                Fragment selectedFragment = null;
//                int id = item.getItemId();
//                switch (id){
//                    case R.id.dashboardFragment:
//                        fragment = new DashboardFragment();
//                        break;
//                    case R.id.timetableFragment:
//                        fragment = new TimetableFragment();
//                        break;
//                    case R.id.journalFragment:
//                        fragment = new JournalFragment();
//                        break;
//                    case R.id.eventsFragment:
//                        fragment = new EventsFragment();
//                        break;
//                }
//
//                final FragmentTransaction transaction = fragmentManager.beginTransaction();
//                transaction.replace(R.id.fragment);
//                return false;
//
//            }
//        });
    }

    @Override
    public void applyTexts(String name, String startTime, String endTime, String startDate, String description) {
//        From here we send to the network and grab it later
//        Toast.makeText(this, name+" "+startTime+" "+endTime+" "+startDate+" "+description+" ", Toast.LENGTH_SHORT).show();

//        Let's pass this to the API
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Adding task");
        dialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, Constant.ADD_TASK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                        String message = jsonObject.getString("message");
                        Toast.makeText(HomeActivity.this, message, Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY: ", error.toString());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = sharedPreferences.getString("token","");
                HashMap<String,String> map=new HashMap<>();
                map.put("Authorization","Bearer"+token);
                return map;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String>map=new HashMap<>();
                map.put("timetable_title", name);
                map.put("date", startDate);
                map.put("start_time", startTime);
                map.put("end_time", endTime);
                map.put("description", description);
                map.put("category_id", "1");
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(HomeActivity.this);
        queue.add(request);
    }

    private void init() {
       fab=findViewById(R.id.fab);
        fab.setOnClickListener(v->{
            Intent i = new Intent(Intent.ACTION_PICK);
            i.setType("image/*");
           startActivityForResult(i,GALLERY_ADD_POST);
       });
   }

   @Override
   protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
       super.onActivityResult(requestCode, resultCode, data);
      if(requestCode==GALLERY_ADD_POST && resultCode==RESULT_OK){
          Uri imgUri = data.getData();
           Intent i = new Intent(HomeActivity.this,AddJournalActivity.class);
           i.setData(imgUri);
           startActivity(i);
       }
   }
}
