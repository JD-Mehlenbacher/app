package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class createInspection extends AppCompatActivity {
    private EditText inspectionName;
    private EditText questionInput;
    private String inspectionNameInputVar;
    private String questionInputVar;

    private Button addQuestion;
    private Button submitInspection;
    private Button backButtonCreateInspection;
    private Spinner chooseBuilding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_create_inspection);

        inspectionName=(EditText) findViewById(R.id.inspectionTitleCreateInspection);
        inspectionName=(EditText) findViewById(R.id.questionInputCreateInspection);

        backButtonCreateInspection = (Button) findViewById(R.id.CreateInspectionBack);
        backButtonCreateInspection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(createInspection.this, com.example.myapplication.homePage.class);
                startActivity(intent);
            }
        });
        addQuestion = (Button) findViewById(R.id.addQuestionCreateInspection);
        addQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        submitInspection = (Button) findViewById(R.id.submitQuestionsCreateInspection);
        submitInspection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



    }
}