package com.koukoutou.salesandinventorysystem.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.koukoutou.salesandinventorysystem.models.Customer;
import com.koukoutou.salesandinventorysystem.models.Order;
import com.koukoutou.salesandinventorysystem.models.OrderDetails;
import com.koukoutou.salesandinventorysystem.models.Product;
import com.koukoutou.salesandinventorysystem.repositories.CustomerRepository;
import com.koukoutou.salesandinventorysystem.repositories.OrderDetailsRepository;
import com.koukoutou.salesandinventorysystem.repositories.OrderRepository;
import com.koukoutou.salesandinventorysystem.repositories.ProductRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class OrderController {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderDetailsRepository orderDetailsRepository;

	@GetMapping("/orders")
	public String orders(Model model, @RequestParam(required = false, name = "page", defaultValue = "1") int page,
			@RequestParam(required = false, name = "size", defaultValue = "50") int size) {

		if (page > 0) {
			page--;
		}
		Pageable pageInfo = PageRequest.of(page, size);

		Page<Order> orderPage = orderRepository.findAll(pageInfo);
		model.addAttribute("order_page", orderPage);

		return "fragments/orders";
	}

	@GetMapping(value = { "/order", "/order/{id}" })
	public String viewOrder(Model model, @PathVariable(required = false) Long id) {

		List<Customer> customers = customerRepository.findAll();
		List<Product> products = productRepository.findAll();

		model.addAttribute("customers", customers);
		model.addAttribute("products", products);

		if (id != null) {
			Optional<Order> order = orderRepository.findById(id);
			if (order.isPresent()) {
				model.addAttribute("order", order.get());
				model.addAttribute("orderDetails", orderDetailsRepository.findAllByOrderId(id));
			} else {
				// TODO return not found
			}
		} else {
			model.addAttribute("order", new Order());
		}

		return "fragments/order_form";
	}

	@Transactional
	@PostMapping(value = { "/order" })
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String createUpdateOrder(@Valid Order order, HttpServletRequest request, Errors errors, Model model) {

		if (errors != null && errors.getErrorCount() > 0) {
			return "fragments/order_form";
		} else {

			String[] productIds = request.getParameterValues("order_detail_product");
			String[] quantities = request.getParameterValues("order_detail_qty");
			String[] prices = request.getParameterValues("order_detail_price");

			List<OrderDetails> existingOrderDetailsList = orderDetailsRepository.findAllByOrderId(order.getId());
			List<OrderDetails> newOrderDetailsList = new ArrayList<OrderDetails>();
			double totalAmount = 0;

			if (productIds != null && productIds.length > 0 && quantities != null && quantities.length > 0) {

				for (int i = 0; i < productIds.length; i++) {

					Product product = productRepository.findById(Long.parseLong(productIds[i])).get();
					int quantity = Integer.parseInt(quantities[i]);
					// by default use the current price of the selected product
					double price = product.getPrice().doubleValue();

					if (prices != null && i < prices.length) {
						// if it's an existing details row use it's price value
						// cause product price might have changed in the meantime
						price = Double.parseDouble(prices[i]);
					}

					price = Math.round(price * 100.0) / 100.0;
					OrderDetails orderDetails = new OrderDetails();
					orderDetails.setProduct(product);
					orderDetails.setPrice(price);
					orderDetails.setQuantity(quantity);
					orderDetails.setOrder(order);

					newOrderDetailsList.add(orderDetails);

					totalAmount += quantity * price;
				}
			}

			order.setTotalAmount(Math.round(totalAmount * 100.0) / 100.0);

			order = orderRepository.save(order);
			// delete existing order details
			orderDetailsRepository.deleteAllByOrderId(order.getId());
			// save new order details
			orderDetailsRepository.saveAll(newOrderDetailsList);
			// update product quantities
			updateProductQuantities(existingOrderDetailsList, newOrderDetailsList);

			return "redirect:/orders";
		}
	}

	@Transactional
	@PostMapping("/orders/delete")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String deleteOrder(@RequestParam("id") Long id) {

		List<OrderDetails> existingOrderDetailsList = orderDetailsRepository.findAllByOrderId(id);
		orderDetailsRepository.deleteAllByOrderId(id);
		orderRepository.deleteById(id);
		// update product quantities
		updateProductQuantities(existingOrderDetailsList, null);

		return "redirect:/orders";
	}

	private void updateProductQuantities(List<OrderDetails> existingOrderDetailsList,
			List<OrderDetails> newOrderDetailsList) {

		Map<Long, Integer> productQuantities = new HashMap<Long, Integer>();

		if (!CollectionUtils.isEmpty(existingOrderDetailsList)) {
			for (OrderDetails detail : existingOrderDetailsList) {
				int qty = productQuantities.getOrDefault(detail.getProduct().getId(), 0);

				qty += detail.getQuantity();
				productQuantities.put(detail.getProduct().getId(), qty);
			}
		}

		if (!CollectionUtils.isEmpty(newOrderDetailsList)) {
			for (OrderDetails detail : newOrderDetailsList) {
				int qty = productQuantities.getOrDefault(detail.getProduct().getId(), 0);

				qty -= detail.getQuantity();
				productQuantities.put(detail.getProduct().getId(), qty);
			}
		}

		productQuantities.entrySet().forEach(entry -> {

			productRepository.updateQuantity(entry.getKey(), entry.getValue());
		});

	}

}