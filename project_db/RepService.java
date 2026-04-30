import java.sql.*;
import java.util.Scanner;

public class RepService {

    // 1️⃣ Make reservation for user
    public static void makeReservationForUser(Scanner sc) throws Exception {
        Connection conn = DBConnection.getConnection();

        System.out.print("Customer ID: ");
        int cid = sc.nextInt();

        System.out.print("Airline ID: ");
        String aid = sc.next();

        System.out.print("Flight #: ");
        int fn = sc.nextInt();

        String ticketSQL = "INSERT INTO Tickets (cid, total_fare, booking_fee, purchase_time, type, status) VALUES (?,300,20,NOW(),'one-way','active')";
        PreparedStatement ps = conn.prepareStatement(ticketSQL, Statement.RETURN_GENERATED_KEYS);

        ps.setInt(1, cid);
        ps.executeUpdate();

        ResultSet keys = ps.getGeneratedKeys();
        keys.next();
        int tid = keys.getInt(1);

        String incSQL = "INSERT INTO `Includes` VALUES (?, ?, ?, NOW(), '12A', 'economy', 'none')";
        PreparedStatement ps2 = conn.prepareStatement(incSQL);

        ps2.setInt(1, tid);
        ps2.setString(2, aid);
        ps2.setInt(3, fn);

        ps2.executeUpdate();

        System.out.println("Reservation created for user!");
    }

    // 2️⃣ Edit reservation
    public static void editReservation(Scanner sc) throws Exception {
        Connection conn = DBConnection.getConnection();

        System.out.print("Ticket ID: ");
        int tid = sc.nextInt();

        System.out.print("New seat: ");
        String seat = sc.next();

        String sql = "UPDATE `Includes` SET seat_number=? WHERE ticket_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, seat);
        ps.setInt(2, tid);

        ps.executeUpdate();

        System.out.println("Reservation updated!");
    }

    // 3️⃣ Add flight
    public static void addFlight(Scanner sc) throws Exception {
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

        Timestamp depTimestamp = Timestamp.valueOf(depTimeInput);
        Timestamp arrTimestamp = Timestamp.valueOf(arrTimeInput);
        
        // ---- FULL INSERT ----
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
    
        System.out.println("Flight added successfully!");
    }

    // 4️⃣ Delete flight
    public static void deleteFlight(Scanner sc) throws Exception {
        Connection conn = DBConnection.getConnection();

        System.out.print("Airline ID: ");
        String aid = sc.next();

        System.out.print("Flight #: ");
        int fn = sc.nextInt();

        String sql = "DELETE FROM Flights WHERE aid=? AND flight_number=?";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, aid);
        ps.setInt(2, fn);

        ps.executeUpdate();

        System.out.println("Flight deleted!");
    }

    // 5️⃣ Waiting list passengers
    public static void viewWaitingList(Scanner sc) throws Exception {
        Connection conn = DBConnection.getConnection();

        System.out.print("Airline ID: ");
        String aid = sc.next();

        System.out.print("Flight #: ");
        int fn = sc.nextInt();

        String sql = "SELECT * FROM Waiting_List WHERE aid=? AND flight_number=?";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, aid);
        ps.setInt(2, fn);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            System.out.println("Ticket: " + rs.getInt("ticket_id"));
        }
    }

    // 6️⃣ Flights by airport
    public static void flightsByAirport(Scanner sc) throws Exception {
        Connection conn = DBConnection.getConnection();

        System.out.print("Airport ID: ");
        String airport = sc.next();

        String sql = "SELECT * FROM Flights WHERE departure_airport=? OR destination_airport=?";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, airport);
        ps.setString(2, airport);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            System.out.println(
                    rs.getString("aid") + " " +
                            rs.getInt("flight_number"));
        }
    }

    // 7️⃣ Reply to question
    public static void replyQuestion(Scanner sc) throws Exception {
        Connection conn = DBConnection.getConnection();

        System.out.print("Question ID: ");
        int qid = sc.nextInt();
        sc.nextLine();

        System.out.print("Reply: ");
        String reply = sc.nextLine();

        String sql = "UPDATE Questions SET response=?, status='answered' WHERE qid=?";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, reply);
        ps.setInt(2, qid);

        ps.executeUpdate();

        System.out.println("Reply sent!");
    }
}
