/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
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

public class LoginForm extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnRegister;

    public LoginForm() {
    //frame
        setTitle("Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        // Username
        add(new JLabel("Username:"));
        txtUsername = new JTextField(20);
        add(txtUsername);

        // Password
        add(new JLabel("Password:"));
        txtPassword = new JPasswordField(20);
        add(txtPassword);

        // tombol Login 
        btnLogin = new JButton("Login");
        btnLogin.addActionListener(e -> login());
        add(btnLogin);

        // tombol Register 
        btnRegister = new JButton("Register");
        btnRegister.addActionListener(e -> openRegistrationForm());
        add(btnRegister);
    }

    private void login() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT role FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");
                if ("admin".equalsIgnoreCase(role)) {
                    new AdminDashboard().setVisible(true);
                } else {
                    new UserDashboard().setVisible(true);
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "salah lur username atau passwordnya.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void openRegistrationForm() {
        // buka form regis
        new RegistrationForm().setVisible(true);
        dispose(); 
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });
    }
}
