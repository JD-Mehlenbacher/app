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
    private TextView passwordsDontMatch;
    private TextView emailDoesNotExist;

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
        confirmButton=(Button) findViewById(R.id.confirmButtonSecurityPage);
        confirmButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
               checkAnswer();
                System.out.println("1");
            }
        });
        changePasswordButton=(Button) findViewById(R.id.confirmButtonSecurityPage);
        changePasswordButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
               checkPassword();
                System.out.println("2");
            }
        });
        confirmEmailButton=(Button) findViewById(R.id.confirmButtonSecurityPage);
        confirmEmailButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                confirmEmail();
                System.out.println("1");
            }
        });



        passwordInput=(EditText) findViewById(R.id.inputSecurity);
        confirmPasswordInput=(EditText) findViewById(R.id.input2Security);
        emailInput=(EditText) findViewById(R.id.inputSecurity);

        passwordsDontMatch=(TextView) findViewById(R.id.errorMessageSecurity);
        incorrectAnswer=(TextView) findViewById(R.id.errorMessageSecurity);
        securityQuestion=(TextView) findViewById(R.id.securityQuestionTextView);
        emailDoesNotExist=(TextView) findViewById(R.id.errorMessageSecurity);

        //Stage 1
        securityQuestion.setVisibility(View.INVISIBLE);
        emailDoesNotExist.setText("Email Does Not Exist");
        emailDoesNotExist.setVisibility(View.INVISIBLE);


    }
    public void stage2(){
        securityQuestion.setVisibility(View.VISIBLE);
        securityQuestion.setText(currentQuestion);
        securityAnswer.setHint("Input Security Answer");
        confirmEmailButton.setVisibility(View.INVISIBLE);
        confirmButton.setVisibility(View.VISIBLE);
        confirmButton.setText("Confirm Answer");
    }
    public void stage3(){
        securityQuestion.setVisibility(View.INVISIBLE);
        securityQuestion.setVisibility(View.INVISIBLE);
        securityAnswer.setHint("Input New Password");
        confirmButton.setVisibility(View.INVISIBLE);
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

                    ResultSet rs = st.executeQuery("SELECT * FROM TenantData");

                    while (rs.next()) {
                        if (rs.getString("Email").equals(emailInputVar)) {

                            
                            break;
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
            Intent intent= new Intent(this, com.example.myapplication.homePage.class);
            startActivity(intent);
        }
        else{
            //handle error
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
                        errorStateHelper.emailDoesntExistSecurity = false;
                        stage2();
                        break;
                    }
                    else{
                        errorStateHelper.emailDoesntExistSecurity=true;

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
    public void checkAnswer(){
        answerInputVar=securityAnswer.getText().toString();
        if(answerInputVar.equals(currentAnswer)){
            errorStateHelper.incorrectAnswerErrorSecurity=false;
            stage3();
        }
        else{
            errorStateHelper.incorrectAnswerErrorSecurity=true;
        }
    }
    public void returnLogin(){
        errorStateHelper.reset();
        Intent intent= new Intent(this, com.example.myapplication.Login.class);
        startActivity(intent);
    }


}