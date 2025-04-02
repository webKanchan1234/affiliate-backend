package com.blogdirectorio.affiliate.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class BatteryEntity {

	
//	private Long baterryId;
	private String capacity;
	private String type;
	private String removal;
	private String charging;
	private String usb;
}
