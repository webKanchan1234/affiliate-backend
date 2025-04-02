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

import com.blogdirectorio.affiliate.dto.ProductDto;
import com.blogdirectorio.affiliate.entity.ProductEntity;
import com.blogdirectorio.affiliate.payloads.ApiResponse;
import com.blogdirectorio.affiliate.payloads.PostResponse;
import com.blogdirectorio.affiliate.services.ProductServices;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/api/products")
public class ProductController {

	private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/products";

	@Autowired
	private ProductServices productServices;
	

//	------------------------create product-----------------
	@PostMapping("/post/category/{categoryId}/brand/{brandId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ProductDto> productPost(@RequestParam("product") String productJson,
			@PathVariable("categoryId") Long categoryId,
			@PathVariable("brandId") Long brandId,
			@RequestParam(value="images", required = false) List<MultipartFile> files)
			throws IOException {
		
		// Parse the JSON request into a ProductDto object
		ObjectMapper objectMapper = new ObjectMapper();
		ProductDto product = objectMapper.readValue(productJson, ProductDto.class);

		// Ensure the uploads folder exists
		File uploadDirectory = new File(UPLOAD_DIR);
		if (!uploadDirectory.exists()) {
			uploadDirectory.mkdirs(); // Create the uploads directory if it doesn't exist
		}

		List<String> imageUrls = new ArrayList<>();

		for (MultipartFile file : files) {
			if (!file.isEmpty()) {
				// Rename file to avoid duplicates (Optional)
				String fileName = System.currentTimeMillis() + "_" + StringUtils.cleanPath(file.getOriginalFilename());
				Path targetLocation = Paths.get(UPLOAD_DIR, fileName);

				// Save file, replacing existing one if needed
				Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

				// Store image path
				imageUrls.add("/uploads/products/" + fileName);
			}
		}

		// Set image URLs
		product.setImageUrls(imageUrls);

		// Save product
		ProductDto mob = this.productServices.createPost(product, categoryId,brandId);

		return new ResponseEntity<>(mob, HttpStatus.CREATED);

	}
	
//	------------------------create product end-----------------
	
	
//	------------------------get all product-----------------

	@GetMapping("/")
	public ResponseEntity<PostResponse> allProducts(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "100", required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = "ProductId", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
		PostResponse Products = this.productServices.allProducts(pageNumber, pageSize, sortBy, sortDir);

		return new ResponseEntity<>(Products, HttpStatus.OK);
	}
	
//	------------------------get all product end-----------------

//	@GetMapping("/{id}")
//	public ResponseEntity<ProductDto> getSingleProduct(@PathVariable("id") Long id) {
//		ProductDto Product = this.productServices.getProductDetails(id);
//
//		return new ResponseEntity<>(Product, HttpStatus.OK);
//	}

//	-----------------find product by category------------
	@GetMapping("/category/{title}")
	public ResponseEntity<List<ProductDto>> findByCategory(@PathVariable("title") String title) {
		List<ProductDto> lists = this.productServices.getProductsByCategory(title);

		return new ResponseEntity<>(lists, HttpStatus.OK);
	}
	
//	-----------------find product by category end------------

	@GetMapping("/brand/{title}")
	public ResponseEntity<List<ProductDto>> findByBrand(@PathVariable("title") String title) {
		List<ProductDto> lists = this.productServices.getProductsByBrand(title);

		return new ResponseEntity<>(lists, HttpStatus.OK);
	}

//	-----------------update product------------
	@PutMapping("/update-product/{productId}/category/{categoryId}/brand/{brandId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ProductDto> updateProduct(
	        @PathVariable("productId") Long productId,
	        @PathVariable("categoryId") Long categoryId,
	        @PathVariable("brandId") Long brandId,
	        @RequestParam("product") String productJson,
	        @RequestParam(value = "images", required = false) List<MultipartFile> files) throws IOException {

	    ObjectMapper objectMapper = new ObjectMapper();
	    ProductDto updatedProduct = objectMapper.readValue(productJson, ProductDto.class);

	    // Fetch existing product
	    ProductDto existingProduct = productServices.getProductDetails(productId);

	    // Delete old images
	    deleteOldImages(existingProduct.getImageUrls());

	    List<String> imageUrls = new ArrayList<>();

	    if (files != null && !files.isEmpty()) {
	        // Ensure the uploads folder exists
	        File uploadDirectory = new File(UPLOAD_DIR);
	        if (!uploadDirectory.exists()) {
	            uploadDirectory.mkdirs();
	        }

	        for (MultipartFile file : files) {
	            if (!file.isEmpty()) {
	                // Rename file to avoid duplicates (Optional)
	                String fileName = System.currentTimeMillis() + "_" + StringUtils.cleanPath(file.getOriginalFilename());
	                Path targetLocation = Paths.get(UPLOAD_DIR, fileName);

	                // Save file
	                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

	                // Store image path
	                imageUrls.add("/uploads/products/" + fileName);
	            }
	        }
	    }

	    // Set updated fields
	    updatedProduct.setImageUrls(imageUrls); // Update images

	    // Update the product in the database
	    ProductDto updated = productServices.updateProduct(productId, updatedProduct, categoryId,brandId);

	    return new ResponseEntity<>(updated, HttpStatus.OK);
	}
	
	
//	-----------------update product end------------
	
	
//	-----------------delete product------------
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> deleteProduct(@PathVariable("id") Long id) throws IOException {
	    // Fetch the product to get the image URLs
	    ProductDto product = productServices.getProductDetails(id);

	    // Delete the product's image files
	    deleteOldImages(product.getImageUrls());

	    // Delete the product
	    String msg = productServices.deleteProduct(id);
	    ApiResponse res = new ApiResponse(msg, true);
	    return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
//	-----------------delete product end------------
	
	
//	-----------------get product by urlName------------
	
	@GetMapping("/{urlName}")
	public ResponseEntity<ProductDto> getProductByUrlName(@PathVariable("urlName") String urlName){
		ProductDto prod=this.productServices.getProductByUrlName(urlName);
		return new ResponseEntity<>(prod,HttpStatus.OK);
	}
	
//	-----------------get product by urlName end------------

	private void deleteOldImages(List<String> imageUrls) throws IOException {
	    if (imageUrls != null && !imageUrls.isEmpty()) {
	        for (String imageUrl : imageUrls) {
	            String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
	            Path filePath = Paths.get(UPLOAD_DIR, fileName);
	            if (Files.exists(filePath)) {
	                Files.delete(filePath); // Delete the old image file
	            }
	        }
	    }
	}
	
	@GetMapping("/subcategory/{title}")
	public ResponseEntity<List<ProductDto>> findBySubcategory(@PathVariable("title") String title) {
		List<ProductDto> lists = this.productServices.getProductsBySubcategory(title);

		return new ResponseEntity<>(lists, HttpStatus.OK);
	}
	
	@GetMapping("/search")
    public List<ProductDto> searchProducts(@RequestParam String keyword) {
        return productServices.searchProducts(keyword);
    }
}
