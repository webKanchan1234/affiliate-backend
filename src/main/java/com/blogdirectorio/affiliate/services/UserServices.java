package com.blogdirectorio.affiliate.services;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

import com.blogdirectorio.affiliate.dto.UserDto;

public interface UserServices {

	public UserDto signUp(UserDto user);
	public UserDto login(UserDto user);
	public List<UserDto> allUsers();
	public UserDto userDetails(String email);
	public UserDto getSingleUser(Long userId);
	public UserDto updateProfiles(UserDetails userDetails,UserDto user);
	public String deleteUser(Long userId);
	
	public String logoutUser(String email);
	public String updateRole(Long Id, UserDto role);
	public String changePassword(String email, String currentPassword, String newPassword);
	
}
