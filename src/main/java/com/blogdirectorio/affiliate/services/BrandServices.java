package com.blogdirectorio.affiliate.services;

import java.util.List;

import com.blogdirectorio.affiliate.dto.BrandDto;

public interface BrandServices {
	
	public BrandDto createBrand(BrandDto brand,Long categoryId);
	public List<BrandDto> allBrands();
	public BrandDto getBrandById(Long brandId);
	public BrandDto updateBrand(Long id,BrandDto brand,Long categoryId);
	public String deleteBrand(Long id);

}
