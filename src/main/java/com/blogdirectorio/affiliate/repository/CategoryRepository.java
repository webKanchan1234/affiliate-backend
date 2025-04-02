package com.blogdirectorio.affiliate.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blogdirectorio.affiliate.entity.CategoryEntity;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long>{

	Optional<CategoryEntity> findByUrlNameIgnoreCase(String name);
}
