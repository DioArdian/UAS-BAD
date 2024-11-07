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
import javax.swing.table.DefaultTableModel;
import com.toedter.calendar.JDateChooser;
import java.text.SimpleDateFormat;

public class ScheduleManagement extends JFrame {
    private JTable scheduleTable;
    private JButton btnAddSchedule, btnDeleteSchedule, btnUpdateSchedule;
    private JTextField txtMovieTitle, txtShowTime, txtPrice, txtAvailableSeats;
    private DefaultTableModel tableModel;
    private JDateChooser dateChooser;

    public ScheduleManagement() {
        setTitle("Schedule Management");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // tabell
        tableModel = new DefaultTableModel(new Object[]{"ID", "Movie Title", "Show Time", "Price", "Date", "Seats Available"}, 0);
        scheduleTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(scheduleTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        panel.add(new JLabel("Judul:"));
        txtMovieTitle = new JTextField(15);
        panel.add(txtMovieTitle);

        panel.add(new JLabel("Jam Tayang:"));
        txtShowTime = new JTextField(10);
        panel.add(txtShowTime);

        panel.add(new JLabel("Harga:"));
        txtPrice = new JTextField(10);
        panel.add(txtPrice);

        panel.add(new JLabel("Pilih Tanggal:"));
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd");
        panel.add(dateChooser);

        panel.add(new JLabel("Seats tersedia:"));
        txtAvailableSeats = new JTextField(10);
        txtAvailableSeats.setEditable(true);  
        panel.add(txtAvailableSeats);

        btnAddSchedule = new JButton("Tambah");
        btnAddSchedule.addActionListener(e -> addSchedule());
        panel.add(btnAddSchedule);

        btnDeleteSchedule = new JButton("Delete");
        btnDeleteSchedule.addActionListener(e -> deleteSchedule());
        panel.add(btnDeleteSchedule);

        btnUpdateSchedule = new JButton("Update");
        btnUpdateSchedule.addActionListener(e -> updateSchedule());
        panel.add(btnUpdateSchedule);

        add(panel, BorderLayout.SOUTH);
        
        loadSchedules();

        scheduleTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedRow = scheduleTable.getSelectedRow();
                if (selectedRow != -1) {
                    int scheduleId = (int) scheduleTable.getValueAt(selectedRow, 0);
                    String movieTitle = (String) scheduleTable.getValueAt(selectedRow, 1);
                    String showTime = (String) scheduleTable.getValueAt(selectedRow, 2);
                    double price = (double) scheduleTable.getValueAt(selectedRow, 3);
                    String date = (String) scheduleTable.getValueAt(selectedRow, 4);
                    int seatsAvailable = (int) scheduleTable.getValueAt(selectedRow, 5);
                    
                    txtMovieTitle.setText(movieTitle);
                    txtShowTime.setText(showTime);
                    txtPrice.setText(String.valueOf(price));
                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        dateChooser.setDate(dateFormat.parse(date));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    txtAvailableSeats.setText(String.valueOf(seatsAvailable));
                }
            }
        });
    }

    // ambil scedul dari database
    private void loadSchedules() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM movie_schedule";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int id = rs.getInt("id");
                String movieTitle = rs.getString("movie_title");
                String showTime = rs.getString("show_time");
                double price = rs.getDouble("price");
                String date = rs.getString("date");
                int seatsAvailable = rs.getInt("seats_available"); 
                tableModel.addRow(new Object[]{id, movieTitle, showTime, price, date, seatsAvailable});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // tambah scedul
    private void addSchedule() {
        String movieTitle = txtMovieTitle.getText();
        String showTime = txtShowTime.getText();
        String priceText = txtPrice.getText();
        String seatsAvailableText = txtAvailableSeats.getText();
        java.util.Date selectedDate = dateChooser.getDate();

        if (movieTitle.isEmpty() || showTime.isEmpty() || priceText.isEmpty() || seatsAvailableText.isEmpty() || selectedDate == null) {
            JOptionPane.showMessageDialog(this, "Masukan Data Yang benar.");
            return;
        }

        try {
            double price = Double.parseDouble(priceText);
            int seatsAvailable = Integer.parseInt(seatsAvailableText); 
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = dateFormat.format(selectedDate);

            try (Connection conn = DatabaseConnection.getConnection()) {
                String query = "INSERT INTO movie_schedule (movie_title, show_time, price, date, seats_available) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, movieTitle);
                stmt.setString(2, showTime);
                stmt.setDouble(3, price);
                stmt.setString(4, dateString);
                stmt.setInt(5, seatsAvailable);
                stmt.executeUpdate();

                txtMovieTitle.setText("");
                txtShowTime.setText("");
                txtPrice.setText("");
                txtAvailableSeats.setText(""); 
                dateChooser.setDate(null);

                tableModel.setRowCount(0);
                loadSchedules();
                JOptionPane.showMessageDialog(this, "Schedule added successfully!");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter valid numbers for price and seats.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // delete scedule
    private void deleteSchedule() {
        int selectedRow = scheduleTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a schedule to delete.");
            return;
        }

        int scheduleId = (int) scheduleTable.getValueAt(selectedRow, 0);

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "DELETE FROM movie_schedule WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, scheduleId);
            stmt.executeUpdate();

            // Refresh tabel
            tableModel.setRowCount(0);
            loadSchedules();
            JOptionPane.showMessageDialog(this, "Schedule deleted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //update
    private void updateSchedule() {
        int selectedRow = scheduleTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a schedule to update.");
            return;
        }

        int scheduleId = (int) scheduleTable.getValueAt(selectedRow, 0);
        String movieTitle = txtMovieTitle.getText();
        String showTime = txtShowTime.getText();
        String priceText = txtPrice.getText();
        String seatsAvailableText = txtAvailableSeats.getText(); 
        java.util.Date selectedDate = dateChooser.getDate();

        if (movieTitle.isEmpty() || showTime.isEmpty() || priceText.isEmpty() || seatsAvailableText.isEmpty() || selectedDate == null) {
            JOptionPane.showMessageDialog(this, "masukan seluruh nya gan");
            return;
        }

        try {
            double price = Double.parseDouble(priceText);
            int seatsAvailable = Integer.parseInt(seatsAvailableText);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = dateFormat.format(selectedDate);

            try (Connection conn = DatabaseConnection.getConnection()) {
                String query = "UPDATE movie_schedule SET movie_title = ?, show_time = ?, price = ?, date = ?, seats_available = ? WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, movieTitle);
                stmt.setString(2, showTime);
                stmt.setDouble(3, price);
                stmt.setString(4, dateString);
                stmt.setInt(5, seatsAvailable);
                stmt.setInt(6, scheduleId);
                stmt.executeUpdate();
                
                txtMovieTitle.setText("");
                txtShowTime.setText("");
                txtPrice.setText("");
                txtAvailableSeats.setText("");
                dateChooser.setDate(null);

                tableModel.setRowCount(0);
                loadSchedules();
                JOptionPane.showMessageDialog(this, "Schedule updated successfully!");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter valid numbers for price and seats.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ScheduleManagement().setVisible(true);
        });
    }
}





