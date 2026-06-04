package com.safefaces.safefaces.Core;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {


    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;

    private static Connection connection;

    private DatabaseConnection() {}

    static {
        Properties props = new Properties();
        try (InputStream in = DatabaseConnection.class
                .getResourceAsStream("/dbdata/config.properties")) {
            if (in == null) {
                throw new ExceptionInInitializerError(
                        "config.properties not found on classpath");
            }
            props.load(in);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(
                    "Cannot load database config: " + e.getMessage());
        }

        String host     = props.getProperty("db.host");
        String port     = props.getProperty("db.port");
        String database = props.getProperty("db.database");
        String schema   = props.getProperty("db.schema");
        USER     = props.getProperty("db.username");
        PASSWORD = props.getProperty("db.password");
        URL = "jdbc:postgresql://" + host + ":" + port + "/" + database
                + "?currentSchema=" + schema
                + "&socketTimeout=30000"
                + "&connectTimeout=10000";
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
