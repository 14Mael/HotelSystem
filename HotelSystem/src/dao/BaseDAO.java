package dao;

import java.sql.*;

public class BaseDAO {
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String url = "jdbc:mysql://localhost:3306/java1?serverTimezone=UTC";
    private static final String user = "root";
    private static final String password = "root";

    protected static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        return DriverManager.getConnection(url, user, password);
    }
}
