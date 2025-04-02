package com.blogdirectorio.affiliate.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.blogdirectorio.affiliate.dto.UserDto;
import com.blogdirectorio.affiliate.entity.UserEntity;
import com.blogdirectorio.affiliate.exceptions.ResourceNotFoundException;
import com.blogdirectorio.affiliate.repository.UserRepository;

@Service
public class UserServicesImpl implements UserServices{
	
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDto signUp(UserDto user) {
		
		UserEntity userEntity=this.modelMapper.map(user, UserEntity.class);
		LocalDate currentDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String formattedDate = currentDate.format(formatter);
		
		userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
		userEntity.setCreatedAt(formattedDate);
//		userEntity.setCreatedAt(LocalDate.now());
		UserEntity saveUser=userRepo.save(userEntity);
			
		return this.modelMapper.map(saveUser, UserDto.class);
	}


	@Override
	public List<UserDto> allUsers() {
		List<UserEntity> userLists=this.userRepo.findAll();
		
		List<UserDto> lists=userLists.stream().map(user->this.modelMapper.map(user,UserDto.class)).collect(Collectors.toList());
		return lists;
	}

	@Override
	public UserDto userDetails(String email) {
		UserEntity user=this.userRepo.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("email", "email", null));
		return this.modelMapper.map(user, UserDto.class);
	}

	@Override
	public UserDto getSingleUser(Long userId) {
		UserEntity user=this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("user", "id", userId));
		
		return this.modelMapper.map(user, UserDto.class);
	}

	@Override
	public UserDto updateProfiles(UserDetails userDetails, UserDto user) {
		String email=userDetails.getUsername();
		UserEntity u=this.userRepo.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("email", "email", null));
		modelMapper.map(user, u);

        // Handle password separately (Encrypt before saving)
        if (user.getPassword() != null) {
            u.setPassword(passwordEncoder.encode(user.getPassword()));
        }
		UserEntity updateUser=this.userRepo.save(u);
		return this.modelMapper.map(updateUser, UserDto.class);
	}

	@Override
	public String deleteUser(Long userId) {
		UserEntity user=this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("user", "id", userId));;
		this.userRepo.deleteById(userId);
		return "Deleted user successfully";
	}

	@Override
	public String logoutUser(String email) {
		UserEntity user=this.userRepo.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("email", "email", null));
		user.setJwtToken(null);
		userRepo.save(user);
		return "Logout successfully";
	}


	@Override
	public UserDto login(UserDto user) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String updateRole(Long Id, UserDto user) {
		// TODO Auto-generated method stub
		UserEntity u=this.userRepo.findById(Id).orElseThrow(()->new ResourceNotFoundException("id", "id", null));
		u.setRole(user.getRole());
		
		userRepo.save(u);
		
		return "Role changed";
	}
	
	@Override
	public String changePassword(String email, String currentPassword, String newPassword) {
	    UserEntity user = userRepo.findByEmail(email)
	        .orElseThrow(() -> new ResourceNotFoundException("User", "email", null));

	    // Check if the current password matches the stored password
//	    if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
//	        throw new IllegalArgumentException("Current password is incorrect");
//	    }

	    // Encode the new password and update
	    user.setPassword(passwordEncoder.encode(newPassword));
	    userRepo.save(user);

	    return "Password updated successfully!";
	}


}
