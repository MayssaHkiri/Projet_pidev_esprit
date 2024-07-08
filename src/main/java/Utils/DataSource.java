package Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSource {
    private String url = "jdbc:mysql://localhost:3306/pi_dev_db";
    private String login = "root";
    private String pwd = "root";
    private static DataSource data;
    private Connection con;

    private DataSource() {
        try {
            con = DriverManager.getConnection(url, login, pwd);
            System.out.println("Connection established");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Connection getCon() {
        try {
            if (con == null || con.isClosed()) {
                con = DriverManager.getConnection(url, login, pwd);
                System.out.println("Reconnection established");
            }
        } catch (SQLException e) {
            System.out.println("Error while reconnecting: " + e.getMessage());
            con = null;
        }
        return con;
    }

    public static DataSource getInstance() {
        if (data == null) {
            data = new DataSource();
        }
        return data;
    }

    public void closeCon() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
                System.out.println("Connection closed");
            }
        } catch (SQLException e) {
            System.out.println("Error while closing connection: " + e.getMessage());
        }
    }
}
