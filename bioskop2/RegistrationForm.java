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
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class RegistrationForm extends JFrame implements ActionListener {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<String> cmbRole;
    private JButton btnRegister, btnBack;

    public RegistrationForm() {
        setTitle("Registration Form");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

//entry untuk username
        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setBounds(20, 20, 80, 25);
        add(lblUsername);

        txtUsername = new JTextField();
        txtUsername.setBounds(100, 20, 165, 25);
        add(txtUsername);

        // entry pass
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(20, 50, 80, 25);
        add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(100, 50, 165, 25);
        add(txtPassword);

        // Role box
        JLabel lblRole = new JLabel("Role:");
        lblRole.setBounds(20, 80, 80, 25);
        add(lblRole);

        cmbRole = new JComboBox<>(new String[]{"admin", "user"});
        cmbRole.setBounds(100, 80, 165, 25);
        add(cmbRole);

        // tombol daftar
        btnRegister = new JButton("Daftar");
        btnRegister.setBounds(20, 120, 90, 25);
        add(btnRegister);
        btnRegister.addActionListener(this);

        // tombol kembali
        btnBack = new JButton("Back");
        btnBack.setBounds(180, 120, 90, 25);
        add(btnBack);
        btnBack.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnRegister) {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());
            String role = (String) cmbRole.getSelectedItem();

            if (registerUser(username, password, role)) {
                JOptionPane.showMessageDialog(this, "Pendaftaran Berhasil Lurr!!!");
                openLoginForm(); 
            } else {
                JOptionPane.showMessageDialog(this, "Pendaftaran Gagal, Coba Lagi gann!");
            }
        } else if (e.getSource() == btnBack) {
            openLoginForm(); 
        }
    }

    // Method register
    private boolean registerUser(String username, String password, String role) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private void openLoginForm() {
        new LoginForm().setVisible(true);
        this.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new RegistrationForm().setVisible(true);
        });
    }
}
