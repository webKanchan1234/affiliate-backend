package com.blogdirectorio.affiliate.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.blogdirectorio.affiliate.dto.AdminReviewDto;
import com.blogdirectorio.affiliate.payloads.ApiResponse;
import com.blogdirectorio.affiliate.services.AdminReviewService;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/api/reviews/admin")
@Tag(name = "Admin Review Controller")
public class AdminReviewController {

    @Autowired
    private AdminReviewService adminReviewService;
    
    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/reviews";
	

    @PostMapping("/create-admin-review/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminReviewDto> createReview(@PathVariable Long productId, @RequestBody AdminReviewDto review) {
    	AdminReviewDto rev=this.adminReviewService.createReview(productId, review);
        return new ResponseEntity<AdminReviewDto>(rev,HttpStatus.CREATED);
    }
    
    @GetMapping("/{reviewId}")
    public ResponseEntity<AdminReviewDto> getReviewById(@PathVariable Long reviewId) {
    	AdminReviewDto rev=this.adminReviewService.getReviewById(reviewId);
        return new ResponseEntity<AdminReviewDto>(rev,HttpStatus.OK);
    }
    
    @DeleteMapping("/{reviewId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteReviewById(@PathVariable Long reviewId) {
    	String rev=this.adminReviewService.deleteReview(reviewId);
    	ApiResponse res= new ApiResponse(rev,true);
        return new ResponseEntity<ApiResponse>(res,HttpStatus.OK);
    }
    
    
    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("image") MultipartFile file) throws IOException {
        System.out.println("---------------image");
        File uploadDirectory = new File(UPLOAD_DIR);
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs();
        }

        String fileName = System.currentTimeMillis() + "_" + StringUtils.cleanPath(file.getOriginalFilename());
        Path targetLocation = Paths.get(UPLOAD_DIR, fileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        String imageUrl = "/uploads/reviews/" + fileName;

        Map<String, String> response = new HashMap<>();
        response.put("imageUrl", imageUrl);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}