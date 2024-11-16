package com.jva48.dao;

import com.jva48.model.*;

import java.util.List;

public interface UserDAO {
    void signUp(User user);
    User login(String username, String password);
    List<Room> getAvailableRooms();
    void bookRoom(int userId, int roomId, String startDate, String endDate);
    List<Booking> getUserBookings(int userId);
    void cancelBooking(int bookingId);

}
