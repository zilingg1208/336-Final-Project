import java.sql.*;
import java.util.Scanner;

public class UserService {
    public static void searchFlights(Scanner sc) throws Exception {
        Connection conn = DBConnection.getConnection();

        System.out.print("From: ");
        String from = sc.next();

        System.out.print("To: ");
        String to = sc.next();

        System.out.print("Trip type (1=one-way, 2=round-trip): ");
        int tripType = sc.nextInt();

        System.out.print("Flexible dates? (1=yes, 0=no): ");
        int flexible = sc.nextInt();

        System.out.print("Departure date (YYYY-MM-DD): ");
        String depDate = sc.next();

        // ---- FILTER INPUTS ----
        System.out.print("Max price (or -1 for no limit): ");
        double maxPrice = sc.nextDouble();

        System.out.print("Max stops (or -1 for no limit): ");
        int maxStops = sc.nextInt();

        System.out.print("Airline filter (or 'any'): ");
        String airline = sc.next();

        System.out.print("Earliest departure time (HH:MM or 'any'): ");
        String depStart = sc.next();

        System.out.print("Latest departure time (HH:MM or 'any'): ");
        String depEnd = sc.next();

        System.out.print("Sort by (1=price, 2=takeoff, 3=landing, 4=duration): ");
        int sortChoice = sc.nextInt();

        // ---- BUILD BASE SQL ----
        String sql = "SELECT *, " +
                "TIMESTAMPDIFF(MINUTE, departure_datetime, arrival_datetime) AS duration " +
                "FROM Flights WHERE departure_airport=? AND destination_airport=? ";

        if (flexible == 1) {
            sql += "AND DATE(departure_datetime) BETWEEN DATE_SUB(?, INTERVAL 3 DAY) AND DATE_ADD(?, INTERVAL 3 DAY) ";
        } else {
            sql += "AND DATE(departure_datetime)=? ";
        }

        // ---- APPLY FILTERS ----
        if (maxPrice != -1)
            sql += "AND price <= ? ";
        if (maxStops != -1)
            sql += "AND stops <= ? ";
        if (!airline.equalsIgnoreCase("any"))
            sql += "AND aid = ? ";
        if (!depStart.equalsIgnoreCase("any"))
            sql += "AND TIME(departure_datetime) >= ? ";
        if (!depEnd.equalsIgnoreCase("any"))
            sql += "AND TIME(departure_datetime) <= ? ";

        // ---- SORTING ----
        switch (sortChoice) {
            case 1:
                sql += "ORDER BY price ";
                break;
            case 2:
                sql += "ORDER BY departure_datetime ";
                break;
            case 3:
                sql += "ORDER BY arrival_datetime ";
                break;
            case 4:
                sql += "ORDER BY duration ";
                break;
            default:
                sql += "ORDER BY price ";
        }

        // ---- PREPARE STATEMENT ----
        PreparedStatement ps = conn.prepareStatement(sql);

        int idx = 1;
        ps.setString(idx++, from);
        ps.setString(idx++, to);

        ps.setString(idx++, depDate);
        if (flexible == 1)
            ps.setString(idx++, depDate);

        if (maxPrice != -1)
            ps.setDouble(idx++, maxPrice);
        if (maxStops != -1)
            ps.setInt(idx++, maxStops);
        if (!airline.equalsIgnoreCase("any"))
            ps.setString(idx++, airline);
        if (!depStart.equalsIgnoreCase("any"))
            ps.setString(idx++, depStart + ":00");
        if (!depEnd.equalsIgnoreCase("any"))
            ps.setString(idx++, depEnd + ":00");

        ResultSet rs = ps.executeQuery();

        System.out.println("\n--- Outbound Flights ---");
        while (rs.next()) {
            System.out.println(
                    rs.getString("aid") + " " +
                            rs.getInt("flight_number") +
                            " $" + rs.getDouble("price") +
                            " Stops:" + rs.getInt("stops") +
                            " Depart:" + rs.getTimestamp("departure_datetime") +
                            " Arrive:" + rs.getTimestamp("arrival_datetime") +
                            " Duration:" + rs.getInt("duration") + "min");
        }

        // ---- ROUND TRIP ----
        if (tripType == 2) {
            System.out.print("\nReturn date (YYYY-MM-DD): ");
            String returnDate = sc.next();

            PreparedStatement ps2 = conn.prepareStatement(sql);

            idx = 1;
            ps2.setString(idx++, to);
            ps2.setString(idx++, from);

            ps2.setString(idx++, returnDate);
            if (flexible == 1)
                ps2.setString(idx++, returnDate);

            if (maxPrice != -1)
                ps2.setDouble(idx++, maxPrice);
            if (maxStops != -1)
                ps2.setInt(idx++, maxStops);
            if (!airline.equalsIgnoreCase("any"))
                ps2.setString(idx++, airline);
            if (!depStart.equalsIgnoreCase("any"))
                ps2.setString(idx++, depStart + ":00");
            if (!depEnd.equalsIgnoreCase("any"))
                ps2.setString(idx++, depEnd + ":00");

            ResultSet rs2 = ps2.executeQuery();

            System.out.println("\n--- Return Flights ---");
            while (rs2.next()) {
                System.out.println(
                        rs2.getString("aid") + " " +
                                rs2.getInt("flight_number") +
                                " $" + rs2.getDouble("price") +
                                " Stops:" + rs2.getInt("stops") +
                                " Depart:" + rs2.getTimestamp("departure_datetime") +
                                " Arrive:" + rs2.getTimestamp("arrival_datetime") +
                                " Duration:" + rs2.getInt("duration") + "min");
            }
        }
    }

    public static void bookOrWaitlist(Scanner sc) throws Exception {
        Connection conn = DBConnection.getConnection();

        // ---- INPUT ----
        System.out.print("Customer ID: ");
        int cid = sc.nextInt();

        System.out.print("Airline ID: ");
        String aid = sc.next();

        System.out.print("Flight #: ");
        int fn = sc.nextInt();

        System.out.print("Class (economy/business/first): ");
        String seatClass = sc.next();

        // ---- STEP 1: CREATE TICKET FIRST ----
        String ticketSQL = "INSERT INTO Tickets (cid, total_fare, booking_fee, purchase_time, type, status) " +
                "VALUES (?, 300, 20, NOW(), 'one-way', 'pending')";

        PreparedStatement psTicket = conn.prepareStatement(ticketSQL, Statement.RETURN_GENERATED_KEYS);
        psTicket.setInt(1, cid);
        psTicket.executeUpdate();

        ResultSet keys = psTicket.getGeneratedKeys();
        keys.next();
        int ticketId = keys.getInt(1);

        // ---- STEP 2: GET CAPACITY ----
        String capSQL = "SELECT capacity FROM Aircraft WHERE aircraft_id = " +
                "(SELECT aircraft_id FROM Flights WHERE aid=? AND flight_number=?)";

        PreparedStatement psCap = conn.prepareStatement(capSQL);
        psCap.setString(1, aid);
        psCap.setInt(2, fn);

        ResultSet rsCap = psCap.executeQuery();

        if (!rsCap.next()) {
            System.out.println("Flight not found!");
            return;
        }

        int capacity = rsCap.getInt("capacity");

        // ---- STEP 3: COUNT CURRENT PASSENGERS ----
        String countSQL = "SELECT COUNT(*) FROM `Includes` WHERE aid=? AND flight_number=?";
        PreparedStatement psCount = conn.prepareStatement(countSQL);

        psCount.setString(1, aid);
        psCount.setInt(2, fn);

        ResultSet rsCount = psCount.executeQuery();
        rsCount.next();
        int current = rsCount.getInt(1);

        // ---- STEP 4: DECIDE BOOK OR WAIT ----
        if (current >= capacity) {
            // ---- WAITING LIST ----
            System.out.println("Flight full → adding to waiting list");

            String waitSQL = "INSERT INTO Waiting_List (ticket_id, aid, flight_number, position) VALUES (?, ?, ?, ?)";

            PreparedStatement psWait = conn.prepareStatement(waitSQL);

            psWait.setInt(1, ticketId);
            psWait.setString(2, aid);
            psWait.setInt(3, fn);
            psWait.setInt(4, 1); // simple position

            psWait.executeUpdate();

            // update ticket status
            String updateSQL = "UPDATE Tickets SET status='waiting' WHERE ticket_id=?";
            PreparedStatement psUpdate = conn.prepareStatement(updateSQL);
            psUpdate.setInt(1, ticketId);
            psUpdate.executeUpdate();

        } else {
            // ---- NORMAL BOOKING ----
            System.out.println("Booking ticket...");

            String incSQL = "INSERT INTO `Includes` " +
                    "(ticket_id, aid, flight_number, departure_datetime, seat_number, class, special_meal) " +
                    "VALUES (?, ?, ?, NOW(), ?, ?, 'none')";

            PreparedStatement psInc = conn.prepareStatement(incSQL);

            psInc.setInt(1, ticketId);
            psInc.setString(2, aid);
            psInc.setInt(3, fn);
            psInc.setString(4, "12A"); // simple seat
            psInc.setString(5, seatClass);

            psInc.executeUpdate();

            // update ticket status
            String updateSQL = "UPDATE Tickets SET status='active' WHERE ticket_id=?";
            PreparedStatement psUpdate = conn.prepareStatement(updateSQL);
            psUpdate.setInt(1, ticketId);
            psUpdate.executeUpdate();

            System.out.println("Ticket booked successfully!");
        }
    }

    public static void viewUpcoming(int cid) throws Exception {
        Connection conn = DBConnection.getConnection();

        String sql = "SELECT * FROM Tickets WHERE cid=? AND purchase_time >= NOW()";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, cid);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            System.out.println("Ticket: " + rs.getInt("ticket_id"));
        }
    }

    public static void viewPast(int cid) throws Exception {
        Connection conn = DBConnection.getConnection();

        String sql = "SELECT * FROM Tickets WHERE cid=? AND purchase_time < NOW()";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, cid);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            System.out.println("Ticket: " + rs.getInt("ticket_id"));
        }
    }

    public static void cancelTicket(int ticketId) throws Exception {
        Connection conn = DBConnection.getConnection();

        String sql = "UPDATE Tickets SET status='cancelled' WHERE ticket_id=? " +
                "AND EXISTS (SELECT 1 FROM `Includes` WHERE ticket_id=? AND class IN ('business','first'))";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, ticketId);
        ps.setInt(2, ticketId);

        int rows = ps.executeUpdate();

        if (rows > 0) {
            System.out.println("Cancelled successfully!");
            promoteWaitingList(conn);
        } else {
            System.out.println("Cannot cancel (economy or not found)");
        }
    }

    public static void promoteWaitingList(Connection conn) throws Exception {

        String sql = "SELECT * FROM Waiting_List ORDER BY position LIMIT 1";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        if (rs.next()) {
            int tid = rs.getInt("ticket_id");
            String aid = rs.getString("aid");
            int fn = rs.getInt("flight_number");

            System.out.println("Promoting waiting list ticket: " + tid);

            String incSQL = "INSERT INTO `Includes` VALUES (?, ?, ?, NOW(), '12B', 'economy', 'none')";
            PreparedStatement ps = conn.prepareStatement(incSQL);

            ps.setInt(1, tid);
            ps.setString(2, aid);
            ps.setInt(3, fn);

            ps.executeUpdate();

            String delSQL = "DELETE FROM Waiting_List WHERE ticket_id=?";
            PreparedStatement ps2 = conn.prepareStatement(delSQL);
            ps2.setInt(1, tid);
            ps2.executeUpdate();
        }
    }

    public static void askQuestion(Scanner sc) throws Exception {
        Connection conn = DBConnection.getConnection();

        System.out.print("Customer ID: ");
        int cid = sc.nextInt();
        sc.nextLine();

        System.out.print("Question: ");
        String q = sc.nextLine();

        String sql = "INSERT INTO Questions (cid, question, status) VALUES (?, ?, 'pending')";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setInt(1, cid);
        ps.setString(2, q);

        ps.executeUpdate();

        System.out.println("Question sent!");
    }
}