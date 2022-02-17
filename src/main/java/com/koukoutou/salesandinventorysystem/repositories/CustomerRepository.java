package com.koukoutou.salesandinventorysystem.repositories;

import org.springframework.data.repository.CrudRepository;

import com.koukoutou.salesandinventorysystem.models.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Long>{

}
