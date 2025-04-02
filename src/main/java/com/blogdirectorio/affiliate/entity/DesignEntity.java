package com.blogdirectorio.affiliate.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class DesignEntity {

	
//	private Long designId;
	private String height;
	private String width;
	private String thickness;
	private String weight;
	private String material;
	private String colors;
	private String waterProof;
	
}
