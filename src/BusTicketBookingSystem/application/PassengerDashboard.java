package BusTicketBookingSystem.application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class PassengerDashboard extends JFrame implements ActionListener {
    private String username;
    private Conn connection;
    private JPanel contentPanel;
    private JButton bookTicketButton, viewBookingsButton, cancelBookingButton, logoutButton;

    public PassengerDashboard(String username) {
        this.username = username; // Assign username
        this.connection = new Conn(); // Establish database connection

        setTitle("Passenger Dashboard - Bus Ticket Booking System");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Sidebar Panel
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(4, 1, 10, 10));
        sidebar.setBackground(new Color(50, 50, 50));
        sidebar.setPreferredSize(new Dimension(200, getHeight()));

        // Sidebar Buttons
        bookTicketButton = createSidebarButton("Book Tickets");
        sidebar.add(bookTicketButton);

        viewBookingsButton = createSidebarButton("View Bookings");
        sidebar.add(viewBookingsButton);

        cancelBookingButton = createSidebarButton("Cancel Booking");
        sidebar.add(cancelBookingButton);

        logoutButton = createSidebarButton("Logout");
        sidebar.add(logoutButton);

        add(sidebar, BorderLayout.WEST);

        // Main Content Panel
        contentPanel = new JPanel();
        contentPanel.setLayout(new CardLayout());
        contentPanel.setBackground(new Color(240, 240, 240));
        add(contentPanel, BorderLayout.CENTER);

        JLabel welcomeLabel = new JLabel("Welcome, " + username + ", to the Bus Ticket Booking System!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Tahoma", Font.BOLD, 28));
        contentPanel.add(welcomeLabel, "Home");

        setVisible(true);
    }

    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Tahoma", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(100, 100, 100));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(150, 150, 150));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(100, 100, 100));
            }
        });
        button.addActionListener(this);
        return button;
    }

    @Override
public void actionPerformed(ActionEvent ae) {
    if (ae.getSource() == bookTicketButton) {
        showPanel("Book Ticket", this::bookTicket);
    } else if (ae.getSource() == viewBookingsButton) {
        showPanel("View Bookings", this::viewBookings);
    } else if (ae.getSource() == cancelBookingButton) {
        showPanel("Cancel Booking", this::cancelBooking);
    } else if (ae.getSource() == logoutButton) {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            setVisible(false);
            new Login().setVisible(true);
        }
    }
}
private void showPanel(String panelName, Runnable panelAction) {
    contentPanel.removeAll(); // Clear existing content
    panelAction.run(); // Execute the specific panel logic
    contentPanel.revalidate(); // Revalidate the layout
    contentPanel.repaint(); // Repaint to reflect changes
}


   
    
    
    // book ticket
    private void bookTicket() {
    // Clear content panel
    contentPanel.removeAll();

    // Layout for book ticket section
    JPanel bookTicketPanel = new JPanel();
    bookTicketPanel.setLayout(new BorderLayout());
    bookTicketPanel.setBackground(new Color(240, 240, 240));

    // Table for buses
    String[] columnNames = {"Bus Number", "Bus Name", "Source", "Destination", "Departure Time", "Seats Available", "Fare"};
    DefaultTableModel model = new DefaultTableModel(columnNames, 0);
    JTable busesTable = new JTable(model);
    busesTable.setRowHeight(25);
    busesTable.setFont(new Font("Tahoma", Font.PLAIN, 14));

    // Fetch buses from the database
    try {
        Connection conn = new Conn().c; // Use the Conn class for database connection
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM buses");
        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getString("bus_number"),
                rs.getString("bus_name"),
                rs.getString("source"),
                rs.getString("destination"),
                rs.getString("departure_time"),
                rs.getString("seats_available"),
                rs.getString("fare")
            });
        }
        conn.close();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error fetching buses: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    JScrollPane tableScroll = new JScrollPane(busesTable);
    bookTicketPanel.add(tableScroll, BorderLayout.CENTER);

    // Booking form
    JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
    formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    formPanel.setBackground(new Color(240, 240, 240));

    JLabel seatsLabel = new JLabel("Seats to Book:");
    JTextField seatsField = new JTextField();

    JButton bookButton = new JButton("Book Ticket");
    bookButton.setBackground(new Color(50, 150, 50));
    bookButton.setForeground(Color.WHITE);

    formPanel.add(new JLabel("Select a bus from the table above.", JLabel.CENTER));
    formPanel.add(new JLabel()); // Empty cell
    formPanel.add(seatsLabel);
    formPanel.add(seatsField);
    formPanel.add(bookButton);

    bookTicketPanel.add(formPanel, BorderLayout.SOUTH);

    // Add panel to content area
    contentPanel.add(bookTicketPanel, "Book Ticket");
    CardLayout cl = (CardLayout) contentPanel.getLayout();
    cl.show(contentPanel, "Book Ticket");

    // Book Ticket Action
    bookButton.addActionListener(e -> {
        int selectedRow = busesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a bus from the table.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String busNumber = model.getValueAt(selectedRow, 0).toString();
        int availableSeats = Integer.parseInt(model.getValueAt(selectedRow, 5).toString());
        int seatsToBook;

        try {
            seatsToBook = Integer.parseInt(seatsField.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number of seats.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (seatsToBook <= 0 || seatsToBook > availableSeats) {
            JOptionPane.showMessageDialog(this, "Invalid number of seats selected.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Connection conn = new Conn().c; // Use Conn class for database connection
            // Update buses table
            int updatedSeats = availableSeats - seatsToBook;
            String updateQuery = "UPDATE buses SET seats_available = ? WHERE bus_number = ?";
            PreparedStatement pst = conn.prepareStatement(updateQuery);
            pst.setInt(1, updatedSeats);
            pst.setString(2, busNumber);
            pst.executeUpdate();

            // Add record to bookings table
            String insertQuery = "INSERT INTO bookings (passenger_name, bus_number, source, destination, booking_date, seats_booked, total_fare) VALUES (?, ?, ?, ?, CURDATE(), ?, ?)";
            pst = conn.prepareStatement(insertQuery);
            pst.setString(1, username); // Replace username with the logged-in user's name
            pst.setString(2, busNumber);
            pst.setString(3, model.getValueAt(selectedRow, 2).toString()); // Source
            pst.setString(4, model.getValueAt(selectedRow, 3).toString()); // Destination
            pst.setInt(5, seatsToBook);
            pst.setDouble(6, seatsToBook * Double.parseDouble(model.getValueAt(selectedRow, 6).toString())); // Total Fare
            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Ticket booked successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            // Refresh table data
            model.setValueAt(updatedSeats, selectedRow, 5);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error booking ticket: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    });
}

    
    
    
    

    // view boking
    private void viewBookings() {
    // Clear content panel
    contentPanel.removeAll();

    // Layout for view bookings section
    JPanel viewBookingsPanel = new JPanel();
    viewBookingsPanel.setLayout(new BorderLayout());
    viewBookingsPanel.setBackground(new Color(240, 240, 240));

    JLabel headingLabel = new JLabel("Your Bookings", JLabel.CENTER);
    headingLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
    headingLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    viewBookingsPanel.add(headingLabel, BorderLayout.NORTH);

    // Table for bookings
    String[] columnNames = {"Booking ID", "Bus Number", "Source", "Destination", "Booking Date", "Seats Booked", "Total Fare", "Status"};
    DefaultTableModel model = new DefaultTableModel(columnNames, 0);
    JTable bookingsTable = new JTable(model);
    bookingsTable.setRowHeight(25);
    bookingsTable.setFont(new Font("Tahoma", Font.PLAIN, 14));

    // Fetch bookings from the database for the logged-in user
    try {
        Connection conn = new Conn().c; // Use the Conn class for database connection
        String query = "SELECT * FROM bookings WHERE passenger_name = ?";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setString(1, username); // Use the logged-in passenger's username
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getInt("booking_id"),
                rs.getString("bus_number"),
                rs.getString("source"),
                rs.getString("destination"),
                rs.getDate("booking_date"),
                rs.getInt("seats_booked"),
                rs.getDouble("total_fare"),
                rs.getString("status")
            });
        }
        conn.close();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error fetching bookings: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    JScrollPane tableScroll = new JScrollPane(bookingsTable);
    viewBookingsPanel.add(tableScroll, BorderLayout.CENTER);

    // Add panel to content area
    contentPanel.add(viewBookingsPanel, "View Bookings");
    CardLayout cl = (CardLayout) contentPanel.getLayout();
    cl.show(contentPanel, "View Bookings");
}

    
    
    
    
    
    
    

   private void cancelBooking() {
    // Clear content panel
    contentPanel.removeAll();

    // Layout for cancel bookings section
    JPanel cancelBookingPanel = new JPanel();
    cancelBookingPanel.setLayout(new BorderLayout());
    cancelBookingPanel.setBackground(new Color(240, 240, 240));

    JLabel headingLabel = new JLabel("Cancel Booking", JLabel.CENTER);
    headingLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
    headingLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    cancelBookingPanel.add(headingLabel, BorderLayout.NORTH);

    // Table for bookings
    String[] columnNames = {"Booking ID", "Bus Number", "Source", "Destination", "Booking Date", "Seats Booked", "Total Fare", "Status"};
    DefaultTableModel model = new DefaultTableModel(columnNames, 0);
    JTable bookingsTable = new JTable(model);
    bookingsTable.setRowHeight(25);
    bookingsTable.setFont(new Font("Tahoma", Font.PLAIN, 14));

    // Fetch bookings from the database for the logged-in user
    try {
        Connection conn = new Conn().c; // Use the Conn class for database connection
        String query = "SELECT * FROM bookings WHERE passenger_name = ? AND status != 'Cancelled'";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setString(1, username); // Use the logged-in passenger's username
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getInt("booking_id"),
                rs.getString("bus_number"),
                rs.getString("source"),
                rs.getString("destination"),
                rs.getDate("booking_date"),
                rs.getInt("seats_booked"),
                rs.getDouble("total_fare"),
                rs.getString("status")
            });
        }
        conn.close();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error fetching bookings: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    JScrollPane tableScroll = new JScrollPane(bookingsTable);
    cancelBookingPanel.add(tableScroll, BorderLayout.CENTER);

    // Cancel Button
    JButton cancelButton = new JButton("Cancel Booking");
    cancelButton.setBackground(new Color(200, 50, 50));
    cancelButton.setForeground(Color.WHITE);
    cancelButton.setFont(new Font("Tahoma", Font.BOLD, 14));
    cancelBookingPanel.add(cancelButton, BorderLayout.SOUTH);

    // Add panel to content area
    contentPanel.add(cancelBookingPanel, "Cancel Booking");
    CardLayout cl = (CardLayout) contentPanel.getLayout();
    cl.show(contentPanel, "Cancel Booking");

    // Cancel Booking Action
    cancelButton.addActionListener(e -> {
        int selectedRow = bookingsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a booking to cancel.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int bookingId = (int) model.getValueAt(selectedRow, 0);
        String busNumber = model.getValueAt(selectedRow, 1).toString();
        int seatsBooked = (int) model.getValueAt(selectedRow, 5);

        try {
            Connection conn = new Conn().c; // Use Conn class for database connection

            // Update booking status to "Cancelled"
            String updateBookingQuery = "UPDATE bookings SET status = 'Cancelled' WHERE booking_id = ?";
            PreparedStatement pst = conn.prepareStatement(updateBookingQuery);
            pst.setInt(1, bookingId);
            pst.executeUpdate();

            // Update seats available in the buses table
            String updateBusQuery = "UPDATE buses SET seats_available = seats_available + ? WHERE bus_number = ?";
            pst = conn.prepareStatement(updateBusQuery);
            pst.setInt(1, seatsBooked);
            pst.setString(2, busNumber);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Booking cancelled successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            // Refresh table data
            model.removeRow(selectedRow);

            conn.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error cancelling booking: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    });
}

    
    
    
    

    public static void main(String[] args) {
        new PassengerDashboard("PassengerUser").setVisible(true);
    }
}
