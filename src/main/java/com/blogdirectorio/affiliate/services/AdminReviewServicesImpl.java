package com.blogdirectorio.affiliate.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blogdirectorio.affiliate.dto.AdminReviewDto;
import com.blogdirectorio.affiliate.dto.ReviewDto;
import com.blogdirectorio.affiliate.entity.AdminReviewEntity;
import com.blogdirectorio.affiliate.entity.ProductEntity;
import com.blogdirectorio.affiliate.entity.ReviewEntity;
import com.blogdirectorio.affiliate.repository.AdminReviewRepository;
import com.blogdirectorio.affiliate.repository.ProductRepository;


@Service
public class AdminReviewServicesImpl implements AdminReviewService{
	
	
	@Autowired
    private AdminReviewRepository adminReviewRepo;

    @Autowired
    private ProductRepository productRepo;
    
    
    @Autowired
	private ModelMapper modelMapper;
    
    

	@Override
	public AdminReviewDto createReview(Long productId, AdminReviewDto review) {
		
		ProductEntity product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
		AdminReviewEntity rev=this.modelMapper.map(review, AdminReviewEntity.class);
		
        rev.setProduct(product);
        AdminReviewEntity saveRev= this.adminReviewRepo.save(rev);
        return this.modelMapper.map(saveRev, AdminReviewDto.class);
	}

	@Override
	public AdminReviewDto updateReview(Long reviewId, Long productId, AdminReviewDto review) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public AdminReviewDto getReviewById(Long reviewId) {
		AdminReviewEntity rev = adminReviewRepo.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("reviewId not found"));
		
		return this.modelMapper.map(rev, AdminReviewDto.class);
	}

	@Override
	public String deleteReview(Long reviewId) {
		AdminReviewEntity rev = adminReviewRepo.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("reviewId not found"));
		this.adminReviewRepo.deleteById(reviewId);
		return "Deleted";
	}

}
