package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Login extends AppCompatActivity {

    private Button HomePage;
    private Button LoginButton;
    private Button forgotButton;

    private EditText UsernameInput;
    private EditText PasswordInput;

    private TextView usernameEmailDoesntExist;
    private TextView incorrectPassword;

    private String userNameInputVar;
    private String passwordInputVar;

    Connection connect;
    String ConnectionResult = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        HomePage=(Button) findViewById(R.id.backButtonSecurity);
        HomePage.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                returnMainPageActivity();
            }
        });
        LoginButton=(Button) findViewById(R.id.confirmButtonSecurity);
        LoginButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                checkError();

            }
        });
        forgotButton=(Button) findViewById(R.id.changePasswordButtonSecurity);
        forgotButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                securityQuestions();
            }
        });
        forgotButton.setVisibility(View.INVISIBLE);
        UsernameInput=(EditText) findViewById(R.id.securityQuestionTextView);
        PasswordInput= (EditText) findViewById(R.id.SecurityQuestionAnswer);

        usernameEmailDoesntExist=(TextView) findViewById(R.id.errorMessageSecurity);
        incorrectPassword=(TextView) findViewById(R.id.passwordsDontMatchTextviewSecurity);

        if(errorStateHelper.usernameEmailErrorLogin ){
            usernameEmailDoesntExist.setVisibility(View.VISIBLE);
        }
        else{
            usernameEmailDoesntExist.setVisibility(View.INVISIBLE);
            if(errorStateHelper.incorrectPasswordLogin ){
                incorrectPassword.setVisibility(View.VISIBLE);
                forgotButton.setVisibility(View.VISIBLE);
            }
            else{
                incorrectPassword.setVisibility(View.INVISIBLE);
            }
        }

    }
    public void returnMainPageActivity(){
        errorStateHelper.reset();
        Intent intent= new Intent(this, com.example.myapplication.MainActivity.class);
        startActivity(intent);
    }
    public void Login(){
        currentUser.userName = userNameInputVar;
        currentUser.email = userNameInputVar;


        Intent intent= new Intent(this, com.example.myapplication.homePage.class);
        startActivity(intent);

    }
    public void securityQuestions(){
        errorStateHelper.reset();
        Intent intent= new Intent(this, com.example.myapplication.SecurityQuestions.class);
        startActivity(intent);
    }
    public void checkError(){
        userNameInputVar=UsernameInput.getText().toString();
        passwordInputVar=PasswordInput.getText().toString();
        checkUsernameEmail();
        checkPassword();
        if (errorStateHelper.usernameEmailErrorLogin ||  errorStateHelper.incorrectPasswordLogin) {
            Intent intent2 = new Intent(this, com.example.myapplication.Login.class);
            startActivity(intent2);
        }
        else{
            Login();
        }
    }
    public void checkUsernameEmail() {
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionClass();
            if (connect != null) {
                Statement st = connect.createStatement();

                ResultSet rs = st.executeQuery("SELECT * FROM TenantData");

                while (rs.next()) {
                    if (rs.getString("Username").equals(userNameInputVar)) {
                        errorStateHelper.usernameEmailErrorLogin = false;
                        break;
                        //ERROR STUFF
                    }
                    else{
                        if (rs.getString("Email").equals(userNameInputVar)) {
                            errorStateHelper.usernameEmailErrorLogin = false;
                            break;
                            //ERROR STUFF
                        }
                        else{
                            errorStateHelper.usernameEmailErrorLogin = true;
                        }
                    }

                }
                st.close();
            }
            else {
                ConnectionResult = "Check Connection";
            }
            connect.close();
        } catch (Exception ex) {
            System.out.println("ERROR");
        }
    }
    public void checkPassword() {
        userNameInputVar=UsernameInput.getText().toString();
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionClass();
            if (connect != null) {
                Statement st = connect.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM TenantData WHERE Username = \'" + userNameInputVar + "\'");
                if (!rs.next()) {
                    rs = st.executeQuery("SELECT * FROM TenantData WHERE Email = \'" + userNameInputVar + "\'");
                }

                while (rs.next()) {
                    if (rs.getString("Password").equals(passwordInputVar)) {
                        System.out.println("PASSWORDS MATCH");
                        System.out.println("STORED: " + rs.getString("Password"));
                        System.out.println("INPUTTED: " + passwordInputVar);
                        errorStateHelper.incorrectPasswordLogin = false;
                    }
                    else{
                        System.out.println("NO Match");
                        System.out.println("STORED: " + rs.getString("Password"));
                        System.out.println("INPUTTED: " + passwordInputVar);
                        errorStateHelper.incorrectPasswordLogin = true;
                    }
                }
                st.close();
            }
            else {
                ConnectionResult = "Check Connection";
            }
            connect.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    }