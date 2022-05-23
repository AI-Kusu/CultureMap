package com.st17.culturemap.DBConnection;

import android.os.AsyncTask;
import android.util.Log;

import com.mysql.cj.xdevapi.Statement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class MySQLHelper {
    private static final String URL = "jdbc:mysql://37.139.32.120:3306/CultureMap";
    private static final String USER = "SAlexey";
    private static final String PASSWORD = "GJ2L8pQ94L7wR6~7";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                System.out.println("Connection to Store DB succesfull!");
            }
        } catch (Exception ex) {
            System.out.println("Connection failed...");
        }
    }
}