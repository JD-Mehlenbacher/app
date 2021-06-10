package com.example.myapplication.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.Maintenance;
import com.example.myapplication.R;
import com.example.myapplication.UnitInspection;
import com.example.myapplication.databinding.FragmentDashboardBinding;
import com.example.myapplication.errorStateHelper;

public class DashboardFragment extends Fragment {
    private Button unitInspectionButton;
    private Button buildingInspectionButton;
    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;

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

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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
