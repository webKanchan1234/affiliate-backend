package com.blogdirectorio.affiliate.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class MediaEntity {

//	private Long mediaId;
	private String radio;
	private String speaker;
	private String audioJack;
	private String mediaOthers;
}
