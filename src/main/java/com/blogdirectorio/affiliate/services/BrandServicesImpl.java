package com.blogdirectorio.affiliate.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blogdirectorio.affiliate.dto.BrandDto;
import com.blogdirectorio.affiliate.entity.BrandEntity;
import com.blogdirectorio.affiliate.entity.CategoryEntity;
import com.blogdirectorio.affiliate.exceptions.ResourceNotFoundException;
import com.blogdirectorio.affiliate.repository.BrandRepository;
import com.blogdirectorio.affiliate.repository.CategoryRepository;

@Service
public class BrandServicesImpl implements BrandServices {

	@Autowired
	private BrandRepository brandRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private CategoryRepository categoryRepo;
	
	@Override
	public BrandDto createBrand(BrandDto brand,Long categoryId) {
		BrandEntity br=this.modelMapper.map(brand, BrandEntity.class);
		CategoryEntity cat=this.categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category", "categoory", categoryId));
		br.setCategory(cat);
		BrandEntity saveEn=this.brandRepo.save(br);
		
		return this.modelMapper.map(saveEn, BrandDto.class);
	}

	@Override
	public List<BrandDto> allBrands() {
		List<BrandEntity> lists=this.brandRepo.findAll();
		List<BrandDto> brands=lists.stream().map(br->this.modelMapper.map(br, BrandDto.class)).collect(Collectors.toList());
		
		return brands;
	}

	@Override
	public BrandDto getBrandById(Long brandId) {
		BrandEntity brand=this.brandRepo.findById(brandId).orElseThrow(()->new ResourceNotFoundException("Brand", "Brand", brandId));
		
		return this.modelMapper.map(brand, BrandDto.class);
	}

	@Override
	public BrandDto updateBrand(Long id, BrandDto brand,Long categoryId) {
//		System.out.println("ser impl");
		BrandEntity b=this.brandRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Brand", "Brand", id));
		CategoryEntity cat=this.categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category", "categoory", categoryId));
		b.setCategory(cat);
		this.modelMapper.map(brand, b);
		this.brandRepo.save(b);

		return this.modelMapper.map(b, BrandDto.class);
	}

	@Override
	public String deleteBrand(Long id) {
		BrandEntity b=this.brandRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Brand", "Brand", id));
		this.brandRepo.deleteById(id);
		return "Brand deleted successfully";
	}

}
