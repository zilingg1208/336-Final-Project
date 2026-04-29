import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n1.Customer 2.Admin 3.Rep 4.Exit");
            int role = sc.nextInt();

            if (role == 1) {

                while (true) {
                    System.out.println("\n--- Customer Menu ---");
                    System.out.println("1. Search Flights");
                    System.out.println("2. Book / Waitlist Ticket");
                    System.out.println("3. View Upcoming Tickets");
                    System.out.println("4. View Past Tickets");
                    System.out.println("5. Cancel Ticket");
                    System.out.println("6. Ask Question");
                    System.out.println("7. Back");

                    int c = sc.nextInt();

                    if (c == 1) {
                        UserService.searchFlights(sc);

                    } else if (c == 2) {
                        UserService.bookOrWaitlist(sc);

                    } else if (c == 3) {
                        System.out.print("Customer ID: ");
                        int cid = sc.nextInt();
                        UserService.viewUpcoming(cid);

                    } else if (c == 4) {
                        System.out.print("Customer ID: ");
                        int cid = sc.nextInt();
                        UserService.viewPast(cid);

                    } else if (c == 5) {
                        System.out.print("Ticket ID: ");
                        int tid = sc.nextInt();
                        UserService.cancelTicket(tid);

                    } else if (c == 6) {
                        UserService.askQuestion(sc);

                    } else if (c == 7) {
                        break;
                    }
                }

            } else if (role == 2) {
                System.out.println(
                        "1.Add Customer 2.Delete Customer 3.Monthly Sales 4.Res by Flight 5.Res by Customer 6.Revenue Flight 7.Revenue Customer 8.Top Customer 9.Active Flights");
                int c = sc.nextInt();

                if (c == 1)
                    AdminService.addCustomer(sc);
                if (c == 2)
                    AdminService.deleteCustomer(sc);
                if (c == 3)
                    AdminService.monthlySales(sc);
                if (c == 4)
                    AdminService.reservationsByFlight(sc);
                if (c == 5)
                    AdminService.reservationsByCustomer(sc);
                if (c == 6)
                    AdminService.revenueByFlight();
                if (c == 7)
                    AdminService.revenueByCustomer();
                if (c == 8)
                    AdminService.topCustomer();
                if (c == 9)
                    AdminService.mostActiveFlights();

            } else if (role == 3) {
                System.out.println(
                        "1.Make Reservation 2.Edit 3.Add Flight 4.Delete Flight 5.Waiting List 6.Flights by Airport 7.Reply");
                int c = sc.nextInt();

                if (c == 1)
                    RepService.makeReservationForUser(sc);
                if (c == 2)
                    RepService.editReservation(sc);
                if (c == 3)
                    RepService.addFlight(sc);
                if (c == 4)
                    RepService.deleteFlight(sc);
                if (c == 5)
                    RepService.viewWaitingList(sc);
                if (c == 6)
                    RepService.flightsByAirport(sc);
                if (c == 7)
                    RepService.replyQuestion(sc);
            } else {
                break;
            }
        }

    }
}