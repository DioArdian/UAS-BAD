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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TicketPurchase extends JFrame {
    private JComboBox<String> movieComboBox;
    private JComboBox<String> timeComboBox;
    private JComboBox<String> dateComboBox; 
    private JTextField quantityField;
    private JButton btnBuyTicket;

    public TicketPurchase() {
 
        setTitle("Ticket Purchase");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());

 
        movieComboBox = new JComboBox<>();
        movieComboBox.setPreferredSize(new Dimension(300, 30));
        loadMovies();
        add(new JLabel("Pilih Film:"));
        add(movieComboBox);

        timeComboBox = new JComboBox<>();
        timeComboBox.setPreferredSize(new Dimension(300, 30));
        add(new JLabel("Pilih Jam Tayang"));
        add(timeComboBox);

        dateComboBox = new JComboBox<>();
        dateComboBox.setPreferredSize(new Dimension(300, 30));
        add(new JLabel("Pilih Tanggal"));
        add(dateComboBox);

        add(new JLabel("Jumlah Tiket"));
        quantityField = new JTextField(5);
        add(quantityField);
        
        btnBuyTicket = new JButton("Beli");
        btnBuyTicket.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                purchaseTicket();
            }
        });
        add(btnBuyTicket);
    }

    private void loadMovies() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT movie_title FROM movie_schedule GROUP BY movie_title";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String movieTitle = rs.getString("movie_title");
                movieComboBox.addItem(movieTitle);
            }

            movieComboBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    loadShowTimes((String) movieComboBox.getSelectedItem());
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ambil dari data dari judul
    private void loadShowTimes(String movieTitle) {
        timeComboBox.removeAllItems();
        dateComboBox.removeAllItems(); 
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT show_time, date FROM movie_schedule WHERE movie_title = ? GROUP BY date, show_time";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, movieTitle);
            ResultSet rs = stmt.executeQuery();


            List<String> availableDates = new ArrayList<>();
            while (rs.next()) {
                String showTime = rs.getString("show_time");
                timeComboBox.addItem(showTime);  

                String date = rs.getString("date");
                if (!availableDates.contains(date)) {
                    availableDates.add(date); 
                }
            }

            for (String date : availableDates) {
                dateComboBox.addItem(date);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

private void purchaseTicket() {
    String selectedMovie = (String) movieComboBox.getSelectedItem();
    String selectedTime = (String) timeComboBox.getSelectedItem();
    String selectedDate = (String) dateComboBox.getSelectedItem();
    String quantityText = quantityField.getText();

    if (selectedMovie != null && selectedTime != null && selectedDate != null && !quantityText.isEmpty()) {
        int ticketQuantity = Integer.parseInt(quantityText);
        int availableSeats = getAvailableSeats(selectedMovie, selectedTime, selectedDate); 

        if (availableSeats >= ticketQuantity) {
            double ticketPrice = getMoviePrice(selectedMovie, selectedTime);
            double totalPrice = ticketPrice * ticketQuantity;
            saveTransaction(selectedMovie, selectedTime, selectedDate, ticketQuantity, totalPrice);
            updateAvailableSeats(selectedMovie, selectedTime, selectedDate, availableSeats - ticketQuantity);
            PaymentForm.showPaymentForm(totalPrice);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Not enough seats available!");
        }
    } else {
        JOptionPane.showMessageDialog(this, "Please select a movie, show time, date, and enter quantity.");
    }
}


    // ambil harga database
    private double getMoviePrice(String movieTitle, String showTime) {
        double price = 0;
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT price FROM movie_schedule WHERE movie_title = ? AND show_time = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, movieTitle);
            stmt.setString(2, showTime);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                price = rs.getDouble("price");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return price;
    }

    // ambil seat tersedia
    private int getAvailableSeats(String movieTitle, String showTime, String date) {
        int availableSeats = 0;
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT seats_available FROM movie_schedule WHERE movie_title = ? AND show_time = ? AND date = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, movieTitle);
            stmt.setString(2, showTime);
            stmt.setString(3, date);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                availableSeats = rs.getInt("seats_available");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return availableSeats;
    }

    // save
    private void saveTransaction(String movieTitle, String showTime, String selectedDate, int ticketQuantity, double totalPrice) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO ticket_sales (user_id, movie_title, show_time, ticket_quantity, total_price, transaction_date) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, 1); // User ID (replace with actual user ID)
            stmt.setString(2, movieTitle); // Movie title
            stmt.setString(3, showTime); // Show time
            stmt.setInt(4, ticketQuantity); // Ticket quantity
            stmt.setDouble(5, totalPrice); // Total price

            stmt.setString(6, selectedDate); // Transaction date

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //update
    private void updateAvailableSeats(String movieTitle, String showTime, String date, int newAvailableSeats) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "UPDATE movie_schedule SET seats_available = ? WHERE movie_title = ? AND show_time = ? AND date = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, newAvailableSeats);
            stmt.setString(2, movieTitle);
            stmt.setString(3, showTime);
            stmt.setString(4, date);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new TicketPurchase().setVisible(true);
    }
}







