package com.safefaces.safefaces.Backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String HOST = "postgres.mau.se";
    private static final String PORT = "55432";
    private static final String DATABASE = "ar5278";
    private static final String USER = "ar5278";
    private static final String PASSWORD = "43h3y67g";  //OK då det som finns i tabellerna är testdata
    private static final String URL = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DATABASE;

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
