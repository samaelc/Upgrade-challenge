package com.samaelc.volcano.reservation.service;

import java.time.LocalDate;
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
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

	private final ReservationDateValidation reservationDateValidation;
	private final ReservationRepository reservationRepository;

	@Override
	@Transactional(readOnly = true)
	public List<LocalDate> getCampsiteAvailability(LocalDate startDate, LocalDate endDate) {
		if (startDate == null) {
			startDate = LocalDate.now().plusDays(1);
		}
		if (endDate == null) {
			endDate = startDate.plusMonths(1);
		}

		boolean validAvailabilityDates = reservationDateValidation.isValidDatesForAvailability(startDate, endDate);

		if (validAvailabilityDates) {
			List<LocalDate> dateRange = startDate.datesUntil(endDate).collect(Collectors.toList());
			List<Reservation> reservedDates = reservationRepository.findReservationsInRage(startDate, endDate);

			reservedDates.stream().forEach(reservation -> dateRange.removeAll(reservation.getStartDate().datesUntil(reservation.getEndDate()).collect(Collectors.toList())));

			return dateRange;
		}
		else {
			throw new InvalidDatesException("The dates provided are nor valid");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Reservation findReservation(final UUID reservationId) {
		Optional<Reservation> reservation = reservationRepository.findById(reservationId);
		if (reservation.isEmpty()) {
			throw new ReservationNotFoundException("Reservation not found with id " + reservationId);
		}
		return reservation.get();
	}

	@Override
	@Transactional
	public Reservation createReservation(final Reservation reservation) {
		if (null != reservation.getId()) {
			throw new ReservationException("Id should not be provided for new reservation");
		}

		return executeReservation(reservation, false, null);
	}

	@Override
	@Transactional
	public boolean cancelReservation(final UUID reservationId) {
		Reservation reservation = findReservation(reservationId);
		reservation.setActive(false);
		reservation = reservationRepository.save(reservation);
		return !reservation.isActive();
	}

	@Override
	@Transactional
	public Reservation updateReservation(final UUID reservationId, final Reservation reservation) {
		Reservation dbReservation = findReservation(reservationId);
		if (!dbReservation.isActive()) {
			throw new ReservationException("The reservation is already canceled");
		}
		return executeReservation(reservation, true, dbReservation);
	}

	private Reservation executeReservation(final Reservation reservation, final boolean releaseReservation, final Reservation dbReservation) {
		boolean validAvailabilityDates = reservationDateValidation.isValidDatesForReservation(reservation.getStartDate(), reservation.getEndDate());

		if (validAvailabilityDates) {
			List<LocalDate> availability = getAvailabilityForReservation(reservation, releaseReservation, dbReservation);

			if (!availability.containsAll(reservation.getStartDate().datesUntil(reservation.getEndDate()).collect(Collectors.toList()))) {
				throw new ReservationNotAvailableException("Reservation not available for current dates");
			}
			reservation.setActive(true);
			return reservationRepository.save(reservation);
		}
		else {
			throw new InvalidDatesException("The dates provided are nor valid");
		}
	}

	private List<LocalDate> getAvailabilityForReservation(final Reservation reservation, final boolean releaseReservation, final Reservation dbReservation) {
		List<LocalDate> availability = getCampsiteAvailability(reservation.getStartDate(), reservation.getEndDate());
		if(releaseReservation) {
			availability.addAll(dbReservation.getStartDate().datesUntil(reservation.getEndDate()).collect(Collectors.toList()));
		}
		return availability;
	}
}
