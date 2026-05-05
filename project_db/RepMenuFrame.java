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
        JButton logout = new JButton("Logout");

        JButton addAirline = new JButton("Add Airline");
        JButton deleteAirline = new JButton("Delete Airline");

        JButton addAircraft = new JButton("Add Aircraft");
        JButton editAircraft = new JButton("Edit Aircraft");
        JButton deleteAircraft = new JButton("Delete Aircraft");

        JButton addAirport = new JButton("Add Airport");
        JButton editAirport = new JButton("Edit Airport");
        JButton deleteAirport = new JButton("Delete Airport");

        JButton editFlight = new JButton("Edit Flight");

        add(reserve);
        add(waiting);
        add(edit);

        add(addFlight);
        add(editFlight);
        add(deleteFlight);

        add(addAirline);
        add(deleteAirline);

        add(addAircraft);
        add(editAircraft);
        add(deleteAircraft);

        add(addAirport);
        add(editAirport);
        add(deleteAirport);

        add(logout);

        reserve.addActionListener(e -> RepService.makeReservationForUser(new Scanner(System.in)));
        edit.addActionListener(e -> RepService.editReservation(new Scanner(System.in)));
        addFlight.addActionListener(e -> RepService.addFlight(new Scanner(System.in)));
        deleteFlight.addActionListener(e -> RepService.deleteFlight(new Scanner(System.in)));
        waiting.addActionListener(e -> RepService.viewWaitingList(new Scanner(System.in)));
        addAircraft.addActionListener(e -> RepService.addAircraft(new Scanner(System.in)));
        editAircraft.addActionListener(e -> RepService.editAircraft(new Scanner(System.in)));
        deleteAircraft.addActionListener(e -> RepService.deleteAircraft(new Scanner(System.in)));

        addAirport.addActionListener(e -> RepService.addAirport(new Scanner(System.in)));
        editAirport.addActionListener(e -> RepService.editAirport(new Scanner(System.in)));
        deleteAirport.addActionListener(e -> RepService.deleteAirport(new Scanner(System.in)));

        addAirline.addActionListener(e -> RepService.addAirline(new Scanner(System.in)));

        deleteAirline.addActionListener(e -> RepService.deleteAirline(new Scanner(System.in)));

        editFlight.addActionListener(e -> RepService.editFlight(new Scanner(System.in)));

        logout.addActionListener(e -> dispose());
        setVisible(true);
    }
}
