package com.koukoutou.salesandinventorysystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.koukoutou.salesandinventorysystem.repositories.UserRepository;

@SpringBootApplication
public class SalesAndInventorySystemApplication {

	UserRepository userRepository;

	public static void main(String[] args) {

		SpringApplication.run(SalesAndInventorySystemApplication.class, args);
	}

}
