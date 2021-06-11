package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Register extends AppCompatActivity {
    private Button HomePage;
    private Button RegisterButton;

    private EditText nameInput;
    private EditText emailInput;
    private EditText phoneInput;
    private EditText userInput;
    private EditText securityQuestion;
    private EditText securityAnswer;
    private EditText passwordInput;
    private EditText confirmPasswordInput;

    private TextView wrongPassword;
    private TextView duplicateField;
    private TextView emptyField;


    private String nameInputVar;
    private String emailInputVar;
    private String phoneInputVar;
    private String userInputVar;
    private String addressInputVar;
    private String passwordInputVar;
    private String confirmPasswordInputVar;
    private String securityQuestionVar;
    private String securityAnswerVar;

    private Spinner addressDropdown;
    List <String> buildings= new ArrayList<String>();
    Connection connect;
    String ConnectionResult = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);
        HomePage = (Button) findViewById(R.id.homePageButtonRegisterPage);
        HomePage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                returnMainPageActivity();
            }
        });
        RegisterButton = (Button) findViewById(R.id.SubmitButtonChangeLogin);
        RegisterButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkError();
            }
        });
        buildings.add("SELECT");
        buildings.add("Blackburn");
        buildings.add("Armstrong");
        buildings.add("Creekside");
        nameInput = (EditText) findViewById(R.id.nameInputRegisterPage);
        emailInput = (EditText) findViewById(R.id.emailInputRegisterPage);
        phoneInput = (EditText) findViewById(R.id.phoneInputChangeLogin);
        userInput = (EditText) findViewById(R.id.usernameInputChangeLogin);
        passwordInput = (EditText) findViewById(R.id.passwordInputChangeLogin);
        confirmPasswordInput = (EditText) findViewById(R.id.confirmPasswordInputChangeLogin);
        securityQuestion = (EditText) findViewById(R.id.securityQuestionRegister);
        securityAnswer = (EditText) findViewById(R.id.securityAnswerRegister);

        wrongPassword = (TextView) findViewById(R.id.passwordsDontMatchChangeLogin);
        duplicateField = (TextView) findViewById(R.id.duplicateFieldChangeLogin);
        emptyField = (TextView) findViewById(R.id.emptyFieldsChangeLogin);

        addressDropdown=(Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(Register.this, android.R.layout.simple_spinner_item,buildings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addressDropdown.setAdapter(adapter);
        addressDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                addressInputVar=buildings.get(position);
                errorStateHelper.selectedAddress=true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        duplicateField.setText(errorStateHelper.duplicateMessageRegister);

        if(errorStateHelper.blankFieldsRegister) {
            emptyField.setVisibility(View.VISIBLE);
        }
        else {
            emptyField.setVisibility(View.INVISIBLE);
        }
        if (errorStateHelper.emailExistsRegister || errorStateHelper.phoneExistsRegister || errorStateHelper.usernameExistsRegister) {
            duplicateField.setVisibility(View.VISIBLE);
        }
        else {
            duplicateField.setVisibility(View.INVISIBLE);
        }

        if (errorStateHelper.passwordErrorRegister) {
            wrongPassword.setVisibility(View.VISIBLE);
        } else {
            wrongPassword.setVisibility(View.INVISIBLE);
        }

    }

    private void registerUser() {
        try {

            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionClass();
            if (connect != null) {
                Statement st = connect.createStatement();
                st.executeUpdate("INSERT INTO TenantData VALUES (" + "\'" + nameInputVar + "\', \'"
                        + userInputVar + "\', \'" + emailInputVar + "\', \'" + phoneInputVar + "\', \'"
                        + addressInputVar + "\', \'" + passwordInputVar + "\', \'" + securityQuestionVar + "\', \'" + securityAnswerVar + "\')");
                currentUser.email = emailInputVar;
                currentUser.userName = userInputVar;
            } else {
                ConnectionResult = "Check Connection";
            }
            connect.close();
        } catch (Exception ex) {
            System.out.println("ERROR");
        }
        Intent intent= new Intent(this, com.example.myapplication.homePage.class);
        startActivity(intent);

    }

    public void returnMainPageActivity() {
        errorStateHelper.reset();
        Intent intent = new Intent(this, com.example.myapplication.homePage.class);
        startActivity(intent);
    }
    public void checkError(){
        passwordInputVar = passwordInput.getText().toString();
        confirmPasswordInputVar = confirmPasswordInput.getText().toString();
        nameInputVar = nameInput.getText().toString();
        emailInputVar = emailInput.getText().toString();
        phoneInputVar = phoneInput.getText().toString();
        userInputVar = userInput.getText().toString();
        passwordInputVar = passwordInput.getText().toString();
        userInputVar = userInput.getText().toString();
        emailInputVar = emailInput.getText().toString();
        phoneInputVar = phoneInput.getText().toString();
        securityQuestionVar = securityQuestion.getText().toString();
        securityAnswerVar = securityAnswer.getText().toString();


        checkPasswordMatch();
        checkDuplicates();
        blankFields();
        if(errorStateHelper.passwordErrorRegister || errorStateHelper.usernameExistsRegister || errorStateHelper.emailExistsRegister || errorStateHelper.phoneExistsRegister || errorStateHelper.blankFieldsRegister){
            // dont register error

            Intent intent2 = new Intent(this, com.example.myapplication.Register.class);
            startActivity(intent2);
        }
        else{//no errors, user registered

            registerUser();
        }
    }
    public void checkPasswordMatch() {
     
        if (!(passwordInputVar.equals(confirmPasswordInputVar))) {
            errorStateHelper.passwordErrorRegister = true;
        } else {
            errorStateHelper.passwordErrorRegister = false;
            wrongPassword.setVisibility(View.INVISIBLE);
        }
    }
    public void blankFields() {
        if (nameInputVar.equals("") || emailInputVar.equals("") || phoneInputVar.equals("")
                || userInputVar.equals("") || errorStateHelper.selectedAddress==false
                || passwordInputVar.equals("") || confirmPasswordInputVar.equals("")
                || securityQuestionVar.equals("") || securityAnswerVar.equals(""))  {
            errorStateHelper.blankFieldsRegister = true;
        }
        else{
            errorStateHelper.blankFieldsRegister = false;
        }
    }
        
    public void checkDuplicates() {
        String duplicateMessage ="";
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionClass();
            if (connect != null) {
                Statement st = connect.createStatement();

                ResultSet rs = st.executeQuery("SELECT * FROM TenantData");

                while (rs.next()) {
                    if (rs.getString("Username").equals(userInputVar)) {
                        errorStateHelper.usernameExistsRegister = true;
                        duplicateMessage += "Username already taken.\n";
                        duplicateField.setVisibility(View.VISIBLE);
                        //ERROR STUFF
                    }
                    if (rs.getString("Email").equals(emailInputVar)) {
                        errorStateHelper.emailExistsRegister = true;
                        duplicateMessage += "Email already in use.\n";
                        duplicateField.setVisibility(View.VISIBLE);
                        //ERROR STUFF
                    }
                    if (rs.getString("Phone_Number").equals(phoneInputVar)) {
                        errorStateHelper.phoneExistsRegister = true;
                        duplicateMessage += "Phone number already in use.\n";
                        duplicateField.setVisibility(View.VISIBLE);
                    }
                }
                
                errorStateHelper.duplicateMessageRegister = duplicateMessage;
                //duplicateField.setText(duplicateMessage);


                st.close();
            } else {
                ConnectionResult = "Check Connection";
            }
            connect.close();
        } catch (Exception ex) {
            System.out.println("ERROR");
        }


    }   

}