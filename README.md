# ✈️ Airline Reservation System

## 📌 Overview
This project is a full-stack airline reservation system built using **Java (JDBC)** and **MySQL**. It supports three roles:

- Customer (User)
- Administrator (Admin)
- Customer Representative (Rep)

The system allows users to search flights, make reservations, manage tickets, and enables admin/representatives to manage the database and generate reports.

---

## 🧱 Technologies Used
- Java (JDBC)
- MySQL
- Swing (GUI)
- SQL (Relational Database Design)

---

## 🗄️ Database Schema
The system includes the following tables:

- Users  
- Employees (Admin & Rep)  
- Airlines  
- Aircraft  
- Airports  
- Flights  
- Tickets  
- Includes (Ticket–Flight relationship)  
- Waiting_List  
- Questions  

👉 See `project.sql` for full schema.

---

## 👤 User Functionality

### ✈️ Flight Search
- Search flights between two airports  
- One-way and round-trip options  
- Flexible date search (±3 days)  
- Sort by:
  - Price
  - Departure time
  - Arrival time
  - Duration
- Filter by:
  - Price
  - Stops
  - Airline
  - Time range

---

### 🎟️ Reservations
- Book flights  
- Automatic **waiting list** if full  
- View:
  - Upcoming flights  
  - Past flights  
- Cancel reservations (**only business/first class**)  
- Automatic promotion from waiting list when a seat becomes available  

---

### ❓ Customer Support
- Submit questions to customer representatives  

---

## 🛠️ Admin Functionality
- Add / Edit / Delete:
  - Users  
  - Customer Representatives  
- Generate reports:
  - Monthly sales  
  - Reservations by flight  
  - Reservations by user  
  - Revenue by:
    - Flight  
    - Airline  
    - Customer  
- Identify:
  - Top customer  
  - Most active flights  

---

## 🧑‍💼 Customer Representative Functionality
- Make reservations on behalf of users  
- Edit existing reservations  
- Add / Edit / Delete:
  - Aircraft  
  - Airports  
  - Flights  
  - Airlines  
- View waiting list for a flight  
- View all flights for a given airport  
- Reply to customer questions  

---

## ⚙️ How to Run

### 1️⃣ Setup Database 
Open MySQL Workbench and run:
```sql
SOURCE project.sql;

### 2️⃣ Compile and Run
javac -cp .:lib/mysql-connector-j-8.3.0.jar *.java
java -cp .:lib/mysql-connector-j-8.3.0.jar Main
