package com.koukoutou.salesandinventorysystem.repositories;

import org.springframework.data.repository.CrudRepository;

import com.koukoutou.salesandinventorysystem.models.Order;

public interface OrderRepository extends CrudRepository<Order, Long>{

}
