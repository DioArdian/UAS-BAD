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
import java.sql.*;

public class AdminDashboard extends JFrame {
    private JButton btnManageSchedule;
    private JLabel lblTotalSales, lblTotalRevenue;
    
    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(200, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        // tombol ATUR JADAWAL
        btnManageSchedule = new JButton("ATUR JADWAL");
        btnManageSchedule.addActionListener(e -> openScheduleManagement());
        add(btnManageSchedule);
        //untuk pendapatan
        lblTotalSales = new JLabel("TOTAL TIKET TERJUAL: ");
        lblTotalRevenue = new JLabel("TOTAL PENDAPATAN: ");
        add(lblTotalSales);
        add(lblTotalRevenue);
        updateDashboard();
    }

    private void openScheduleManagement() {
        new ScheduleManagement().setVisible(true);
    }

    // Method untuk update total
    private void updateDashboard() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String salesQuery = "SELECT SUM(ticket_quantity) AS total_sales FROM ticket_sales";
            Statement stmt = conn.createStatement();
            ResultSet rsSales = stmt.executeQuery(salesQuery);
            if (rsSales.next()) {
                int totalSales = rsSales.getInt("total_sales");
                lblTotalSales.setText("TOTAL TIKET TERJUAL: " + totalSales);
            }
            String revenueQuery = "SELECT SUM(total_price) AS total_revenue FROM ticket_sales";
            ResultSet rsRevenue = stmt.executeQuery(revenueQuery);
            if (rsRevenue.next()) {
                double totalRevenue = rsRevenue.getDouble("total_revenue");
                lblTotalRevenue.setText("TOTAL PENDAPATAN: " + totalRevenue);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AdminDashboard().setVisible(true);
        });
    }
}



