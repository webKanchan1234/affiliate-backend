package com.blogdirectorio.affiliate.payloads;

import java.util.List;

import com.blogdirectorio.affiliate.dto.ProductDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PostResponse {

	private List<ProductDto> content;
	private Integer pageNumber;
	private Integer pageSize;
	private Long totalElements;
	private Integer totalSize;
	private boolean lastPage;
}
