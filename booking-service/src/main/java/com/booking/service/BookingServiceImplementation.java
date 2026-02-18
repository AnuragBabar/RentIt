package com.booking.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.booking.client.ItemServiceClient;
import com.booking.client.UserServiceClient;
import com.booking.client.dto.ItemResponse;
import com.booking.dto.BookingRequestDto;
import com.booking.dto.BookingResponseDto;
import com.booking.entity.Booking;
import com.booking.entity.BookingStatus;
import com.booking.exception.BookingNotFoundException;
import com.booking.exception.InvalidBookingStateException;
import com.booking.exception.ItemNotAvailableException;
import com.booking.logging.CentralLogger;
import com.booking.repository.BookingRepository;

import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class BookingServiceImplementation implements BookingServiceInterface {

	private final ModelMapper modelMapper;
	public final BookingRepository bookingRepo;
	private final ItemServiceClient itemClient;
	private final UserServiceClient userClient;
	private final CentralLogger centralLogger;

	@Override
	public BookingResponseDto createBooking(BookingRequestDto bookingDto) {

		ItemResponse item = itemClient.getItemById(bookingDto.getItemId());

		if (item == null || !item.isAvailable()) {
			throw new ItemNotAvailableException("Item is not available for booking");
		}

		Booking booking = modelMapper.map(bookingDto, Booking.class);
		booking.setPricePerDay(item.getPricePerDay());
		booking.setStatus(BookingStatus.REQUESTED);
		Booking saveBooking = bookingRepo.save(booking);

		BookingResponseDto response = modelMapper.map(saveBooking, BookingResponseDto.class);
		response.setItemName(item.getItemName());
		return response;
	}

	@Override
	public BookingResponseDto approveBooking(Long bookingId) {
		Booking booking = bookingRepo.findById(bookingId)
				.orElseThrow(() -> new BookingNotFoundException("Booking not found.."));

		try {
			itemClient.markItemAsBooked(booking.getItemId());
		} catch (FeignException e) {
			if (e.status() == 400) {
				throw new ItemNotAvailableException("Item is already booked and cannot be approved.");
			}
			throw e;
		}

		booking.setStatus(BookingStatus.APPROVED);
		centralLogger.log("Booking-Approved | Booking Id = " + bookingId);
		return enrichBookingDto(bookingRepo.save(booking));
	}

	@Override
	public BookingResponseDto rejectBooking(Long bookingId) {
		Booking booking = bookingRepo.findById(bookingId)
				.orElseThrow(() -> new BookingNotFoundException("Booking not found.."));

		if (booking.getStatus() == BookingStatus.CONFIRMED) {
			throw new InvalidBookingStateException(
					"Confirmed booking cannot be rejected");
		}

		if (booking.getStatus() == BookingStatus.APPROVED) {
			itemClient.markItemAsAvailable(booking.getItemId());
		}

		booking.setStatus(BookingStatus.REJECTED);
		bookingRepo.save(booking);
		centralLogger.log(
				"BOOKING_REJECTED | bookingId=" + bookingId);
		return enrichBookingDto(booking);
	}

	@Override
	public List<BookingResponseDto> getBookingsByOwner(Long ownerId) {
		return bookingRepo.findByOwnerId(ownerId)
				.stream()
				.map(this::enrichBookingDto)
				.collect(Collectors.toList());
	}

	@Override
	public List<BookingResponseDto> getBookingsByRenter(Long renterId) {
		return bookingRepo.findByRenterId(renterId)
				.stream()
				.map(this::enrichBookingDto)
				.collect(Collectors.toList());
	}

	@Override
	public BookingResponseDto getBookingById(Long id) {
		Booking booking = bookingRepo.findById(id)
				.orElseThrow(() -> new BookingNotFoundException("Booking not found with id " + id));

		System.out.println(
				">>> BOOKING pricePerDay FROM ENTITY = " + booking.getPricePerDay());

		return enrichBookingDto(booking);
	}

	@Override
	public BookingResponseDto confirmBooking(Long id) {
		Booking booking = bookingRepo.findById(id)
				.orElseThrow(() -> new BookingNotFoundException("Booking not found with id " + id));

		if (booking.getStatus() == BookingStatus.CONFIRMED) {
			return enrichBookingDto(booking);
		}

		if (booking.getStatus() != BookingStatus.APPROVED) {
			throw new InvalidBookingStateException("only Approved bookings can be confirmed");
		}

		booking.setStatus(BookingStatus.CONFIRMED);
		bookingRepo.save(booking);

		centralLogger.log("BOOKING_CONFIRMED | booking id = " + id);

		return enrichBookingDto(booking);
	}

	@Override
	public BookingResponseDto cancelBooking(Long id) {
		Booking booking = bookingRepo.findById(id)
				.orElseThrow(() -> new BookingNotFoundException("Booking not found with id " + id));

		if (booking.getStatus() == BookingStatus.APPROVED) {
			itemClient.markItemAsAvailable(booking.getItemId());
		}

		booking.setStatus(BookingStatus.CANCELLED);
		bookingRepo.save(booking);

		centralLogger.log("BOOKING_CANCELLED | booking id = " + id);

		return enrichBookingDto(booking);
	}

	private BookingResponseDto enrichBookingDto(Booking booking) {
		BookingResponseDto dto = modelMapper.map(booking, BookingResponseDto.class);

		try {
			// Fetch Item details
			ItemResponse item = itemClient.getItemById(booking.getItemId());
			if (item != null) {
				dto.setItemName(item.getItemName());
			}

			// Fetch Renter details
			if (booking.getRenterId() != null) {
				com.booking.client.dto.UserResponse renter = userClient.getUserById(booking.getRenterId());
				if (renter != null) {
					dto.setRenterName(renter.getUserName());
				}
			}

			// Fetch Owner details
			if (booking.getOwnerId() != null) {
				com.booking.client.dto.UserResponse owner = userClient.getUserById(booking.getOwnerId());
				if (owner != null) {
					dto.setOwnerName(owner.getUserName());
				}
			}
		} catch (Exception e) {
			// Log error but generally return what we have
			System.err.println("Failed to enrich booking details: " + e.getMessage());
		}

		return dto;
	}

}
