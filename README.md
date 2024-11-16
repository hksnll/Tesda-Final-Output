# Hotel Reservation System

This project is a simple Java-based hotel reservation system using JDBC and BCrypt for password management. The application allows users to sign up, log in, view available rooms, reserve rooms, and view their reservations.

## Table of Contents
- [Features](#features)
- [Getting Started](#getting-started)
- [Prerequisites](#prerequisites)
- [Installation ](#installation)
- [Usage](#usage)
- [Technologies Used](#technologies-used)
- [Acknowledgments](#acknowledgments)
- [Developers](#developers)

## Features
- User Authentication (Sign Up and Login)
- View Available Rooms
- Reserve Rooms
- View User Reservations

## Getting Started

Follow these instructions to set up the project locally.

### Prerequisites
- Java 21
- MySQL Database
- IDE (IntelliJ IDEA recommended)
- Maven (for dependency management)

### Installation
1. **Clone the repository**
    ```bash
    git clone https://github.com/hksnll/Tesda-Final-Output.git
    ```

2. **Navigate to the project directory**
    ```bash
    cd hotel-booking-system
    ```

3. **Create Database**
    ```sql
    CREATE DATABASE hotel_db;
    ```

4. **Create Tables**
    ```sql
    CREATE TABLE users (
        id INT AUTO_INCREMENT PRIMARY KEY,
        username VARCHAR(255) NOT NULL,
        password VARCHAR(255) NOT NULL,
        UNIQUE (username)
    );

    CREATE TABLE rooms (
        id INT AUTO_INCREMENT PRIMARY KEY,
        room_number VARCHAR(255) NOT NULL,
        type VARCHAR(255) NOT NULL,
        price_per_night DOUBLE NOT NULL,
        available BOOLEAN DEFAULT TRUE
    );

    CREATE TABLE bookings (
        id INT AUTO_INCREMENT PRIMARY KEY,
        user_id INT,
        room_id INT,
        start_date DATE,
        end_date DATE,
        FOREIGN KEY (user_id) REFERENCES users(id),
        FOREIGN KEY (room_id) REFERENCES rooms(id)
    );
    ```

5. **Insert Rooms**
    ```sql
    INSERT INTO Rooms (room_number, type, price_per_night, availability) VALUES
        ('101', 'Single', 1600.00, TRUE),
        ('102', 'Double', 2500.00, TRUE),
        ('103', 'Deluxe', 3500.00, TRUE);
    ```
    
    

## Usage
1. **Run the Application**

    You can run the application from your IDE or by using the command line if you have a main class with the `public static void main(String[] args)` method.

2. **Interact with the System**

    - **Sign Up Users**: Create new user accounts.
    - **Login**: Log in with existing user credentials.
    - **View Available Rooms**: Check available rooms in the hotel.
    - **Reserve Rooms**: Book available rooms.
    - **View Reservations**: View all bookings made by a user.

## Technologies Used
- **Java 21:** The main programming language.
- **JDBC:** For database connectivity.
- **BCrypt:** For password hashing.
- **MySQL:** As the relational database.

## Acknowledgments
- This project was inspired by the need for a simple, yet practical hotel reservation system for learning purposes.
- Thanks to [BCrypt](https://www.mindrot.org/projects/jBCrypt/) for providing the hashing library.
- [JetBrains](https://www.jetbrains.com/) for providing an excellent IDE (IntelliJ IDEA).

## Developers
- Macalintal, Hannah
- Plaza, Lemwell
- Rodriguez, Leah
- Villacora, Joel Jr.
