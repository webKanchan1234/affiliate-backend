package com.blogdirectorio.affiliate.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "contactus")
public class ContactusEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long contactusId;
	private String name;
	private String email;
	private String message;
}
