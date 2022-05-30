package com.st17.culturemap.DBConnection;

import androidx.appcompat.app.AppCompatActivity;

import com.yandex.mapkit.geometry.Point;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MySQLHelper extends AppCompatActivity {
    private static final String URL = "jdbc:mysql://37.139.32.120:3306/CultureMap";
    private static final String USER = "SAlexey";
    private static final String PASSWORD = "GJ2L8pQ94L7wR6~7";



    private Connection getDbConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");

        Connection dbConn = DriverManager.getConnection(URL, USER, PASSWORD);
        return dbConn;
    }

    // Метод для получения всех точек из таблицы
    public ArrayList<Point> getPoints() throws SQLException, ClassNotFoundException {
        String sql = "SELECT latitude, longitude FROM Points";

        Statement statement = getDbConnection().createStatement();
        ResultSet res = statement.executeQuery(sql);

        ArrayList<Point> points = new ArrayList<>();
        while (res.next())
            points.add(new Point(res.getDouble("latitude"), res.getDouble("longitude")));

        return points;
    }


}