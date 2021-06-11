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

public class EditProfile extends AppCompatActivity {
    private Button backButton;
    private Button submitButton;
    private EditText nameInput;
    private EditText emailInput;
    private EditText phoneInput;
    private EditText usernameInput;
    private EditText passwordInput;
    private EditText passwordConfirmInput;
    private Spinner addressInput;
    private EditText securityQuestionInput;
    private EditText securityAnswerInput;
    private TextView wrongPassword;
    private TextView duplicateField;
    private TextView emptyField;

    private String nameInputVar;
    private String emailInputVar;
    private String phoneInputVar;
    private String usernameInputVar;
    private String passwordInputVar;
    private String passwordConfirmInputVar;
    private String addressInputVar;
    private String securityQuestionInputVar;
    private String securityAnswerInputVar;

    List<String> buildings= new ArrayList<String>();


    Connection connect;
    String ConnectionResult = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_edit_profile);

        buildings.add("SELECT");
        buildings.add("Blackburn");
        buildings.add("Armstrong");
        buildings.add("Creekside");
        
        nameInput=(EditText) findViewById(R.id.nameInputChangeLogin);
        emailInput=(EditText) findViewById(R.id.emailInputChangeLogin);
        phoneInput=(EditText) findViewById(R.id.phoneInputChangeLogin);
        usernameInput=(EditText) findViewById(R.id.usernameInputChangeLogin);
        passwordInput=(EditText) findViewById(R.id.passwordInputChangeLogin);
        passwordConfirmInput=(EditText) findViewById(R.id.confirmPasswordInputChangeLogin);
        addressInput=(Spinner) findViewById(R.id.addressChangeLogin);
        securityQuestionInput = (EditText) findViewById(R.id.securityQuestionEditProfile);
        securityAnswerInput = (EditText) findViewById(R.id.securityAnswerEditProfile);

        wrongPassword=(TextView) findViewById(R.id.passwordsDontMatchChangeLogin);
        duplicateField=(TextView) findViewById(R.id.duplicateFieldChangeLogin);
        emptyField=(TextView) findViewById(R.id.emptyFieldsChangeLogin);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditProfile.this, android.R.layout.simple_spinner_item,buildings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addressInput.setAdapter(adapter);
        addressInput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                addressInputVar=buildings.get(position);
                errorStateHelper.selectedAdressChangeLogin=true;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        backButton=(Button) findViewById(R.id.backButtonChangeLoginPage);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goToSettingsPage();
            }
        });
        submitButton=(Button) findViewById(R.id.SubmitButtonChangeLogin);
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkErrors();

            }
        });

        getData();
        duplicateField.setText(errorStateHelper.duplicateMessageChangeLogin);
        if(errorStateHelper.blankFieldsChangeLogin) {
            emptyField.setVisibility(View.VISIBLE);
        }
        else {
            emptyField.setVisibility(View.INVISIBLE);
        }
        if (errorStateHelper.emailExistsChangeLogin || errorStateHelper.phoneExistsChangeLogin
                || errorStateHelper.usernameExistsChangeLogin) {
            duplicateField.setVisibility(View.VISIBLE);
        }
        else {
            duplicateField.setVisibility(View.INVISIBLE);
        }

        if (errorStateHelper.passwordErrorChangeLogin) {
            wrongPassword.setVisibility(View.VISIBLE);
        } else {
            wrongPassword.setVisibility(View.INVISIBLE);
        }
    }
    public void checkErrors(){
        nameInputVar=nameInput.getText().toString();
        emailInputVar=emailInput.getText().toString();
        phoneInputVar=phoneInput.getText().toString();
        usernameInputVar=usernameInput.getText().toString();
        passwordInputVar=passwordInput.getText().toString();
        passwordConfirmInputVar=passwordConfirmInput.getText().toString();
        securityQuestionInputVar = securityQuestionInput.getText().toString();
        securityAnswerInputVar = securityAnswerInput.getText().toString();
        checkPasswordMatch();
        checkDuplicates();
        blankFields();
        if(errorStateHelper.passwordErrorChangeLogin || errorStateHelper.usernameExistsChangeLogin
                || errorStateHelper.emailExistsChangeLogin || errorStateHelper.phoneExistsChangeLogin
                || errorStateHelper.blankFieldsChangeLogin){
            // dont register error

            Intent intent2 = new Intent(this, com.example.myapplication.EditProfile.class);
            startActivity(intent2);
        }
        else{//no errors, user registered

            submitChanges();
        }
    }
    public void goToSettingsPage(){
        errorStateHelper.reset();
        Intent intent= new Intent(this, com.example.myapplication.homePage.class);
        startActivity(intent);
    }
    public void submitChanges(){

        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionClass();
            if (connect != null) {
                Statement st = connect.createStatement();

                st.executeUpdate("DELETE FROM TenantData WHERE Email = \'" + currentUser.email + "\'");

                currentUser.email = emailInputVar;
                currentUser.userName = usernameInputVar;

                st.executeUpdate("INSERT INTO TenantData VALUES (" + "\'" + nameInputVar + "\', \'"
                        + usernameInputVar + "\', \'" + emailInputVar + "\', \'" + phoneInputVar + "\', \'"
                        + addressInputVar + "\', \'" + passwordInputVar + "\', \'" + securityQuestionInputVar + "\', \'" + securityAnswerInputVar + "\')");

                st.close();

                errorStateHelper.reset();
                Intent intent= new Intent(this, com.example.myapplication.homePage.class);
                startActivity(intent);
            }
            else {
                ConnectionResult = "Check Connection";
            }
            connect.close();
        } catch (Exception ex) {
            System.out.println("ERROR");
        }

    }
    public void getData() {
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionClass();
            if (connect != null) {
                Statement st = connect.createStatement();

                ResultSet rs = st.executeQuery("SELECT * FROM TenantData WHERE Email = \'" + currentUser.email + "\'");

                while (rs.next()) {
                    nameInput.setText(rs.getString("Name"));
                    usernameInput.setText(rs.getString("Username"));
                    emailInput.setText(rs.getString("Email"));
                    phoneInput.setText(rs.getString("Phone_Number"));
                    securityQuestionInput.setText(rs.getString("Security_Question"));
                    securityAnswerInput.setText(rs.getString("Security_Answer"));
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
    public void checkPasswordMatch() {

        if (!(passwordInputVar.equals(passwordConfirmInputVar))) {
            errorStateHelper.passwordErrorChangeLogin = true;
        } else {
            errorStateHelper.passwordErrorChangeLogin = false;
            wrongPassword.setVisibility(View.INVISIBLE);
        }
    }
    public void blankFields() {
        if (nameInputVar.equals("") || emailInputVar.equals("") || phoneInputVar.equals("")
                || usernameInputVar.equals("") || errorStateHelper.selectedAddress==false
                || passwordInputVar.equals("") || passwordConfirmInputVar.equals("")
                || securityQuestionInputVar.equals("") || securityAnswerInputVar.equals(""))  {
            errorStateHelper.blankFieldsChangeLogin = true;
        }
        else{
            errorStateHelper.blankFieldsChangeLogin = false;
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
                    if (rs.getString("Username").equals(usernameInputVar)&&!(currentUser.userName.equals(rs.getString("Username")))) {
                        errorStateHelper.usernameExistsChangeLogin = true;
                        duplicateMessage += "Username already taken.\n";
                        duplicateField.setVisibility(View.VISIBLE);
                        //ERROR STUFF
                    }
                    if (rs.getString("Email").equals(emailInputVar)&&!(currentUser.email.equals(rs.getString("Email")))) {
                        errorStateHelper.emailExistsChangeLogin = true;
                        duplicateMessage += "Email already in use.\n";
                        duplicateField.setVisibility(View.VISIBLE);
                        //ERROR STUFF
                    }

                }

                errorStateHelper.duplicateMessageChangeLogin = duplicateMessage;
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