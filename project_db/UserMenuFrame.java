import javax.swing.*;
import java.awt.*;

public class UserMenuFrame extends JFrame {

    public UserMenuFrame(String user) {

        setTitle("User Menu");
        setSize(400, 400);
        setLayout(new GridLayout(6, 1));

        JButton search = new JButton("Search Flights");
        JButton book = new JButton("Book / Waitlist");
        JButton upcoming = new JButton("View Upcoming");
        JButton past = new JButton("View Past");
        JButton cancel = new JButton("Cancel Ticket");
        JButton question = new JButton("Ask Question");
        JButton logout = new JButton("Logout");

        add(search);
        add(book);
        add(upcoming);
        add(past);
        add(cancel);
        add(question);
        add(logout);

        // ---- SEARCH ----
        search.addActionListener(e -> {
            try {
                JTextField fromField = new JTextField();
                JTextField toField = new JTextField();
                JTextField depDateField = new JTextField("YYYY-MM-DD");
                JTextField returnDateField = new JTextField("YYYY-MM-DD");

                JTextField maxPriceField = new JTextField("-1");
                JTextField maxStopsField = new JTextField("-1");
                JTextField airlineField = new JTextField("any");
                JTextField depStartField = new JTextField("any");
                JTextField depEndField = new JTextField("any");

                String[] tripOptions = { "One-way", "Round-trip" };
                JComboBox<String> tripBox = new JComboBox<>(tripOptions);

                String[] flexOptions = { "No", "Yes" };
                JComboBox<String> flexBox = new JComboBox<>(flexOptions);

                String[] sortOptions = { "Price", "Takeoff Time", "Landing Time", "Duration" };
                JComboBox<String> sortBox = new JComboBox<>(sortOptions);

                JPanel panel = new JPanel(new GridLayout(0, 1));
                panel.add(new JLabel("From:"));
                panel.add(fromField);
                panel.add(new JLabel("To:"));
                panel.add(toField);
                panel.add(new JLabel("Departure Date:"));
                panel.add(depDateField);
                panel.add(new JLabel("Return Date (if round-trip):"));
                panel.add(returnDateField);

                panel.add(new JLabel("Trip Type:"));
                panel.add(tripBox);
                panel.add(new JLabel("Flexible Dates:"));
                panel.add(flexBox);

                panel.add(new JLabel("Max Price (-1 = no limit):"));
                panel.add(maxPriceField);
                panel.add(new JLabel("Max Stops (-1 = no limit):"));
                panel.add(maxStopsField);
                panel.add(new JLabel("Airline (or 'any'):"));
                panel.add(airlineField);

                panel.add(new JLabel("Earliest Departure (HH:MM or 'any'):"));
                panel.add(depStartField);
                panel.add(new JLabel("Latest Departure (HH:MM or 'any'):"));
                panel.add(depEndField);

                panel.add(new JLabel("Sort By:"));
                panel.add(sortBox);

                int result = JOptionPane.showConfirmDialog(null, panel,
                        "Search Flights", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {

                    int tripType = tripBox.getSelectedIndex() + 1; // 1 or 2
                    int flexible = flexBox.getSelectedIndex(); // 0 or 1
                    int sortChoice = sortBox.getSelectedIndex() + 1; // 1–4

                    double maxPrice = Double.parseDouble(maxPriceField.getText());
                    int maxStops = Integer.parseInt(maxStopsField.getText());

                    UserService.searchFlights(
                            fromField.getText(),
                            toField.getText(),
                            tripType,
                            flexible,
                            depDateField.getText(),
                            maxPrice,
                            maxStops,
                            airlineField.getText(),
                            depStartField.getText(),
                            depEndField.getText(),
                            sortChoice,
                            returnDateField.getText());
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // ---- BOOK ----
        book.addActionListener(e -> {
            try {
                JTextField aidField = new JTextField();
                JTextField fnField = new JTextField();
                JTextField seatField = new JTextField();

                String[] classOptions = { "economy", "business", "first" };
                JComboBox<String> classBox = new JComboBox<>(classOptions);

                JPanel panel = new JPanel(new GridLayout(0, 1));
                panel.add(new JLabel("Airline ID:"));
                panel.add(aidField);

                panel.add(new JLabel("Flight #:"));
                panel.add(fnField);

                panel.add(new JLabel("Class:"));
                panel.add(classBox);

                panel.add(new JLabel("Seat (e.g., 12A):"));
                panel.add(seatField);

                int result = JOptionPane.showConfirmDialog(null, panel,
                        "Book / Waitlist Ticket", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {

                    String aid = aidField.getText();
                    int fn = Integer.parseInt(fnField.getText());
                    String seatClass = (String) classBox.getSelectedItem();
                    String seat = seatField.getText();

                    UserService.bookOrWaitlist(aid, fn, seatClass, seat);

                    JOptionPane.showMessageDialog(null, "✅ Request processed!");

                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // ---- UPCOMING ----
        upcoming.addActionListener(e -> {
            try {
                int uid = ProjectFrame.cid;
                UserService.viewUpcoming(uid);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // ---- PAST ----
        past.addActionListener(e -> {
            try {
                int uid = ProjectFrame.cid;
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
                String q = JOptionPane.showInputDialog("Enter your question:");

                if (q == null || q.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "❌ Question cannot be empty.");
                    return;
                }

                UserService.askQuestion(q.trim());

                JOptionPane.showMessageDialog(null, "✅ Question sent!");

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        logout.addActionListener(e -> {
            try {
                // 🔥 clear session
                ProjectFrame.user = "";
                ProjectFrame.cid = -1;
                ProjectFrame.userLoggedin = false;

                // 🔥 go back to login screen
                new ProjectFrame().initialize();

                // 🔥 close current window
                dispose();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        setVisible(true);
    }
}
