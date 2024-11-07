/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bioskop2;

/**
 *
 * @author dioar
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UserDashboard extends JFrame {
    private JButton btnViewSchedule, btnBuyTicket, btnViewPurchasedTickets;

    public UserDashboard() {
        setTitle("User Dashboard");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
//tombol lihat jadwal
        btnViewSchedule = new JButton("Lihat Film");
        btnViewSchedule.addActionListener(e -> openFilmSchedule());
        add(btnViewSchedule);

        // tombol beli
        btnBuyTicket = new JButton("Beli Tiket");
        btnBuyTicket.addActionListener(e -> openTicketPurchase());
        add(btnBuyTicket);
//lihat tiket beli
        btnViewPurchasedTickets = new JButton("Tiket anda");
        btnViewPurchasedTickets.addActionListener(e -> viewPurchasedTickets());
        add(btnViewPurchasedTickets);
    }

    private void openFilmSchedule() {
        new FilmSchedule().setVisible(true);
    }

    private void openTicketPurchase() {
        new TicketPurchase().setVisible(true);
    }

    private void viewPurchasedTickets() {
        new PurchasedTickets().setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new UserDashboard().setVisible(true);
        });
    }
}

