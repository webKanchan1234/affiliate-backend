package com.blogdirectorio.affiliate.services;

import java.util.List;

import com.blogdirectorio.affiliate.dto.ProductDto;
import com.blogdirectorio.affiliate.payloads.PostResponse;

public interface ProductServices {

	public ProductDto createPost(ProductDto mobile,Long categoryID,Long brandId);
	public ProductDto getProductDetails(Long id);
	public PostResponse allProducts(Integer pageNumber,Integer pageSize, String sortBy,String sortDir);
	public ProductDto updateProduct(Long id, ProductDto mobile,Long categoryId,Long brandId);
	public String deleteProduct(Long id);
	public List<ProductDto> getProductsByCategory(String category);
//	public List<ProductDto> getProductsByCategoryName(String category);
	public List<ProductDto> searchProducts(String title);
	public ProductDto getProductByUrlName(String urlName);
	public List<ProductDto> getProductsByBrand(String brand);
	public List<ProductDto> getProductsBySubcategory(String subcategory);
}
