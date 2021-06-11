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
import java.sql.Statement;

public class SecurityQuestions extends AppCompatActivity {

    private Button HomePage;
    private Button confirmButton;
    private Button changePasswordButton;
    private Button confirmEmailButton;

    private EditText passwordInput;
    private EditText confirmPasswordInput;
    private EditText securityAnswer;
    private EditText emailInput;


    private TextView securityQuestion;
    private TextView incorrectAnswer;
    private TextView errorMessage;
    private TextView emailDoesNotExist;
    private TextView SecurityText;

    private String passwordInputVar;
    private String confirmPasswordInputVar;
    private String answerInputVar;
    private String emailInputVar;

    private String currentAnswer;
    private String currentQuestion;



    Connection connect;
    String ConnectionResult = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_security_questions);
        HomePage=(Button) findViewById(R.id.backButtonSecurity);
        HomePage.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                returnLogin();

            }
        });
        confirmButton=(Button) findViewById(R.id.confirmAnswerButtonSecurityPage);
        confirmButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
               checkAnswer();
                System.out.println("1");
            }
        });
        confirmButton.setVisibility(View.INVISIBLE);
        changePasswordButton=(Button) findViewById(R.id.confirmPasswordButtonSecurityPage);
        changePasswordButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
               checkPassword();
                System.out.println("2");
            }
        });
        changePasswordButton.setVisibility(View.INVISIBLE);
        confirmEmailButton=(Button) findViewById(R.id.confirmEmailButtonSecurityPage);
        confirmEmailButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                confirmEmail();
                System.out.println("3");
            }
        });
        confirmEmailButton.setVisibility(View.INVISIBLE);



        passwordInput=(EditText) findViewById(R.id.inputSecurity);
        confirmPasswordInput=(EditText) findViewById(R.id.input2Security);
        emailInput=(EditText) findViewById(R.id.inputSecurity);
        securityAnswer=(EditText) findViewById(R.id.inputSecurity);
        passwordInput.setVisibility(View.INVISIBLE);
        confirmPasswordInput.setVisibility(View.INVISIBLE);

        errorMessage=(TextView) findViewById(R.id.errorMessageSecurity);
        incorrectAnswer=(TextView) findViewById(R.id.errorMessageSecurity);
        securityQuestion=(TextView) findViewById(R.id.securityQuestionTextView);
        emailDoesNotExist=(TextView) findViewById(R.id.errorMessageSecurity);
        SecurityText=(TextView) findViewById(R.id.SecurityText);
        securityQuestion.setVisibility(View.INVISIBLE);
        switch(errorStateHelper.stageSecurity){
            case 1:
                stage1();
                break;
            case 2:
                stage2();
                break;
            case 3:
                stage3();
                break;
        }
        errorMessage.setVisibility(View.VISIBLE);
        if(errorStateHelper.incorrectAnswerSecurity){
            errorMessage.setText("Incorrect Answer");
        }

        else if(errorStateHelper.emailDoesntExistSecurity){
            errorMessage.setText("Email Does not Exist");
        }
        else if(errorStateHelper.passwordsDontMatchSecurity){
            errorMessage.setText("Passwords Dont Match");
        }
        else{
            errorMessage.setText("");
        }
    }
    public void stage1(){
        SecurityText.setText("Confirm Email");
        confirmEmailButton.setVisibility(View.VISIBLE);
        emailDoesNotExist.setText("Email Does Not Exist");
        emailInput.setVisibility(View.VISIBLE);
    }
    public void stage2(){
        currentQuestion=currentUser.question;
        SecurityText.setText("Confirm Answer");
        securityAnswer.setText("");
        securityAnswer.setVisibility(View.VISIBLE);
        securityQuestion.setVisibility(View.VISIBLE);
        securityQuestion.setText(currentQuestion);
        securityAnswer.setHint("Input Security Answer");
        confirmButton.setText("Confirm Answer");
        confirmButton.setVisibility(View.VISIBLE);
        System.out.println("stage2");
    }
    public void stage3(){
        SecurityText.setText("Confirm Password");
        passwordInput.setHint("Input New Password");
        confirmPasswordInput.setHint("Confirm New Password");
        confirmPasswordInput.setVisibility(View.VISIBLE);
        passwordInput.setVisibility(View.VISIBLE);
        System.out.println("STAGE 3");
        changePasswordButton.setVisibility(View.VISIBLE);
        changePasswordButton.setText("Confirm New Password");
    }
    public void checkPassword(){
        confirmPasswordInputVar=confirmPasswordInput.getText().toString();
        passwordInputVar=passwordInput.getText().toString();
        if(confirmPasswordInputVar.equals(passwordInputVar)){
            try {
                ConnectionHelper connectionHelper = new ConnectionHelper();
                connect = connectionHelper.connectionClass();
                if (connect != null) {
                    Statement st = connect.createStatement();
                    System.out.println("PASSWORD: " + passwordInputVar);
                    System.out.println("EMAIL: " + currentUser.email);
                    st.executeUpdate("UPDATE TenantData SET Password = \'" + passwordInputVar + "\' WHERE Email = \'" + currentUser.email + "\'");
                    st.close();
                }
                else {
                    ConnectionResult = "Check Connection";
                }
            } catch (Exception ex) {
                System.out.println("UPDATE NO WORK");
            }
            errorStateHelper.passwordsDontMatchSecurity=false;
            Intent intent= new Intent(this, com.example.myapplication.homePage.class);
            startActivity(intent);

        }
        else{
            errorStateHelper.passwordsDontMatchSecurity=true;
            Intent intent= new Intent(this, com.example.myapplication.SecurityQuestions.class);
            startActivity(intent);

        }
    }
    public void confirmEmail(){
        emailInputVar=emailInput.getText().toString();
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionClass();
            if (connect != null) {
                Statement st = connect.createStatement();

                ResultSet rs = st.executeQuery("SELECT * FROM TenantData");

                while (rs.next()) {

                    if (rs.getString("Email").equals(emailInputVar)) {
                        currentQuestion=rs.getString("Security_Question");
                        currentAnswer=rs.getString("Security_Answer");
                        currentUser.email = emailInputVar;
                        currentUser.question=currentQuestion;
                        currentUser.answer=currentAnswer;
                        errorStateHelper.emailDoesntExistSecurity = false;
                        errorStateHelper.stageSecurity=2;
                        Intent intent= new Intent(this, com.example.myapplication.SecurityQuestions.class);
                        startActivity(intent);
                        break;
                    }
                    else{
                        errorStateHelper.emailDoesntExistSecurity=true;
                        Intent intent= new Intent(this, com.example.myapplication.SecurityQuestions.class);
                        startActivity(intent);
                    }
                }
                st.close();
            }
            else {
                ConnectionResult = "Check Connection";
            }
            connect.close();
        } catch (Exception ex) {
            System.out.println("EMAIL NO WORK");
        }


    }

    public void checkAnswer(){
        currentAnswer=currentUser.answer;
        currentQuestion=currentUser.question;
        answerInputVar=securityAnswer.getText().toString();
        System.out.println(answerInputVar);
        System.out.println(currentAnswer);
        if(answerInputVar.equals(currentAnswer)){
            errorStateHelper.stageSecurity=3;
            errorStateHelper.incorrectAnswerSecurity=false;
            Intent intent= new Intent(this, com.example.myapplication.SecurityQuestions.class);
            startActivity(intent);

        }
        else{
            errorStateHelper.incorrectAnswerSecurity=true;
            Intent intent= new Intent(this, com.example.myapplication.SecurityQuestions.class);
            startActivity(intent);
        }
    }

    public void returnLogin(){
        errorStateHelper.stageSecurity=1;
        errorStateHelper.reset();
        Intent intent= new Intent(this, com.example.myapplication.Login.class);
        startActivity(intent);
    }


}