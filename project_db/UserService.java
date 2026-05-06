import java.sql.*;

public class UserService {
    public static void searchFlights(
            String from, String to,
            int tripType, int flexible,
            String depDate,
            double maxPrice, int maxStops,
            String airline,
            String depStart, String depEnd,
            int sortChoice,
            String returnDate // for round-trip
    ) throws Exception {

        Connection conn = DBConnection.getConnection();

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

    public static void bookOrWaitlist(String aid, int fn, String seatClass, String seat) throws Exception {

        Connection conn = DBConnection.getConnection();

        try {
            conn.setAutoCommit(false);

            // ✅ use logged-in user instead of asking
            int cid = ProjectFrame.cid;

            // ---- GET PRICE ----
            String priceSQL = "SELECT price FROM Flights WHERE aid=? AND flight_number=?";
            PreparedStatement psPrice = conn.prepareStatement(priceSQL);
            psPrice.setString(1, aid);
            psPrice.setInt(2, fn);

            ResultSet rsPrice = psPrice.executeQuery();
            if (!rsPrice.next()) {
                System.out.println("Flight not found!");
                return;
            }

            double price = rsPrice.getDouble("price");

            // ---- CREATE TICKET ----
            String ticketSQL = "INSERT INTO Tickets (cid, total_fare, booking_fee, purchase_time, type, status) " +
                    "VALUES (?, ?, 20, NOW(), 'one-way', 'pending')";

            PreparedStatement psTicket = conn.prepareStatement(ticketSQL, Statement.RETURN_GENERATED_KEYS);
            psTicket.setInt(1, cid);
            psTicket.setDouble(2, price);
            psTicket.executeUpdate();

            ResultSet keys = psTicket.getGeneratedKeys();
            keys.next();
            int ticketId = keys.getInt(1);

            // ---- CAPACITY ----
            String capSQL = "SELECT capacity FROM Aircraft WHERE aircraft_id = " +
                    "(SELECT aircraft_id FROM Flights WHERE aid=? AND flight_number=?)";

            PreparedStatement psCap = conn.prepareStatement(capSQL);
            psCap.setString(1, aid);
            psCap.setInt(2, fn);

            ResultSet rsCap = psCap.executeQuery();
            rsCap.next();
            int capacity = rsCap.getInt("capacity");

            // ---- CURRENT COUNT ----
            String countSQL = "SELECT COUNT(*) FROM `Includes` WHERE aid=? AND flight_number=?";
            PreparedStatement psCount = conn.prepareStatement(countSQL);
            psCount.setString(1, aid);
            psCount.setInt(2, fn);

            ResultSet rsCount = psCount.executeQuery();
            rsCount.next();
            int current = rsCount.getInt(1);

            // ---- SEAT CHECK ----
            String seatCheckSQL = "SELECT 1 FROM `Includes` WHERE aid=? AND flight_number=? AND seat_number=?";
            PreparedStatement psSeat = conn.prepareStatement(seatCheckSQL);
            psSeat.setString(1, aid);
            psSeat.setInt(2, fn);
            psSeat.setString(3, seat);

            ResultSet rsSeat = psSeat.executeQuery();
            if (rsSeat.next()) {
                System.out.println("Seat already taken.");
                return;
            }

            // ---- FULL OR NOT ----
            if (current >= capacity) {
                System.out.println("Flight full → added to waiting list");

                String posSQL = "SELECT COUNT(*) FROM Waiting_List WHERE aid=? AND flight_number=?";
                PreparedStatement psPos = conn.prepareStatement(posSQL);
                psPos.setString(1, aid);
                psPos.setInt(2, fn);

                ResultSet rsPos = psPos.executeQuery();
                rsPos.next();
                int position = rsPos.getInt(1) + 1;

                String waitSQL = "INSERT INTO Waiting_List VALUES (?, ?, ?, ?)";
                PreparedStatement psWait = conn.prepareStatement(waitSQL);

                psWait.setInt(1, ticketId);
                psWait.setString(2, aid);
                psWait.setInt(3, fn);
                psWait.setInt(4, position);
                psWait.executeUpdate();

                PreparedStatement psUpdate = conn.prepareStatement(
                        "UPDATE Tickets SET status='waiting' WHERE ticket_id=?");
                psUpdate.setInt(1, ticketId);
                psUpdate.executeUpdate();

            } else {
                System.out.println("Booking ticket...");

                String incSQL = "INSERT INTO `Includes` " +
                        "(ticket_id, aid, flight_number, departure_datetime, seat_number, class, special_meal) " +
                        "VALUES (?, ?, ?, NOW(), ?, ?, 'none')";

                PreparedStatement psInc = conn.prepareStatement(incSQL);
                psInc.setInt(1, ticketId);
                psInc.setString(2, aid);
                psInc.setInt(3, fn);
                psInc.setString(4, seat);
                psInc.setString(5, seatClass);

                psInc.executeUpdate();

                PreparedStatement psUpdate = conn.prepareStatement(
                        "UPDATE Tickets SET status='active' WHERE ticket_id=?");
                psUpdate.setInt(1, ticketId);
                psUpdate.executeUpdate();

                System.out.println("Ticket booked!");
            }

            conn.commit();

        } catch (Exception e) {
            conn.rollback();
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void viewUpcoming(int cid) throws Exception {
        Connection conn = DBConnection.getConnection();

        String sql = "SELECT i.aid, i.flight_number, i.seat_number, f.departure_datetime " +
                "FROM Tickets t " +
                "JOIN `Includes` i ON t.ticket_id = i.ticket_id " +
                "JOIN Flights f ON i.aid = f.aid AND i.flight_number = f.flight_number " +
                "WHERE t.cid = ? AND f.departure_datetime > NOW() " +
                "ORDER BY f.departure_datetime";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, cid);

        ResultSet rs = ps.executeQuery();

        boolean found = false;

        System.out.println("\n--- Upcoming Flights ---");

        while (rs.next()) {
            found = true;

            System.out.println(
                    rs.getString("aid") + " " +
                            rs.getInt("flight_number") +
                            " Seat: " + rs.getString("seat_number") +
                            " Depart: " + rs.getTimestamp("departure_datetime"));
        }

        if (!found) {
            System.out.println("No upcoming flights.");
        }
    }

    public static void viewPast(int cid) throws Exception {
        Connection conn = DBConnection.getConnection();

        String sql = "SELECT i.aid, i.flight_number, i.seat_number, f.departure_datetime " +
                "FROM Tickets t " +
                "JOIN `Includes` i ON t.ticket_id = i.ticket_id " +
                "JOIN Flights f ON i.aid = f.aid AND i.flight_number = f.flight_number " +
                "WHERE t.cid = ? AND f.departure_datetime < NOW() " +
                "ORDER BY f.departure_datetime DESC";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, cid);

        ResultSet rs = ps.executeQuery();

        boolean found = false;

        System.out.println("\n--- Past Flights ---");

        while (rs.next()) {
            found = true;

            System.out.println(
                    rs.getString("aid") + " " +
                            rs.getInt("flight_number") +
                            " Seat: " + rs.getString("seat_number") +
                            " Depart: " + rs.getTimestamp("departure_datetime"));
        }

        if (!found) {
            System.out.println("No past flights.");
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

            // ✅ ONLY RUN IF CANCELLED
            String getFlightSQL = "SELECT aid, flight_number FROM `Includes` WHERE ticket_id=?";
            PreparedStatement psGet = conn.prepareStatement(getFlightSQL);
            psGet.setInt(1, ticketId);

            ResultSet rs = psGet.executeQuery();

            if (rs.next()) {
                String aid = rs.getString("aid");
                int fn = rs.getInt("flight_number");

                promoteWaitingList(conn, aid, fn);
            }

        } else {
            System.out.println("Cannot cancel (economy or not found)");
        }
    }

    public static void promoteWaitingList(Connection conn, String aid, int fn) throws Exception {

        String sql = "SELECT * FROM Waiting_List WHERE aid=? AND flight_number=? ORDER BY position LIMIT 1";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, aid);
        ps.setInt(2, fn);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            int tid = rs.getInt("ticket_id");

            // ✅ alert
            System.out.println("📢 ALERT: Seat available! Promoting ticket " + tid);

            // insert into Includes
            String incSQL = "INSERT INTO `Includes` (ticket_id, aid, flight_number, departure_datetime, seat_number, class, special_meal) "
                    +
                    "VALUES (?, ?, ?, NOW(), 'AUTO', 'economy', 'none')";

            PreparedStatement ps2 = conn.prepareStatement(incSQL);
            ps2.setInt(1, tid);
            ps2.setString(2, aid);
            ps2.setInt(3, fn);
            ps2.executeUpdate();

            // remove from waiting list
            String delSQL = "DELETE FROM Waiting_List WHERE ticket_id=?";
            PreparedStatement ps3 = conn.prepareStatement(delSQL);
            ps3.setInt(1, tid);
            ps3.executeUpdate();
        }
    }

    public static void askQuestion(String question) throws Exception {
        Connection conn = DBConnection.getConnection();

        int cid = ProjectFrame.cid; // use logged-in user

        if (cid == -1) {
            System.out.println("❌ User not logged in.");
            return;
        }

        String sql = "INSERT INTO Questions (cid, question, status) VALUES (?, ?, 'pending')";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setInt(1, cid);
        ps.setString(2, question);

        ps.executeUpdate();
    }

}
