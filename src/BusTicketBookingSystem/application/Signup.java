package BusTicketBookingSystem.application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Signup extends JFrame implements ActionListener {
    JTextField fullNameField, mobileField, emailField, usernameField;
    JPasswordField passwordField;
    JComboBox<String> roleBox;
    JButton signupButton, backButton;

    public Signup() {
        setTitle("Signup - Bus Ticket Booking System");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel titleLabel = new JLabel("Signup Page");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 36));
        titleLabel.setBounds(400, 50, 600, 50);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel);

        JPanel panel = new JPanel();
        panel.setBounds(400, 150, 600, 500);
        panel.setLayout(null);
        panel.setBorder(BorderFactory.createTitledBorder("SignUp Details"));
        add(panel);

        JLabel fullNameLabel = new JLabel("Full Name:");
        fullNameLabel.setBounds(50, 50, 150, 30);
        panel.add(fullNameLabel);

        fullNameField = new JTextField();
        fullNameField.setBounds(200, 50, 300, 30);
        panel.add(fullNameField);

        JLabel mobileLabel = new JLabel("Mobile No:");
        mobileLabel.setBounds(50, 100, 150, 30);
        panel.add(mobileLabel);

        mobileField = new JTextField();
        mobileField.setBounds(200, 100, 300, 30);
        panel.add(mobileField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(50, 150, 150, 30);
        panel.add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(200, 150, 300, 30);
        panel.add(emailField);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(50, 200, 150, 30);
        panel.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(200, 200, 300, 30);
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 250, 150, 30);
        panel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(200, 250, 300, 30);
        panel.add(passwordField);

        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setBounds(50, 300, 150, 30);
        panel.add(roleLabel);

        String[] roles = {"Admin", "Passenger"};
        roleBox = new JComboBox<>(roles);
        roleBox.setBounds(200, 300, 200, 30);
        panel.add(roleBox);

        signupButton = new JButton("Signup");
        signupButton.setBounds(200, 400, 120, 40);
        signupButton.addActionListener(this);
        panel.add(signupButton);

        backButton = new JButton("Back");
        backButton.setBounds(350, 400, 120, 40);
        backButton.addActionListener(this);
        panel.add(backButton);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == signupButton) {
            String fullName = fullNameField.getText();
            String mobile = mobileField.getText();
            String email = emailField.getText();
            String username = usernameField.getText();
            String password = String.valueOf(passwordField.getPassword());
            String role = (String) roleBox.getSelectedItem();

            if (fullName.isEmpty() || mobile.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required!", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Conn conn = new Conn();
                String query = "INSERT INTO users (full_name, mobile, email, username, password, role) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement pst = conn.c.prepareStatement(query);
                pst.setString(1, fullName);
                pst.setString(2, mobile);
                pst.setString(3, email);
                pst.setString(4, username);
                pst.setString(5, password);
                pst.setString(6, role);
                pst.executeUpdate();

                JOptionPane.showMessageDialog(null, "Signup Successful!");
                conn.c.close();

                setVisible(false);
                if (role.equals("Admin")) {
                    new BusAdminDashboard(username).setVisible(true);
                } else if (role.equals("Passenger")) {
                    new PassengerDashboard(username).setVisible(true);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        } else if (ae.getSource() == backButton) {
            setVisible(false);
            new Login().setVisible(true);
        }
    }

    public static void main(String[] args) {
        new Signup().setVisible(true);
    }
}
