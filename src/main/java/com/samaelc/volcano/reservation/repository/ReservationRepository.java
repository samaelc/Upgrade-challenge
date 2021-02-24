package com.samaelc.volcano.reservation.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import javax.persistence.LockModeType;

import com.samaelc.volcano.reservation.model.Reservation;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


public interface ReservationRepository extends CrudRepository<Reservation, UUID> {
	@Lock(LockModeType.PESSIMISTIC_READ)
	@Query("select r from Reservation r "
			+ "where ((r.startDate < ?1 and ?2 < r.endDate) "
			+ "or (?1 < r.endDate and r.endDate <= ?2) "
			+ "or (?1 <= r.startDate and r.startDate <=?2)) "
			+ "and r.active = true "
			+ "order by r.startDate asc")
	List<Reservation> findReservationsInRage(LocalDate startDate, LocalDate endDate);
}
