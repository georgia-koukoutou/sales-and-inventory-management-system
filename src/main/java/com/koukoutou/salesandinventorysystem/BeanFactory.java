package com.koukoutou.salesandinventorysystem;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;

@Configuration
public class BeanFactory {

	@Bean
	public LayoutDialect layoutDialect() {

		return new LayoutDialect();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {

		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}

}
