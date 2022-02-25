package com.koukoutou.salesandinventorysystem.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.koukoutou.salesandinventorysystem.repositories.OrderDetailsRepository;

@Controller
public class OrderDetailsController {

	@Autowired
	private OrderDetailsRepository orderDetailsRepository;
}