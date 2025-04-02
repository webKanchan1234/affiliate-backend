package com.blogdirectorio.affiliate.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.blogdirectorio.affiliate.dto.BrandDto;
import com.blogdirectorio.affiliate.dto.UserDto;
import com.blogdirectorio.affiliate.payloads.ApiResponse;
import com.blogdirectorio.affiliate.services.UserServices;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/api/user")
public class UserController {

	
	@Autowired
	private UserServices userService;
	
	private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/users";
	

	
	

	@GetMapping("/users")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<UserDto>> allUsers(){
		List<UserDto> users=this.userService.allUsers();
		return new ResponseEntity<>(users,HttpStatus.OK);
	}
	
	
	
	@GetMapping("/{userId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<UserDto> getSingleUser(@PathVariable("userId") Long userId){
		UserDto user=this.userService.getSingleUser(userId);
		return new ResponseEntity<>(user,HttpStatus.OK);
	}
	
	@GetMapping("/profile")
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public ResponseEntity<UserDto> getUserProfile(@AuthenticationPrincipal UserDetails userDetails){
		 String email = userDetails.getUsername();
		 UserDto user=this.userService.userDetails(email);
		return new ResponseEntity<>(user,HttpStatus.OK);
	}
	
//	------------update profile start----------------
	 @PutMapping("/update-profile")
	    public ResponseEntity<UserDto> updateProfiles(
	            @AuthenticationPrincipal UserDetails userDetails,
	            @RequestParam("user") String userJson,
	            @RequestParam(value = "image", required = false) MultipartFile file) {
	        try {
	            ObjectMapper objectMapper = new ObjectMapper();
	            UserDto user = objectMapper.readValue(userJson, UserDto.class);

	            File uploadDirectory = new File(UPLOAD_DIR);
	            if (!uploadDirectory.exists()) {
	                uploadDirectory.mkdirs(); // Create the uploads directory if it doesn't exist
	            }

	            // Fetch the existing user to get the old image URL
	            UserDto existingUser = userService.userDetails(userDetails.getUsername());

	            // Delete the old image if a new image is provided
	            if (file != null && !file.isEmpty()) {
	                deleteOldImage(existingUser.getImage()); // Delete the old image

	                // Save the new image
	                String fileName = System.currentTimeMillis() + "_" + StringUtils.cleanPath(file.getOriginalFilename());
	                Path targetLocation = Paths.get(UPLOAD_DIR, fileName);
	                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

	                // Update the image URL in the user object
	                String imageUrl = "/uploads/users/" + fileName;
	                user.setImage(imageUrl);
	            }

	            // Update the user profile
	            UserDto updatedUser = userService.updateProfiles(userDetails, user);
	            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
	        } catch (IOException e) {
	            e.printStackTrace();
	            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }
	
	
//	------------update profile end----------------
	
	@PostMapping("/logout")
	public ResponseEntity<ApiResponse> logoutUser(@AuthenticationPrincipal UserDetails userDetails){
		String email=userDetails.getUsername();
		String msg=this.userService.logoutUser(email);
		ApiResponse res=new ApiResponse(msg, true);
		return new ResponseEntity<>(res,HttpStatus.OK);
	}
	
	@PutMapping("/update-role/{userId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> updateRole(@RequestBody UserDto user,@PathVariable("userId") Long userId){
		String msg=this.userService.updateRole(userId,user);
		ApiResponse res=new ApiResponse(msg,true);
		return new ResponseEntity<>(res,HttpStatus.OK);
	}
	
	
	@DeleteMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable("userId") Long userId) throws IOException {
        // Fetch the user to get the image URL
        UserDto user = userService.getSingleUser(userId);

        // Delete the user's image file
        deleteOldImage(user.getImage());

        // Delete the user
        String msg = userService.deleteUser(userId);
        return new ResponseEntity<>(new ApiResponse(msg, true), HttpStatus.OK);
    }
	

	// Helper method to delete old image
    private void deleteOldImage(String imageUrl) throws IOException {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
            Path filePath = Paths.get(UPLOAD_DIR, fileName);
            if (Files.exists(filePath)) {
                Files.delete(filePath); // Delete the old image file
            }
        }
    }
    
    
    @PutMapping("/change-password")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<String> changePassword(
        @AuthenticationPrincipal UserDetails userDetails, 
        @RequestBody UserDto user
    ) {
        String email = userDetails.getUsername(); // Get logged-in user's email
        
        String message = userService.changePassword(email, userDetails.getPassword(), user.getPassword());
        return ResponseEntity.ok(message);
    }
}
