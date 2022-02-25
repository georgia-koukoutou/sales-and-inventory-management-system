package com.koukoutou.salesandinventorysystem.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.koukoutou.salesandinventorysystem.models.OrderDetails;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {

	List<OrderDetails> findAllByOrderId(Long orderId);

	void deleteAllByOrderId(Long orderId);

}
