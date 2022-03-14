package com.koukoutou.salesandinventorysystem.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.koukoutou.salesandinventorysystem.models.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Page<User> findAllByRole(Pageable pageInfo, User.Role role);

	User findByUsername(String username);
}