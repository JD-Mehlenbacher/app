package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.myapplication.ui.dashboard.DashboardFragment;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;


public class BuildingInspection extends Fragment {
    private CheckBox row1Ok;
    private CheckBox row1NotOk;


    private Button next;

    private TextView checkBoxError;
    private TextView FieldText;

    private EditText comments;

    private ArrayList<Integer> values;
    private ArrayList<String> fields;
    private ArrayList<String> commentList;

    private int question=0;



    Connection connect;
    String ConnectionResult = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_building_inspection3, container, false);

        values = new ArrayList<>(Arrays.asList(-1,-1,-1,-1,-1));
        fields = new ArrayList<>(Arrays.asList("Smoke Alarm","HVAC","Lobby Cleanliness","Gym Cleanliness","Lights"));
        commentList = new ArrayList<>(Arrays.asList("","","","",""));

        FieldText=(TextView) rootView.findViewById(R.id.firstRowTextBuilding);

        row1Ok=(CheckBox) rootView.findViewById(R.id.firstOkBuilding);
        row1NotOk=(CheckBox) rootView.findViewById(R.id.firstNotOkBuilding);

        comments=(EditText) rootView.findViewById(R.id.commentBuilding);

        next = (Button) rootView.findViewById(R.id.BuildingNext);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(question==fields.size()-1) {
                    submit();
                }
                else{
                    next();
                }

            }
        });
        FieldText.setText(fields.get(0));
        checkBoxError=(TextView) rootView.findViewById(R.id.checkBoxErrorBuilding);
        if(errorStateHelper.checkBuildingError){
            checkBoxError.setVisibility(View.VISIBLE);
        }
        else{
            checkBoxError.setVisibility(View.INVISIBLE);
        }


        return rootView;
    }

    public void setArray(int index, CheckBox one, CheckBox two) {
        if (one.isChecked() && !two.isChecked()) {
            values.set(index, 1);
        }
        else if (!one.isChecked() && two.isChecked()) {
            values.set(index, 0);
        }
        else {
            values.set(index, -1);
        }

    }
    public void storeComment(int index,EditText comment){
        commentList.set(index,comment.getText().toString());
    }
    public void next(){
       setArray(question,row1Ok,row1NotOk);
        if(values.get(question)==-1){
            checkBoxError.setVisibility(View.VISIBLE);
            row1NotOk.setChecked(false);
            row1Ok.setChecked(false);

        }
        else{
            checkBoxError.setVisibility(View.INVISIBLE);
            row1NotOk.setChecked(false);
            row1Ok.setChecked(false);
            storeComment(question,comments);
            comments.setText("");
            question++;
            FieldText.setText(fields.get(question));
            if(question==fields.size()-1) {
                next.setText("SUBMIT");
            }
        }
    }
    public void submit() {
        setArray(question,row1Ok,row1NotOk);
        if(values.get(question)==-1){
            checkBoxError.setVisibility(View.VISIBLE);
            row1NotOk.setChecked(false);
            row1Ok.setChecked(false);
        }
        else{
            storeComment(question,comments);
            try {

                ConnectionHelper connectionHelper = new ConnectionHelper();
                connect = connectionHelper.connectionClass();
                if (connect != null) {
                    Statement st = connect.createStatement();
                    st.executeUpdate("INSERT INTO BuildingInspection VALUES (" + values.get(0) + ", "
                            + values.get(1) + ", " + values.get(2) + ", " + values.get(3) + ", " + values.get(4) +", \'" + commentList.get(0) +"\', \'" + commentList.get(1) +"\', \'" + commentList.get(2) +"\', \'" + commentList.get(3) +"\', \'" + commentList.get(4) + "\')");
                } else {
                    ConnectionResult = "Check Connection";
                }
                connect.close();
            } catch (Exception ex) {
                System.out.println("ERROR");

            }

            Intent intent = new Intent(getActivity(), com.example.myapplication.homePage.class);
            startActivity(intent);
        }



    }

}
