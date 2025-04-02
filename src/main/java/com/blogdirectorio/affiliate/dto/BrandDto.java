package com.blogdirectorio.affiliate.dto;

import java.util.List;

import com.blogdirectorio.affiliate.entity.CategoryEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrandDto {

	private Long brandId;
	private String title; // e.g., "Vivo"
	private String urlName; // e.g., "vivo-mobile"
	private String description;
	private String image;
	
//	private CategoryDto category;

//	private List<ProductDto> products;
	
}
