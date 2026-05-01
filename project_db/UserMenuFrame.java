import javax.swing.*;
import java.awt.*;
import java.util.Scanner;

public class UserMenuFrame extends JFrame {

    private String username;

    public UserMenuFrame(String user) {
        this.username = user;

        setTitle("User Menu");
        setSize(400, 400);
        setLayout(new GridLayout(6, 1));

        JButton search = new JButton("Search Flights");
        JButton book = new JButton("Book / Waitlist");
        JButton upcoming = new JButton("View Upcoming");
        JButton past = new JButton("View Past");
        JButton cancel = new JButton("Cancel Ticket");
        JButton question = new JButton("Ask Question");

        add(search);
        add(book);
        add(upcoming);
        add(past);
        add(cancel);
        add(question);

        // ---- SEARCH ----
        search.addActionListener(e -> {
            try {
                UserService.searchFlights(new Scanner(System.in));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // ---- BOOK ----
        book.addActionListener(e -> {
            try {
                UserService.bookOrWaitlist(new Scanner(System.in));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // ---- UPCOMING ----
        upcoming.addActionListener(e -> {
            try {
                String input = JOptionPane.showInputDialog("User ID:");
                int uid = Integer.parseInt(input);
                UserService.viewUpcoming(uid);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // ---- PAST ----
        past.addActionListener(e -> {
            try {
                String input = JOptionPane.showInputDialog("User ID:");
                int uid = Integer.parseInt(input);
                UserService.viewPast(uid);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // ---- CANCEL ----
        cancel.addActionListener(e -> {
            try {
                String input = JOptionPane.showInputDialog("Ticket ID:");
                int tid = Integer.parseInt(input);
                UserService.cancelTicket(tid);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // ---- QUESTION ----
        question.addActionListener(e -> {
            try {
                UserService.askQuestion(new Scanner(System.in));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        setVisible(true);
    }
}