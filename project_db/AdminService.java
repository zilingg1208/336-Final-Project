import java.sql.*;
import java.util.Scanner;

public class AdminService {

    public static void addCustomer(Scanner sc) {
        try {
            Connection conn = DBConnection.getConnection();
    
            System.out.print("Customer ID: ");
            int cid = sc.nextInt();
            sc.nextLine();
    
            System.out.print("Name: ");
            String name = sc.nextLine();
    
            System.out.print("Email: ");
            String email = sc.nextLine();
    
            System.out.print("Username: ");
            String user = sc.nextLine();
    
            System.out.print("Password: ");
            String pass = sc.nextLine();
    
            // Basic validation
            if (name.isEmpty() || email.isEmpty() || user.isEmpty() || pass.isEmpty()) {
                System.out.println("❌ All fields must be filled.");
                return;
            }
    
            // Check duplicate ID or username
            String checkSQL = "SELECT * FROM Customers WHERE cid=? OR username=?";
            PreparedStatement psCheck = conn.prepareStatement(checkSQL);
            psCheck.setInt(1, cid);
            psCheck.setString(2, user);
    
            ResultSet rs = psCheck.executeQuery();
            if (rs.next()) {
                System.out.println("❌ Customer ID or Username already exists.");
                return;
            }
    
            // Use column names (safe)
            String sql = "INSERT INTO Customers (cid, name, email, username, password) " +
                         "VALUES (?, ?, ?, ?, ?)";
    
            PreparedStatement ps = conn.prepareStatement(sql);
    
            ps.setInt(1, cid);
            ps.setString(2, name);
            ps.setString(3, email);
            ps.setString(4, user);
            ps.setString(5, pass);
    
            ps.executeUpdate();
    
            System.out.println("Customer added!");
    
        } catch (Exception e) {
            System.out.println("Error adding customer: " + e.getMessage());
        }
    }

    public static void deleteCustomer(Scanner sc) {
        try {
            Connection conn = DBConnection.getConnection();
    
            System.out.print("Customer ID: ");
            int cid = sc.nextInt();
    
            // check existence
            String checkSQL = "SELECT * FROM Customers WHERE cid=?";
            PreparedStatement check = conn.prepareStatement(checkSQL);
            check.setInt(1, cid);
    
            ResultSet rs = check.executeQuery();
            if (!rs.next()) {
                System.out.println("Customer not found.");
                return;
            }
    
            String sql = "DELETE FROM Customers WHERE cid=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, cid);
    
            ps.executeUpdate();
    
            System.out.println("Customer deleted!");
    
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void monthlySales(Scanner sc) {
        try {
            Connection conn = DBConnection.getConnection();
    
            System.out.print("Month (1-12): ");
            int m = sc.nextInt();
    
            String sql = "SELECT SUM(total_fare + booking_fee) AS total " +
                         "FROM Tickets WHERE MONTH(purchase_time)=?";
    
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, m);
    
            ResultSet rs = ps.executeQuery();
    
            if (rs.next()) {
                double total = rs.getDouble("total");
    
                if (rs.wasNull()) {
                    System.out.println("No sales for this month.");
                } else {
                    System.out.println("Monthly Sales: $" + total);
                }
            }
    
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void reservationsByFlight(Scanner sc) {
        try {
            Connection conn = DBConnection.getConnection();
    
            System.out.print("Airline ID: ");
            String aid = sc.next();
    
            System.out.print("Flight #: ");
            int fn = sc.nextInt();
    
            String sql = "SELECT ticket_id, seat_number, class " +
                         "FROM `Includes` WHERE aid=? AND flight_number=?";
    
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, aid);
            ps.setInt(2, fn);
    
            ResultSet rs = ps.executeQuery();
    
            boolean found = false;
    
            while (rs.next()) {
                found = true;
                System.out.println("Ticket: " + rs.getInt("ticket_id") +
                                   " | Seat: " + rs.getString("seat_number") +
                                   " | Class: " + rs.getString("class"));
            }
    
            if (!found) {
                System.out.println("No reservations found.");
            }
    
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void reservationsByCustomer(Scanner sc) throws Exception {
        Connection conn = DBConnection.getConnection();

        System.out.print("Customer ID: ");
        int cid = sc.nextInt();

        String sql = "SELECT * FROM Tickets WHERE cid=?";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setInt(1, cid);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            System.out.println("Ticket: " + rs.getInt("ticket_id"));
        }
    }

    public static void revenueByFlight() {
        try {
            Connection conn = DBConnection.getConnection();
    
            String sql = "SELECT aid, flight_number, SUM(total_fare) AS revenue " +
                         "FROM `Includes` JOIN Tickets USING(ticket_id) " +
                         "GROUP BY aid, flight_number";
    
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
    
            while (rs.next()) {
                System.out.println(
                    rs.getString("aid") + " " +
                    rs.getInt("flight_number") +
                    " | Revenue: $" + rs.getDouble("revenue")
                );
            }
    
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void revenueByCustomer() {
        try {
            Connection conn = DBConnection.getConnection();
    
            String sql = "SELECT cid, SUM(total_fare) AS total " +
                         "FROM Tickets GROUP BY cid ORDER BY total DESC";
    
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
    
            boolean found = false;
    
            System.out.println("\n--- Revenue by Customer ---");
    
            while (rs.next()) {
                found = true;
                System.out.println("Customer " + rs.getInt("cid") +
                                   " | Revenue: $" + rs.getDouble("total"));
            }
    
            if (!found) {
                System.out.println("No data available.");
            }
    
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void topCustomer() {
        try {
            Connection conn = DBConnection.getConnection();
    
            String sql = "SELECT cid, SUM(total_fare) AS total " +
                         "FROM Tickets GROUP BY cid ORDER BY total DESC LIMIT 1";
    
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
    
            if (rs.next()) {
                System.out.println("Top Customer: " + rs.getInt("cid") +
                                   " | Revenue: $" + rs.getDouble("total"));
            } else {
                System.out.println("No customers found.");
            }
    
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void mostActiveFlights() {
        try {
            Connection conn = DBConnection.getConnection();
    
            String sql = "SELECT aid, flight_number, COUNT(*) AS total " +
                         "FROM `Includes` " +
                         "GROUP BY aid, flight_number " +
                         "ORDER BY total DESC";
    
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
    
            boolean found = false;
    
            System.out.println("\n--- Most Active Flights ---");
    
            while (rs.next()) {
                found = true;
    
                System.out.println(
                    rs.getString("aid") + " " +
                    rs.getInt("flight_number") +
                    " | Tickets: " + rs.getInt("total")
                );
            }
    
            if (!found) {
                System.out.println("No flight data available.");
            }
    
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
