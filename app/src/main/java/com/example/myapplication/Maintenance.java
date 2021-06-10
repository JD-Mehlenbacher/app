package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class Maintenance extends AppCompatActivity {

    private Button HomePage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_maintenance);
        BuildingInspection buildingInspection= new BuildingInspection();
        UnitInspection unitInspection= new UnitInspection();

        HomePage = (Button) findViewById(R.id.MaintenanceBack);
        HomePage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                returnMainPageActivity();
            }
        });

        FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.maintenanceFL,buildingInspection);
        if(errorStateHelper.buildingOrUnit){

            transaction.replace(R.id.maintenanceFL,buildingInspection);
        }
        else{
            transaction.replace(R.id.maintenanceFL,unitInspection);
        }
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void returnMainPageActivity() {
        errorStateHelper.reset();
        Intent intent = new Intent(this, com.example.myapplication.homePage.class);
        startActivity(intent);
    }
}