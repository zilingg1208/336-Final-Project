import java.sql.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class ProjectFrame extends JFrame {
    // GUI related-variables:
    final private Font mainFont = new Font("Lucida Sans", Font.BOLD, 18);
    JTextField tfuser, tfpasswd;
    JLabel msg;

    // Database related variables:
    public static Connection con = null;
    public static Statement stmt = null;
    public static boolean userLoggedin = false;
    public static String user = "";
    public static int cid = -1;

    public void initialize() throws Exception {
        // inputPanel: ------------------------------------
        // -- inputPanel components
        JLabel lbuser = new JLabel("Username");
        lbuser.setFont(mainFont);

        tfuser = new JTextField();
        tfuser.setFont(mainFont);

        JLabel lbpasswd = new JLabel("Password");
        lbpasswd.setFont(mainFont);

        tfpasswd = new JTextField();
        tfpasswd.setFont(mainFont);

        // -- create inputPanel and add its components
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2, 2, 5, 5));
        inputPanel.setOpaque(false); // so that form color is seen as background

        inputPanel.add(lbuser);
        inputPanel.add(tfuser);
        inputPanel.add(lbpasswd);
        inputPanel.add(tfpasswd);

        // msg : ------------------------------------
        msg = new JLabel(); // text will be added later (it is global variable)
        msg.setFont(mainFont);

        // buttonPanel: ------------------------------------
        // -- buttonPanel components
        JButton btnAdd = new JButton("Add User");
        btnAdd.setFont(mainFont);
        // add a listener
        btnAdd.addActionListener(e -> {
            try {
                Connection conn = ProjectFrame.con;

                // 🔥 ask for full info
                String name = JOptionPane.showInputDialog("Enter Name:");
                String email = JOptionPane.showInputDialog("Enter Email:");
                String username = JOptionPane.showInputDialog("Enter Username:");
                String password = JOptionPane.showInputDialog("Enter Password:");

                // ❌ validation
                if (name == null || email == null || username == null || password == null ||
                        name.trim().isEmpty() || email.trim().isEmpty() ||
                        username.trim().isEmpty() || password.trim().isEmpty()) {

                    msg.setText("All fields required");
                    return;
                }

                // 🔍 check duplicate username OR email
                String checkSQL = "SELECT * FROM Users WHERE username=? OR email=?";
                PreparedStatement psCheck = conn.prepareStatement(checkSQL);
                psCheck.setString(1, username);
                psCheck.setString(2, email);

                ResultSet rs = psCheck.executeQuery();

                if (rs.next()) {
                    msg.setText("Username or Email already exists");
                    return;
                }

                // ✅ insert user
                String sql = "INSERT INTO Users (name, email, username, password) VALUES (?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);

                ps.setString(1, name);
                ps.setString(2, email);
                ps.setString(3, username);
                ps.setString(4, password);

                ps.executeUpdate();

                msg.setText("User created successfully!");

            } catch (Exception ex) {
                ex.printStackTrace();
                msg.setText("Error creating user");
            }
        });

        JButton btnLogin = new JButton("Login");
        btnLogin.setFont(mainFont);
        // add a listener
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    String username = tfuser.getText().trim();
                    String passwd = tfpasswd.getText().trim();

                    if (username.isEmpty() || passwd.isEmpty()) {
                        msg.setText("Please enter username and password");
                        return;
                    }

                    Connection conn = ProjectFrame.con;

                    // =========================
                    // 1. CHECK USERS (CUSTOMER)
                    // =========================
                    String userSQL = "SELECT * FROM Users WHERE username=? AND password=?";
                    PreparedStatement ps1 = conn.prepareStatement(userSQL);
                    ps1.setString(1, username);
                    ps1.setString(2, passwd);

                    ResultSet rs1 = ps1.executeQuery();

                    if (rs1.next()) {
                        // store logged-in user info
                        ProjectFrame.user = username;
                        ProjectFrame.cid = rs1.getInt("cid");

                        System.out.println("DEBUG: Logged in as customer, cid = " + ProjectFrame.cid);

                        msg.setText("Welcome " + username);

                        new UserMenuFrame(user);
                        dispose();
                        return;
                    }

                    // =========================
                    // 2. CHECK EMPLOYEES (ADMIN / REP)
                    // =========================
                    String empSQL = "SELECT * FROM Employees WHERE username=? AND password=?";
                    PreparedStatement ps2 = conn.prepareStatement(empSQL);
                    ps2.setString(1, username);
                    ps2.setString(2, passwd);

                    ResultSet rs2 = ps2.executeQuery();

                    if (rs2.next()) {
                        String role = rs2.getString("role");

                        System.out.println("DEBUG: Logged in as " + role);

                        msg.setText("Welcome " + role + " " + username);

                        if (role.equalsIgnoreCase("admin")) {
                            new AdminMenuFrame();
                        } else if (role.equalsIgnoreCase("rep")) {
                            new RepMenuFrame();
                        }

                        dispose();
                        return;
                    }

                    // =========================
                    // 3. LOGIN FAILED
                    // =========================
                    msg.setText("Invalid username or password");

                } catch (Exception ex) {
                    ex.printStackTrace();
                    msg.setText("Database error");
                }
            }
        });

        JButton btnClear = new JButton("Clear");
        btnClear.setFont(mainFont);
        // add a listener
        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // clear the text fields
                tfuser.setText("");
                tfpasswd.setText("");
                msg.setText("");
            }
        });

        // -- create buttonPanel and add its components
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3, 5, 5));
        buttonPanel.setOpaque(false);
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnClear);

        // mainPanel: ------------------------------------
        // -------------- create main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(230, 140, 140));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        // -- add mainPanel's components
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(msg, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // -- Add the mainPanel to our JForm and set up basic attributes
        this.add(mainPanel);

        this.setTitle("Login Page");
        this.setSize(500, 300);
        this.setMinimumSize(new Dimension(300, 200));
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        // Initialize the connection to the database
        String url = "jdbc:mysql://localhost:3306/testproject";
        String user = "testuser";
        String password = "abc123";
        try {
            con = DriverManager.getConnection(url, user, password);
            stmt = con.createStatement();
        } catch (SQLException e) {
            System.out.println("Unable to create a connection to the database");
            e.printStackTrace();
            System.exit(0);
        }
        ProjectFrame myFrame = new ProjectFrame();
        myFrame.initialize();
    }
}
