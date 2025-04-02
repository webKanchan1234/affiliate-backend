package com.blogdirectorio.affiliate.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blogdirectorio.affiliate.dto.CategoryDto;
import com.blogdirectorio.affiliate.entity.CategoryEntity;
import com.blogdirectorio.affiliate.exceptions.ResourceNotFoundException;
import com.blogdirectorio.affiliate.repository.CategoryRepository;

@Service
public class CategoryServicesImpl implements CategoryServices {
	
	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private CategoryRepository categoryRepo;

	@Override
	public CategoryDto createCategory(CategoryDto category) {
		CategoryEntity cat=this.modelMapper.map(category, CategoryEntity.class);
		CategoryEntity save=this.categoryRepo.save(cat);
		
		return this.modelMapper.map(save, CategoryDto.class);
	}

	@Override
	public List<CategoryDto> getAllCategories() {
		List<CategoryEntity> lists=this.categoryRepo.findAll();
		List<CategoryDto> mobs=lists.stream().map(mob->this.modelMapper.map(mob, CategoryDto.class)).collect(Collectors.toList());
		return mobs;
	}

	@Override
	public CategoryDto getSingleCategory(Long id) {
		CategoryEntity cat=this.categoryRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Category", "id", id));
		
		return this.modelMapper.map(cat, CategoryDto.class);
	}

	@Override
	public CategoryDto updateCategory(Long id, CategoryDto category) {
		CategoryEntity cat=this.categoryRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Category", "id", id));
		
		this.modelMapper.map(category, cat);
		this.categoryRepo.save(cat);
		return this.modelMapper.map(cat, CategoryDto.class);
	}

	@Override
	public String deleteCategory(Long id) {
		CategoryEntity cat=this.categoryRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Category", "id", id));
		this.categoryRepo.deleteById(id);
		return "Delete Category Successfully";
	}

}
