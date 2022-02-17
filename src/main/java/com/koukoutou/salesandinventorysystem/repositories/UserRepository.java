package com.koukoutou.salesandinventorysystem.repositories;

import org.springframework.data.repository.CrudRepository;

import com.koukoutou.salesandinventorysystem.models.User;

public interface UserRepository extends CrudRepository<User, Long>{

}