package com.blogdirectorio.affiliate;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class AffiliateApplication extends SpringBootServletInitializer  {

	public static void main(String[] args) {
		SpringApplication.run(AffiliateApplication.class, args);
		System.out.println("Affiliate runnning...");
	}
	

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(AffiliateApplication.class);
    }

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();

		// Prevent null values from overwriting existing values
		modelMapper.getConfiguration().setSkipNullEnabled(true);

		return modelMapper;
	}
	
}
