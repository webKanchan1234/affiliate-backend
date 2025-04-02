package com.blogdirectorio.affiliate.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SectionDto {
    
    private Long id;

    private String type; // "text" or "image"
    private String content; // Text content or Base64 image
}
