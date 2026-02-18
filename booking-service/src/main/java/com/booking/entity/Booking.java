package com.booking.entity;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long itemId;
	private Long ownerId;
	private Long renterId;
	
	private LocalDate startDate;
	private LocalDate endDate;
	private double pricePerDay;
	@Enumerated(EnumType.STRING)
	private BookingStatus status;

}
