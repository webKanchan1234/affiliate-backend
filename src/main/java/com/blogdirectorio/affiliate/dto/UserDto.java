package com.blogdirectorio.affiliate.dto;

import java.time.LocalDate;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
	
	private Long id;
	private String name;
	private String email;
	private String password;
	private String image;
	private String createdAt;
	private String role="ROLE_USER";
}
