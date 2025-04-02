package com.blogdirectorio.affiliate.services;

import java.util.List;

import com.blogdirectorio.affiliate.dto.ContactusDto;

public interface ContactusServices {

	public ContactusDto contactUsPost(ContactusDto contact);
	public List<ContactusDto> allMessages();
	public String deleteMessage(Long id);
}
