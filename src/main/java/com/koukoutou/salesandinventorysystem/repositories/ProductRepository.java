package com.koukoutou.salesandinventorysystem.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.koukoutou.salesandinventorysystem.models.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	public List<Product> findByName(String name);

}
