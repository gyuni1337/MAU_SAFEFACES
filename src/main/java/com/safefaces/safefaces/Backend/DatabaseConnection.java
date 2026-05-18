package com.safefaces.safefaces.Backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class responsible for managing the database connection.
 * Provides methods to establish and close a connection to the PostgreSQL database.
 *
 * This class uses a singleton-like approach to reuse a single connection instance.
 *
 * @author Noor Nabi
 * @author Gyundyuz Sadulov
 */
public class DatabaseConnection {

    /** Database host address. */
    private static final String HOST = "postgres.mau.se";

    /** Database port number. */
    private static final String PORT = "55432";

    /** Database name. */
    private static final String DATABASE = "ar5278";

    /** Database username. */
    private static final String USER = "ar5278";

    /** Database password. */
    private static final String PASSWORD = "43h3y67g";  //OK då det som finns i tabellerna är testdata

    /** Complete JDBC connection URL. */
    private static final String URL = "jdbc:postgresql://" + HOST
            + ":" + PORT + "/"
            + DATABASE + "?currentSchema=safefaces"
            + "&socketTimeout=30000" // 30 sek
            + "&connectTimeout=10000"; // 10 sek
//            + "&options=-c%20idle_in_transaction_session_timeout%3D30000";

    /** Cached connection instance. */
    private static Connection connection;

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private DatabaseConnection() {
    }

    /**
     * Returns an active database connection.
     * If no connection exists or if it has been closed, a new connection is created.
     *
     * @return a valid {@link Connection} to the database
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {

        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }

    /**
     * Closes the current database connection if it exists.
     * Prints an error message if the connection cannot be closed.
     */
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
