package com.blogdirectorio.affiliate.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "categories")
public class CategoryEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long categoryId;
	private String title;
	private String urlName;
	private String description;
	
	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonManagedReference // ✅ Prevents infinite recursion
	private List<BrandEntity> brands;
	
	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
	private List<ProductEntity> product;
	private String image;
	private String imageFileId;
}
