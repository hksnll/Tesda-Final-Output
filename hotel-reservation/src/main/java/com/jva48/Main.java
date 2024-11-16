package com.jva48;
import com.jva48.dao.*;
import com.jva48.model.*;

import java.util.List;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserDAO userDao = new UserDAOImplementation();
        System.out.println("~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~");

        System.out.println(" Welcome to hotel reservation");
        System.out.println("~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~");
        while (true) {

            System.out.println("--->   Enter your choice   <---");
            System.out.println("          [1] Sign Up");
            System.out.println("          [2] Login");
            System.out.println("          [3] Exit");
            System.out.println("==============================");
            System.out.print("      Enter Choice: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                switch (choice) {
                    case 1:
                        // Sign up
                        System.out.println("###############################");
                        System.out.println("#                             #");
                        System.out.println("#           Sign up           #");
                        System.out.println("#                             #");
                        System.out.println("###############################");
                        System.out.print("Enter Username: ");
                        String username = scanner.nextLine().trim();
                        System.out.print("Enter Password: ");
                        String password = scanner.nextLine().trim();
                        User user = new User(username, password);
                        try {
                            userDao.signUp(user);
                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    case 2:
                        // Login
                        System.out.println("###############################");
                        System.out.println("#                             #");
                        System.out.println("#            Login            #");
                        System.out.println("#                             #");
                        System.out.println("###############################");
                        System.out.print("Enter Username: ");
                        String loginUsername = scanner.nextLine().trim();
                        System.out.print("Enter Password: ");
                        String loginPassword = scanner.nextLine().trim();
                        User loginUser = userDao.login(loginUsername, loginPassword);
                        if (loginUser != null) {
                            System.out.println("Login successfully. Welcome " + loginUsername);
                            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++");
                            handleReservations(scanner, userDao, loginUser.getId());
                        } else {
                            System.out.println("Invalid Username or password");
                        }
                        break;
                    case 3:
                        // Exit
                        System.out.println("Exiting...");
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice, please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }

    private static void handleReservations(Scanner scanner, UserDAO userDao, int userId) {
        while (true) {
            System.out.println("============================");
            System.out.println("|           MENU           |");
            System.out.println("============================");
            System.out.println("| [1] View available rooms |");
            System.out.println("| [2] Reserve a room       |");
            System.out.println("| [3] View my reservations |");
            System.out.println("| [4] Logout               |");
            System.out.println("============================");
            System.out.print("Enter Choice: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                switch (choice) {
                    case 1:
                        // View available rooms
                        List<Room> rooms = userDao.getAvailableRooms();
                        if (rooms.size() > 0) {
                            System.out.println("Available rooms: ");
                            for (Room room : rooms) {
                                System.out.println("Room ID: " + room.getId() +
                                        ", Room Number: " + room.getRoomNumber() +
                                        ", Type: " + room.getType() +
                                        ", Price: " + room.getPricePerNight());
                            }
                        } else {
                            System.out.println("No available rooms.");
                        }
                        break;
                    case 2:
                        // Book a room
                        System.out.println("===================================");
                        System.out.print("Enter Room ID: ");
                        int roomId = Integer.parseInt(scanner.nextLine().trim());
                        System.out.println("===================================");
                        System.out.print("Enter Start Date (YYYY-MM-DD): ");
                        String startDate = scanner.nextLine().trim();
                        System.out.println("===================================");
                        System.out.print("Enter End Date (YYYY-MM-DD): ");
                        String endDate = scanner.nextLine().trim();
                        System.out.println("===================================");
                        userDao.bookRoom(userId, roomId, startDate, endDate);
                        break;
                    case 3:
                        // View my bookings
                        List<Booking> bookings = userDao.getUserBookings(userId);
                        if (bookings.size() > 0) {
                            for (Booking booking : bookings) {
                                System.out.println("Booking ID: " + booking.getId() +
                                        ", Room ID: " + booking.getRoomId() +
                                        ", Start Date: " + booking.getStartDate() +
                                        ", End Date: " + booking.getEndDate());
                            }
                        } else {
                            System.out.println("You have no bookings.");
                        }
                        break;
                    case 4:
                        // Logout
                        return;
                    default:
                        System.out.println("Invalid choice, please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }
}