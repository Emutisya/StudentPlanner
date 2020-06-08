package com.example.studentplanner;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    TextView tvLoginBack;
    Button btnRegister;
    EditText name,email,password,campusName;

    private ProgressDialog pDialog;


    private static String url_add_new_user = "https://2dca467b3118.ngrok.io/api/register";

    private  static final String TAG_SUCCESS = "success";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        tvLoginBack =(TextView) findViewById(R.id.tv_login_back);
        btnRegister=(Button) findViewById(R.id.btn_register);

        //Getting edit text values
        name = findViewById(R.id.et_name);
        email = findViewById(R.id.et_email);
        password = findViewById(R.id.et_password);
        campusName = findViewById(R.id.et_school);


        tvLoginBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent( RegisterActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Start a background thread to register user
                new CreateNewUser().execute();
//                Intent i =new Intent( RegisterActivity.this, Dashboard1Activity.class);
//                startActivity(i);
            }
        });
    }

    class CreateNewUser extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //Show a progress dialog until thread finishes running
            pDialog = new ProgressDialog(RegisterActivity.this);

            pDialog.setMessage("Registering...");

            pDialog.setIndeterminate(false);

            pDialog.setCancelable(false);

            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String mName = name.getText().toString();
            String mEmail = email.getText().toString();
            String mPassword = password.getText().toString();

            //Building params
            HashMap<String, String> params = new HashMap<>();
            params.put("name", mName);
            params.put("email", mEmail);
            params.put("password", mPassword);

            //Calling the JSONParser object
            String response = new JSONParser().makeHttpRequest(url_add_new_user, "POST", params);

            Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_SHORT).show();

            Log.d("Create response", response);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //End the progress dialog
            pDialog.dismiss();
        }
    }
}
