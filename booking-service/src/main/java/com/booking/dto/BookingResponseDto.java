package com.booking.dto;

import java.time.LocalDate;

import com.booking.entity.BookingStatus;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponseDto {

    private Long id;
    private Long itemId;
    private Long ownerId;
    private Long renterId;
    private double pricePerDay;
    private LocalDate startDate;
    private LocalDate endDate;
    private BookingStatus status;
    private String itemName;
    private String renterName;
    private String ownerName;
}
