package com.blogdirectorio.affiliate;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AffiliateApplication {

	public static void main(String[] args) {
		SpringApplication.run(AffiliateApplication.class, args);
		System.out.println("Affiliate runnning...");
	}

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();

		// Prevent null values from overwriting existing values
		modelMapper.getConfiguration().setSkipNullEnabled(true);

		return modelMapper;
	}
}
