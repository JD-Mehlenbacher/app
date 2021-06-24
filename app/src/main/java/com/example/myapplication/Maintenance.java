package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Maintenance extends AppCompatActivity {

    private CheckBox row1Ok;
    private CheckBox row1NotOk;


    private Button nextQuestion;
    private Button takePicture;

    private TextView checkBoxError;
    private TextView FieldText;
    private TextView title;

    private EditText comments;

    private Switch workOrderSwitch;


    private ArrayList<String> fields;

    private ImageView test;


    private int id;
    private int questionNumber=0;
    private int wordOrderInputVar;

    private Bitmap image;
    private byte[] byteImage;
    private String encodedImage;
    private Bitmap decodebitmap;


    Connection connect;
    String ConnectionResult = "";

    static final int REQUEST_IMAGE_CAPTURE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_maintenance);


        fields = new ArrayList<>();


        FieldText=(TextView) findViewById(R.id.firstRowTextBuilding);
        title = (TextView) findViewById(R.id.inspectionNameMaintenance);
        title.setText(errorStateHelper.currentInspection);

        workOrderSwitch = (Switch) findViewById(R.id.wordOrderSwitchBuildingInspection);

        test = (ImageView) findViewById(R.id.testImage);



        row1Ok=(CheckBox) findViewById(R.id.firstOkBuilding);
        row1NotOk=(CheckBox) findViewById(R.id.firstNotOkBuilding);

        comments=(EditText) findViewById(R.id.commentBuilding);
        getQuestions();
        getInspectionID();

        takePicture = (Button) findViewById(R.id.takePictureMaint);
        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();

            }
        });




        nextQuestion = (Button) findViewById(R.id.BuildingNextMaint);
        nextQuestion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(questionNumber<=fields.size()-1) {
                    checkError();

                }
                else{
                    if(!row1Ok.isChecked() && !row1NotOk.isChecked()){
                        checkBoxError.setText("One Box Must be Checked");
                        checkBoxError.setVisibility(View.VISIBLE);
                    }
                    else if(row1Ok.isChecked() && row1NotOk.isChecked()){
                        checkBoxError.setText("Cannot Check Both Boxes");
                        checkBoxError.setVisibility(View.VISIBLE);
                    }
                    else{
                        if(workOrderSwitch.isChecked()){
                            wordOrderInputVar=1;
                        }
                        else{
                            wordOrderInputVar=0;
                        }
                        if(row1Ok.isChecked()) {
                            submitAnswers(1);
                        }
                        else{
                            submitAnswers(0);
                        }

                    }
                    Intent intent= new Intent(Maintenance.this, com.example.myapplication.homePage.class);
                    startActivity(intent);

                }

            }
        });

        FieldText.setText(fields.get(0));
        checkBoxError=(TextView) findViewById(R.id.checkBoxErrorBuilding);
        if(errorStateHelper.checkBuildingError){
            checkBoxError.setVisibility(View.VISIBLE);
        }
        else{
            checkBoxError.setVisibility(View.INVISIBLE);
        }


        displayQuestion();
    }
    public void checkError(){
        //System.out.println("CHECKING ERRORS");
        if(!row1Ok.isChecked() && !row1NotOk.isChecked()){
            checkBoxError.setText("One Box Must be Checked");
            checkBoxError.setVisibility(View.VISIBLE);
        }
        else if(row1Ok.isChecked() && row1NotOk.isChecked()){
            checkBoxError.setText("Cannot Check Both Boxes");
            checkBoxError.setVisibility(View.VISIBLE);
        }
        else{
            if(workOrderSwitch.isChecked()){
                wordOrderInputVar=1;
            }
            else{
                wordOrderInputVar=0;
            }
            if(row1Ok.isChecked()) {
                submitAnswers(1);
                displayQuestion();
            }
            else{
                submitAnswers(0);
                displayQuestion();
            }

        }
    }
    public void displayQuestion(){
        FieldText.setText(fields.get(questionNumber));
        questionNumber++;
        row1Ok.setChecked(false);
        row1NotOk.setChecked(false);
        workOrderSwitch.setChecked(false);
        comments.setText("");
        checkBoxError.setVisibility(View.INVISIBLE);
    }

    public void getInspectionID() {
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionClass();
            if (connect != null) {
                Statement st = connect.createStatement();

                ResultSet rs = st.executeQuery("SELECT MAX(Inspection_ID) FROM BuildingInspectionAnswers");

                if (rs.next()) {
                    id = rs.getInt(1) + 1;
                    System.out.println("ID IS: " + id);
                }
                st.close();
            }
            else {
                ConnectionResult = "Check Connection";
            }
            connect.close();
        } catch (Exception ex) {
            System.out.println("Get id error");
            id = 1;
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            System.out.println("PIC ERROR");

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            image = (Bitmap) extras.get("data");
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            test.setImageBitmap(image);
            image.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
            byteImage = byteArrayOutputStream.toByteArray();
            encodedImage = android.util.Base64.encodeToString(byteImage, android.util.Base64.DEFAULT);


            //decode to display
            byte[] decodeString = Base64.decode(encodedImage, Base64.DEFAULT);
            Bitmap decodebitmap = BitmapFactory.decodeByteArray(
                    decodeString, 0, decodeString.length
            );
            test.setImageBitmap(decodebitmap);
        }
    }

    public void getQuestions() {
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionClass();
            if (connect != null) {
                Statement st = connect.createStatement();

                ResultSet rs = st.executeQuery("SELECT * FROM BuildingInspectionQuestions WHERE Inspection_Title = \'"
                        + errorStateHelper.currentInspection + "\' and Building_Name = \'" + errorStateHelper.currentBuilding + "\'");

                while (rs.next()) {
                    fields.add(rs.getString("Question"));
                }
                st.close();
            }
            else {
                ConnectionResult = "Check Connection";
            }
            connect.close();
        } catch (Exception ex) {
            System.out.println("Get questions error");
        }
    }

    public void submitAnswers(int answer) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionClass();
            if (connect != null) {
                Statement st = connect.createStatement();

                st.executeUpdate("INSERT INTO BuildingInspectionAnswers VALUES (" + id + ", \'"
                        + currentUser.userName + "\', \'" + formatter.format(date) + "\', "
                        + questionNumber + ", " + answer + ", \'" + comments.getText().toString()
                        + "\', " + wordOrderInputVar + ", \'" + encodedImage + "\', \'"
                        + errorStateHelper.currentBuilding + "\', \'" + errorStateHelper.currentInspection + "\')");

                encodedImage = null;

                st.close();

            }
            else {
                ConnectionResult = "Check Connection";
            }
            connect.close();
        } catch (Exception ex) {
            System.out.println("submit answer error");
        }
    }


}
