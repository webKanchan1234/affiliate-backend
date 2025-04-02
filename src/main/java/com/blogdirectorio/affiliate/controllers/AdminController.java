package com.blogdirectorio.affiliate.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogdirectorio.affiliate.dto.CategoryDto;
import com.blogdirectorio.affiliate.dto.UserDto;
import com.blogdirectorio.affiliate.payloads.ApiResponse;
import com.blogdirectorio.affiliate.services.CategoryServices;
import com.blogdirectorio.affiliate.services.ProductServices;
import com.blogdirectorio.affiliate.services.UserServices;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/api/admin")
public class AdminController {
	
	@Autowired
	private UserServices userService;
	
	@Autowired
	private ProductServices productServices;
	
	@Autowired
	private CategoryServices categoryServices;

	
	
	
	
	
	
	
}
