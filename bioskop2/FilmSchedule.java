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
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class FilmSchedule extends JFrame {
    private JTable scheduleTable;
    private DefaultTableModel tableModel;

    public FilmSchedule() {
       
        setTitle("Film Schedules");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // buat tabel
        tableModel = new DefaultTableModel(new Object[]{"Judul", "Jam Tayang", "Harga", "Tanggal", "Seats Tersedia"}, 0);
        scheduleTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(scheduleTable);
        add(scrollPane, BorderLayout.CENTER);

        // ambil dari data base
        loadSchedules();
    }

    // metod ke databae
    private void loadSchedules() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM movie_schedule";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            tableModel.setRowCount(0);
            while (rs.next()) {
                String movieTitle = rs.getString("movie_title");
                String showTime = rs.getString("show_time");
                double price = rs.getDouble("price");
                String date = rs.getString("date");
                int seatsAvailable = rs.getInt("seats_available");
                tableModel.addRow(new Object[]{movieTitle, showTime, price, date, seatsAvailable});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FilmSchedule().setVisible(true);
        });
    }
}


