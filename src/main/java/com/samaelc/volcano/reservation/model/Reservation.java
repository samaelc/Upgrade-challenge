package com.samaelc.volcano.reservation.model;

import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "reservation")
@Data
public class Reservation {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(
			name = "UUID",
			strategy = "org.hibernate.id.UUIDGenerator"
	)
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@Version
	@Column(name = "version", nullable = false)
	@JsonIgnore
	private Long version;

	@NotNull
	@Column(name = "email", nullable = false)
	private String email;

	@NotNull
	@Column(name = "full_name", nullable = false)
	private String fullName;

	@NotNull
	@Column(name = "start_date", nullable = false)
	private LocalDate startDate;

	@NotNull
	@Column(name = "end_date", nullable = false)
	private LocalDate endDate;

	@JsonIgnore
	@Column(name = "active", nullable = false)
	private boolean active;
}
