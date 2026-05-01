import javax.swing.*;
import java.awt.*;
import java.util.Scanner;

public class AdminMenuFrame extends JFrame {

    public AdminMenuFrame() {
        setTitle("Admin Menu");
        setSize(400, 400);
        setLayout(new GridLayout(6, 1));

        JButton btnAdd = new JButton("Add Customer");
        JButton btnDelete = new JButton("Delete Customer");
        JButton btnSales = new JButton("Monthly Sales");
        JButton btnRevenue = new JButton("Revenue by Customer");
        JButton btnTop = new JButton("Top Customer");
        JButton btnBack = new JButton("Back");

        add(btnAdd);
        add(btnDelete);
        add(btnSales);
        add(btnRevenue);
        add(btnTop);
        add(btnBack);

        btnAdd.addActionListener(e -> AdminService.addUser(new Scanner(System.in)));
        btnDelete.addActionListener(e -> AdminService.deleteUser(new Scanner(System.in)));
        btnSales.addActionListener(e -> AdminService.monthlySales(new Scanner(System.in)));
        btnRevenue.addActionListener(e -> AdminService.revenueByUser());
        btnTop.addActionListener(e -> AdminService.topUser());

        btnBack.addActionListener(e -> dispose());

        setVisible(true);
    }
}