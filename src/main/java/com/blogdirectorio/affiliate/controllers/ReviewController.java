package com.blogdirectorio.affiliate.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogdirectorio.affiliate.dto.ReviewDto;
import com.blogdirectorio.affiliate.dto.UserDto;
import com.blogdirectorio.affiliate.payloads.ApiResponse;
import com.blogdirectorio.affiliate.services.ReviewServices;
import com.blogdirectorio.affiliate.services.UserServices;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/api/reviews")
@Tag(name = "Review Controller")
@CrossOrigin
public class ReviewController {

	@Autowired
	private ReviewServices reviewServices;
	
	@Autowired
	private UserServices userService;
	
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	@PostMapping("/create/product/{productId}")
	public ResponseEntity<ReviewDto> createReview(@Valid @RequestBody ReviewDto review,@PathVariable("productId") Long productId,@AuthenticationPrincipal UserDetails userDetails){
		String email = userDetails.getUsername();
	    UserDto user = this.userService.userDetails(email);
	    String name = user.getName();
	    
	    // Check if the user already has a review for the product
	    Optional<ReviewDto> existingReview = reviewServices.getReviewByUserAndProduct(name, productId);

	    if (existingReview.isPresent()) {
	        // Update the existing review
	        ReviewDto updatedReview = reviewServices.updateReview(review,existingReview.get().getReviewId());
	        return new ResponseEntity<>(updatedReview, HttpStatus.OK);
	    } else {
	        // Create a new review
	        review.setReviewerName(name);
	        ReviewDto newReview = this.reviewServices.createReview(review, productId);
	        return new ResponseEntity<>(newReview, HttpStatus.CREATED);
	    }
	}
	
	
	@GetMapping("/")
	public ResponseEntity<List<ReviewDto>> getAllReviews(){
		List<ReviewDto> reviews=this.reviewServices.getAllReviews();
		
		return new ResponseEntity<>(reviews,HttpStatus.OK);
	}
	
	@GetMapping("/{reviewId}")
	public ResponseEntity<ReviewDto> getSingleReview(@PathVariable("reviewId") Long reviewId){
		
		ReviewDto rev=this.reviewServices.getSingleReview(reviewId);
		
		return new ResponseEntity<>(rev,HttpStatus.OK);
	}
	
	@PutMapping("/update-review/{reviewId}")
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public ResponseEntity<ReviewDto> updateReview(@Valid @RequestBody ReviewDto review,
			@PathVariable("reviewId") Long reviewId,
			@AuthenticationPrincipal UserDetails userDetails){
		String email = userDetails.getUsername();
		UserDto user=this.userService.userDetails(email);
		String name=user.getName();
		review.setReviewerName(name);
		ReviewDto rev=this.reviewServices.updateReview(review, reviewId);
		
		return new ResponseEntity<>(rev,HttpStatus.OK);
	}
	
	
	@DeleteMapping("/{reviewId}")
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public ResponseEntity<ApiResponse> deleteReview(@PathVariable("reviewId") Long reviewId){
		
		String rev=this.reviewServices.deleteReview(reviewId);
		ApiResponse res=new ApiResponse(rev,true);
		return new ResponseEntity<>(res,HttpStatus.OK);
	}
}
