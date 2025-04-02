package com.blogdirectorio.affiliate.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDto {

	private Long reviewId;
	private String reviewerName;
	private String reviewTitle;
	private String reviewDesc;
	private int rating;
	private LocalDateTime createdAt=LocalDateTime.now();
	
}
