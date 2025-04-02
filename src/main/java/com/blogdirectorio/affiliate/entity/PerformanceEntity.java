package com.blogdirectorio.affiliate.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class PerformanceEntity {

//	private Long performanceId;
	private String chip;
	private String cpu;
	private String architecture;
	private String graphics;
	private String ram;
	
}
