import java.sql.*;

public class Main {
    public static void main(String[] args) {
        try {
            String url = "jdbc:mysql://localhost:3306/testproject";
            String user = "root";
            String password = "password";

            // 🔥 THIS FIXES YOUR ERROR
            ProjectFrame.con = DriverManager.getConnection(url, user, password);
            ProjectFrame.stmt = ProjectFrame.con.createStatement();

            new ProjectFrame().initialize();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
