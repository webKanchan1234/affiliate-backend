package com.blogdirectorio.affiliate.services;

import java.util.List;

import com.blogdirectorio.affiliate.dto.CategoryDto;

public interface CategoryServices {

	public CategoryDto createCategory(CategoryDto category);
	public List<CategoryDto> getAllCategories();
	public CategoryDto getSingleCategory(Long id);
	public CategoryDto updateCategory(Long id,CategoryDto category);
	public String deleteCategory(Long id);
	
}
