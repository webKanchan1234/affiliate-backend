package com.blogdirectorio.affiliate.repository;

import com.blogdirectorio.affiliate.entity.AdminReviewEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminReviewRepository extends JpaRepository<AdminReviewEntity, Long> {
//    List<AdminReviewEntity> findByProductId(Long productId); // Find reviews by product ID
}