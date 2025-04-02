package com.blogdirectorio.affiliate.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class GeneralEntity {

//	private Long generalId;
	private String launchDate;
	private String brand;
	private String model;
	private String os;
	private String simSlot;
	private String simSize;
	private String network;
	
	
}
