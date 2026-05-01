import java.sql.Connection;
import java.sql.DriverManager;

public class TestConnection {
    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/testproject",
                    "root",
                    "Password" // ← your MySQL password
            );

            System.out.println("✅ Connected to database!");

        } catch (Exception e) {
            System.out.println("❌ Connection failed");
            e.printStackTrace();
        }
    }
}
