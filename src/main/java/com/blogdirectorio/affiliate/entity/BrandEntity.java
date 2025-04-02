package com.blogdirectorio.affiliate.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "brands")
public class BrandEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long brandId;
	private String title; // e.g., "Vivo"
	private String urlName; // e.g., "vivo-mobile"
	private String image;
	private String description;
	
	@ManyToOne
	@JoinColumn(name = "category_id")
	private CategoryEntity category;
	
//	@OneToMany(mappedBy = "brand", cascade = CascadeType.ALL)
//	private List<ProductEntity> products;
	
}
