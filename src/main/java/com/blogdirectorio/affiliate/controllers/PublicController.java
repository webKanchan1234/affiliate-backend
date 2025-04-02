package com.blogdirectorio.affiliate.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blogdirectorio.affiliate.dto.CategoryDto;
import com.blogdirectorio.affiliate.dto.ProductDto;
import com.blogdirectorio.affiliate.payloads.ApiResponse;
import com.blogdirectorio.affiliate.services.CategoryServices;
import com.blogdirectorio.affiliate.services.ProductServices;

@RestController
@RequestMapping("/v1/api/public")
public class PublicController {

	@Autowired
	private ProductServices productServices;
	

	@Autowired
	private CategoryServices categoryServices;
	
	
	
	
//	@GetMapping("/search")
//	public ResponseEntity<List<ProductDto>> searchProducts(@RequestParam String query){
//		List<ProductDto> products = productServices.searchProducts(query);
//		return new ResponseEntity<>(products,HttpStatus.OK);
//	}
	

	@GetMapping("/")
	public ResponseEntity<List<CategoryDto>> getAllCategories(){
		List<CategoryDto>lists=this.categoryServices.getAllCategories();
		return new ResponseEntity<List<CategoryDto>>(lists,HttpStatus.OK);
	}
}
