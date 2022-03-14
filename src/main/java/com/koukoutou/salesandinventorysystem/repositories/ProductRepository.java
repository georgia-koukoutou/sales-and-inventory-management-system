package com.koukoutou.salesandinventorysystem.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.koukoutou.salesandinventorysystem.models.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	public List<Product> findByName(String name);

	@Modifying
	@Query("update Product set quantity = quantity + :quantity where id = :id")
	void updateQuantity(@Param(value = "id") long id, @Param(value = "quantity") int quantity);

}
