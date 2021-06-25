package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class DeleteInspection extends AppCompatActivity {
    private Spinner buildingDropdown;
    private Spinner typeDropdown;

    private Button deleteButton;
    private Button confirmDeleteButton;

    private String buildingInputVar;
    private String typeInputVar;

    Connection connect;
    String ConnectionResult = "";

    private ArrayList<String> buildings;
    private ArrayList<String> types;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_delete_inspection);

        buildings = new ArrayList<>();
        types = new ArrayList<>();

        deleteButton=(Button) findViewById(R.id.deleteInspectionButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                confirmDeleteButton.setVisibility(View.VISIBLE);

            }
        });
        confirmDeleteButton=(Button) findViewById(R.id.confirmInspectionDelete);
        confirmDeleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                deleteInspection();
                Intent intent= new Intent(DeleteInspection.this, com.example.myapplication.homePage.class);
                startActivity(intent);

            }
        });
        confirmDeleteButton.setVisibility(View.INVISIBLE);
        getBuildings();
        buildingInputVar = buildings.get(0);


        buildingDropdown=(Spinner) findViewById(R.id.buildingDropDownDelete);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(DeleteInspection.this, android.R.layout.simple_spinner_dropdown_item, buildings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        buildingDropdown.setAdapter(adapter);
        buildingDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                buildingInputVar=buildings.get(position);

                getInspections();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        getInspections();

        typeDropdown=(Spinner) findViewById(R.id.typeDropDownDelete);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(DeleteInspection.this, android.R.layout.simple_spinner_dropdown_item, types);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        typeDropdown.setAdapter(adapter2);
        typeDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeInputVar=types.get(position);
                errorStateHelper.selectedTypeInspectionError=false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                errorStateHelper.selectedTypeInspectionError=true;
            }
        });
    }

    public void deleteInspection(){
        try {

            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionClass();
            if (connect != null) {
                Statement st = connect.createStatement();
                st.executeUpdate("DELETE FROM BuildingInspectionQuestions WHERE Building_Name = \'"
                        + buildingInputVar + "\' and Inspection_Title = \'" + typeInputVar + "\'");

            } else {
                ConnectionResult = "Check Connection";
            }
            connect.close();
        } catch (Exception ex) {
            System.out.println("Error deleting inspection.");
        }
    }

    public void getBuildings() {
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionClass();
            if (connect != null) {
                Statement st = connect.createStatement();

                ResultSet rs = st.executeQuery("SELECT * FROM BuildingInfo");

                while (rs.next()) {
                    //System.out.println("ADDING A BUILDING!: " + rs.getString("Building_Name"));
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

    public void getInspections() {
        types.clear();
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionClass();
            if (connect != null) {
                Statement st = connect.createStatement();

                ResultSet rs = st.executeQuery("SELECT * FROM BuildingInspectionQuestions WHERE Question_Number = 1 and Building_Name = \'"
                        + buildingInputVar + "\'");

                while (rs.next()) {
                    types.add(rs.getString("Inspection_Title"));
                }
                ArrayAdapter<String> adaps = new ArrayAdapter<String>(DeleteInspection.this, android.R.layout.simple_spinner_dropdown_item, types);
                typeDropdown.setAdapter(adaps);
                st.close();
            }
            else {
                ConnectionResult = "Check Connection";
            }
            connect.close();
        } catch (Exception ex) {
            System.out.println("Get inspections error");
        }
    }

}