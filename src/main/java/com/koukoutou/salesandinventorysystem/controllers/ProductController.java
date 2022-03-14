package com.koukoutou.salesandinventorysystem.controllers;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.koukoutou.salesandinventorysystem.models.Product;
import com.koukoutou.salesandinventorysystem.repositories.ProductRepository;

@Controller
public class ProductController {

	@Autowired
	private ProductRepository productRepository;

	@GetMapping("/products")
	public String products(Model model, @RequestParam(required = false, name = "page", defaultValue = "1") int page,
			@RequestParam(required = false, name = "size", defaultValue = "50") int size) {

		if (page > 0) {
			page--;
		}
		Pageable pageInfo = PageRequest.of(page, size);

		Page<Product> productPage = productRepository.findAll(pageInfo);
		model.addAttribute("product_page", productPage);

		return "fragments/products";
	}

	@GetMapping(value = { "/product", "/product/{id}" })
	public String viewProduct(Model model, @PathVariable(required = false) Long id) {

		if (id != null) {
			Optional<Product> product = productRepository.findById(id);
			if (product.isPresent()) {
				model.addAttribute("product", product.get());
			} else {
				// TODO return not found
			}
		} else {
			model.addAttribute("product", new Product());
		}

		return "fragments/product_form";
	}

	@PostMapping(value = { "/product" })
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String createUpdateProduct(@Valid Product product, Errors errors, Model model) {

		if (errors != null && errors.getErrorCount() > 0) {
			return "fragments/product_form";
		} else {
			productRepository.save(product);
			return "redirect:/products";
		}
	}

	@PostMapping("/products/delete")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String deleteProduct(@RequestParam("id") Long id) {

		productRepository.deleteById(id);

		return "redirect:/products";
	}
}
