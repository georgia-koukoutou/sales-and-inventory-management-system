package com.koukoutou.salesandinventorysystem.controllers;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.koukoutou.salesandinventorysystem.models.User;
import com.koukoutou.salesandinventorysystem.models.User.Role;
import com.koukoutou.salesandinventorysystem.repositories.UserRepository;

@Controller
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping("/users")
	public String users(Model model, @RequestParam(required = false, name = "page", defaultValue = "1") int page,
			@RequestParam(required = false, name = "size", defaultValue = "50") int size) {

		if (page > 0) {
			page--;
		}
		Pageable pageInfo = PageRequest.of(page, size);

		Page<User> userPage = userRepository.findAllByRole(pageInfo, Role.USER);
		model.addAttribute("user_page", userPage);

		return "fragments/users";
	}

	@PostMapping("/users/delete")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String deleteUser(@RequestParam("id") Long id) {

		userRepository.deleteById(id);

		return "redirect:/users";
	}

	@GetMapping(value = { "/user", "/user/{id}" })
	public String viewUser(Model model, @PathVariable(required = false) Long id) {

		if (id != null) {
			Optional<User> user = userRepository.findById(id);
			if (user.isPresent()) {
				model.addAttribute("user", user.get());
			} else {
				// TODO return not found
			}
		} else {
			model.addAttribute("user", new User());
		}

		return "fragments/user_form";
	}

	@PostMapping(value = { "/user" })
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String createUpdateUser(@Valid User user, Errors errors, Model model) {

		if (errors != null && errors.getErrorCount() > 0) {
			return "fragments/user_form";
		} else {
			String encodedPassword = passwordEncoder.encode(user.getPassword());
			user.setPassword(encodedPassword);
			user.setRole(Role.USER);
			userRepository.save(user);
			return "redirect:/users";
		}
	}

	@GetMapping("/login")
	public String showLoginForm(Model model) {

		User user = new User();
		model.addAttribute("user", user);

		return "fragments/login_form";
	}

	@PostMapping("/login")
	public String loginUser() {

		return "home";
	}

	@PostMapping("/logout")
	public String logoutUser() {

		return "fragments/login_form";
	}

	@GetMapping("/login-error")
	public String showLoginError(Model model) {

		return "fragments/login_error";
	}

	@GetMapping("/account")
	public String showAccountInfo(@AuthenticationPrincipal User user, Model model) {

		long id = user.getId();
		Optional<User> currentUser = userRepository.findById(id);
		model.addAttribute("user", currentUser.get());

		return "fragments/account";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/account")
	public String updateAccountInfo(@Valid User user, Errors errors, Model model) {

		if (errors != null && errors.getErrorCount() > 0) {
			return "fragments/account";
		} else {
			String encodedPassword = passwordEncoder.encode(user.getPassword());
			user.setPassword(encodedPassword);
			user.setRole(Role.ADMIN);
			userRepository.save(user);
		}

		return "redirect:/account";
	}
}
