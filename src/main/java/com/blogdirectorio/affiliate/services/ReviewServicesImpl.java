package com.blogdirectorio.affiliate.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blogdirectorio.affiliate.dto.ReviewDto;
import com.blogdirectorio.affiliate.entity.BrandEntity;
import com.blogdirectorio.affiliate.entity.ProductEntity;
import com.blogdirectorio.affiliate.entity.ReviewEntity;
import com.blogdirectorio.affiliate.exceptions.ResourceNotFoundException;
import com.blogdirectorio.affiliate.repository.ProductRepository;
import com.blogdirectorio.affiliate.repository.ReviewRepository;

@Service
public class ReviewServicesImpl implements ReviewServices {
	
	@Autowired
	private ProductRepository productRepo;
	
	@Autowired
	private ReviewRepository reviewRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	

	@Override
	public ReviewDto createReview(ReviewDto review, Long productId) {

		ProductEntity pro=this.productRepo.findById(productId).orElseThrow(()->new ResourceNotFoundException("productId", "productId", productId));
		
		ReviewEntity rev=this.modelMapper.map(review, ReviewEntity.class);
		rev.setProduct(pro);
		
		ReviewEntity saveRev=this.reviewRepo.save(rev);
		
		return this.modelMapper.map(saveRev, ReviewDto.class);
	}

	@Override
	public ReviewDto getSingleReview(Long reviewId) {
		
		ReviewEntity review=this.reviewRepo.findById(reviewId).orElseThrow(()->new ResourceNotFoundException("reviewId", "reviewId", reviewId));
		
		return this.modelMapper.map(review, ReviewDto.class);
	}

	@Override
	public List<ReviewDto> getAllReviews() {
		
		List<ReviewEntity> lists=this.reviewRepo.findAll();
		List<ReviewDto> reviews=lists.stream().map(rev->this.modelMapper.map(rev, ReviewDto.class)).collect(Collectors.toList());
		
		return reviews;
	}

	@Override
	public ReviewDto updateReview(ReviewDto reviewDto,Long reviewId ) {
	    ReviewEntity existingReview = reviewRepo.findById(reviewId)
	            .orElseThrow(() -> new ResourceNotFoundException("Review not found","reviewId",reviewId));

	    this.modelMapper.map(reviewDto, existingReview); // Update timestamp
	    

	    ReviewEntity updatedReview = reviewRepo.save(existingReview);
	    return modelMapper.map(updatedReview, ReviewDto.class);
	}

	@Override
	public String deleteReview(Long reviewId) {
		
		ReviewEntity review=this.reviewRepo.findById(reviewId).orElseThrow(()->new ResourceNotFoundException("reviewId", "reviewId", reviewId));
		this.reviewRepo.deleteById(reviewId);
		return "Review deleted successfully";
	}
	
	public Optional<ReviewDto> getReviewByUserAndProduct(String reviewerName, Long productId) {
	    return reviewRepo.findByReviewerNameAndProduct_ProductId(reviewerName, productId)
	            .map(review -> modelMapper.map(review, ReviewDto.class));
	}

}
