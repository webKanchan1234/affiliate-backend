package com.blogdirectorio.affiliate.controllers;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/uploads")
@Tag(name = "File Controller")
@CrossOrigin
public class FileController {

	private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";
	private static final String UPLOAD_DIR_CAT = System.getProperty("user.dir") + "/uploads/categories";
	
	private static final String UPLOAD_DIR_BRANDS = System.getProperty("user.dir") + "/uploads/brands";
	private static final String UPLOAD_DIR_PRODUCTS = System.getProperty("user.dir") + "/uploads/products";

	private static final String UPLOAD_DIR_USERS = System.getProperty("user.dir") + "/uploads/users";

	private static final String UPLOAD_DIR_REVIEWS = System.getProperty("user.dir") + "/uploads/reviews";

	
	
    @GetMapping("/{filename}")
    public Resource getFile(@PathVariable String filename) throws IOException {
        Path filePath = Paths.get(UPLOAD_DIR).resolve(filename).normalize();
        Resource resource = new UrlResource(filePath.toUri());
        
        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new IOException("File not found or not readable.");
        }
        
    }
    
    @GetMapping("/brands/{filename}")
    public Resource getFileBrands(@PathVariable String filename) throws IOException {
        Path filePath = Paths.get(UPLOAD_DIR_BRANDS).resolve(filename).normalize();
        Resource resource = new UrlResource(filePath.toUri());
        
        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new IOException("File not found or not readable.");
        }
        
    }
    
    

    @GetMapping("/categories/{filename}")
    public Resource getProductFile(@PathVariable String filename) throws IOException {
        Path filePath = Paths.get(UPLOAD_DIR_CAT).resolve(filename).normalize();
        Resource resource = new UrlResource(filePath.toUri());
        
        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new IOException("File not found or not readable.");
        }
        
    }
    
    @GetMapping("/products/{filename}")
    public Resource getCategoryFile(@PathVariable String filename) throws IOException {
        Path filePath = Paths.get(UPLOAD_DIR_PRODUCTS).resolve(filename).normalize();
        Resource resource = new UrlResource(filePath.toUri());
        
        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new IOException("File not found or not readable.");
        }
        
    }
    
    @GetMapping("/users/{filename}")
    public Resource getUserFile(@PathVariable String filename) throws IOException {
        Path filePath = Paths.get(UPLOAD_DIR_USERS).resolve(filename).normalize();
        Resource resource = new UrlResource(filePath.toUri());
        
        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new IOException("File not found or not readable.");
        }
        
    }
    
    
    
    @GetMapping("/reviews/{filename}")
    public Resource getreviewFile(@PathVariable String filename) throws IOException {
        Path filePath = Paths.get(UPLOAD_DIR_REVIEWS).resolve(filename).normalize();
        Resource resource = new UrlResource(filePath.toUri());
        
        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new IOException("File not found or not readable.");
        }
        
    }
    
}
