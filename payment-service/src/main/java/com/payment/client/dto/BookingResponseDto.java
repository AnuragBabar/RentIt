package com.payment.client.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class BookingResponseDto {

	private Long id;
    private Long itemId;
    private Long ownerId;
    private Long renterId;
    private LocalDate startDate;
    private LocalDate endDate;
    private double pricePerDay;
    private String status;
}
