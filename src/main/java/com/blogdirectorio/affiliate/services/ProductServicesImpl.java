package com.blogdirectorio.affiliate.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.blogdirectorio.affiliate.dto.ProductDto;
import com.blogdirectorio.affiliate.entity.BrandEntity;
import com.blogdirectorio.affiliate.entity.CategoryEntity;
import com.blogdirectorio.affiliate.entity.ProductEntity;
import com.blogdirectorio.affiliate.exceptions.ResourceNotFoundException;
import com.blogdirectorio.affiliate.payloads.PostResponse;
import com.blogdirectorio.affiliate.repository.BrandRepository;
import com.blogdirectorio.affiliate.repository.CategoryRepository;
import com.blogdirectorio.affiliate.repository.ProductRepository;

@Service
public class ProductServicesImpl implements ProductServices{
	
	
	
	@Autowired
	private ProductRepository productRepo;
	
	@Autowired
	private CategoryRepository categoryRepo;
	
	@Autowired
	private BrandRepository brandRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	
	private static final Logger logger = LoggerFactory.getLogger(ProductServices.class);

	@Override
	public ProductDto createPost(ProductDto mobile, Long categoryId,Long brandId) {
		CategoryEntity category=this.categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("category", "categoryId", categoryId));
		
		BrandEntity brand=this.brandRepo.findById(brandId).orElseThrow(()->new ResourceNotFoundException("brand", "brandId", brandId));
		
		System.out.println("------------------------");
		ProductEntity mob=this.modelMapper.map(mobile, ProductEntity.class);
		mob.setCategory(category);
		mob.setBrand(brand);
		ProductEntity saveMob=productRepo.save(mob);
		
		return this.modelMapper.map(saveMob, ProductDto.class);
	}

	@Override
	public ProductDto getProductDetails(Long id) {
		ProductEntity saveMob=this.productRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Product", "id", id));
		
		return this.modelMapper.map(saveMob, ProductDto.class);
	}

	@Override
	public PostResponse allProducts(Integer pageNumber,Integer pageSize, String sortBy, String sortDir) {
		
		Sort sort=null;
		if(sortDir.equalsIgnoreCase("asc")) {
			sort=Sort.by(sortBy).ascending();
		}else {
			sort=Sort.by(sortBy).descending();
		}
		Pageable page=PageRequest.of(pageNumber, pageSize,sort);
		Page<ProductEntity> pages=this.productRepo.findAll(page);
		List<ProductEntity> lists=pages.getContent();
		List<ProductDto> mobiles=lists.stream().map(mob->this.modelMapper.map(mob, ProductDto.class)).collect(Collectors.toList());
		
		PostResponse postResponse=new PostResponse();
		postResponse.setContent(mobiles);
		postResponse.setPageNumber(pages.getNumber());
		postResponse.setPageSize(pages.getSize());
		postResponse.setTotalElements(pages.getTotalElements());
		postResponse.setTotalSize(pages.getTotalPages());
		postResponse.setLastPage(pages.isLast());
		
		return postResponse;
	}

	@Override
	public ProductDto updateProduct(Long id, ProductDto mobile,Long categoryId,Long brandId) {
		ProductEntity mob=this.productRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("id", "id", id));
		
		CategoryEntity cat=this.categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category", "category", categoryId));
		
		BrandEntity brand =this.brandRepo.findById(brandId).orElseThrow(()->new ResourceNotFoundException("brandId", "brandId", brandId));
		
		modelMapper.map(mobile, mob);
		mob.setCategory(cat);
		mob.setBrand(brand);
		this.productRepo.save(mob);
		
		return this.modelMapper.map(mob, ProductDto.class);
	}

	@Override
	public String deleteProduct(Long id) {
		ProductEntity saveMob=this.productRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Product", "id", id));
		
		this.productRepo.deleteById(id);
		return "Deleted successfully";
	}

	@Override
	public List<ProductDto> getProductsByCategory(String categoryName) {
		
//		System.out.println("🔍 Searching for category: {}"+ categoryName);

        CategoryEntity category = categoryRepo.findByUrlNameIgnoreCase(categoryName)
                .orElseThrow(() -> {
//                    logger.error("❌ Category '{}' not found", categoryName);
                    return new ResourceNotFoundException("Category", "name", null);
                });
        

//        System.out.println("✅ Category found: {}"+ category.getUrlName());

        List<ProductEntity> products = productRepo.findByCategory(category);
        if (products.isEmpty()) {
//        	System.out.println("⚠️ No products found for category: {}"+ categoryName);
        } else {
//        	System.out.println("📦 Found {} products in category '{}'"+ products.size()+ categoryName);
        }
		List<ProductDto> mobs=products.stream().map(mob->this.modelMapper.map(mob, ProductDto.class)).collect(Collectors.toList());
		
		return mobs;
	}
	
//	@Override
//	public List<ProductDto> getProductsByCategoryName(String category) {
//		
//
//		List<ProductEntity> lists=this.productRepo.findByCategoryName(category);
//		List<ProductDto> mobs=lists.stream().map(mob->this.modelMapper.map(mob, ProductDto.class)).collect(Collectors.toList());
//		
//		return mobs;
//	}

	@Override
	public List<ProductDto> searchProducts(String keyword) {
		List<ProductEntity> lists=this.productRepo.searchProducts(keyword);
		List<ProductDto> mobiles=lists.stream().map(mob->this.modelMapper.map(mob, ProductDto.class)).collect(Collectors.toList());
		return mobiles;
	}

	@Override
	public ProductDto getProductByUrlName(String urlName) {
		ProductEntity pro=productRepo.findByUrlName(urlName)
                .orElseThrow(() -> new RuntimeException("Product not found"));
		
        return this.modelMapper.map(pro, ProductDto.class);
    }
	
	
	
	
	@Override
	public List<ProductDto> getProductsByBrand(String brandName) {
	    System.out.println("🔍 Searching for brand: " + brandName);

	    BrandEntity brand = brandRepo.findByUrlNameIgnoreCase(brandName)
	        .orElseThrow(() -> {
	            System.out.println("❌ Brand not found: " + brandName);
	            return new ResourceNotFoundException("Brand", "name", null);
	        });

	    System.out.println("✅ Brand found: " + brand.getUrlName());

	    List<ProductEntity> products = productRepo.findByBrand(brand);
	    
	    if (products.isEmpty()) {
	        System.out.println("⚠️ No products found for brand: " + brandName);
	    } else {
	        System.out.println("📦 Found " + products.size() + " products for brand: " + brandName);
	    }

	    return products.stream()
	        .map(product -> modelMapper.map(product, ProductDto.class))
	        .collect(Collectors.toList());
	}

	@Override
	public List<ProductDto> getProductsBySubcategory(String subcategory) {
		List<ProductEntity> products=productRepo.findBySubcategory(subcategory);
		
		return products.stream()
		        .map(product -> modelMapper.map(product, ProductDto.class))
		        .collect(Collectors.toList());
	}

	
	
}
