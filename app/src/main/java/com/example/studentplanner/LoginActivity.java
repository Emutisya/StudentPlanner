package com.example.studentplanner;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {
EditText etEmail, etPassword;
TextView tvRegister;
Button btnLogin;

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
               login();
            }
        });

    }
    private void login(){
       // StringRequest request = new StringRequest(Request.Method.POST, Constant.LOGIN,)
    }
}
