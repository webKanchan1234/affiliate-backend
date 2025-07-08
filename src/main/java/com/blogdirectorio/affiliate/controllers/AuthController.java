package com.blogdirectorio.affiliate.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogdirectorio.affiliate.dto.UserDto;
import com.blogdirectorio.affiliate.entity.UserEntity;
import com.blogdirectorio.affiliate.exceptions.ResourceNotFoundException;
import com.blogdirectorio.affiliate.payloads.AuthRequest;
import com.blogdirectorio.affiliate.payloads.AuthResponse;
import com.blogdirectorio.affiliate.repository.UserRepository;
import com.blogdirectorio.affiliate.security.JwtTokenHelper;
import com.blogdirectorio.affiliate.services.UserServices;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/api/auth")
@Tag(name = "Auth Controller")
public class AuthController {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private UserServices userServices;
	
	@Autowired
	private JwtTokenHelper jwtTokenHelper;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	@PostMapping("/signup")
	public ResponseEntity<UserDto> signUpUser(@Valid @RequestBody UserDto user) {
		UserDto u= userServices.signUp(user);
		return new ResponseEntity<>(u,HttpStatus.CREATED);
	}

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> createToken(@RequestBody AuthRequest request){
		
		// Load user details
		UserEntity loadUser = (UserEntity) this.userDetailsService.loadUserByUsername(request.getEmail());
	    Long userId = loadUser.getId();
	    
//	    System.out.prin	tln("userId Auth con "+userId);
	    UserEntity user=userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("user","id",userId));
	    String password=loadUser.getPassword();
	    String role=user.getRole();
	 // Compare raw password with hashed password
	    if (!passwordEncoder.matches(request.getPassword(), password)) {
	        throw new RuntimeException("Invalid email or password!");
	    }
	    
		this.authenticate(request.getEmail(),request.getPassword());
		
		
	    
		UserDetails userDetails=this.userDetailsService.loadUserByUsername(request.getEmail());
		String token=this.jwtTokenHelper.generateToken(userDetails,userId);
		AuthResponse respo=new AuthResponse();
		respo.setToken(token);
		return new ResponseEntity<AuthResponse>(respo,HttpStatus.OK);
	}
	
	
	private void authenticate(String email, String password) {
		
		UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(email,password);
		try {
			this.authenticationManager.authenticate(authenticationToken);
		} catch (BadCredentialsException e) {
			throw new RuntimeException("Invalid Credentials");
		}
		
	}

}
