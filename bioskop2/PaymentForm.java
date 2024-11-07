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

public class PaymentForm extends JFrame {
    private JLabel lblTotalPrice;
    private JTextField tfPaymentAmount;
    private JButton btnPay;
    private JRadioButton rbtnQris, rbtnBankTransfer, rbtnOvo, rbtnGoPay;
    private ButtonGroup paymentGroup;

    public PaymentForm(double totalPrice) {
        setTitle("Payment Form");
        setSize(350, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());
        
        lblTotalPrice = new JLabel("Total Price: " + totalPrice);
        add(lblTotalPrice);

        // Radio buttons untuk QRIS, Bank Transfer, OVO, GoPay
        rbtnQris = new JRadioButton("QRIS");
        rbtnBankTransfer = new JRadioButton("Bank Transfer");
        rbtnOvo = new JRadioButton("OVO");
        rbtnGoPay = new JRadioButton("GoPay");
        paymentGroup = new ButtonGroup();
        paymentGroup.add(rbtnQris);
        paymentGroup.add(rbtnBankTransfer);
        paymentGroup.add(rbtnOvo);
        paymentGroup.add(rbtnGoPay);

        // default qris
        rbtnQris.setSelected(true);

        add(rbtnQris);
        add(rbtnBankTransfer);
        add(rbtnOvo);
        add(rbtnGoPay);

        // untuk entry jumlah pembayaran
        add(new JLabel("Masukan Total Pembayaran"));
        tfPaymentAmount = new JTextField(10);
        add(tfPaymentAmount);

        // tombol confirmasinya
        btnPay = new JButton("Pay Now");
        btnPay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processPayment(totalPrice);
            }
        });
        add(btnPay);
    }

    private void processPayment(double totalPrice) {
        double paymentAmount = Double.parseDouble(tfPaymentAmount.getText());
        
        String paymentMethod = "QRIS";
        if (rbtnBankTransfer.isSelected()) {
            paymentMethod = "Bank Transfer";
        } else if (rbtnOvo.isSelected()) {
            paymentMethod = "OVO";
        } else if (rbtnGoPay.isSelected()) {
            paymentMethod = "GoPay";
        }
//validasi pembayaran
        if (paymentAmount >= totalPrice) {
            JOptionPane.showMessageDialog(this, "Payment Successful! Method: " + paymentMethod);
            dispose(); 
        } else {
            JOptionPane.showMessageDialog(this, "Insufficient payment. Try again.");
        }
    }

    public static void showPaymentForm(double totalPrice) {
        new PaymentForm(totalPrice).setVisible(true);
    }
}




