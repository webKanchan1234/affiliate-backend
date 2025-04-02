package com.blogdirectorio.affiliate.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blogdirectorio.affiliate.entity.AffiliateProofEntity;

public interface AffiliateProofRepository extends JpaRepository<AffiliateProofEntity, Long>{
	Optional<AffiliateProofEntity> findByOrderId(String orderId);
}
