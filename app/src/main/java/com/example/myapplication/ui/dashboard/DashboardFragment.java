package com.example.myapplication.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.ConnectionHelper;
import com.example.myapplication.Maintenance;
import com.example.myapplication.R;
import com.example.myapplication.Register;
import com.example.myapplication.UnitInspection;
import com.example.myapplication.createInspection;
import com.example.myapplication.databinding.FragmentDashboardBinding;
import com.example.myapplication.errorStateHelper;
import com.example.myapplication.homePage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class DashboardFragment extends Fragment {
    private Button unitInspectionButton;
    private Button buildingInspectionButton;
    private Button customInspectionButton;
    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;
    private Spinner buildingDropdown;
    private Spinner typeDropdown;

    private ArrayList<String> buildings;
    private ArrayList<String> types;

    private String buildingInputVar;
    private String typeInputVar;


    Connection connect;
    String ConnectionResult = "";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //final TextView textView = binding.textNotifications;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

                //textView.setText(s);
            }
        });

        buildings= new ArrayList<>();
        types= new ArrayList<>();

        unitInspectionButton=(Button) root.findViewById(R.id.unitDailyInspectionButton);
        unitInspectionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                unitInspection();
            }
        });
        buildingInspectionButton=(Button) root.findViewById(R.id.buildingDailyInspectionButton);
        buildingInspectionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                BuildingInspection();
            }
        });
        customInspectionButton=(Button) root.findViewById(R.id.customInspectionButton);
        customInspectionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("INSPECTION: " + typeInputVar);


                //typeDropdown.setVisibility(View.VISIBLE);

            }
        });
        getBuildings();
        buildingInputVar = buildings.get(0);


        buildingDropdown=(Spinner) root.findViewById(R.id.buildingDropDownInspection);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, buildings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        buildingDropdown.setAdapter(adapter);
        buildingDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                buildingInputVar=buildings.get(position);
                //typeDropdown.setVisibility(View.VISIBLE);
                getInspections();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        getInspections();

        typeDropdown=(Spinner) root.findViewById(R.id.typeDropDownInspection);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, types);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //typeDropdown.setVisibility(View.INVISIBLE);
        typeDropdown.setAdapter(adapter2);
        typeDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeInputVar=types.get(position);
                //adapter2.clear();
                //adapter2.addAll(types);
                errorStateHelper.selectedTypeInspectionError=false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                errorStateHelper.selectedTypeInspectionError=true;
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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
                ArrayAdapter<String> adaps = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, types);
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

    public void customInspection(){
        errorStateHelper.buildingOrUnit=false;
        Intent intent = new Intent(getActivity().getApplicationContext(), createInspection.class);
        startActivity(intent);
    }
   public void unitInspection(){
       errorStateHelper.buildingOrUnit=false;
       Intent intent = new Intent(getActivity().getApplicationContext(), Maintenance.class);
       startActivity(intent);
   }
    public void BuildingInspection(){
        errorStateHelper.buildingOrUnit=true;
        Intent intent = new Intent(getActivity().getApplicationContext(), Maintenance.class);
        startActivity(intent);

    }
}
