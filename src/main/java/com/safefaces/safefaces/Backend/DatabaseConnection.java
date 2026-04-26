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
    private static final String URL = "jdbc:postgresql://" + HOST
            + ":" + PORT + "/"
            + DATABASE + "?currentSchema=safefaces"
            + "&socketTimeout=30"
            + "&connectTimeout=10"
            + "&options=-c%20idle_in_transaction_session_timeout%3D30000";

    private static Connection connection;

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {

        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Fel vid stängning av anslutning: " + e.getMessage());
            }
        }
    }
}
