package com.blogdirectorio.affiliate.entity;

import java.util.List;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class DisplayEntity {

	
//	private Long id;
	private String disType;
	private String screenSize;
	private String resolution;
	private String brightness;
	private String refreshRate;
	private String aspectRation;
	private String pixel;
	private String touch;
	private String hd;
	
}
