package com.booking.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.booking.dto.BookingRequestDto;
import com.booking.dto.BookingResponseDto;
import com.booking.security.RoleValidator;
import com.booking.service.BookingServiceInterface;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/bookings")
@AllArgsConstructor
public class BookingController {

	BookingServiceInterface service;

	@PostMapping
	public BookingResponseDto createBooking(@Valid @RequestBody BookingRequestDto dto, HttpServletRequest request) {
		RoleValidator.requireRole(request, "RENTER");
		return service.createBooking(dto);
	}

	@PutMapping("/{id}/approve")
	public BookingResponseDto approve(@PathVariable Long id, HttpServletRequest request) {
		RoleValidator.requireRole(request, "OWNER");
		return service.approveBooking(id);
	}

	@PutMapping("/{id}/reject")
	public BookingResponseDto reject(@PathVariable Long id, HttpServletRequest request) {
		RoleValidator.requireRole(request, "OWNER");
		return service.rejectBooking(id);
	}

	@GetMapping("/owner/{ownerId}")
	public List<BookingResponseDto> getByOwner(@PathVariable Long ownerId) {
		return service.getBookingsByOwner(ownerId);
	}

	@GetMapping("/renter/{renterId}")
	public List<BookingResponseDto> getByRenter(@PathVariable Long renterId) {
		return service.getBookingsByRenter(renterId);
	}

	@GetMapping("/{id}")
	public BookingResponseDto getBookingById(@PathVariable Long id) {
		return service.getBookingById(id);
	}

	@PutMapping("/{id}/confirm")
	public BookingResponseDto confirmBooking(@PathVariable Long id) {
		return service.confirmBooking(id);
	}

	@PutMapping("/{id}/cancel")
	public BookingResponseDto cancelBooking(@PathVariable Long id, HttpServletRequest request) {
		RoleValidator.requireRole(request, "RENTER");
		return service.cancelBooking(id);
	}

}
