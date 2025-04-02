package com.blogdirectorio.affiliate.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.blogdirectorio.affiliate.entity.BrandEntity;
import com.blogdirectorio.affiliate.entity.CategoryEntity;
import com.blogdirectorio.affiliate.entity.ProductEntity;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

	
	public List<ProductEntity> findByCategory(CategoryEntity category);
	Optional<ProductEntity> findByUrlName(String urlName);
	List<ProductEntity> findBySubcategory(String subcategory);
	
	List<ProductEntity> findByBrand(BrandEntity brand);



	@Query("SELECT p FROM ProductEntity p WHERE " +
		       "(LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
		       "LOWER(p.companyName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
		       "LOWER(p.modelName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
		       "LOWER(p.color) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
		       "LOWER(p.brand.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " + 
		       "LOWER(p.category.title) LIKE LOWER(CONCAT('%', :keyword, '%'))) OR " + 
		       "(CAST(p.id AS string) LIKE CONCAT('%', :keyword, '%')) OR " + 
		       "(CAST(p.price AS string) LIKE CONCAT('%', :keyword, '%'))")
	List<ProductEntity> searchProducts(@Param("keyword") String keyword);
	

	







	
}
