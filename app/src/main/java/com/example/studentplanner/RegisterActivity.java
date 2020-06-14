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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    TextView tvLoginBack, tvResponse;
    Button btnRegister;
    EditText name,email,password,campusName;
    private ProgressDialog pDialog;

    private static final String url_add_new_user = "http://05779cf5ec50.ngrok.io/api/register";

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

        tvResponse = findViewById(R.id.tv_response);


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

                //Call the register method
                try {
                    registerUser();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private void registerUser() throws JSONException {
        //Add object to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Show progress dialog while data loads
        ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("name", name.getText());
            jsonBody.put("email", email.getText());
            jsonBody.put("password", password.getText()); //Password encrypted on API side
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Volley JSON request
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url_add_new_user, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    //Registration works well
                    progressDialog.dismiss();
                    Log.d("Response", response.getString("success"));
                    tvResponse.setText(response.getString("message"));
//                    Redirect user to login page
                    Intent i =new Intent( RegisterActivity.this, LoginActivity.class);
                    startActivity(i);

                    Toast.makeText(RegisterActivity.this,"Response received: "+response.getString("message"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, error -> {

        }) {
                public Map<String, String> getHeaders() throws AuthFailureError{
                    final Map<String, String> params = new  HashMap<>();
                    params.put("Content-type", "application/json");
                    return params;
                }

        };

        //Make the request
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

//    class CreateNewUser extends AsyncTask<String, String, String> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            //Show a progress dialog until thread finishes running
//            pDialog = new ProgressDialog(RegisterActivity.this);
//
//            pDialog.setMessage("Registering...");
//
//            pDialog.setIndeterminate(false);
//
//            pDialog.setCancelable(false);
//
//            pDialog.show();
//        }
//
//        @Override
//        protected String doInBackground(String... strings) {
//            String mName = name.getText().toString();
//            String mEmail = email.getText().toString();
//            String mPassword = password.getText().toString();
//
//            //Building params
//            HashMap<String, String> params = new HashMap<>();
//            params.put("name", mName);
//            params.put("email", mEmail);
//            params.put("password", mPassword);
//
//            //Calling the JSONParser object
//            String response = new JSONParser().makeHttpRequest(url_add_new_user, "POST", params);
//
//            Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_SHORT).show();
//
//            Log.d("Create response", response);
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//
//            //End the progress dialog
//            pDialog.dismiss();
//        }
//    }
}

