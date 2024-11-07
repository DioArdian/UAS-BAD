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
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class PurchasedTickets extends JFrame {
    private JTable ticketsTable;
    private JScrollPane scrollPane;

    public PurchasedTickets() {
        setTitle("Purchased Tickets");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // tabel tiket sudah di beli
        String[] columns = {"ID", "Movie Title", "Show Time", "Quantity", "Total Price", "Purchase Date"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);

        Object[][] ticketData = getPurchasedTicketData();
        for (Object[] row : ticketData) {
            tableModel.addRow(row);
        }

        ticketsTable = new JTable(tableModel);
        scrollPane = new JScrollPane(ticketsTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    private Object[][] getPurchasedTicketData() {
    Object[][] data = new Object[0][0];

    String query = "SELECT id, movie_title, show_time, ticket_quantity, total_price, transaction_date " +
                   "FROM ticket_sales WHERE user_id = ?";
    
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query, 
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) { 
        stmt.setInt(1, 1);  
        ResultSet rs = stmt.executeQuery();

        int rowCount = 0;
        while (rs.next()) {
            rowCount++;
        }

        rs.beforeFirst();
        data = new Object[rowCount][6];
        int rowIndex = 0;
        while (rs.next()) {
            data[rowIndex][0] = rs.getInt("id");
            data[rowIndex][1] = rs.getString("movie_title");
            data[rowIndex][2] = rs.getString("show_time");
            data[rowIndex][3] = rs.getInt("ticket_quantity");
            data[rowIndex][4] = rs.getDouble("total_price");
            data[rowIndex][5] = rs.getString("transaction_date");
            rowIndex++;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return data;
}


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PurchasedTickets().setVisible(true);
        });
    }
}

