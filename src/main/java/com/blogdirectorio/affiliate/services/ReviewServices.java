package com.blogdirectorio.affiliate.services;

import java.util.List;
import java.util.Optional;

import com.blogdirectorio.affiliate.dto.ReviewDto;

public interface ReviewServices {

	public ReviewDto createReview(ReviewDto review,Long productId);
	public ReviewDto getSingleReview(Long reviewId);
	public List<ReviewDto> getAllReviews();
	public ReviewDto updateReview(ReviewDto review,Long reviewId);
	public String deleteReview(Long reviewId);
	Optional<ReviewDto> getReviewByUserAndProduct(String reviewer, Long productId);
	
}
