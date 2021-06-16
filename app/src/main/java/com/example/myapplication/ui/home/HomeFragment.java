package com.example.myapplication.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.ConnectionHelper;
import com.example.myapplication.R;
import com.example.myapplication.currentUser;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.example.myapplication.errorStateHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private ImageView building;
    Connection connect;
    String ConnectionResult = "";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        building=(ImageView) root.findViewById(R.id.TOKEN);

        setImage();

        //final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
          @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void setImage() {
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionClass();
            if (connect != null) {
                Statement st = connect.createStatement();


                ResultSet rs = st.executeQuery("SELECT * FROM TenantData WHERE Email = \'" + currentUser.email + "\'");
                ResultSet check = rs;
                if (!check.next()) {
                     rs = st.executeQuery("SELECT * FROM TenantData WHERE Username = \'" + currentUser.userName + "\'");
                }

                    if (rs.getString("Address").equals("Creekside")) {
                        building.setImageResource(R.drawable._47_development_drive_jun01_3d010_rgb_color2);
                        errorStateHelper.building = "Creekside";
                    }
                    else if (rs.getString("Address").equals("Blackburn")){
                        building.setImageResource(R.drawable._10_blackburn_nav_2);
                        errorStateHelper.building = "Blackburn";
                    }
                    else {
                        building.setImageResource(R.drawable._39_armstrong_007_nav);
                        errorStateHelper.building = "Armstrong";
                    }
                st.close();
            }
            else {
                ConnectionResult = "Check Connection";
            }
            connect.close();
        } catch (Exception ex) {
            System.out.println("Error setting image.");
        }
    }
}