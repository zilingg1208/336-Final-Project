import java.sql.*;
import java.util.Scanner;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class AdminService {

    public static void addRepresentative(String username, String password) {
        try {
            Connection conn = DBConnection.getConnection();

            username = username.trim();
            password = password.trim();

            String checkSQL = "SELECT * FROM Employees WHERE username=?";
            PreparedStatement psCheck = conn.prepareStatement(checkSQL);
            psCheck.setString(1, username);

            ResultSet rs = psCheck.executeQuery();
            if (rs.next()) {
                System.out.println("❌ Username already exists.");
                return;
            }

            String sql = "INSERT INTO Employees (username, password, role) VALUES (?, ?, 'rep')";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, username);
            ps.setString(2, password);

            ps.executeUpdate();

            System.out.println("✅ Representative added!");

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    public static void editRepresentative(String username, String newUser, String newPass) {
        try {
            Connection conn = DBConnection.getConnection();

            String checkSQL = "SELECT * FROM Employees WHERE username=? AND role='rep'";
            PreparedStatement psCheck = conn.prepareStatement(checkSQL);
            psCheck.setString(1, username);

            ResultSet rs = psCheck.executeQuery();
            if (!rs.next()) {
                System.out.println("❌ Representative not found.");
                return;
            }

            String dupSQL = "SELECT * FROM Employees WHERE username=? AND username<>?";
            PreparedStatement psDup = conn.prepareStatement(dupSQL);
            psDup.setString(1, newUser);
            psDup.setString(2, username);

            ResultSet rsDup = psDup.executeQuery();
            if (rsDup.next()) {
                System.out.println("❌ Username already exists.");
                return;
            }

            String sql = "UPDATE Employees SET username=?, password=? WHERE username=? AND role='rep'";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, newUser);
            ps.setString(2, newPass);
            ps.setString(3, username);

            ps.executeUpdate();

            System.out.println("✅ Representative updated!");

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    public static void deleteRepresentative(String username) {
        try {
            Connection conn = DBConnection.getConnection();

            String checkSQL = "SELECT * FROM Employees WHERE username=? AND role='rep'";
            PreparedStatement psCheck = conn.prepareStatement(checkSQL);
            psCheck.setString(1, username);

            ResultSet rs = psCheck.executeQuery();

            if (!rs.next()) {
                System.out.println("❌ Representative not found.");
                return;
            }

            String sql = "DELETE FROM Employees WHERE username=? AND role='rep'";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);

            ps.executeUpdate();

            System.out.println("✅ Representative deleted successfully!");

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    public static void addUser(String name, String email, String username, String password) {
        try {
            Connection conn = DBConnection.getConnection();

            if (name.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
                System.out.println("❌ All fields are required.");
                return;
            }

            String checkSQL = "SELECT * FROM Users WHERE username=? OR email=?";
            PreparedStatement psCheck = conn.prepareStatement(checkSQL);
            psCheck.setString(1, username);
            psCheck.setString(2, email);

            ResultSet rs = psCheck.executeQuery();
            if (rs.next()) {
                System.out.println("❌ Username or Email already exists.");
                return;
            }

            String checkEmpSQL = "SELECT * FROM Employees WHERE username=?";
            PreparedStatement psCheckEmp = conn.prepareStatement(checkEmpSQL);
            psCheckEmp.setString(1, username);

            ResultSet rs2 = psCheckEmp.executeQuery();
            if (rs2.next()) {
                System.out.println("❌ Username already used by admin/rep.");
                return;
            }

            String sql = "INSERT INTO Users (name, email, username, password) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, username);
            ps.setString(4, password);

            ps.executeUpdate();

            System.out.println("✅ User created successfully!");

        } catch (Exception e) {
            System.out.println("❌ Error adding user: " + e.getMessage());
        }
    }

    public static void editUser(int cid, String name, String email, String username, String password) {
        try {
            Connection conn = DBConnection.getConnection();

            String checkSQL = "SELECT * FROM Users WHERE cid=?";
            PreparedStatement psCheck = conn.prepareStatement(checkSQL);
            psCheck.setInt(1, cid);

            ResultSet rs = psCheck.executeQuery();
            if (!rs.next()) {
                System.out.println("❌ User not found.");
                return;
            }

            String dupSQL = "SELECT * FROM Users WHERE (username=? OR email=?) AND cid<>?";
            PreparedStatement psDup = conn.prepareStatement(dupSQL);
            psDup.setString(1, username);
            psDup.setString(2, email);
            psDup.setInt(3, cid);

            ResultSet rsDup = psDup.executeQuery();
            if (rsDup.next()) {
                System.out.println("❌ Username or email already used.");
                return;
            }

            String sql = "UPDATE Users SET name=?, email=?, username=?, password=? WHERE cid=?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, username);
            ps.setString(4, password);
            ps.setInt(5, cid);

            ps.executeUpdate();

            System.out.println("✅ User updated successfully!");

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    public static void deleteUser(int cid) {
        try {
            Connection conn = DBConnection.getConnection();

            String checkSQL = "SELECT * FROM Users WHERE cid=?";
            PreparedStatement check = conn.prepareStatement(checkSQL);
            check.setInt(1, cid);

            ResultSet rs = check.executeQuery();
            if (!rs.next()) {
                System.out.println("User not found.");
                return;
            }

            String sql = "DELETE FROM Users WHERE cid=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, cid);

            ps.executeUpdate();

            System.out.println("User deleted!");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void monthlySales(int m) {
        try {
            Connection conn = DBConnection.getConnection();

            String sql = "SELECT SUM(total_fare + booking_fee) AS total FROM Tickets WHERE MONTH(purchase_time)=?";
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

    public static void reservationsByFlight(String aid, int fn) {
        try {
            Connection conn = DBConnection.getConnection();

            String sql = "SELECT ticket_id, seat_number, class FROM `Includes` WHERE aid=? AND flight_number=?";
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

    public static void reservationsByUser(int cid) throws Exception {
        Connection conn = DBConnection.getConnection();

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

            StringBuilder result = new StringBuilder();

            while (rs.next()) {
                result.append(
                        rs.getString("aid") + " " +
                                rs.getInt("flight_number") +
                                " | Revenue: $" + rs.getDouble("revenue") + "\n");
            }

            if (result.length() == 0)
                result.append("No data available.");

            JTextArea area = new JTextArea(result.toString(), 15, 40);
            area.setEditable(false);
            JOptionPane.showMessageDialog(null, new JScrollPane(area));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public static void revenueByUser() {
        try {
            Connection conn = DBConnection.getConnection();

            String sql = "SELECT cid, SUM(total_fare) AS total " +
                    "FROM Tickets GROUP BY cid ORDER BY total DESC";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            StringBuilder result = new StringBuilder("--- Revenue by User ---\n");

            while (rs.next()) {
                result.append("User ")
                        .append(rs.getInt("cid"))
                        .append(" | Revenue: $")
                        .append(rs.getDouble("total"))
                        .append("\n");
            }

            if (result.length() == 0)
                result.append("No data available.");

            JTextArea area = new JTextArea(result.toString(), 15, 40);
            area.setEditable(false);
            JOptionPane.showMessageDialog(null, new JScrollPane(area));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public static void topUser() {
        try {
            Connection conn = DBConnection.getConnection();

            String sql = "SELECT cid, SUM(total_fare) AS total " +
                    "FROM Tickets GROUP BY cid ORDER BY total DESC LIMIT 1";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            if (rs.next()) {
                JOptionPane.showMessageDialog(null,
                        "Top User: " + rs.getInt("cid") +
                                " | Revenue: $" + rs.getDouble("total"));
            } else {
                JOptionPane.showMessageDialog(null, "No users found.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
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

            StringBuilder result = new StringBuilder("--- Most Active Flights ---\n");

            while (rs.next()) {
                result.append(
                        rs.getString("aid") + " " +
                                rs.getInt("flight_number") +
                                " | Tickets: " + rs.getInt("total") + "\n");
            }

            if (result.length() == 0)
                result.append("No flight data available.");

            JTextArea area = new JTextArea(result.toString(), 15, 40);
            area.setEditable(false);
            JOptionPane.showMessageDialog(null, new JScrollPane(area));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public static void revenueByAirline() {
        try {
            Connection conn = DBConnection.getConnection();

            String sql = "SELECT aid, SUM(total_fare) AS revenue " +
                    "FROM `Includes` JOIN Tickets USING(ticket_id) " +
                    "GROUP BY aid";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            StringBuilder result = new StringBuilder("--- Revenue by Airline ---\n");

            while (rs.next()) {
                result.append(
                        rs.getString("aid") +
                                " | Revenue: $" + rs.getDouble("revenue") + "\n");
            }

            if (result.length() == 0)
                result.append("No data available.");

            JTextArea area = new JTextArea(result.toString(), 15, 40);
            area.setEditable(false);
            JOptionPane.showMessageDialog(null, new JScrollPane(area));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
}
