package com.koukoutou.salesandinventorysystem.repositories;

import org.springframework.data.repository.CrudRepository;

import com.koukoutou.salesandinventorysystem.models.OrderDetails;

public interface OrderDetailsRepository extends CrudRepository<OrderDetails, Long>{

}
