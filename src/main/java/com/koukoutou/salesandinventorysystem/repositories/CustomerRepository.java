package com.koukoutou.salesandinventorysystem.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.koukoutou.salesandinventorysystem.models.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

	public List<Customer> findByName(String name);

}
