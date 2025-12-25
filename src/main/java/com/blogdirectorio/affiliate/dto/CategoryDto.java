package com.blogdirectorio.affiliate.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDto {

	private Long categoryId;
	private String title;
	private String urlName;
	private String description;
	private String image;       // CDN URL
	private String imageFileId;  // ImageKit fileId
	private List<BrandDto> brands;
//	private ProductDto Product;
}
