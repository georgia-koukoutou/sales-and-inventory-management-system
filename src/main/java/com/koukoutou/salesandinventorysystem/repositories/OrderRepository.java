package com.koukoutou.salesandinventorysystem.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.koukoutou.salesandinventorysystem.models.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
