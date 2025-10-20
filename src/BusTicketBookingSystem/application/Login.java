package BusTicketBookingSystem.application;

import BusTicketBookingSystem.application.Conn;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Login extends JFrame implements ActionListener {
    JTextField usernameField;
    JPasswordField passwordField;
    JComboBox<String> roleBox;
    JButton loginButton, signupButton;

    public Login() {
        setTitle("Welcome to Bus Ticket Booking System - Login");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel titleLabel = new JLabel("Welcome to Bus Ticket Booking System");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 36));
        titleLabel.setBounds(360, 50, 600, 50);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel);

        JPanel panel = new JPanel();
        panel.setBounds(400, 150, 500, 400);
        panel.setLayout(null);
        panel.setBackground(new Color(43, 47, 51));
        panel.setBorder(BorderFactory.createLineBorder(new Color(100, 149, 237), 3, true));
        add(panel);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setBounds(100, 80, 100, 30);
        panel.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(200, 80, 200, 35);
        usernameField.setFont(new Font("Tahoma", Font.PLAIN, 16));
        usernameField.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        passwordLabel.setBounds(100, 130, 100, 30);
        passwordLabel.setForeground(Color.WHITE);
        panel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(200, 130, 200, 35);
        passwordField.setFont(new Font("Tahoma", Font.PLAIN, 16));
        passwordField.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        panel.add(passwordField);

        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        roleLabel.setBounds(100, 180, 100, 30);
        roleLabel.setForeground(Color.WHITE);
        panel.add(roleLabel);

        String[] roles = {"Admin", "Passenger"};
        roleBox = new JComboBox<>(roles);
        roleBox.setBounds(200, 180, 200, 35);
        roleBox.setFont(new Font("Tahoma", Font.PLAIN, 16));
        panel.add(roleBox);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBounds(100, 250, 300, 50);
        buttonPanel.setLayout(new GridLayout(1, 2, 20, 0));
        buttonPanel.setBackground(new Color(43, 47, 51));
        panel.add(buttonPanel);

        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Tahoma", Font.BOLD, 18));
        loginButton.setBackground(new Color(100, 149, 237));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createEmptyBorder());
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(this);
        buttonPanel.add(loginButton);

        signupButton = new JButton("Signup");
        signupButton.setFont(new Font("Tahoma", Font.BOLD, 18));
        signupButton.setBackground(new Color(34, 139, 34));
        signupButton.setForeground(Color.WHITE);
        signupButton.setFocusPainted(false);
        signupButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signupButton.setBorder(BorderFactory.createEmptyBorder());
        signupButton.addActionListener(this);
        buttonPanel.add(signupButton);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == loginButton) {
            String username = usernameField.getText().trim();
            String password = String.valueOf(passwordField.getPassword()).trim();
            String role = (String) roleBox.getSelectedItem();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Conn conn = new Conn();
                String query = "SELECT * FROM users WHERE username = ? AND password = ? AND role = ?";
                PreparedStatement pst = conn.c.prepareStatement(query);
                pst.setString(1, username);
                pst.setString(2, password);
                pst.setString(3, role);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    JOptionPane.showMessageDialog(null, "Login Successful!");
                    setVisible(false);
                    // Navigate to the respective dashboard based on the role
                    if (role.equals("Admin")) {
                         new BusAdminDashboard(username).setVisible(true);
                    } else if (role.equals("Passenger")) {
                         new PassengerDashboard(username).setVisible(true);
                    } 
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Credentials!");
                }

                conn.c.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        } else if (ae.getSource() == signupButton) {
            setVisible(false);
            new Signup().setVisible(true);
        }
    }

    public static void main(String[] args) {
        new Login().setVisible(true);
    }
}
