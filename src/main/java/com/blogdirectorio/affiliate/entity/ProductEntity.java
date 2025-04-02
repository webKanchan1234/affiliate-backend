package com.blogdirectorio.affiliate.entity;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "products")
public class ProductEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long productId;
	private String name;
	private String companyName;
	private String modelName;
	private String color;
	private String price;
	@Column(columnDefinition = "TEXT")
	private String subcategory;
	@Column(columnDefinition = "TEXT")
	private String urlName;
//	private String test;
	
	
	
	@ElementCollection
    @Column(columnDefinition = "TEXT")
	private List<String> imageUrls;
	
	@ElementCollection
    @Column(columnDefinition = "TEXT")
	private List<String> specifications;
	
	
	
	@ElementCollection
    @Column(columnDefinition = "TEXT")
	private List<String> pros;
	
	@ElementCollection
    @Column(columnDefinition = "TEXT")
	private List<String> cons;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
		    name = "product_sellers",
		    joinColumns = @JoinColumn(name = "product_id"),
		    inverseJoinColumns = @JoinColumn(name = "seller_id")
		)
    private List<SellerEntity> sellers;
	
	@Embedded
	private GeneralEntity general;
	
	@Embedded
	private PerformanceEntity performance;
	
	@Embedded
	private DisplayEntity display;
	
	@Embedded
	private DesignEntity design;
	
	@Embedded
	private CameraEntity camera;
	
	@Embedded
	private BatteryEntity battery;
	
	
	@Embedded
	private StorageEntity storage;
	
	@Embedded
	private NetworkEntity network;
	
	@Embedded
	private MediaEntity media;
	
	@Embedded
	private SensorEntity sensor;
	
	@ManyToOne
	private CategoryEntity category;
	
	
	@ManyToOne
	@JoinColumn(name = "brand_id", nullable = false)
	private BrandEntity brand;
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ReviewEntity> reviews;
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AdminReviewEntity> adminReview;
	
}
