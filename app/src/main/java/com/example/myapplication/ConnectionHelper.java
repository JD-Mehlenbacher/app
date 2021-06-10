package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionHelper {
    Connection con;
    String uname,pass,ip,port,database;
    @SuppressLint("NewApi")

    public Connection connectionClass(){
        ip="192.168.10.45";
        database="patryTest";
        uname="pab";
        pass="plog2000";
        port="1433";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        java.sql.Connection connection=null;
        String ConnectionURL=null;

        try{
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL="jdbc:jtds:sqlserver://"+ ip + ";databaseName="+database+";user="+uname+";password="+pass+";";
            connection= DriverManager.getConnection(ConnectionURL);
        }
        catch(SQLException ex){
            Log.e("Error",ex.getMessage());
        }
        catch(ClassNotFoundException e){
            Log.e("Error from Class",e.getMessage());
        }
        catch(Exception e){
            Log.e("O BRUH",e.getMessage());
        }
        return connection;
    }
}
