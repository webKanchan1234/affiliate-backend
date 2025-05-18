package com.blogdirectorio.affiliate.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DisplayDto {

//	private Long id;
	private String disType;
	private String screenSize;
	private String resolution;
	private String brightness;
	private String refreshRate;
	@NotBlank(message = "Ration name is required")
	private String aspectRation;
	private String pixel;
	private String touch;
	private String hd;
	
}
