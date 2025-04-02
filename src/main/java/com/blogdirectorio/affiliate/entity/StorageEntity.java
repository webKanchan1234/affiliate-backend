package com.blogdirectorio.affiliate.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class StorageEntity {

//	private Long storageId;
	private String internal;
	private String expandable;
	private String usbs;
//	private String othersStorage;
	
}
