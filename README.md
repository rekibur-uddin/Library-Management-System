# 🚌 Bus Ticket Booking System 📝

A **Java-based Bus Ticket Booking System** built using **NetBeans IDE** and **MySQL**, designed to simplify bus ticket bookings and passenger management.
This project is part of the **BCA Final Year Project** and includes **ER diagrams**, **database scripts**, and a **detailed project report**.

---

## 📌 Project Overview

The **Bus Ticket Booking System** provides a secure and user-friendly platform where:

* 🧑‍💼 **Admins** can manage buses, bookings, and passenger data.
* 🧑‍🎓 **Passengers** can register, view buses, book tickets, and check booking status.

The project uses **Java Swing** for GUI and **MySQL** for backend data storage, ensuring reliability and fast performance.

---

## ✨ Features

### 🧑‍💼 Admin Panel

* Add / Update / Delete Buses
* View All Bookings and Passengers
* Track Seats Availability
* Generate Reports

### 🧑‍🎓 Passenger Module

* Register & Login
* View Available Buses
* Book Tickets
* View Booking Status & Details

### 🧠 Database Integration

* 🔐 Secure MySQL Database
* 📝 Real-time Booking Management
* 📊 ER Diagrams + Full Project Documentation

---

## 🛠️ Tech Stack

| Component        | Technology            |
| ---------------- | --------------------- |
| 💻 Frontend      | Java Swing (NetBeans) |
| 🗄️ Backend      | MySQL                 |
| 🧠 IDE           | NetBeans              |
| 📝 Language      | Java (JDK 8+)         |
| 🔐 DB Connection | JDBC                  |

---

## 📂 Project Structure

```
Bus-Ticket-Booking-System/
├─ nbproject/
├─ src/
│  └─ busticketbooking/  # Java source files (Admin & Passenger Modules)
├─ dist/
├─ database/
│  └─ busticketbookingsystem.sql
├─ report/
│  ├─ Project-Report.pdf
│  └─ ER-Diagram.pdf
├─ screenshots/
├─ README.md
└─ .gitignore
```

---

## 📊 ER Diagram

📌 **[View ER Diagram (PDF)](./Project%20Report.pdf)**

---

## 📘 Project Report

📄 **[Download Full Project Report (PDF)](./Project%20Report.pdf)**

The report includes:

* Problem Definition
* Objective
* Existing & Proposed System
* System Design (DFD, ERD, Schema)
* Implementation Details
* Output Screenshots
* Conclusion

---

## 🧰 Database

### **Database Name:** `BusTicketBookingSystem`

#### **Tables and Sample Data:**

```sql
CREATE DATABASE BusTicketBookingSystem;
USE BusTicketBookingSystem;

-- Users Table
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    role ENUM('Admin', 'Passenger') NOT NULL,
    mobile VARCHAR(15) NOT NULL,
    email VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Buses Table
CREATE TABLE buses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    bus_number VARCHAR(20) UNIQUE NOT NULL,
    bus_name VARCHAR(50) NOT NULL,
    source VARCHAR(50) NOT NULL,
    destination VARCHAR(50) NOT NULL,
    departure_time VARCHAR(10) NOT NULL,
    arrival_time VARCHAR(10) NOT NULL,
    seats_available VARCHAR(10) NOT NULL,
    fare VARCHAR(10) NOT NULL
);

INSERT INTO buses (bus_number, bus_name, source, destination, departure_time, arrival_time, seats_available, fare)
VALUES
('BUS001', 'City Express', 'Mumbai', 'Pune', '06:00:00', '09:30:00', 40, 500.00),
('BUS002', 'Night Rider', 'Delhi', 'Jaipur', '22:00:00', '04:00:00', 45, 700.00),
('BUS003', 'Morning Star', 'Kolkata', 'Durgapur', '08:00:00', '11:00:00', 50, 300.00),
('BUS004', 'Highway King', 'Chennai', 'Bangalore', '18:00:00', '23:00:00', 35, 600.00),
('BUS005', 'Golden Arrow', 'Hyderabad', 'Vijayawada', '05:30:00', '08:30:00', 42, 400.00);

-- Passengers Table
CREATE TABLE passengers (
    passenger_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    bus_number VARCHAR(255),
    source VARCHAR(255),
    destination VARCHAR(255),
    booking_date DATE,
    seats_booked INT,
    total_fare DECIMAL(10, 2)
);

INSERT INTO passengers (name, bus_number, source, destination, booking_date, seats_booked, total_fare)
VALUES
('John Doe', 'B101', 'Mumbai', 'Pune', '2025-01-15', 2, 400.00),
('Alice Smith', 'B102', 'Delhi', 'Agra', '2025-01-16', 1, 150.00),
('Bob Johnson', 'B103', 'Chennai', 'Bangalore', '2025-01-17', 3, 600.00);

-- Bookings Table
CREATE TABLE bookings (
    booking_id INT AUTO_INCREMENT PRIMARY KEY,
    passenger_name VARCHAR(255),
    bus_number VARCHAR(255),
    source VARCHAR(255),
    destination VARCHAR(255),
    booking_date DATE,
    seats_booked INT,
    total_fare DECIMAL(10, 2),
    status VARCHAR(20) DEFAULT 'Pending'
);

INSERT INTO bookings (passenger_name, bus_number, source, destination, booking_date, seats_booked, total_fare)
VALUES
('John Doe', 'B101', 'Mumbai', 'Pune', '2025-01-15', 2, 400.00),
('Alice Smith', 'B102', 'Delhi', 'Agra', '2025-01-16', 1, 150.00),
('Bob Johnson', 'B103', 'Chennai', 'Bangalore', '2025-01-17', 3, 600.00);
```

---

## 🧰 How to Run Locally

1️⃣ Clone the Repository

```bash
git clone https://github.com/rekibur-uddin/Bus-Ticket-Booking-System.git
cd Bus-Ticket-Booking-System
```

2️⃣ Open in NetBeans

* Open NetBeans IDE
* Go to File → Open Project
* Select the project folder

3️⃣ Import Database

* Open phpMyAdmin or MySQL Workbench
* Create schema `BusTicketBookingSystem`
* Import `database/busticketbookingsystem.sql`

4️⃣ Update DB Credentials

* In source code, set JDBC URL, username, password as per local MySQL

5️⃣ Run Project

* Click ▶️ Run Project in NetBeans

---

## ✍️ Author

👤 Rekibur Uddin
📧 [Visit My Portfolio](https://rekiburuddin.blogspot.com)

---

## ⭐ Support

If you like this project, consider giving it a ⭐ on [GitHub](https://github.com/rekibur-uddin/Bus-Ticket-Booking-System) 🙌.
