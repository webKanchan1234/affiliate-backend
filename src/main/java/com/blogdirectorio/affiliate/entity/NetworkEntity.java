package com.blogdirectorio.affiliate.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class NetworkEntity {

//	private Long networkId;
	private String slot;
	private String size;
	private String support;
	private String vlt;
	private String wifi;
	private String bluetooth;
	private String networkOthers;
}
