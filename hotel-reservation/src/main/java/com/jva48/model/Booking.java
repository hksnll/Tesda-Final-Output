package com.jva48.model;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    private int id;
    private int userId;
    private int roomId;
    private String startDate;
    private String endDate;
}