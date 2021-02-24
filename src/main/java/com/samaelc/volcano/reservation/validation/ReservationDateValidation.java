package com.samaelc.volcano.reservation.validation;

import java.time.LocalDate;
import java.time.Period;

import org.springframework.stereotype.Component;

@Component
public class ReservationDateValidation {

	public boolean isValidDatesForAvailability(final LocalDate startDate, final LocalDate endDate) {
		LocalDate currentDate = LocalDate.now();
		return startDate.isAfter(currentDate) && endDate.isAfter(currentDate) && !startDate.equals(endDate);
	}

	public boolean isValidDatesForReservation(final LocalDate startDate, final LocalDate endDate) {
		LocalDate currentDate = LocalDate.now();
		return startDate.isAfter(currentDate) && endDate.isAfter(currentDate) && !startDate.equals(endDate) &&
				Period.between(startDate, endDate).getDays() <= 3;
	}
}
