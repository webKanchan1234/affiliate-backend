package com.blogdirectorio.affiliate.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.blogdirectorio.affiliate.entity.UserEntity;
import com.blogdirectorio.affiliate.exceptions.ResourceNotFoundException;
import com.blogdirectorio.affiliate.repository.UserRepository;

@Service
public class CustomeUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepo;
	
	public UserDetails loadUserByUsername(String username) {
		UserEntity user=this.userRepo.findByEmail(username).orElseThrow(()->new ResourceNotFoundException("username", "username", null));
		
		return user;
	}
}
