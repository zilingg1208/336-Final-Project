import javax.swing.*;
import java.awt.*;

public class AdminMenuFrame extends JFrame {

    public AdminMenuFrame() {

        setTitle("Admin Menu");
        setSize(500, 500);
        setLayout(new GridLayout(0, 2, 10, 10));
        setLocationRelativeTo(null);

        JButton addRep = new JButton("Add Rep");
        JButton editRep = new JButton("Edit Rep");
        JButton deleteRep = new JButton("Delete Rep");

        JButton addUser = new JButton("Add User");
        JButton editUser = new JButton("Edit User");
        JButton deleteUser = new JButton("Delete User");

        JButton sales = new JButton("Monthly Sales");
        JButton byFlight = new JButton("Reservations by Flight");
        JButton byUser = new JButton("Reservations by User");

        JButton revFlight = new JButton("Revenue by Flight");
        JButton revUser = new JButton("Revenue by User");
        JButton topUser = new JButton("Top User");

        JButton activeFlights = new JButton("Most Active Flights");
        JButton revAirline = new JButton("Revenue by Airline");

        JButton logout = new JButton("Logout");

        add(addRep);
        add(editRep);
        add(deleteRep);
        add(addUser);
        add(editUser);
        add(deleteUser);
        add(sales);
        add(byFlight);
        add(byUser);
        add(revFlight);
        add(revUser);
        add(topUser);
        add(activeFlights);
        add(revAirline);
        add(logout);

        // ---------------- ADD REP ----------------
        addRep.addActionListener(e -> {
            JTextField user = new JTextField();
            JTextField pass = new JTextField();

            JPanel p = new JPanel(new GridLayout(0, 1));
            p.add(new JLabel("Username:"));
            p.add(user);
            p.add(new JLabel("Password:"));
            p.add(pass);

            if (JOptionPane.showConfirmDialog(null, p, "Add Rep",
                    JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {

                AdminService.addRepresentative(
                        user.getText(),
                        pass.getText());
            }
        });

        // ---------------- EDIT REP ----------------
        editRep.addActionListener(e -> {
            JTextField oldUser = new JTextField();
            JTextField newUser = new JTextField();
            JTextField newPass = new JTextField();

            JPanel p = new JPanel(new GridLayout(0, 1));
            p.add(new JLabel("Old Username:"));
            p.add(oldUser);
            p.add(new JLabel("New Username:"));
            p.add(newUser);
            p.add(new JLabel("New Password:"));
            p.add(newPass);

            if (JOptionPane.showConfirmDialog(null, p, "Edit Rep",
                    JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {

                AdminService.editRepresentative(
                        oldUser.getText(),
                        newUser.getText(),
                        newPass.getText());
            }
        });

        // ---------------- DELETE REP ----------------
        deleteRep.addActionListener(e -> {
            String user = JOptionPane.showInputDialog("Username:");

            if (user != null) {
                AdminService.deleteRepresentative(user);
            }
        });

        // ---------------- ADD USER ----------------
        addUser.addActionListener(e -> {
            JTextField name = new JTextField();
            JTextField email = new JTextField();
            JTextField user = new JTextField();
            JTextField pass = new JTextField();

            JPanel p = new JPanel(new GridLayout(0, 1));
            p.add(new JLabel("Name:"));
            p.add(name);
            p.add(new JLabel("Email:"));
            p.add(email);
            p.add(new JLabel("Username:"));
            p.add(user);
            p.add(new JLabel("Password:"));
            p.add(pass);

            if (JOptionPane.showConfirmDialog(null, p, "Add User",
                    JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {

                AdminService.addUser(
                        name.getText(),
                        email.getText(),
                        user.getText(),
                        pass.getText());
            }
        });

        // ---------------- EDIT USER ----------------
        editUser.addActionListener(e -> {
            JTextField cid = new JTextField();
            JTextField name = new JTextField();
            JTextField email = new JTextField();
            JTextField user = new JTextField();
            JTextField pass = new JTextField();

            JPanel p = new JPanel(new GridLayout(0, 1));
            p.add(new JLabel("CID:"));
            p.add(cid);
            p.add(new JLabel("Name:"));
            p.add(name);
            p.add(new JLabel("Email:"));
            p.add(email);
            p.add(new JLabel("Username:"));
            p.add(user);
            p.add(new JLabel("Password:"));
            p.add(pass);

            if (JOptionPane.showConfirmDialog(null, p, "Edit User",
                    JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {

                AdminService.editUser(
                        Integer.parseInt(cid.getText()),
                        name.getText(),
                        email.getText(),
                        user.getText(),
                        pass.getText());
            }
        });

        // ---------------- DELETE USER ----------------
        deleteUser.addActionListener(e -> {
            try {
                String input = JOptionPane.showInputDialog("User ID:");

                // user pressed cancel
                if (input == null)
                    return;

                input = input.trim();

                // empty input
                if (input.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "❌ ID cannot be empty");
                    return;
                }

                int cid = Integer.parseInt(input);

                AdminService.deleteUser(cid);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "❌ Invalid ID (must be a number)");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // ---------------- MONTHLY SALES ----------------
        sales.addActionListener(e -> {
            String m = JOptionPane.showInputDialog("Month (1-12):");

            if (m != null) {
                AdminService.monthlySales(Integer.parseInt(m));
            }
        });

        // ---------------- RES BY FLIGHT ----------------
        byFlight.addActionListener(e -> {
            JTextField aid = new JTextField();
            JTextField fn = new JTextField();

            JPanel p = new JPanel(new GridLayout(0, 1));
            p.add(new JLabel("Airline ID:"));
            p.add(aid);
            p.add(new JLabel("Flight #:"));
            p.add(fn);

            if (JOptionPane.showConfirmDialog(null, p, "Flight Reservations",
                    JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {

                AdminService.reservationsByFlight(
                        aid.getText(),
                        Integer.parseInt(fn.getText()));
            }
        });

        // ---------------- RES BY USER ----------------
        byUser.addActionListener(e -> {
            try {
                String input = JOptionPane.showInputDialog("User ID:");

                if (input == null || input.trim().isEmpty())
                    return;

                int cid = Integer.parseInt(input.trim());

                AdminService.reservationsByUser(cid);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "❌ Invalid ID (must be a number)");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        // ---------------- REPORTS ----------------
        revFlight.addActionListener(e -> AdminService.revenueByFlight());
        revUser.addActionListener(e -> AdminService.revenueByUser());
        topUser.addActionListener(e -> AdminService.topUser());
        activeFlights.addActionListener(e -> AdminService.mostActiveFlights());
        revAirline.addActionListener(e -> AdminService.revenueByAirline());

        // ---------------- LOGOUT ----------------
        logout.addActionListener(e -> {
            new ProjectFrame();
            dispose();
        });

        setVisible(true);
    }
}
