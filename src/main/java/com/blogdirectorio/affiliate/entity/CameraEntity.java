package com.blogdirectorio.affiliate.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class CameraEntity {

	
//	private Long cameraId;
	private String cameraSet;
	private String cameraResolution;
	private String focus;
	private String flash;
	private String imageResolution;
	private String features;
	private String recording;
	private String cameraOthers;
}
