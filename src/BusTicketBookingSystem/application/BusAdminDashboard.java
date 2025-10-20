package BusTicketBookingSystem.application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

public class BusAdminDashboard extends JFrame implements ActionListener {
    private String username;
    private Conn connection;
    private JPanel contentPanel;
    private JButton addBusButton, viewBusesButton, viewPassengersButton, manageBookingsButton, generateReportButton, logoutButton;

    public BusAdminDashboard(String username) {
        this.username = username; // Assign username
        this.connection = new Conn(); // Establish database connection

        setTitle("Admin Dashboard - Bus Ticket Booking System");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        fetchAdminName(); // Fetch admin's full name from the database

        // Sidebar Panel
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(6, 1, 10, 10));
        sidebar.setBackground(new Color(50, 50, 50));
        sidebar.setPreferredSize(new Dimension(200, getHeight()));

        // Sidebar Buttons
        addBusButton = createSidebarButton("Add Bus");
        sidebar.add(addBusButton);

        viewBusesButton = createSidebarButton("View Buses");
        sidebar.add(viewBusesButton);

        viewPassengersButton = createSidebarButton("View Passengers");
        sidebar.add(viewPassengersButton);

        manageBookingsButton = createSidebarButton("Manage Bookings");
        sidebar.add(manageBookingsButton);

        generateReportButton = createSidebarButton("Generate Report");
        sidebar.add(generateReportButton);

        logoutButton = createSidebarButton("Logout");
        sidebar.add(logoutButton);

        add(sidebar, BorderLayout.WEST);

        // Main Content Panel
        contentPanel = new JPanel();
        contentPanel.setLayout(new CardLayout());
        contentPanel.setBackground(new Color(240, 240, 240));
        add(contentPanel, BorderLayout.CENTER);

        JLabel welcomeLabel = new JLabel("Welcome, Admin " + username + ", to the Bus Ticket Booking System!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Tahoma", Font.BOLD, 28));
        contentPanel.add(welcomeLabel, "Home");

        setVisible(true);
    }

    private void fetchAdminName() {
        try {
            String query = "SELECT full_name FROM users WHERE username = ? AND role = 'Admin'";
            PreparedStatement pst = connection.c.prepareStatement(query);
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                username = rs.getString("full_name");
            }
            connection.c.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error fetching admin name: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
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
        if (ae.getSource() == addBusButton) {
           displayAddBusForm();
        } else if (ae.getSource() == viewBusesButton) {
            viewBusesFun();
        } else if (ae.getSource() == viewPassengersButton) {
            viewPassengersFun();
        } else if (ae.getSource() == manageBookingsButton) {
            manageBookingsFun();
        } else if (ae.getSource() == generateReportButton) {
            generateReport();
        } else if (ae.getSource() == logoutButton) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                setVisible(false);
                new Login().setVisible(true);
            }
        }
    }
    
    
    
    // add bus
  private void displayAddBusForm() {
    // Create main panel for form
    JPanel addBusPanel = new JPanel(new GridLayout(9, 2, 10, 10));
    addBusPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    addBusPanel.setBackground(new Color(240, 240, 240));

    // Add labels and text fields
    addBusPanel.add(new JLabel("Bus Number:"));
    JTextField busNumberField = new JTextField();
    addBusPanel.add(busNumberField);

    addBusPanel.add(new JLabel("Bus Name:"));
    JTextField busNameField = new JTextField();
    addBusPanel.add(busNameField);

    addBusPanel.add(new JLabel("Source:"));
    JTextField sourceField = new JTextField();
    addBusPanel.add(sourceField);

    addBusPanel.add(new JLabel("Destination:"));
    JTextField destinationField = new JTextField();
    addBusPanel.add(destinationField);

    addBusPanel.add(new JLabel("Departure Time:"));
    JTextField departureTimeField = new JTextField();
    addBusPanel.add(departureTimeField);

    addBusPanel.add(new JLabel("Arrival Time:"));
    JTextField arrivalTimeField = new JTextField();
    addBusPanel.add(arrivalTimeField);

    addBusPanel.add(new JLabel("Seats Available:"));
    JTextField seatsAvailableField = new JTextField();
    addBusPanel.add(seatsAvailableField);

    addBusPanel.add(new JLabel("Fare:"));
    JTextField fareField = new JTextField();
    addBusPanel.add(fareField);

    // Add Submit button
    JButton submitButton = new JButton("Add Bus");
    submitButton.setBackground(new Color(50, 150, 50));
    submitButton.setForeground(Color.WHITE);
    addBusPanel.add(submitButton);

    // Add panel to dialog
    int result = JOptionPane.showConfirmDialog(
            this, addBusPanel, "Add New Bus", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

    if (result == JOptionPane.OK_OPTION) {
        // Retrieve input values
        String busNumber = busNumberField.getText().trim();
        String busName = busNameField.getText().trim();
        String source = sourceField.getText().trim();
        String destination = destinationField.getText().trim();
        String departureTime = departureTimeField.getText().trim();
        String arrivalTime = arrivalTimeField.getText().trim();
        String seatsAvailable = seatsAvailableField.getText().trim();
        String fare = fareField.getText().trim();

        // Insert into database without complex validation
        addBusToDatabase(busNumber, busName, source, destination, departureTime, arrivalTime, seatsAvailable, fare);
    }
}

// Insert bus into database
private void addBusToDatabase(String busNumber, String busName, String source, String destination,
                              String departureTime, String arrivalTime, String seatsAvailable, String fare) {
    Connection conn = null;
    PreparedStatement pst = null;
    
    try {
        conn = new Conn().c;  // Use the database connection
        String query = "INSERT INTO buses (bus_number, bus_name, source, destination, departure_time, arrival_time, seats_available, fare) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        pst = conn.prepareStatement(query);
        pst.setString(1, busNumber);
        pst.setString(2, busName);
        pst.setString(3, source);
        pst.setString(4, destination);
        pst.setString(5, departureTime);
        pst.setString(6, arrivalTime);
        pst.setString(7, seatsAvailable);
        pst.setString(8, fare);

        int rowsAffected = pst.executeUpdate();
        
        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(this, "Bus added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error adding bus: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    } finally {
        try {
            if (pst != null) pst.close();  // Close PreparedStatement
            if (conn != null) conn.close();  // Close connection
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}







// view buses
private void viewBusesFun() {
    // Create a panel to hold the table for viewing buses
    JPanel viewBusesPanel = new JPanel(new BorderLayout());
    
    // Column names for the table
    String[] columnNames = {"Bus Number", "Bus Name", "Source", "Destination", "Departure Time", "Arrival Time", "Seats Available", "Fare"};
    
    // Table model for managing the table data
    DefaultTableModel model = new DefaultTableModel();
    model.setColumnIdentifiers(columnNames);
    
    // Create the JTable
    JTable busesTable = new JTable(model);
    
    // Set custom font for the table
    busesTable.setFont(new Font("Arial", Font.PLAIN, 14));
    
    // Set custom row height for better readability
    busesTable.setRowHeight(30);
    
    // Set custom alternating row colors
    busesTable.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (row % 2 == 0) {
                c.setBackground(new Color(240, 240, 240)); // Light gray for even rows
            } else {
                c.setBackground(Color.WHITE); // White for odd rows
            }
            return c;
        }
    });
    
    // Customize the table header
    JTableHeader header = busesTable.getTableHeader();
    header.setFont(new Font("Arial", Font.BOLD, 16));
    header.setBackground(new Color(100, 149, 237));  // Cornflower blue
    header.setForeground(Color.WHITE);
    
    // Add a scroll pane for the table
    JScrollPane scrollPane = new JScrollPane(busesTable);
    viewBusesPanel.add(scrollPane, BorderLayout.CENTER); // Add the table to the panel
    
    // Fetch and display the bus data
    fetchBusesData(model);
    
    // Show the panel
    CardLayout cl = (CardLayout) contentPanel.getLayout();
    contentPanel.add(viewBusesPanel, "View Buses");
    cl.show(contentPanel, "View Buses");
}

private void fetchBusesData(DefaultTableModel model) {
    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    try {
        conn = new Conn().c;  // Use the database connection
        String query = "SELECT * FROM buses";  // Query to get all buses
        pst = conn.prepareStatement(query);
        rs = pst.executeQuery();

        // Loop through the ResultSet and add rows to the table
        while (rs.next()) {
            String busNumber = rs.getString("bus_number");
            String busName = rs.getString("bus_name");
            String source = rs.getString("source");
            String destination = rs.getString("destination");
            String departureTime = rs.getString("departure_time");
            String arrivalTime = rs.getString("arrival_time");
            String seatsAvailable = rs.getString("seats_available");
            String fare = rs.getString("fare");

            // Add row to the table
            model.addRow(new Object[]{busNumber, busName, source, destination, departureTime, arrivalTime, seatsAvailable, fare});
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error fetching bus data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    } finally {
        try {
            if (rs != null) rs.close();  // Close ResultSet
            if (pst != null) pst.close();  // Close PreparedStatement
            if (conn != null) conn.close();  // Close connection
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}







// view passenger
private void viewPassengersFun() {
    // Create a panel to hold the table for viewing passengers
    JPanel viewPassengersPanel = new JPanel(new BorderLayout());
    
    // Column names for the table
    String[] columnNames = {"Passenger ID", "Name", "Bus Number", "Source", "Destination", "Booking Date", "Seats Booked", "Total Fare"};
    
    // Table model for managing the table data
    DefaultTableModel model = new DefaultTableModel();
    model.setColumnIdentifiers(columnNames);
    
    // Create the JTable
    JTable passengersTable = new JTable(model);
    
    // Set custom font for the table
    passengersTable.setFont(new Font("Arial", Font.PLAIN, 14));
    
    // Set custom row height for better readability
    passengersTable.setRowHeight(30);
    
    // Set custom alternating row colors
    passengersTable.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (row % 2 == 0) {
                c.setBackground(new Color(240, 240, 240)); // Light gray for even rows
            } else {
                c.setBackground(Color.WHITE); // White for odd rows
            }
            return c;
        }
    });
    
    // Customize the table header
    JTableHeader header = passengersTable.getTableHeader();
    header.setFont(new Font("Arial", Font.BOLD, 16));
    header.setBackground(new Color(100, 149, 237));  // Cornflower blue
    header.setForeground(Color.WHITE);
    
    // Add a scroll pane for the table
    JScrollPane scrollPane = new JScrollPane(passengersTable);
    viewPassengersPanel.add(scrollPane, BorderLayout.CENTER); // Add the table to the panel
    
    // Fetch and display the passenger data
    fetchPassengersData(model);
    
    // Show the panel
    CardLayout cl = (CardLayout) contentPanel.getLayout();
    contentPanel.add(viewPassengersPanel, "View Passengers");
    cl.show(contentPanel, "View Passengers");
}
private void fetchPassengersData(DefaultTableModel model) {
    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    try {
        conn = new Conn().c;  // Use the database connection
        String query = "SELECT * FROM passengers";  // Query to get all passengers
        pst = conn.prepareStatement(query);
        rs = pst.executeQuery();

        // Loop through the ResultSet and add rows to the table
        while (rs.next()) {
            String passengerId = rs.getString("passenger_id");
            String name = rs.getString("name");
            String busNumber = rs.getString("bus_number");
            String source = rs.getString("source");
            String destination = rs.getString("destination");
            String bookingDate = rs.getString("booking_date");
            String seatsBooked = rs.getString("seats_booked");
            String totalFare = rs.getString("total_fare");

            // Add row to the table
            model.addRow(new Object[]{passengerId, name, busNumber, source, destination, bookingDate, seatsBooked, totalFare});
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error fetching passenger data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    } finally {
        try {
            if (rs != null) rs.close();  // Close ResultSet
            if (pst != null) pst.close();  // Close PreparedStatement
            if (conn != null) conn.close();  // Close connection
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


  





// manage booking 
private void manageBookingsFun() {
    // Create a panel to hold the table for managing bookings
    JPanel manageBookingsPanel = new JPanel(new BorderLayout());
    
    // Column names for the table
    String[] columnNames = {"Booking ID", "Passenger Name", "Bus Number", "Source", "Destination", "Booking Date", "Seats Booked", "Total Fare", "Actions"};
    
    // Table model for managing the table data
    DefaultTableModel model = new DefaultTableModel();
    model.setColumnIdentifiers(columnNames);
    
    // Create the JTable
    JTable bookingsTable = new JTable(model);
    
    // Set custom font for the table
    bookingsTable.setFont(new Font("Arial", Font.PLAIN, 14));
    
    // Set custom row height for better readability
    bookingsTable.setRowHeight(30);
    
    // Set custom alternating row colors
    bookingsTable.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (row % 2 == 0) {
                c.setBackground(new Color(240, 240, 240)); // Light gray for even rows
            } else {
                c.setBackground(Color.WHITE); // White for odd rows
            }
            return c;
        }
    });
    
    // Add a button column for actions (update/cancel)
    bookingsTable.getColumnModel().getColumn(8).setCellRenderer(new ButtonRenderer());
    bookingsTable.getColumnModel().getColumn(8).setCellEditor(new ButtonEditor(new JCheckBox(), bookingsTable));  // Pass the table reference
    
    // Customize the table header
    JTableHeader header = bookingsTable.getTableHeader();
    header.setFont(new Font("Arial", Font.BOLD, 16));
    header.setBackground(new Color(100, 149, 237));  // Cornflower blue
    header.setForeground(Color.WHITE);
    
    // Add a scroll pane for the table
    JScrollPane scrollPane = new JScrollPane(bookingsTable);
    manageBookingsPanel.add(scrollPane, BorderLayout.CENTER); // Add the table to the panel
    
    // Fetch and display the booking data
    fetchBookingsData(model);
    
    // Show the panel
    CardLayout cl = (CardLayout) contentPanel.getLayout();
    contentPanel.add(manageBookingsPanel, "Manage Bookings");
    cl.show(contentPanel, "Manage Bookings");
}

private void fetchBookingsData(DefaultTableModel model) {
    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    try {
        conn = new Conn().c;  // Use the database connection
        String query = "SELECT * FROM bookings";  // Query to get all bookings
        pst = conn.prepareStatement(query);
        rs = pst.executeQuery();

        // Loop through the ResultSet and add rows to the table
        while (rs.next()) {
            String bookingId = rs.getString("booking_id");
            String passengerName = rs.getString("passenger_name");
            String busNumber = rs.getString("bus_number");
            String source = rs.getString("source");
            String destination = rs.getString("destination");
            String bookingDate = rs.getString("booking_date");
            String seatsBooked = rs.getString("seats_booked");
            String totalFare = rs.getString("total_fare");

            // Add row to the table with an action button column
            model.addRow(new Object[]{bookingId, passengerName, busNumber, source, destination, bookingDate, seatsBooked, totalFare, "Manage"});
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error fetching booking data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    } finally {
        try {
            if (rs != null) rs.close();  // Close ResultSet
            if (pst != null) pst.close();  // Close PreparedStatement
            if (conn != null) conn.close();  // Close connection
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
class ButtonRenderer extends JButton implements TableCellRenderer {
    public ButtonRenderer() {
        setText("Manage");
        setBackground(new Color(100, 149, 237)); // Cornflower blue
        setForeground(Color.WHITE);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return this;
    }
}

class ButtonEditor extends DefaultCellEditor {
    private String label;
    private JButton button;
    private JTable bookingsTable;

    public ButtonEditor(JCheckBox checkBox, JTable bookingsTable) {
        super(checkBox);
        this.bookingsTable = bookingsTable;  // Store the table reference
        button = new JButton();
        button.setOpaque(true);
        button.setText("Manage");
        button.setBackground(new Color(100, 149, 237)); // Cornflower blue
        button.setForeground(Color.WHITE);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = bookingsTable.getSelectedRow();  // Get the selected row
                if (row != -1) {
                    String bookingId = bookingsTable.getValueAt(row, 0).toString();  // Get booking ID
                    showAcceptDeclineDialog(bookingId);  // Show the accept/decline dialog
                }
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        return button;
    }

    private void showAcceptDeclineDialog(String bookingId) {
        // Show dialog to accept or decline the booking
        Object[] options = {"Accept", "Decline", "Cancel"};
        int choice = JOptionPane.showOptionDialog(null,
                "Do you want to accept or decline this booking?", 
                "Manage Booking",
                JOptionPane.DEFAULT_OPTION, 
                JOptionPane.INFORMATION_MESSAGE, 
                null, options, options[2]);
        
        if (choice == 0) {
            // Accept the booking
            updateBookingStatus(bookingId, "Accepted");
        } else if (choice == 1) {
            // Decline the booking
            updateBookingStatus(bookingId, "Declined");
        }
    }

   private void updateBookingStatus(String bookingId, String status) {
    try {
        // Ensure the connection is open
        if (connection.c == null || connection.c.isClosed()) {
            connection = new Conn();  // Reconnect if connection is closed
        }

        String query = "UPDATE bookings SET status = ? WHERE booking_id = ?";
        PreparedStatement pst = connection.c.prepareStatement(query);
        pst.setString(1, status);
        pst.setString(2, bookingId);

        // Execute update and check if successful
        int result = pst.executeUpdate();
        if (result > 0) {
            JOptionPane.showMessageDialog(null, "Booking status updated to: " + status);
        } else {
            JOptionPane.showMessageDialog(null, "Failed to update booking status.");
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error updating status: " + e.getMessage());
    }
}

}







// reoport generation
private void generateReport() {
    // Ensure the connection is open
    try {
        if (connection.c == null || connection.c.isClosed()) {
            connection = new Conn();  // Reinitialize the connection
        }
        
        // Create a new frame for the report
        JFrame reportFrame = new JFrame("Booking Report");
        reportFrame.setSize(600, 400);
        reportFrame.setLocationRelativeTo(null);
        
        // Create a table to display the bookings data
        String[] columnNames = {"Booking ID", "Passenger Name", "Bus Number", "Source", "Destination", "Booking Date", "Seats Booked", "Total Fare", "Status"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        // Fetch data from the bookings table
        String query = "SELECT booking_id, passenger_name, bus_number, source, destination, booking_date, seats_booked, total_fare, status FROM bookings";
        PreparedStatement pst = connection.c.prepareStatement(query);
        ResultSet rs = pst.executeQuery();

        // Add rows to the table model
        while (rs.next()) {
            Object[] row = {
                rs.getInt("booking_id"),
                rs.getString("passenger_name"),
                rs.getString("bus_number"),
                rs.getString("source"),
                rs.getString("destination"),
                rs.getDate("booking_date"),
                rs.getInt("seats_booked"),
                rs.getDouble("total_fare"),
                rs.getString("status")
            };
            model.addRow(row);
        }

        // Create a table and set the model
        JTable bookingsTable = new JTable(model);
        bookingsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Add the table to a JScrollPane for scrollable view
        JScrollPane scrollPane = new JScrollPane(bookingsTable);
        reportFrame.add(scrollPane);
        
        // Make the report frame visible
        reportFrame.setVisible(true);

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error fetching report data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    } finally {
        try {
            if (connection.c != null && !connection.c.isClosed()) {
                connection.c.close();  // Close the connection after operation
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error closing connection: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}






    private void showContent(String cardName) {
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        if (!cardName.equals("Home")) {
            JPanel newPanel = new JPanel(); // Placeholder for specific feature content
            newPanel.setBackground(new Color(230, 230, 230));
            newPanel.add(new JLabel(cardName + " Content Goes Here!", JLabel.CENTER));
            contentPanel.add(newPanel, cardName);
        }
        cl.show(contentPanel, cardName);
    }

    public static void main(String[] args) {
        new BusAdminDashboard("adminUser").setVisible(true);
    }
}
