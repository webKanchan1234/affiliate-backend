package com.blogdirectorio.affiliate.dto;

import java.util.List;

import com.blogdirectorio.affiliate.entity.ReviewEntity;

import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto {

	private Long productId;
	@NotBlank(message = "Product name is required")
	private String name;
	private String companyName;
	@NotBlank(message = "Model name is required")
	private String modelName;
	private String color;
	private String price;
	private String subcategory;
	private String urlName;
	private List<String> imageUrls;
	private List<String> specifications;
	
	private List<String> pros;
	private List<String> cons;
	private List<SellerDto> sellers;
	private GeneralDto general;
	private PerformanceDto performance;
	private DisplayDto display;
	private DesignDto design;
	private CameraDto camera;
	private BatteryDto battery;
	private StorageDto storage;
	private NetworkDto network;
	private MediaDto media;
	private SensorDto sensor;
	
	private CategoryDto category;
	private BrandDto brand;
	
	private List<ReviewDto> reviews;
	private List<AdminReviewDto> adminReview;
	
}
