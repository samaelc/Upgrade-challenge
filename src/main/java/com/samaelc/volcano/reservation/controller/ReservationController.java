package com.samaelc.volcano.reservation.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import com.samaelc.volcano.reservation.exception.InvalidDatesException;
import com.samaelc.volcano.reservation.exception.ReservationNotAvailableException;
import com.samaelc.volcano.reservation.exception.ReservationNotFoundException;
import com.samaelc.volcano.reservation.model.Reservation;
import com.samaelc.volcano.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("volcano-reservation")
@RequiredArgsConstructor
public class ReservationController {

	private final ReservationService reservationService;

	@GetMapping(value = "/availability", produces = "application/json")
	public ResponseEntity<List<LocalDate>> getCampsiteAvailability(
			@RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
		return ResponseEntity.status(HttpStatus.OK).body(reservationService.getCampsiteAvailability(startDate, endDate));
	}

	@GetMapping(value = "/reservation/{reservationId}", produces = "application/json")
	public ResponseEntity<Reservation> getReservation(@PathVariable("reservationId") UUID reservationId) {
		return ResponseEntity.status(HttpStatus.OK).body(reservationService.findReservation(reservationId));
	}

	@PostMapping(value = "/reservation", produces = "application/json")
	public ResponseEntity<Reservation> createReservation(@RequestBody @Valid Reservation reservation) {
		return ResponseEntity.status(HttpStatus.OK).body(reservationService.createReservation(reservation));
	}

	@DeleteMapping(value = "/reservation/{reservationId}", produces = "application/json")
	public ResponseEntity<UUID> cancelReservation(@PathVariable("reservationId") UUID reservationId) {
		if (reservationService.cancelReservation(reservationId)) {
			return new ResponseEntity<>(reservationId, HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(value = "/reservation/{reservationId}", produces = "application/json")
	public ResponseEntity<Reservation> updateReservation(@PathVariable("reservationId") UUID reservationId, @RequestBody @Valid Reservation reservation) {
		return ResponseEntity.status(HttpStatus.OK).body(reservationService.updateReservation(reservationId, reservation));
	}
}
