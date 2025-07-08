package com.blogdirectorio.affiliate.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SellerDto {

	private Long sellerId;
	private String name;
	private String logo;
	private String link;
	private String price;
}
