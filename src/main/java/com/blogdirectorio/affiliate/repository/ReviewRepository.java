package com.blogdirectorio.affiliate.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blogdirectorio.affiliate.entity.ReviewEntity;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long>{

	Optional<ReviewEntity> findByReviewerNameAndProduct_ProductId(String reviewerName, Long productId);


}
