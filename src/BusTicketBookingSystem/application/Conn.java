package BusTicketBookingSystem.application;

import java.sql.*;

public class Conn {
    public Connection c;
    public Statement s;

    public Conn() {
        try {
            // Load JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establish Connection
            c = DriverManager.getConnection("jdbc:mysql://localhost:3306/BusTicketBookingSystem", "root", "Rekib123");
            s = c.createStatement();
            System.out.println("Database Connection Successful");
        } catch (Exception e) {
            e.printStackTrace(); // Print stack trace to understand the issue
        }
    }

    Object getConnection() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
