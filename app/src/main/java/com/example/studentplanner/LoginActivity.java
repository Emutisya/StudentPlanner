package com.example.studentplanner;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
EditText etEmail, etPassword;
TextView tvRegister;
Button btnLogin;

    private final String url_login_user = "http://73fee4bf4cfb.ngrok.io/api/login";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etEmail= (EditText) findViewById(R.id.et_email);
        etPassword=(EditText) findViewById(R.id.et_password);
        btnLogin=(Button) findViewById(R.id.btn_login);
        tvRegister=(TextView) findViewById(R.id.tv_register);


        tvRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
               loginUser();
            }
        });

    }
    private void loginUser(){
       // StringRequest request = new StringRequest(Request.Method.POST, Constant.LOGIN,)

        //Login with Volleyz!
        //Add object to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Show progress dialog while data loads
        ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("email", etEmail.getText());
            jsonBody.put("password", etPassword.getText()); //Password encrypted on API side
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Volley JSON request
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url_login_user, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    //Registration works well
                    progressDialog.dismiss();
                    Log.d("Response", response.getString("success"));
//                    Redirect user to dashboard page
                    Intent i =new Intent( LoginActivity.this, Dashboard1Activity.class);
                    startActivity(i);

                    Toast.makeText(LoginActivity.this,"You have logged in successfully "+response.getString("message"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, error -> {

        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();
                params.put("Content-type", "application/json");
                return params;
            }

        };

        //Make the request
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
}
