package com.blogdirectorio.affiliate.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "sellers")
@Getter
@Setter
public class SellerEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sellerId;
    private String name;
    private String logo;
    private String link;
    
}
