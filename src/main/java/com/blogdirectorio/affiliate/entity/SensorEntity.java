package com.blogdirectorio.affiliate.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class SensorEntity {

//	private Long sensorId;
	private String finger;
	private String position;
	private String sensorType;
	private String sensorOthers;
}
