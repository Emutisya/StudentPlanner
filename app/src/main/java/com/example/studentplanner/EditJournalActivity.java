package com.example.studentplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Models.journal;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditJournalActivity extends AppCompatActivity {

    private int position=0, id=0;
    private EditText txtAbout,txtFeelings,txtTag,txtDate;
    private Button btnSave;
    private ProgressDialog dialog;
    private SharedPreferences sharedPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_journal);
        init();
    }

    private void init() {

        sharedPreferences=getApplication().getSharedPreferences("name", Context.MODE_PRIVATE);

        txtAbout=findViewById(R.id.txtAboutEditJournal);
        txtFeelings=findViewById(R.id.txtFeelingsEditJournal);
        txtTag=findViewById(R.id.txtTagEditJournal);
        btnSave = findViewById(R.id.btnEditJournal);
        txtDate=findViewById(R.id.txtDateEditJournal);
        dialog=new ProgressDialog(this);
        dialog.setCancelable(false);
        position = getIntent().getIntExtra("position",0);
        id = getIntent().getIntExtra("journalId",0);

        txtAbout.setText(getIntent().getStringExtra("text"));
        //txtTag.setText(getIntent().getStringExtra("Ftext"));
      //  txtFeelings.setText(getIntent().getStringExtra("text"));
      //  txtDate.setText(getIntent().getStringExtra("text"));

btnSave.setOnClickListener(v->{
        if (txtAbout.getText().toString().isEmpty()) {


            Toast.makeText(this, "Please tell Us more about your day", Toast.LENGTH_SHORT).show();
        }
        else if (txtFeelings.getText().toString().isEmpty()) {


            Toast.makeText(this, "Tell us how you felt..", Toast.LENGTH_SHORT).show();
        }
        else if (txtDate.getText().toString().isEmpty()) {


            Toast.makeText(this, "When did this happen....", Toast.LENGTH_SHORT).show();
        }
        else if (txtTag.getText().toString().isEmpty()) {


            Toast.makeText(this, "write a tag for more engagement", Toast.LENGTH_SHORT).show();
        }
        else {

        saveJournal();


        }
    });
}

    private void saveJournal() {

        dialog.setMessage("Saving...");
        dialog.show();
        StringRequest request =new StringRequest(Request.Method.POST,Constant.UPDATE_JOURNAL,response -> {

            try{
                JSONObject object =new JSONObject(response);
                if(object.getBoolean("success")){
                    //UPDATE JOURNAL IN RECYCLER VIEW
                    journal journal = JournalFragment.arrayList.get(position);
                    journal.setAbout(txtAbout.getText().toString());
                    journal.setFeelings(txtFeelings.getText().toString());
                    journal.setTag(txtTag.getText().toString());
                    journal.setDate(txtDate.getText().toString());

                    JournalFragment.arrayList.set(position,journal);
                    JournalFragment.recyclerView.getAdapter().notifyItemChanged(position);
                    JournalFragment.recyclerView.getAdapter().notifyDataSetChanged();
                    Toast.makeText(this,"Journal Entry Edited",Toast.LENGTH_SHORT).show();
                    finish();
                }

            }catch (JSONException e){
                e.printStackTrace();
            }

        },error->{

        }){

            //ADD TOKEN TO HEADER
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError{
                String token= sharedPreferences.getString("token","");
                HashMap<String,String>map=new HashMap<>();
                map.put("Authorization","Bearer"+token);
                return map;
            }

            @Override
            protected Map<String, String>getParams() throws AuthFailureError{
                HashMap<String,String>map=new HashMap<>();
                map.put("id",id+"");
                map.put("about",txtAbout.getText().toString());
                map.put("tag",txtTag.getText().toString());
                map.put("feelings",txtFeelings.getText().toString());
                map.put("date",txtDate.getText().toString());

                return map;

            }
        };
        RequestQueue queue = Volley.newRequestQueue(EditJournalActivity.this);
        queue.add(request);
    }


    public void cancelEdit(View view){
        super.onBackPressed();
    }
}