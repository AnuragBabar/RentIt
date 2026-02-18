package com.booking.service;

import java.util.List;

import com.booking.dto.*;

public interface BookingServiceInterface {

	public BookingResponseDto createBooking(BookingRequestDto bookingDto);

	public BookingResponseDto approveBooking(Long bookingId);

	public BookingResponseDto rejectBooking(Long bookingId);

	List<BookingResponseDto> getBookingsByOwner(Long ownerId);

	List<BookingResponseDto> getBookingsByRenter(Long renterId);

	BookingResponseDto getBookingById(Long id);

	public BookingResponseDto confirmBooking(Long id);

	public BookingResponseDto cancelBooking(Long id);

}
