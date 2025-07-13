package com.blogdirectorio.affiliate.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.blogdirectorio.affiliate.dto.BrandDto;
import com.blogdirectorio.affiliate.dto.CategoryDto;
import com.blogdirectorio.affiliate.payloads.ApiResponse;
import com.blogdirectorio.affiliate.services.BrandServices;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/api/brands")
@Tag(name = "Brand Controller")
@CrossOrigin
public class BrandController {

	@Autowired
	private BrandServices brandServices;
	
	private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/brands";
	
	@PostMapping("/create/category/{categoryId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<BrandDto> createBrand(@RequestParam("brand") String brandJson,
			@PathVariable("categoryId") Long categoryId,
			@RequestParam("image") MultipartFile file) throws IOException{
		
		ObjectMapper objectMapper = new ObjectMapper();
		BrandDto brand = objectMapper.readValue(brandJson, BrandDto.class);
		
		File uploadDirectory = new File(UPLOAD_DIR);
		if (!uploadDirectory.exists()) {
			uploadDirectory.mkdirs(); 
		}
		

		if (!file.isEmpty()) {
			// Rename file to avoid duplicates (Optional)
			String fileName = System.currentTimeMillis() + "_" + StringUtils.cleanPath(file.getOriginalFilename());
			Path targetLocation = Paths.get(UPLOAD_DIR, fileName);

			// Save file, replacing existing one if needed
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

			// Store image path
			String imageUrl = "/uploads/brands/" + fileName;
			brand.setImage(imageUrl);
		}
		
		BrandDto save=this.brandServices.createBrand(brand,categoryId);
		return new ResponseEntity<>(save,HttpStatus.CREATED);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<BrandDto>> allBrands(){
		List<BrandDto> lists=this.brandServices.allBrands();
		return new ResponseEntity<>(lists,HttpStatus.OK);
	}
	
	@GetMapping("/{brandId}")
	public ResponseEntity<BrandDto> getBrandById(@RequestParam("brandId") Long brandId){
		BrandDto brand=this.brandServices.getBrandById(brandId);
		return new ResponseEntity<>(brand,HttpStatus.OK);
	}
	
	
	@DeleteMapping("/{brandId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> deleteBrand(@PathVariable("brandId") Long brandId) throws IOException {
	    // Fetch the brand to get the image URL
		System.out.println(brandId);
	    BrandDto brand = brandServices.getBrandById(brandId);

	    // Delete the brand's image file
	    deleteOldImage(brand.getImage());

	    // Delete the brand
	    String msg = brandServices.deleteBrand(brandId);
	    ApiResponse res = new ApiResponse(msg, true);
	    return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
	/////-------------update start----------------
	@PutMapping("/{brandId}/category/{categoryId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<BrandDto> updateBrand(
	        @PathVariable("brandId") Long brandId,
	        @RequestParam("brand") String brandJson,
	        @PathVariable("categoryId") Long categoryId,
	        @RequestParam(value = "image", required = false) MultipartFile file) throws IOException {

	    ObjectMapper objectMapper = new ObjectMapper();
	    BrandDto brandDto = objectMapper.readValue(brandJson, BrandDto.class);

	    // Fetch existing brand from DB
	    BrandDto existingBrand = brandServices.getBrandById(brandId);

	    // Update text fields
	    existingBrand.setTitle(brandDto.getTitle());
	    existingBrand.setUrlName(brandDto.getUrlName());

	    if (file != null && !file.isEmpty()) {
	        // ✅ New image uploaded → delete old image
	        deleteOldImage(existingBrand.getImage());

	        // ✅ Save new image
	        String fileName = System.currentTimeMillis() + "_" + StringUtils.cleanPath(file.getOriginalFilename());
	        Path targetLocation = Paths.get(UPLOAD_DIR, fileName);
	        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

	        String imageUrl = "/uploads/brands/" + fileName;
	        existingBrand.setImage(imageUrl);
	    } else {
	        // ✅ No new image → keep the old image (already set in existingBrand)
	    }

	    // ✅ Update brand in DB
	    BrandDto updatedBrand = brandServices.updateBrand(brandId, existingBrand, categoryId);
	    return new ResponseEntity<>(updatedBrand, HttpStatus.OK);
	}


/////-------------update end ----------------
	
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
