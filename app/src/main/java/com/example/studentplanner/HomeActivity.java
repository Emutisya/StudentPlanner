package com.example.studentplanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Models.ExampleDialog;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeActivity extends AppCompatActivity implements ExampleDialog.ExampleDialogListener {
    private Fragment fragment;
    private FragmentManager fragmentManager;
    private FloatingActionButton fab;
    private static final int GALLERY_ADD_POST=2;

    private TextView startTime, endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        View navHost = findViewById(R.id.fragment);
        NavHostFragment navHostFragment  = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        fragmentManager = getSupportFragmentManager();
        NavigationUI.setupWithNavController(bottomNavigationView, navHostFragment.getNavController());
        init();




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
    public void applyTexts(String name, String startTime, String endTime, String startDate, String endDate, String priority) {
//        From here we send to the network and grab it later
        Toast.makeText(this, name+" "+startTime+" "+endTime+" "+startDate+" "+endDate+" "+priority+" ", Toast.LENGTH_SHORT).show();
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
