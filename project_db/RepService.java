import java.sql.*;
import java.util.Scanner;

public class RepService {

    // 1️⃣ Make reservation for user
    public static void makeReservationForUser(Scanner sc) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            System.out.print("User ID: ");
            int cid = sc.nextInt();

            System.out.print("Trip type (one-way/round): ");
            String tripType = sc.next();

            System.out.print("Total fare: ");
            double fare = sc.nextDouble();

            System.out.print("Booking fee: ");
            double fee = sc.nextDouble();

            // ✅ Create ticket first
            String ticketSQL = "INSERT INTO Tickets " +
                    "(cid, total_fare, booking_fee, purchase_time, type, status) " +
                    "VALUES (?, ?, ?, NOW(), ?, 'active')";

            PreparedStatement ps1 = conn.prepareStatement(ticketSQL, Statement.RETURN_GENERATED_KEYS);
            ps1.setInt(1, cid);
            ps1.setDouble(2, fare);
            ps1.setDouble(3, fee);
            ps1.setString(4, tripType);

            ps1.executeUpdate();

            ResultSet keys = ps1.getGeneratedKeys();
            keys.next();
            int tid = keys.getInt(1);

            // ✅ Ask how many flights
            System.out.print("How many flights in this reservation? ");
            int numFlights = sc.nextInt();

            for (int i = 0; i < numFlights; i++) {
                System.out.println("\n--- Flight " + (i + 1) + " ---");
                System.out.print("Airline ID: ");

                String aid = sc.next();
                System.out.print("Flight #: ");
                int fn = sc.nextInt();
                sc.nextLine(); // clear buffer

                System.out.print("Seat (e.g., 12A): ");
                String seat = sc.nextLine();
                System.out.print("Class (economy/business): ");
                String seatClass = sc.nextLine();
                System.out.print("Meal (none/veg/etc): ");
                String meal = sc.nextLine();

                // ✅ 1. Check flight exists + get datetime
                String checkFlightSQL = "SELECT departure_datetime FROM Flights WHERE aid = ? AND flight_number = ?";
                PreparedStatement psCheck = conn.prepareStatement(checkFlightSQL);
                psCheck.setString(1, aid);
                psCheck.setInt(2, fn);

                ResultSet rs = psCheck.executeQuery();

                if (!rs.next()) {
                    throw new Exception("Flight " + aid + " " + fn + " does not exist!");
                }

                Timestamp depDatetime = rs.getTimestamp("departure_datetime");

                // ✅ 2. Check seat availability
                String seatCheckSQL = "SELECT * FROM `Includes` WHERE aid = ? AND flight_number = ? AND seat_number = ?";
                PreparedStatement psSeat = conn.prepareStatement(seatCheckSQL);
                psSeat.setString(1, aid);
                psSeat.setInt(2, fn);
                psSeat.setString(3, seat);

                ResultSet rsSeat = psSeat.executeQuery();

                if (rsSeat.next()) {
                    throw new Exception("Seat " + seat + " is already taken on flight " + fn);
                }

                // ✅ 3. Insert into Includes
                String incSQL = "INSERT INTO `Includes` " +
                        "(ticket_id, aid, flight_number, departure_datetime, seat_number, class, special_meal) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";

                PreparedStatement ps2 = conn.prepareStatement(incSQL);

                ps2.setInt(1, tid);
                ps2.setString(2, aid);
                ps2.setInt(3, fn);
                ps2.setTimestamp(4, depDatetime);
                ps2.setString(5, seat);
                ps2.setString(6, seatClass);
                ps2.setString(7, meal);

                ps2.executeUpdate();
            }

            conn.commit();

            System.out.println("\n✅ Reservation created successfully with " + numFlights + " flights!");

        } catch (Exception e) {
            try {
                if (conn != null)
                    conn.rollback();
            } catch (Exception ignored) {
            }

            System.out.println("\n❌ Reservation failed: " + e.getMessage());
        }
    }

    // 2️⃣ Edit reservation
    public static void editReservation(Scanner sc) {
        Connection conn = null;

        try {
            conn = DBConnection.getConnection();

            System.out.print("Ticket ID: ");
            int tid = sc.nextInt();

            System.out.print("Airline ID: ");
            String aid = sc.next();

            System.out.print("Flight #: ");
            int fn = sc.nextInt();

            System.out.print("New seat: ");
            String seat = sc.next();

            // ✅ Check if seat already taken
            String seatCheckSQL = "SELECT * FROM `Includes` WHERE aid=? AND flight_number=? AND seat_number=?";
            PreparedStatement psCheck = conn.prepareStatement(seatCheckSQL);

            psCheck.setString(1, aid);
            psCheck.setInt(2, fn);
            psCheck.setString(3, seat);

            ResultSet rs = psCheck.executeQuery();

            if (rs.next()) {
                System.out.println("❌ Seat already taken!");
                return;
            }

            // ✅ Update ONLY one flight
            String sql = "UPDATE `Includes` SET seat_number=? " +
                    "WHERE ticket_id=? AND aid=? AND flight_number=?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, seat);
            ps.setInt(2, tid);
            ps.setString(3, aid);
            ps.setInt(4, fn);

            int rows = ps.executeUpdate();

            if (rows == 0) {
                System.out.println("❌ No matching reservation found.");
            } else {
                System.out.println("✅ Reservation updated!");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // 3️⃣ Add flight
    public static void addFlight(Scanner sc) {
        try {
            Connection conn = DBConnection.getConnection();

            System.out.print("Airline ID: ");
            String aid = sc.next();

            System.out.print("Flight #: ");
            int fn = sc.nextInt();

            System.out.print("Aircraft ID: ");
            int aircraft = sc.nextInt();

            System.out.print("Departure Airport (e.g., JFK): ");
            String dep = sc.next();

            System.out.print("Destination Airport (e.g., LAX): ");
            String dest = sc.next();

            sc.nextLine(); // clear buffer

            System.out.print("Departure datetime (YYYY-MM-DD HH:MM:SS): ");
            String depTime = sc.nextLine();

            System.out.print("Arrival datetime (YYYY-MM-DD HH:MM:SS): ");
            String arrTime = sc.nextLine();

            System.out.print("Days of week (e.g., Mon,Tue,Wed): ");
            String days = sc.nextLine();

            System.out.print("Price: ");
            double price = sc.nextDouble();

            System.out.print("Stops: ");
            int stops = sc.nextInt();

            System.out.print("Type (domestic/international): ");
            String type = sc.next();

            // ✅ FIX: correct variable names
            Timestamp depTimestamp = Timestamp.valueOf(depTime);
            Timestamp arrTimestamp = Timestamp.valueOf(arrTime);

            // ✅ Validation
            if (arrTimestamp.before(depTimestamp)) {
                System.out.println("❌ Arrival time must be after departure.");
                return;
            }

            String sql = "INSERT INTO Flights (" +
                    "aid, flight_number, aircraft_id, departure_airport, destination_airport, " +
                    "price, stops, type, days_of_week, departure_datetime, arrival_datetime) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, aid);
            ps.setInt(2, fn);
            ps.setInt(3, aircraft);
            ps.setString(4, dep);
            ps.setString(5, dest);
            ps.setDouble(6, price);
            ps.setInt(7, stops);
            ps.setString(8, type);
            ps.setString(9, days);
            ps.setTimestamp(10, depTimestamp);
            ps.setTimestamp(11, arrTimestamp);

            ps.executeUpdate();

            System.out.println("✅ Flight added successfully!");

        } catch (Exception e) {
            System.out.println("❌ Error adding flight: " + e.getMessage());
        }
    }

    // 4️⃣ Delete flight
    public static void deleteFlight(Scanner sc) {
        try {
            Connection conn = DBConnection.getConnection();

            System.out.print("Airline ID: ");
            String aid = sc.next();

            System.out.print("Flight #: ");
            int fn = sc.nextInt();

            // ✅ Check if flight is used
            String checkSQL = "SELECT * FROM `Includes` WHERE aid=? AND flight_number=?";
            PreparedStatement psCheck = conn.prepareStatement(checkSQL);

            psCheck.setString(1, aid);
            psCheck.setInt(2, fn);

            ResultSet rs = psCheck.executeQuery();

            if (rs.next()) {
                System.out.println("❌ Cannot delete — flight is used in reservations.");
                return;
            }

            String sql = "DELETE FROM Flights WHERE aid=? AND flight_number=?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, aid);
            ps.setInt(2, fn);

            int rows = ps.executeUpdate();

            if (rows == 0) {
                System.out.println("❌ Flight not found.");
            } else {
                System.out.println("✅ Flight deleted!");
            }

        } catch (Exception e) {
            System.out.println("❌ Error deleting flight: " + e.getMessage());
        }
    }

    // 5️⃣ Waiting list passengers
    public static void viewWaitingList(Scanner sc) {
        try {
            Connection conn = DBConnection.getConnection();

            System.out.print("Airline ID: ");
            String aid = sc.next();

            System.out.print("Flight #: ");
            int fn = sc.nextInt();

            // ✅ Order by time (FIFO)
            String sql = "SELECT * FROM Waiting_List " +
                    "WHERE aid=? AND flight_number=? " +
                    "ORDER BY position ";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, aid);
            ps.setInt(2, fn);

            ResultSet rs = ps.executeQuery();

            boolean found = false;

            System.out.println("\n--- Waiting List ---");

            while (rs.next()) {
                found = true;

                int ticketId = rs.getInt("ticket_id");
                Timestamp time = rs.getTimestamp("request_time");

                System.out.println("Ticket: " + ticketId +
                        " | Requested at: " + time);
            }

            if (!found) {
                System.out.println("No users in waiting list.");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // 6️⃣ Flights by airport
    public static void flightsByAirport(Scanner sc) {
        try {
            Connection conn = DBConnection.getConnection();

            System.out.print("Airport ID: ");
            String airport = sc.next();

            String sql = "SELECT * FROM Flights " +
                    "WHERE departure_airport=? OR destination_airport=? " +
                    "ORDER BY departure_datetime";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, airport);
            ps.setString(2, airport);

            ResultSet rs = ps.executeQuery();

            boolean found = false;

            System.out.println("\n--- Flights for Airport " + airport + " ---");

            while (rs.next()) {
                found = true;

                String aid = rs.getString("aid");
                int fn = rs.getInt("flight_number");
                String dep = rs.getString("departure_airport");
                String dest = rs.getString("destination_airport");
                Timestamp depTime = rs.getTimestamp("departure_datetime");

                // ✅ show direction
                String type = dep.equalsIgnoreCase(airport) ? "DEPART" : "ARRIVE";

                System.out.println(
                        "[" + type + "] " +
                                aid + " " + fn +
                                " | " + dep + " -> " + dest +
                                " | Time: " + depTime);
            }

            if (!found) {
                System.out.println("No flights found for this airport.");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // 7️⃣ Reply to question
    public static void replyQuestion(Scanner sc) {
        try {
            Connection conn = DBConnection.getConnection();

            System.out.print("Question ID: ");
            int qid = sc.nextInt();
            sc.nextLine();

            // ✅ Check if question exists + status
            String checkSQL = "SELECT status FROM Questions WHERE qid=?";
            PreparedStatement psCheck = conn.prepareStatement(checkSQL);
            psCheck.setInt(1, qid);

            ResultSet rs = psCheck.executeQuery();

            if (!rs.next()) {
                System.out.println("❌ Question not found.");
                return;
            }

            String status = rs.getString("status");

            if ("answered".equalsIgnoreCase(status)) {
                System.out.println("⚠️ This question is already answered.");
                return;
            }

            System.out.print("Reply: ");
            String reply = sc.nextLine();

            // ✅ Update
            String sql = "UPDATE Questions SET response=?, status='answered' WHERE qid=?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, reply);
            ps.setInt(2, qid);

            int rows = ps.executeUpdate();

            if (rows == 0) {
                System.out.println("❌ Update failed.");
            } else {
                System.out.println("✅ Reply sent!");
            }

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }
}
