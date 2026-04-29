import java.sql.*;

public class Test {
    public static void main(String[] args) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");

        Connection c = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/project_db",
            "root",
            "Crystal7n7nX"
        );

        System.out.println("Connected");
    }
}
