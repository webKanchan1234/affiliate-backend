package com.blogdirectorio.affiliate.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.blogdirectorio.affiliate.dto.BrandDto;
import com.blogdirectorio.affiliate.dto.CategoryDto;
import com.blogdirectorio.affiliate.payloads.ApiResponse;
import com.blogdirectorio.affiliate.services.CategoryServices;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/api/categories")
@Tag(name = "Category Controller")
@CrossOrigin
public class CategoryController {

	@Autowired
	private CategoryServices categoryServices;

	private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/categories";

	@PostMapping("/create")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CategoryDto> createCategory(@RequestParam("category") String categoryJson,
			@RequestParam("image") MultipartFile file) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		CategoryDto category = objectMapper.readValue(categoryJson, CategoryDto.class);

		File uploadDirectory = new File(UPLOAD_DIR);
		if (!uploadDirectory.exists()) {
			uploadDirectory.mkdirs(); // Create the uploads directory if it doesn't exist
		}

		if (!file.isEmpty()) {
			// Rename file to avoid duplicates (Optional)
			String fileName = System.currentTimeMillis() + "_" + StringUtils.cleanPath(file.getOriginalFilename());
			Path targetLocation = Paths.get(UPLOAD_DIR, fileName);

			// Save file, replacing existing one if needed
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

			// Store image path
			String imageUrl = "/uploads/categories/" + fileName;
			category.setImage(imageUrl);
		}
		
		CategoryDto cat=this.categoryServices.createCategory(category);

		return new ResponseEntity<CategoryDto>(cat, HttpStatus.CREATED);
	}

	@GetMapping("/")
	public ResponseEntity<List<CategoryDto>> getAllCategories() {
		List<CategoryDto> lists = this.categoryServices.getAllCategories();
		return new ResponseEntity<List<CategoryDto>>(lists, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<CategoryDto> getCategory(@PathVariable("id") Long id) {
		CategoryDto cat = this.categoryServices.getSingleCategory(id);
		return new ResponseEntity<CategoryDto>(cat, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> deleteCategory(@PathVariable("id") Long id) throws IOException {
	    // Fetch the category to get the image URL
	    CategoryDto category = categoryServices.getSingleCategory(id);

	    // Delete the category's image file
	    deleteOldImage(category.getImage());

	    // Delete the category
	    String msg = categoryServices.deleteCategory(id);
	    ApiResponse res = new ApiResponse(msg, true);
	    return new ResponseEntity<>(res, HttpStatus.OK);
	}
	

	//------------------update category start-------------
	@PutMapping("/update-category/{categoryId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CategoryDto> updateCategory(@RequestParam("category") String categoryJson,
	                                                 @PathVariable("categoryId") Long categoryId,
	                                                 @RequestParam(value = "image", required = false) MultipartFile file) throws IOException {
	    ObjectMapper objectMapper = new ObjectMapper();
	    CategoryDto categoryDto = objectMapper.readValue(categoryJson, CategoryDto.class);

	    // Fetch existing category
	    CategoryDto existingCategory = categoryServices.getSingleCategory(categoryId);
	    existingCategory.setTitle(categoryDto.getTitle());
	    existingCategory.setUrlName(categoryDto.getUrlName());

	    // Handle image update if a new file is provided
	    if (file != null && !file.isEmpty()) {
	        // Delete the old image
	        deleteOldImage(existingCategory.getImage());

	        // Rename file to avoid duplicates
	        String fileName = System.currentTimeMillis() + "_" + StringUtils.cleanPath(file.getOriginalFilename());
	        Path targetLocation = Paths.get(UPLOAD_DIR, fileName);

	        // Save new file, replacing existing one
	        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

	        // Update image URL
	        String imageUrl = "/uploads/categories/" + fileName;
	        existingCategory.setImage(imageUrl);
	    }

	    // Save updated category
	    CategoryDto updatedCategory = categoryServices.updateCategory(categoryId, existingCategory);
	    return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
	}
	
	//------------------update category end-------------
	
	private void deleteOldImage(String imageUrl) throws IOException {
	    if (imageUrl != null && !imageUrl.isEmpty()) {
	        String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
	        Path filePath = Paths.get(UPLOAD_DIR, fileName);
	        if (Files.exists(filePath)) {
	            Files.delete(filePath); // Delete the old image file
	        }
	    }
	}

}
