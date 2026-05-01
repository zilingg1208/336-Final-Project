import javax.swing.*;
import java.awt.*;
import java.util.Scanner;

public class RepMenuFrame extends JFrame {

    public RepMenuFrame() {
        setTitle("Rep Menu");
        setSize(400, 400);
        setLayout(new GridLayout(6, 1));

        JButton reserve = new JButton("Make Reservation");
        JButton edit = new JButton("Edit Reservation");
        JButton addFlight = new JButton("Add Flight");
        JButton deleteFlight = new JButton("Delete Flight");
        JButton waiting = new JButton("View Waiting List");

        add(reserve);
        add(edit);
        add(addFlight);
        add(deleteFlight);
        add(waiting);

        reserve.addActionListener(e -> RepService.makeReservationForUser(new Scanner(System.in)));
        edit.addActionListener(e -> RepService.editReservation(new Scanner(System.in)));
        addFlight.addActionListener(e -> RepService.addFlight(new Scanner(System.in)));
        deleteFlight.addActionListener(e -> RepService.deleteFlight(new Scanner(System.in)));
        waiting.addActionListener(e -> RepService.viewWaitingList(new Scanner(System.in)));

        setVisible(true);
    }
}