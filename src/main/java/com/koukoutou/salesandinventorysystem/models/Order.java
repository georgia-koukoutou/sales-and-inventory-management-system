package com.koukoutou.salesandinventorysystem.models;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@NotNull
	@Column(name = "total_amount", nullable = false)
	private double totalAmount;

	@NotNull
	@Column(name = "date_ordered", nullable = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateOrdered;

	@NotNull
	@ManyToOne()
	@JoinColumn(name = "customer_id")
	private Customer customer;
}
