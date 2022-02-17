package com.koukoutou.salesandinventorysystem;

import org.springframework.context.annotation.Bean;

import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;

public class BeanFactory {
	
	@Bean
	public LayoutDialect layoutDialect() {
		
	    return new LayoutDialect();
	}

}
