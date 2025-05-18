package com.blogdirectorio.affiliate.services;

import java.util.List;

import com.blogdirectorio.affiliate.dto.ContactusDto;

public interface ContactusServices {

	public String contactUsPost(ContactusDto contact);
	public List<ContactusDto> allMessages();
	public String deleteMessage(Long id);
	public String contactEmail(String name, String email, String message);
	
}
