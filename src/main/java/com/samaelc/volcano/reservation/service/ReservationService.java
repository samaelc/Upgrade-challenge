package com.samaelc.volcano.reservation.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.samaelc.volcano.reservation.model.Reservation;

public interface ReservationService {

	List<LocalDate> getCampsiteAvailability(LocalDate startDate, LocalDate endDate);

	Reservation findReservation(UUID reservationId);

	Reservation createReservation(Reservation reservation);

	boolean cancelReservation(UUID reservationId);

	Reservation updateReservation(UUID reservationId, Reservation reservation);
}
