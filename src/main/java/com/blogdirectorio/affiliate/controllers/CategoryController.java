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
import com.blogdirectorio.affiliate.utility.ImageKitService;
import com.blogdirectorio.affiliate.utility.ImageUploadResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/api/categories")
@Tag(name = "Category Controller")
@CrossOrigin
public class CategoryController {
	
	@Autowired
	private ImageKitService imageKitService;

	@Autowired
	private CategoryServices categoryServices;

	private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/categories";
	
	
	
//	create category-------------------------------

	@PostMapping("/create")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CategoryDto> createCategory(@RequestParam("category") String categoryJson,
			@RequestParam("image") MultipartFile file) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		CategoryDto category = objectMapper.readValue(categoryJson, CategoryDto.class);

//		File uploadDirectory = new File(UPLOAD_DIR);
//		if (!uploadDirectory.exists()) {
//			uploadDirectory.mkdirs(); // Create the uploads directory if it doesn't exist
//		}
//
//		if (!file.isEmpty()) {
//			// Rename file to avoid duplicates (Optional)
//			String fileName = System.currentTimeMillis() + "_" + StringUtils.cleanPath(file.getOriginalFilename());
//			Path targetLocation = Paths.get(UPLOAD_DIR, fileName);
//
//			// Save file, replacing existing one if needed
//			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
//
//			// Store image path
//			String imageUrl = "/uploads/categories/" + fileName;
//			category.setImage(imageUrl);
//		}
		
		if (file != null && !file.isEmpty()) {
		    ImageUploadResponse res = imageKitService.uploadImage(file, "/categories");
		    category.setImage(res.getUrl());
		    category.setImageFileId(res.getFileId());
		}
		
		CategoryDto cat=this.categoryServices.createCategory(category);

		return new ResponseEntity<CategoryDto>(cat, HttpStatus.CREATED);
	}
	
	
	
	
	
	
//	get all getAllCategories-----------------------------------

	@GetMapping("/")
	public ResponseEntity<List<CategoryDto>> getAllCategories() {
		List<CategoryDto> lists = this.categoryServices.getAllCategories();
		return new ResponseEntity<List<CategoryDto>>(lists, HttpStatus.OK);
	}
	
	
	
	
	
	
	
//	function for getCategory by id
	
	

	@GetMapping("/{id}")
	public ResponseEntity<CategoryDto> getCategory(@PathVariable("id") Long id) {
		CategoryDto cat = this.categoryServices.getSingleCategory(id);
		return new ResponseEntity<CategoryDto>(cat, HttpStatus.OK);
	}
	
	
	
	
//	function for deleteCategory by id
	
	
	
	

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> deleteCategory(@PathVariable("id") Long id) throws IOException {
		CategoryDto category = categoryServices.getSingleCategory(id);

	    // 🔥 delete from ImageKit CDN
	    imageKitService.deleteImageByFileId(category.getImageFileId());

	    // 🔥 delete from DB
	    String msg = categoryServices.deleteCategory(id);

	    return ResponseEntity.ok(new ApiResponse(msg, true));
	}
	
	
	
	
	
	
	
	

	//------------------update category start-------------
	@PutMapping("/update-category/{categoryId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CategoryDto> updateCategory(
	        @RequestParam("category") String categoryJson,
	        @PathVariable Long categoryId,
	        @RequestParam(value = "image", required = false) MultipartFile file
	) throws IOException {

	    ObjectMapper objectMapper = new ObjectMapper();
	    CategoryDto incoming = objectMapper.readValue(categoryJson, CategoryDto.class);

	    // 1️⃣ Fetch existing category
	    CategoryDto existing = categoryServices.getSingleCategory(categoryId);
	    existing.setTitle(incoming.getTitle());
	    existing.setUrlName(incoming.getUrlName());

	    // 2️⃣ If new image uploaded → replace in ImageKit
	    if (file != null && !file.isEmpty()) {

	        // 🔥 delete old image from ImageKit
	        imageKitService.deleteImageByFileId(existing.getImageFileId());

	        // 🔥 upload new image
	        ImageUploadResponse res = imageKitService.uploadImage(file, "/categories");

	        existing.setImage(res.getUrl());
	        existing.setImageFileId(res.getFileId());
	    }

	    // 3️⃣ Save updated category
	    CategoryDto updated = categoryServices.updateCategory(categoryId, existing);

	    return ResponseEntity.ok(updated);
	}

	
	
	
	
	
	
	
	
	
	//------------------deleteOldImage category-------------
	
//	private void deleteOldImage(String imageUrl) throws IOException {
//	    if (imageUrl != null && !imageUrl.isEmpty()) {
//	        String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
//	        Path filePath = Paths.get(UPLOAD_DIR, fileName);
//	        if (Files.exists(filePath)) {
//	            Files.delete(filePath); // Delete the old image file
//	        }
//	    }
//	}

}
