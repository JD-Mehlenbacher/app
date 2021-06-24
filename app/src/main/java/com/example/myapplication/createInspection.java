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



public class createInspection extends AppCompatActivity {
    private EditText inspectionName;
    private EditText questionInput;

    private String inspectionNameInputVar;
    private String questionInputVar;
    private String buildingInputVar;

    private Button addQuestion;
    private Button submitInspection;
    private Button backButtonCreateInspection;
    private Spinner chooseBuilding;

    private TextView errorMessage;

    private int questionNumber = 1;

    Connection connect;
    String ConnectionResult = "";




    private ArrayList<String> buildings= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_create_inspection);

        inspectionName=(EditText) findViewById(R.id.inspectionTitleCreateInspection);
        questionInput=(EditText) findViewById(R.id.questionInputCreateInspection);

        inspectionNameInputVar = "";

        errorMessage=(TextView) findViewById(R.id.incorrectInputCreate);

        getBuildings();
        chooseBuilding=(Spinner) findViewById(R.id.createInspectionChooseBuilding);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(createInspection.this, android.R.layout.simple_spinner_item,buildings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chooseBuilding.setAdapter(adapter);
        chooseBuilding.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                buildingInputVar=buildings.get(position);
                errorStateHelper.selectedAddress=true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        backButtonCreateInspection = (Button) findViewById(R.id.CreateInspectionBack);
        backButtonCreateInspection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorStateHelper.reset();
                Intent intent= new Intent(createInspection.this, com.example.myapplication.homePage.class);
                startActivity(intent);
            }
        });
        addQuestion = (Button) findViewById(R.id.addQuestionCreateInspection);
        addQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionInputVar=questionInput.getText().toString();
                if(questionInputVar.equals("")){
                    errorMessage.setVisibility(View.VISIBLE);
                    errorMessage.setText("Must Fill In Question");
                    errorStateHelper.blankQuestionCreation=true;
                }
                else {
                    questionInputVar = questionInput.getText().toString();
                    submitQuestion();
                    questionInput.setText("");
                    questionNumber++;
                    errorMessage.setVisibility(View.INVISIBLE);
                    errorStateHelper.blankQuestionCreation=true;

                }
            }
        });
        submitInspection = (Button) findViewById(R.id.submitQuestionsCreateInspection);
        submitInspection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("IT IS: " + errorStateHelper.blankQuestionCreation);
                inspectionNameInputVar = inspectionName.getText().toString();
                if(!(errorStateHelper.startedCreation)&&(inspectionNameInputVar.equals(""))){
                    errorStateHelper.incorrectInputCreation=true;
                    errorMessage.setVisibility(View.VISIBLE);
                    errorMessage.setText("Must Give Inspection a Title");
                }
                else if(!errorStateHelper.startedCreation&&!inspectionNameInputVar.equals("")){
                    errorStateHelper.incorrectInputCreation=false;
                    errorMessage.setVisibility(View.INVISIBLE);
                    inspectionNameInputVar = inspectionName.getText().toString();
                    questionInput.setVisibility(View.VISIBLE);
                    submitInspection.setText("Submit Inspection");
                    addQuestion.setVisibility(View.VISIBLE);
                    inspectionName.setVisibility(View.INVISIBLE);
                    chooseBuilding.setVisibility(View.INVISIBLE);
                    errorStateHelper.startedCreation=true;
                    questionInputVar="";
                }
                else if(errorStateHelper.startedCreation && !inspectionNameInputVar.equals("") && !questionInput.getText().toString().equals("")) {
                    questionInputVar = questionInput.getText().toString();
                    submitQuestion();
                    errorStateHelper.reset();
                    Intent intent = new Intent(createInspection.this, homePage.class);
                    startActivity(intent);
                }
                else{
                    errorMessage.setVisibility(View.VISIBLE);
                    errorMessage.setText("Must Fill In Question");
                }


            }
        });
        questionInput.setVisibility(View.INVISIBLE);
        submitInspection.setText("START");
        addQuestion.setVisibility(View.INVISIBLE);


    }

    public void getBuildings() {
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionClass();
            if (connect != null) {
                Statement st = connect.createStatement();

                ResultSet rs = st.executeQuery("SELECT * FROM BuildingInfo");

                while (rs.next()) {

                    buildings.add(rs.getString("Building_Name"));
                }
                st.close();
            }
            else {
                ConnectionResult = "Check Connection";
            }
            connect.close();
        } catch (Exception ex) {
            System.out.println("Get buildings error");
        }
    }

    public void submitQuestion() {
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionClass();
            if (connect != null) {
                Statement st = connect.createStatement();
                st.executeUpdate("INSERT INTO BuildingInspectionQuestions VALUES (" + "\'" + buildingInputVar + "\', \'"
                        + inspectionNameInputVar + "\', " + questionNumber + ", \'" + questionInputVar + "\')");
            } else {
                ConnectionResult = "Check Connection";
            }
            connect.close();
        } catch (Exception ex) {
            System.out.println("Error submitting question.");
        }
    }
}