package com.blogdirectorio.affiliate.dto;

import java.util.List;

import com.blogdirectorio.affiliate.entity.SectionEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminReviewDto {

	private Long id;
//	private ProductDto product;
	private List<SectionDto> sections;
}
