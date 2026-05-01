import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            try {
                System.out.println("\n1.Customer 2.Admin 3.Rep 4.Exit");
                int role = sc.nextInt();

                // ================= CUSTOMER =================
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
                        } else {
                            System.out.println("Invalid option.");
                        }
                    }
                }

                // ================= ADMIN =================
                else if (role == 2) {
                    while (true) {
                        System.out.println("\n--- Admin Menu ---");
                        System.out.println("1.Add Customer");
                        System.out.println("2.Delete Customer");
                        System.out.println("3.Monthly Sales");
                        System.out.println("4.Res by Flight");
                        System.out.println("5.Res by Customer");
                        System.out.println("6.Revenue Flight");
                        System.out.println("7.Revenue Customer");
                        System.out.println("8.Top Customer");
                        System.out.println("9.Active Flights");
                        System.out.println("10.Back");

                        int c = sc.nextInt();

                        if (c == 1)
                            AdminService.addCustomer(sc);
                        else if (c == 2)
                            AdminService.deleteCustomer(sc);
                        else if (c == 3)
                            AdminService.monthlySales(sc);
                        else if (c == 4)
                            AdminService.reservationsByFlight(sc);
                        else if (c == 5)
                            AdminService.reservationsByCustomer(sc);
                        else if (c == 6)
                            AdminService.revenueByFlight();
                        else if (c == 7)
                            AdminService.revenueByCustomer();
                        else if (c == 8)
                            AdminService.topCustomer();
                        else if (c == 9)
                            AdminService.mostActiveFlights();
                        else if (c == 10)
                            break;
                        else
                            System.out.println("Invalid option.");
                    }
                }

                // ================= REP =================
                else if (role == 3) {
                    while (true) {
                        System.out.println("\n--- Rep Menu ---");
                        System.out.println("1.Make Reservation");
                        System.out.println("2.Edit Reservation");
                        System.out.println("3.Add Flight");
                        System.out.println("4.Delete Flight");
                        System.out.println("5.Waiting List");
                        System.out.println("6.Flights by Airport");
                        System.out.println("7.Reply Question");
                        System.out.println("8.Back");

                        int c = sc.nextInt();

                        if (c == 1)
                            RepService.makeReservationForUser(sc);
                        else if (c == 2)
                            RepService.editReservation(sc);
                        else if (c == 3)
                            RepService.addFlight(sc);
                        else if (c == 4)
                            RepService.deleteFlight(sc);
                        else if (c == 5)
                            RepService.viewWaitingList(sc);
                        else if (c == 6)
                            RepService.flightsByAirport(sc);
                        else if (c == 7)
                            RepService.replyQuestion(sc);
                        else if (c == 8)
                            break;
                        else
                            System.out.println("Invalid option.");
                    }
                }

                // ================= EXIT =================
                else if (role == 4) {
                    System.out.println("Goodbye!");
                    break;
                } else {
                    System.out.println("Invalid role.");
                }

            } catch (Exception e) {
                System.out.println("Invalid input. Try again.");
                sc.nextLine(); // clear bad input
            }
        }

        sc.close();
    }
}
