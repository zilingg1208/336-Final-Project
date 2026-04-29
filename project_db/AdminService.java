import java.sql.*;
import java.util.Scanner;

public class AdminService {

    public static void addCustomer(Scanner sc) throws Exception {
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

        String sql = "INSERT INTO Customers VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setInt(1, cid);
        ps.setString(2, name);
        ps.setString(3, email);
        ps.setString(4, user);
        ps.setString(5, pass);

        ps.executeUpdate();

        System.out.println("Customer added!");
    }

    public static void deleteCustomer(Scanner sc) throws Exception {
        Connection conn = DBConnection.getConnection();

        System.out.print("Customer ID: ");
        int cid = sc.nextInt();

        String sql = "DELETE FROM Customers WHERE cid=?";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setInt(1, cid);
        ps.executeUpdate();

        System.out.println("Deleted!");
    }

    public static void monthlySales(Scanner sc) throws Exception {
        Connection conn = DBConnection.getConnection();

        System.out.print("Month (1-12): ");
        int m = sc.nextInt();

        String sql = "SELECT SUM(total_fare + booking_fee) FROM Tickets WHERE MONTH(purchase_time)=?";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setInt(1, m);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            System.out.println("Monthly Sales: $" + rs.getDouble(1));
        }
    }

    public static void reservationsByFlight(Scanner sc) throws Exception {
        Connection conn = DBConnection.getConnection();

        System.out.print("Airline ID: ");
        String aid = sc.next();

        System.out.print("Flight #: ");
        int fn = sc.nextInt();

        String sql = "SELECT * FROM `Includes` WHERE aid=? AND flight_number=?";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, aid);
        ps.setInt(2, fn);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            System.out.println("Ticket: " + rs.getInt("ticket_id"));
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

    public static void revenueByFlight() throws Exception {
        Connection conn = DBConnection.getConnection();

        String sql = "SELECT aid, flight_number, SUM(total_fare) " +
                "FROM `Includes` JOIN Tickets USING(ticket_id) " +
                "GROUP BY aid, flight_number";

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {
            System.out.println(rs.getString(1) + " " +
                    rs.getInt(2) +
                    " Revenue: $" + rs.getDouble(3));
        }
    }

    public static void revenueByCustomer() throws Exception {
        Connection conn = DBConnection.getConnection();

        String sql = "SELECT cid, SUM(total_fare) FROM Tickets GROUP BY cid";

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {
            System.out.println("Customer " + rs.getInt(1) +
                    " $" + rs.getDouble(2));
        }
    }

    public static void topCustomer() throws Exception {
        Connection conn = DBConnection.getConnection();

        String sql = "SELECT cid, SUM(total_fare) AS total " +
                "FROM Tickets GROUP BY cid ORDER BY total DESC LIMIT 1";

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        if (rs.next()) {
            System.out.println("Top Customer: " + rs.getInt("cid") +
                    " Revenue: $" + rs.getDouble("total"));
        }
    }

    public static void mostActiveFlights() throws Exception {
        Connection conn = DBConnection.getConnection();

        String sql = "SELECT aid, flight_number, COUNT(*) AS total " +
                "FROM `Includes` GROUP BY aid, flight_number ORDER BY total DESC";

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {
            System.out.println(rs.getString(1) + " " +
                    rs.getInt(2) +
                    " tickets: " + rs.getInt(3));
        }
    }
}