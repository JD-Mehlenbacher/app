package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    private TextView welcomeText;
    private Button loginButton;
    private Button registerButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        registerButton=(Button) findViewById(R.id.RegisterButtonSettingsPage);
        registerButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                openRegisterActivity();
            }
        });
        loginButton=(Button) findViewById(R.id.loginButtonMainPage);
        loginButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                openLoginActivity();
            }
        });

        welcomeText = (TextView) findViewById(R.id.welcomeTextMainPage);


    }

    public void openRegisterActivity(){
        errorStateHelper.reset();
        Intent intent= new Intent(this,Register.class);
        startActivity(intent);
    }

    public void openLoginActivity(){
        errorStateHelper.reset();
        Intent intent= new Intent(this,Login.class);
        startActivity(intent);
    }


}