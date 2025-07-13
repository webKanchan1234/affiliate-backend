package com.blogdirectorio.affiliate.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogdirectorio.affiliate.dto.ContactusDto;
import com.blogdirectorio.affiliate.payloads.ApiResponse;
import com.blogdirectorio.affiliate.services.ContactusServices;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/api/contactus")
@Tag(name = "Contact Controller")
@CrossOrigin
public class ContactusController {

	@Autowired
	private ContactusServices contactusServices;
	
	@PostMapping("/post")
	public ResponseEntity<String> contactUs(@RequestBody ContactusDto contact){
		String msg=this.contactusServices.contactUsPost(contact);
		
		return new ResponseEntity<>(msg,HttpStatus.CREATED);
	}
	
	@GetMapping("/messages")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<ContactusDto>> allMessages(){
		List<ContactusDto> msgs=this.contactusServices.allMessages();
		return new ResponseEntity<>(msgs,HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> deleteMessage(@PathVariable("id") Long id){
		String msg=this.contactusServices.deleteMessage(id);
		ApiResponse res=new ApiResponse(msg,true);
		return new ResponseEntity<>(res,HttpStatus.OK);
	}
	
	@PostMapping("/email")
	public ResponseEntity<ApiResponse> contactUsOnEmail(@RequestBody ContactusDto contact){
		String msg=this.contactusServices.contactEmail(contact.getName(),contact.getEmail(),contact.getMessage());
		ApiResponse res=new ApiResponse(msg,true);
		return new ResponseEntity<>(res,HttpStatus.CREATED);
	}
}
