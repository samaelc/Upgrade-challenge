package com.samaelc.volcano.reservation.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.samaelc.volcano.reservation.exception.InvalidDatesException;
import com.samaelc.volcano.reservation.exception.ReservationException;
import com.samaelc.volcano.reservation.exception.ReservationNotAvailableException;
import com.samaelc.volcano.reservation.exception.ReservationNotFoundException;
import com.samaelc.volcano.reservation.model.Reservation;
import com.samaelc.volcano.reservation.repository.ReservationRepository;
import com.samaelc.volcano.reservation.validation.ReservationDateValidation;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.MockitoAnnotations.openMocks;

class ReservationServiceImplTest {

	private ReservationService reservationService;

	@Mock
	private ReservationRepository reservationRepository;
	private ReservationDateValidation reservationDateValidation;

	@BeforeEach
	public void setup() {
		openMocks(this);
		reservationDateValidation = new ReservationDateValidation();
		reservationService = new ReservationServiceImpl(reservationDateValidation, reservationRepository);
	}

	@Test
	public void testGetCampsiteAvailabilityValid() {
		when(reservationRepository.findReservationsInRage(any(), any())).thenReturn(Collections.emptyList());
		List<LocalDate> response = reservationService.getCampsiteAvailability(null, null);

		LocalDate startDate = LocalDate.now().plusDays(1);
		LocalDate endDate = startDate.plusMonths(1);

		assertEquals(startDate.datesUntil(endDate).collect(Collectors.toList()), response);

		Reservation reservation = new Reservation();
		reservation.setStartDate(startDate);
		reservation.setEndDate(startDate.plusDays(2));

		when(reservationRepository.findReservationsInRage(any(), any())).thenReturn(Lists.newArrayList(reservation));
		response = reservationService.getCampsiteAvailability(null, null);
		assertEquals(startDate.plusDays(2).datesUntil(endDate).collect(Collectors.toList()), response);
	}

	@Test
	public void testGetCampsiteAvailabilityWithWrongDates() {
		when(reservationRepository.findReservationsInRage(any(), any())).thenReturn(Collections.emptyList());

		assertThrows(InvalidDatesException.class,
				() -> reservationService.getCampsiteAvailability(LocalDate.now(), LocalDate.now()));

		assertThrows(InvalidDatesException.class,
				() -> reservationService.getCampsiteAvailability(LocalDate.now().minusDays(1), LocalDate.now().plusDays(2)));

		assertThrows(InvalidDatesException.class,
				() -> reservationService.getCampsiteAvailability(LocalDate.now().plusDays(1), LocalDate.now().minusDays(2)));

	}

	@Test
	public void findReservationExisting() {
		UUID reservationId = UUID.randomUUID();
		when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(new Reservation()));
		Reservation response = reservationService.findReservation(reservationId);
		assertNotNull(response);
	}

	@Test
	public void findReservationNotExistent() {
		UUID reservationId = UUID.randomUUID();
		when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());
		assertThrows(ReservationNotFoundException.class,
				() -> reservationService.findReservation(reservationId));
	}


	@Test
	public void createReservationValid() {
		Reservation reservationInDB = buildReservation(LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));
		Reservation reservation = buildReservation(LocalDate.now().plusDays(3), LocalDate.now().plusDays(5));

		when(reservationRepository.findReservationsInRage(any(), any())).thenReturn(Lists.newArrayList(reservationInDB));
		when(reservationRepository.save(any())).thenReturn(reservation);

		Reservation response = reservationService.createReservation(reservation);

		assertNotNull(response);
	}

	@Test
	public void createReservationWithNoAvailability() {
		Reservation reservationInDB = buildReservation(LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));
		Reservation reservation = buildReservation(LocalDate.now().plusDays(2), LocalDate.now().plusDays(4));

		when(reservationRepository.findReservationsInRage(any(), any())).thenReturn(Lists.newArrayList(reservationInDB));

		assertThrows(ReservationNotAvailableException.class,
				() -> reservationService.createReservation(reservation));
	}

	@Test
	public void cancelReservationValid() {
		UUID reservationId = UUID.randomUUID();
		when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(new Reservation()));
		when(reservationRepository.save(any())).thenReturn(new Reservation());

		boolean response = reservationService.cancelReservation(reservationId);
		assertTrue(response);
	}

	@Test
	public void cancelReservationNotExistent() {
		UUID reservationId = UUID.randomUUID();
		when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

		assertThrows(ReservationNotFoundException.class,
				() -> reservationService.cancelReservation(reservationId));
	}

	@Test
	public void updateReservationValid() {
		UUID reservationId = UUID.randomUUID();
		Reservation reservationInDB = buildReservation(LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));
		Reservation reservation = buildReservation(LocalDate.now().plusDays(3), LocalDate.now().plusDays(5));

		when(reservationRepository.findReservationsInRage(any(), any())).thenReturn(Lists.newArrayList(reservationInDB));
		when(reservationRepository.findById(reservationId)).thenReturn(Optional.ofNullable(reservation));
		when(reservationRepository.save(any())).thenReturn(reservation);

		Reservation response = reservationService.updateReservation(reservationId, reservation);

		assertNotNull(response);
	}

	@Test
	public void updateReservationWithNotAvailability() {
		UUID reservationId = UUID.randomUUID();
		Reservation reservationInDB = buildReservation(LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));
		Reservation oldReservation = buildReservation(LocalDate.now().plusDays(3), LocalDate.now().plusDays(5));
		Reservation newReservation = buildReservation(LocalDate.now().plusDays(2), LocalDate.now().plusDays(4));

		when(reservationRepository.findReservationsInRage(any(), any())).thenReturn(Lists.newArrayList(reservationInDB));
		when(reservationRepository.findById(reservationId)).thenReturn(Optional.ofNullable(oldReservation));

		assertThrows(ReservationNotAvailableException.class,
				() -> reservationService.updateReservation(reservationId, newReservation));
	}

	@Test
	public void updateReservationWithCanceledReservation() {
		UUID reservationId = UUID.randomUUID();
		Reservation reservationInDB = buildReservation(LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));
		Reservation oldReservation = buildReservation(LocalDate.now().plusDays(3), LocalDate.now().plusDays(5));
		oldReservation.setActive(false);
		Reservation newReservation = buildReservation(LocalDate.now().plusDays(2), LocalDate.now().plusDays(4));

		when(reservationRepository.findReservationsInRage(any(), any())).thenReturn(Lists.newArrayList(reservationInDB));
		when(reservationRepository.findById(reservationId)).thenReturn(Optional.ofNullable(oldReservation));

		assertThrows(ReservationException.class,
				() -> reservationService.updateReservation(reservationId, newReservation));
	}

	private Reservation buildReservation(LocalDate startDate, LocalDate endDate) {
		Reservation reservation = new Reservation();
		reservation.setStartDate(startDate);
		reservation.setEndDate(endDate);
		reservation.setActive(true);
		return reservation;
	}
}