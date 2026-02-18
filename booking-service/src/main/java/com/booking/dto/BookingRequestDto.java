package com.booking.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class BookingRequestDto {

	@NotNull
	private Long itemId;
	
	@NotNull
	private Long ownerId;
	
	@NotNull
	private Long renterId;
	
	@NotNull
	private double pricePerDay;
	
	@NotNull
	private LocalDate startDate;
	
	@NotNull
	private LocalDate endDate;
}
