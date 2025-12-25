package com.blogdirectorio.affiliate.helper;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter @Setter
public class ProductImage {

    @Column(columnDefinition = "TEXT")
    private String url;

    private String fileId;
}
