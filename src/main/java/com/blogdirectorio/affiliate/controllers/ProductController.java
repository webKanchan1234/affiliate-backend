package com.blogdirectorio.affiliate.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
import org.springframework.web.server.ResponseStatusException;

import com.blogdirectorio.affiliate.dto.ProductDto;
import com.blogdirectorio.affiliate.entity.ProductEntity;
import com.blogdirectorio.affiliate.exceptions.CustomUnknownPropertyHandler;
import com.blogdirectorio.affiliate.helper.ProductImage;
import com.blogdirectorio.affiliate.payloads.ApiResponse;
import com.blogdirectorio.affiliate.payloads.PostResponse;
import com.blogdirectorio.affiliate.services.ProductServices;
import com.blogdirectorio.affiliate.utility.ImageKitService;
import com.blogdirectorio.affiliate.utility.ImageUploadResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;

@RestController
@RequestMapping("/v1/api/products")
@Tag(name = "Product Controller")
@CrossOrigin
public class ProductController {

	private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/products";
	
	@Autowired
	private ImageKitService imageKitService;

	@Autowired
	private ProductServices productServices;
	
	@Autowired
	private Validator validator; 
	

//	------------------------create product-----------------
	@PostMapping("/post/category/{categoryId}/brand/{brandId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> productPost(
	        @RequestParam("product") String productJson,
	        @PathVariable("categoryId") Long categoryId,
	        @PathVariable("brandId") Long brandId,
	        @RequestParam(value = "images", required = false) List<MultipartFile> files) {

	    try {
	    	ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            CustomUnknownPropertyHandler unknownHandler = new CustomUnknownPropertyHandler();
            objectMapper.addHandler(unknownHandler);

            ProductDto product = objectMapper.readValue(productJson, ProductDto.class);

            List<String> unknownKeys = unknownHandler.getUnknownProperties();

            if (!unknownKeys.isEmpty()) {
                return ResponseEntity.badRequest().body(unknownKeys);
            }
	        // ✅ Validate fields using Validator
	        Set<ConstraintViolation<ProductDto>> violations = validator.validate(product);
	        if (!violations.isEmpty()) {
	            List<String> errorMessages = violations.stream()
	                .map(ConstraintViolation::getMessage)
	                .collect(Collectors.toList());
	            return ResponseEntity.badRequest().body(errorMessages);
	        }

	        // ✅ Save images
//	        List<String> imageUrls = new ArrayList<>();
//	        if (files != null) {
//	            for (MultipartFile file : files) {
//	                if (!file.isEmpty()) {
//	                    String fileName = System.currentTimeMillis() + "_" + StringUtils.cleanPath(file.getOriginalFilename());
//	                    Path targetLocation = Paths.get(UPLOAD_DIR, fileName);
//	                    Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
//	                    imageUrls.add("/uploads/products/" + fileName);
//	                }
//	            }
//	        }
	        
	        List<ProductImage> images = new ArrayList<>();

	        if (files != null) {
	            for (MultipartFile file : files) {
	                if (!file.isEmpty()) {
	                    ImageUploadResponse res =
	                            imageKitService.uploadImage(file, "/products");

	                    ProductImage img = new ProductImage();
	                    img.setUrl(res.getUrl());
	                    img.setFileId(res.getFileId());

	                    images.add(img);
	                }
	            }
	        }

	        product.setImageUrls(images);

	        ProductDto saved = productServices.createPost(product, categoryId, brandId);
	        return ResponseEntity.status(HttpStatus.CREATED).body(saved);

	    } catch (UnrecognizedPropertyException e) {
	        // ✅ Collect unknown properties in a list
	        List<String> unknownProperties = Collections.singletonList("Unknown JSON key: " + e.getPropertyName());
	        return ResponseEntity.badRequest().body(unknownProperties);
	    } catch (JsonMappingException e) {
	        // ✅ Collect all unrecognized fields
	        List<String> unknownKeys = new ArrayList<>();
	        for (JsonMappingException.Reference ref : e.getPath()) {
	            if (ref.getFieldName() != null) {
	                unknownKeys.add("Invalid field: " + ref.getFieldName());
	            }
	        }
	        return ResponseEntity.badRequest().body(unknownKeys);
	    } catch (JsonProcessingException e) {
	        return ResponseEntity.badRequest().body(Collections.singletonList("Invalid JSON format: " + e.getOriginalMessage()));
	    } catch (IOException e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonList("File error: " + e.getMessage()));
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonList("Unexpected error: " + e.getMessage()));
	    }
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

	    // Fetch existing product from DB
	    ProductDto existingProduct = productServices.getProductDetails(productId);

//	    List<String> imageUrls = new ArrayList<>();
//
//	    if (files != null && !files.isEmpty() && files.stream().anyMatch(file -> !file.isEmpty())) {
//	        // ✅ New images uploaded → Delete old images
//	        deleteOldImages(existingProduct.getImageUrls());
//
//	        // ✅ Ensure uploads folder exists
//	        File uploadDirectory = new File(UPLOAD_DIR);
//	        if (!uploadDirectory.exists()) {
//	            uploadDirectory.mkdirs();
//	        }
//
//	        // ✅ Save new images
//	        for (MultipartFile file : files) {
//	            if (!file.isEmpty()) {
//	                String fileName = System.currentTimeMillis() + "_" + StringUtils.cleanPath(file.getOriginalFilename());
//	                Path targetLocation = Paths.get(UPLOAD_DIR, fileName);
//	                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
//	                imageUrls.add("/uploads/products/" + fileName);
//	            }
//	        }
//
//	        updatedProduct.setImageUrls(imageUrls); // Set new image URLs
//	    } else {
//	        // ✅ No new image uploaded → retain old ones
//	        updatedProduct.setImageUrls(existingProduct.getImageUrls());
//	    }
	    
	    
	    if (files != null && files.stream().anyMatch(f -> !f.isEmpty())) {

	        // 🔥 delete old images from ImageKit
	        for (ProductImage img : existingProduct.getImageUrls()) {
	            imageKitService.deleteImageByFileId(img.getFileId());
	        }

	        // 🔥 upload new images
	        List<ProductImage> newImages = new ArrayList<>();

	        for (MultipartFile file : files) {
	            if (!file.isEmpty()) {
	                ImageUploadResponse res =
	                        imageKitService.uploadImage(file, "/products");

	                ProductImage img = new ProductImage();
	                img.setUrl(res.getUrl());
	                img.setFileId(res.getFileId());

	                newImages.add(img);
	            }
	        }

	        updatedProduct.setImageUrls(newImages);
	    } else {
	        updatedProduct.setImageUrls(existingProduct.getImageUrls());
	    }

	    // ✅ Proceed with updating the product
	    ProductDto updated = productServices.updateProduct(productId, updatedProduct, categoryId, brandId);
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
//	    deleteOldImages(product.getImageUrls());
	    // 🔥 delete images from ImageKit
	    for (ProductImage img : product.getImageUrls()) {
	        imageKitService.deleteImageByFileId(img.getFileId());
	    }


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
