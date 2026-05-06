# ✈️ Airline Reservation System

## 📌 Overview

This project is a full-stack airline reservation system built using **Java (JDBC)** and **MySQL** with a **Swing GUI** interface.

The system supports three roles:

* 👤 Customer (User)
* 🛠️ Administrator (Admin)
* 🧑‍💼 Customer Representative (Rep)

It allows users to search flights, make reservations, manage tickets, and enables administrators and representatives to manage system data and generate reports.

---

## 🧱 Technologies Used

* Java (JDBC)
* MySQL
* Java Swing (GUI)
* SQL (Relational Database Design)

---

## 🔐 Login Credentials

### 👤 Admin

* Username: admin1
* Password: 123

### 🧑‍💼 Representative

* Username: rep1
* Password: 123

### 👥 Sample Customers

* alice / 123
* bob / 123
* charlie / 123

---

## 🗄️ Database Schema

The system includes the following tables:

* Users
* Employees (Admin & Rep)
* Airlines
* Aircraft
* Airports
* Flights
* Tickets
* Includes (Ticket–Flight relationship)
* Waiting_List
* Questions

👉 See `project.sql` for full schema.

---

## ⚙️ Setup & Run Instructions

### 1️⃣ Setup Database

Open MySQL Workbench and run:

```sql
SOURCE project.sql;
```

Ensure your connection URL in Java matches:

```
jdbc:mysql://localhost:3306/testproject
```

---

### 2️⃣ Compile and Run

```bash
javac -cp ".:lib/mysql-connector-j-9.7.0.jar" *.java
java -cp ".:lib/mysql-connector-j-9.7.0.jar" Main
```

---

## 🧠 System Design Notes

* `cid` is AUTO_INCREMENT → no manual input required
* Login system checks:

  * Users table (customers)
  * Employees table (admin/rep)
* After login:

  * System stores session (`ProjectFrame.cid`)
  * No need to re-enter customer ID
* Uses **JDBC PreparedStatements** for secure queries

---

# 👤 User Features

## ✈️ Flight Search

* Search flights between two airports
* One-way flights on a specific date
* Round-trip flights on specific dates
* Flexible date search (±3 days)
* Browse available flights
* Sort results by:

  * Price
  * Departure time
  * Arrival time
  * Duration
* Filter flights by:

  * Price
  * Number of stops
  * Airline
  * Departure/arrival time

---

## 🎟️ Reservations & Tickets

* Book flights
* Automatically join **waiting list** if flight is full
* View:

  * Upcoming flights
  * Past reservations
* Cancel reservations (**business/first class only**)
* Automatic promotion from waiting list when a seat becomes available

---

## 🔔 Alerts & Support

* Receive alert when seat becomes available
* Submit questions to customer representatives

---

# 🛠️ Admin Features

* Add, edit, delete:

  * Users
  * Customer Representatives
* Generate reports:

  * Monthly sales
  * Reservations by flight
  * Reservations by user
  * Revenue by:

    * Flight
    * Airline
    * Customer
* Identify:

  * Top revenue customer
  * Most active flights

---

# 🧑‍💼 Customer Representative Features

* Make reservations on behalf of users
* Edit existing reservations
* Manage system data:

  * Aircraft
  * Airports
  * Flights
  * Airlines
* View waiting list (FIFO using position)
* View all flights for an airport
* Reply to customer questions

---

## 🧠 Waiting List Design

* Implemented using `Waiting_List` table
* Uses `position` column for FIFO ordering
* Ensures fair seat allocation
* Automatically promotes next user when a seat becomes available

---

## ⚠️ Notes / Assumptions

* Seat availability is checked before booking
* Duplicate seat assignments are prevented
* If full → user joins waiting list
* Foreign key constraints ensure data integrity
* System uses modular structure:

  * `ProjectFrame` → Login
  * `UserService`, `AdminService`, `RepService` → Logic
  * MenuFrames → GUI navigation

---

## ✅ Additional Improvements

* Fixed SQL errors (column mismatches, joins)
* Implemented session handling (no repeated input)
* Removed hardcoded values (e.g., dynamic pricing)
* Ensured database consistency with proper constraints

---

## 📌 Final Notes

This system demonstrates:

* Full relational database integration
* Multi-role access control
* Real-world reservation logic (waiting list, constraints)
* Clean separation of frontend (GUI) and backend (database logic)

---

**Thank you!**
