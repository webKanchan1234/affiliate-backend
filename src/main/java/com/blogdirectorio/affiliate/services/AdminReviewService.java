package com.blogdirectorio.affiliate.services;

import com.blogdirectorio.affiliate.dto.AdminReviewDto;

public interface AdminReviewService {

	public AdminReviewDto createReview(Long productId, AdminReviewDto review);
	public AdminReviewDto updateReview(Long reviewId,Long productId, AdminReviewDto review);
	public String deleteReview(Long reviewId);
	public AdminReviewDto getReviewById(Long reviewId);
}
