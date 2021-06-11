package com.example.myapplication.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.EditProfile;
import com.example.myapplication.R;
import com.example.myapplication.Register;
import com.example.myapplication.databinding.FragmentNotificationsBinding;
import com.example.myapplication.errorStateHelper;


public class NotificationsFragment extends Fragment {
    private Button registerButton;
    private Button editProfileButton;
    private NotificationsViewModel notificationsViewModel;
    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //final TextView textView = binding.textNotifications;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

                //textView.setText(s);
            }
        });
        registerButton=(Button) root.findViewById(R.id.RegisterButtonSettingsPage);
        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goToRegisterPage();
            }
        });
        editProfileButton=(Button) root.findViewById(R.id.EditProfileButtonSettingsPage);
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goToEditProfile();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public void goToRegisterPage(){
        errorStateHelper.reset();
        Intent intent2 = new Intent(getActivity(), Register.class);
        startActivity(intent2);
    }
    public void goToEditProfile(){
        errorStateHelper.reset();
        Intent intent2 = new Intent(getActivity(), EditProfile.class);
        startActivity(intent2);
    }
}