package com.example.studentplanner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Models.User;
import com.example.Models.journal;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddJournalActivity extends AppCompatActivity {

    private Button btnJournal;
    private ImageView imgJournal;
    private EditText txtAbout, txtFeelings, txtTag;
    private Bitmap bitmap = null;
    private static final int GALLERY_CHANGE_POST = 3;
    private ProgressDialog dialog;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_journal);
        init();
    }

    private void init() {

        preferences=getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);

        btnJournal = findViewById(R.id.btnAddJournal);
        imgJournal = findViewById(R.id.imgAddJournal);
        txtAbout = findViewById(R.id.txtAboutAddJournal);
        txtFeelings = findViewById(R.id.txtFeelingsAddJournal);
        txtTag = findViewById(R.id.txtTagAddJournal);
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);


        imgJournal.setImageURI(getIntent().getData());


    }

    public void cancelJournal(View view) {
        super.onBackPressed();
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), getIntent().getData());
        } catch (IOException e) {
            e.printStackTrace();
        }

        btnJournal.setOnClickListener(v -> {
            if (!txtAbout.getText().toString().isEmpty()) {
                journal();
            } else {
                Toast.makeText(this, "Please tell Us more about your day", Toast.LENGTH_SHORT).show();
            }
            if (!txtFeelings.getText().toString().isEmpty()) {
                journal();
            } else {
                Toast.makeText(this, "Tell us how you felt..", Toast.LENGTH_SHORT).show();
            }
            if (!txtTag.getText().toString().isEmpty()) {
                journal();
            } else {
                Toast.makeText(this, "write a tag for more engagement", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void journal() {

        dialog.setMessage("Creating Journal Entry...");
        dialog.show();


        StringRequest request = new StringRequest(Request.Method.POST, Constant.ADD_JOURNAL,response-> {

            try {
                JSONObject object =new JSONObject(response);
                if(object.getBoolean("success")){
                    JSONObject journalObject=object.getJSONObject("journal");
                    JSONObject userObject=journalObject.getJSONObject("user");

                    User user= new User();
                    user.setId(userObject.getInt("id"));
                    user.setUserName(userObject.getString("name"));
                    user.setPhoto(userObject.getString("photo"));


                    journal journal = new journal();

                    journal.setUser(user);
                    journal.setId(journalObject.getInt("id"));
                    journal.setSelfLike(false);
                    journal.setPhoto(journalObject.getString("photo"));
                    journal.setAbout(journalObject.getString("about"));
                    journal.setFeelings(journalObject.getString("feelings"));
                    journal.setTag(journalObject.getString("tag"));
                    journal.setComments(0);
                    journal.setLikes(0);
                    journal.setDate(journalObject.getString("created_at"));


                    JournalFragment.arrayList.add(0,journal);
                    JournalFragment.recyclerView.getAdapter().notifyItemInserted(0);
                    JournalFragment.recyclerView.getAdapter().notifyDataSetChanged();
                    Toast.makeText(this,"Journal Entry Added",Toast.LENGTH_SHORT).show();
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dialog.dismiss();
        }, error -> {
error.printStackTrace();
dialog.dismiss();
        }) {
            //ADD TOKEN TO HEADER
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError{
                String token= preferences.getString("token","");
                HashMap<String,String>map = new HashMap<>();
                map.put("Authorization","Bearer"+token);
                return map;



        }
        //ADD PARAMETERS


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String>map = new HashMap<>();
                map.put("about",txtAbout.getText().toString().trim());
                map.put("feelings",txtFeelings.getText().toString().trim());
                map.put("tag",txtTag.getText().toString().trim());
                map.put("photo",bitmapToString(bitmap));
                return map;

            }
        };

        RequestQueue queue = Volley.newRequestQueue(AddJournalActivity.this);
        queue.add(request);


}

    private String bitmapToString(Bitmap bitmap) {
        if(bitmap!=null){
            ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
            byte [] array=byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(array, Base64.DEFAULT);
        }

        return "";
    }

    public void changePhoto(View view) {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i,GALLERY_CHANGE_POST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_CHANGE_POST && resultCode==RESULT_OK){
            Uri imgUri = data.getData();
            imgJournal.setImageURI(imgUri);

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imgUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}