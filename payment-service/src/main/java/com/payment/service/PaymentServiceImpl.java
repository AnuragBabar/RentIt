package com.payment.service;

import java.time.temporal.ChronoUnit;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.payment.client.BookingClient;
import com.payment.client.dto.BookingResponseDto;
import com.payment.dto.PaymentRequestDto;
import com.payment.dto.PaymentResponseDto;
import com.payment.entity.Payment;
import com.payment.entity.PaymentStatus;
import com.payment.exception.BookingNotFoundException;
import com.payment.exception.DuplicatePaymentException;
import com.payment.exception.InvalidPaymentException;
import com.payment.logging.CentralLogger;
import com.payment.repository.PaymentRepository;

import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentServiceInterface {

	private final ModelMapper modelMapper;
	private final PaymentRepository paymentRepo;
	private final BookingClient bookingClient;
	private final CentralLogger centralLogger;

	@Override
	public PaymentResponseDto makePayment(PaymentRequestDto paymentDto) {

		paymentRepo.findByBookingId(paymentDto.getBookingId())
				.ifPresent(payment -> {
					throw new DuplicatePaymentException(
							"Payment already exists for booking id " + paymentDto.getBookingId());
				});

		BookingResponseDto booking;
		try {
			booking = bookingClient.getBookingById(paymentDto.getBookingId());
		} catch (FeignException.NotFound ex) {
			throw new BookingNotFoundException(
					"Booking not found with id " + paymentDto.getBookingId());
		}

		long days = ChronoUnit.DAYS.between(booking.getStartDate(), booking.getEndDate());

		if (days <= 0) {
			throw new InvalidPaymentException("Invalid booking duration");
		}

		double expectedAmount = days * booking.getPricePerDay();

		if (Double.compare(paymentDto.getAmount(), expectedAmount) != 0) {
			throw new InvalidPaymentException(
					"Incorrect payment amount. Expected amount :- " + expectedAmount);
		}

		Payment payment = new Payment();
		payment.setBookingId(paymentDto.getBookingId());
		payment.setAmount(paymentDto.getAmount());
		payment.setStatus(PaymentStatus.SUCCESS);

		Payment savedPayment = paymentRepo.save(payment);

		bookingClient.confirmBooking(paymentDto.getBookingId());

		centralLogger.log(
				"PAYMENT_SUCCESS | bookingId="
						+ paymentDto.getBookingId()
						+ " | amount="
						+ paymentDto.getAmount());

		return modelMapper.map(savedPayment, PaymentResponseDto.class);

	}

}
