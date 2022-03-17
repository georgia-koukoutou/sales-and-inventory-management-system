package com.koukoutou.salesandinventorysystem.controllers;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;
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

import com.koukoutou.salesandinventorysystem.models.Customer;
import com.koukoutou.salesandinventorysystem.repositories.CustomerRepository;

@Controller
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("/customers")
    public String customers(Model model, @RequestParam(required = false, name = "page", defaultValue = "1") int page,
            @RequestParam(required = false, name = "size", defaultValue = "50") int size) {

        if (page > 0) {
            page--;
        }
        Pageable pageInfo = PageRequest.of(page, size);

        Page<Customer> customerPage = customerRepository.findAll(pageInfo);
        model.addAttribute("customer_page", customerPage);

        return "fragments/customers";
    }

    @GetMapping(value = { "/customer", "/customer/{id}" })
    public String viewCustomer(Model model, @PathVariable(required = false) Long id) {

        if (id != null) {
            Optional<Customer> customer = customerRepository.findById(id);
            if (customer.isPresent()) {
                model.addAttribute("customer", customer.get());
            } else {
                throw new EntityNotFoundException();
            }
        } else {
            model.addAttribute("customer", new Customer());
        }

        return "fragments/customer_form";
    }

    @PostMapping(value = { "/customer" })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String createUpdateCustomer(@Valid Customer customer, Errors errors, Model model) {

        if (errors != null && errors.getErrorCount() > 0) {
            return "fragments/customer_form";
        } else {
            customerRepository.save(customer);
            return "redirect:/customers";
        }
    }

    @PostMapping("/customers/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteCustomer(@RequestParam("id") Long id) {

        customerRepository.deleteById(id);

        return "redirect:/customers";
    }
}
