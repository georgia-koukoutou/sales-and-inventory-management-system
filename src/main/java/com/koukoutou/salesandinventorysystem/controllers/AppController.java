package com.koukoutou.salesandinventorysystem.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.koukoutou.salesandinventorysystem.repositories.CustomerRepository;
import com.koukoutou.salesandinventorysystem.repositories.OrderRepository;
import com.koukoutou.salesandinventorysystem.repositories.ProductRepository;

@Controller
public class AppController {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private OrderRepository orderRepository;

	@GetMapping("/")
	public String home(Model model) {

		long productCount = productRepository.count();
		long customerCount = customerRepository.count();
		long orderCount = orderRepository.count();

		model.addAttribute("product_count", productCount);
		model.addAttribute("customer_count", customerCount);
		model.addAttribute("order_count", orderCount);

		return "fragments/home";
	}

}