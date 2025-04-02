package com.blogdirectorio.affiliate.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blogdirectorio.affiliate.entity.BrandEntity;
import com.blogdirectorio.affiliate.entity.CategoryEntity;

public interface BrandRepository extends JpaRepository<BrandEntity, Long>{
	Optional<BrandEntity> findByUrlNameIgnoreCase(String name);
}
