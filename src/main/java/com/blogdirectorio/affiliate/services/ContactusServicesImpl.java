package com.blogdirectorio.affiliate.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blogdirectorio.affiliate.dto.ContactusDto;
import com.blogdirectorio.affiliate.entity.ContactusEntity;
import com.blogdirectorio.affiliate.exceptions.ResourceNotFoundException;
import com.blogdirectorio.affiliate.repository.ContactusRepository;

@Service
public class ContactusServicesImpl implements ContactusServices {

	@Autowired
	private ContactusRepository contactusRepo;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public ContactusDto contactUsPost(ContactusDto contact) {
		ContactusEntity msg=this.modelMapper.map(contact, ContactusEntity.class);
		ContactusEntity save=this.contactusRepo.save(msg);
		return this.modelMapper.map(save, ContactusDto.class);
	}

	@Override
	public List<ContactusDto> allMessages() {
		List<ContactusEntity> lists=this.contactusRepo.findAll();
		List<ContactusDto> msgs=lists.stream().map(li->this.modelMapper.map(li, ContactusDto.class)).collect(Collectors.toList());
		
		return msgs;
	}

	@Override
	public String deleteMessage(Long id) {
		ContactusEntity msg=this.contactusRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Message not found ", "id", id));
		this.contactusRepo.deleteById(id);
		return "Message deleted";
	}
	
	
}
