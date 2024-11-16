package com.jva48.dao;

import com.jva48.model.*;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImplementation implements UserDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/hotel_db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";




    private Connection connect() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Ensure MySQL JDBC driver is loaded
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    @Override
    public void signUp(User user) {
        if (isUsernameExists(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists.");
        }
        String encryptedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12));
        String query = "INSERT INTO users(username, password) VALUES (?, ?)";
        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, encryptedPassword);
            preparedStatement.executeUpdate();
            System.out.println("Successfully Registered!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isUsernameExists(String username) {
        String query = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public User login(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ?";
        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String hashedPassword = resultSet.getString("password");
                    if (BCrypt.checkpw(password, hashedPassword)) {
                        return new User(resultSet.getInt("id"), resultSet.getString("username"), hashedPassword);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public List<Room> getAvailableRooms() {
        List<Room> rooms = new ArrayList<>();
        String query = "SELECT * FROM rooms WHERE availability=true";
        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                rooms.add(new Room(
                        resultSet.getInt("id"),
                        resultSet.getString("room_number"),
                        resultSet.getString("type"),
                        resultSet.getDouble("price_per_night")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return rooms;
    }

    @Override
    public void bookRoom(int userId, int roomId, String startDate, String endDate) {
        if (!isRoomAvailable(roomId)) {
            System.out.println("Room is not available or does not exist!");
            return;
        }

        String query = "INSERT INTO bookings(user_id, room_id, start_date, end_date) VALUES (?, ?, ?, ?)";
        String updateRoomQuery = "UPDATE rooms SET available=false WHERE id=?";
        try (Connection connection = connect();
             PreparedStatement bookStatement = connection.prepareStatement(query);
             PreparedStatement updateRoomStatement = connection.prepareStatement(updateRoomQuery)) {
            // Book the room
            bookStatement.setInt(1, userId);
            bookStatement.setInt(2, roomId);
            bookStatement.setString(3, startDate);
            bookStatement.setString(4, endDate);
            bookStatement.executeUpdate();
            // Update the room availability
            updateRoomStatement.setInt(1, roomId);
            updateRoomStatement.executeUpdate();
            System.out.println("Room booked successfully!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean isRoomAvailable(int roomId) {
        String query = "SELECT * FROM rooms WHERE id=? AND available=true";
        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, roomId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next(); // Return true if a row is found
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public List<Booking> getUserBookings(int userId) {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT * FROM bookings WHERE user_id=?";
        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    bookings.add(new Booking(
                            resultSet.getInt("id"),
                            resultSet.getInt("user_id"),
                            resultSet.getInt("room_id"),
                            resultSet.getString("start_date"),
                            resultSet.getString("end_date")
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return bookings;
    }

    @Override
    public void cancelBooking(int bookingId) {
        String query = "DELETE FROM bookings WHERE id=?";
        String updateRoomQuery = "UPDATE rooms SET available=true WHERE id=(SELECT room_id FROM bookings WHERE id=?)";
        try (Connection connection = connect();
             PreparedStatement deleteStatement = connection.prepareStatement(query);
             PreparedStatement updateRoomStatement = connection.prepareStatement(updateRoomQuery)) {
            // Update the room availability
            updateRoomStatement.setInt(1, bookingId);
            updateRoomStatement.executeUpdate();
            // Delete the booking
            deleteStatement.setInt(1, bookingId);
            deleteStatement.executeUpdate();
            System.out.println("Booking cancelled successfully!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public void addRoom(Room room) {
        String query = "INSERT INTO rooms(room_number, type, price_per_night, available) VALUES (?, ?, ?, ?)";
        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, room.getRoomNumber());
            preparedStatement.setString(2, room.getType());
            preparedStatement.setDouble(3, room.getPricePerNight());
            preparedStatement.setBoolean(4, room.isAvailable());
            preparedStatement.executeUpdate();
            System.out.println("Room added successfully!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean hasUserBooked(int userId) {
        String query = "SELECT COUNT(*) FROM bookings WHERE user_id=?";
        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}