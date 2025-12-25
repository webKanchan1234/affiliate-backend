package com.blogdirectorio.affiliate.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.blogdirectorio.affiliate.dto.ContactusDto;
import com.blogdirectorio.affiliate.entity.ContactusEntity;
import com.blogdirectorio.affiliate.exceptions.ResourceNotFoundException;
import com.blogdirectorio.affiliate.repository.ContactusRepository;

@Service
public class ContactusServicesImpl implements ContactusServices {

	private final ContactusRepository contactusRepo;
    private final ModelMapper modelMapper;
    private final JavaMailSender mailSender;

    public ContactusServicesImpl(
            ContactusRepository contactusRepo,
            ModelMapper modelMapper,
            JavaMailSender mailSender) {
        this.contactusRepo = contactusRepo;
        this.modelMapper = modelMapper;
        this.mailSender = mailSender;
    }
	
//	
	@Value("${spring.mail.username}")
	private String mailUsername;
//
	 @Value("${spring.mail.password}")
	 private String mailPassword;

	@Override
	public String contactUsPost(ContactusDto contact) {
		ContactusEntity msg=this.modelMapper.map(contact, ContactusEntity.class);
		ContactusEntity save=this.contactusRepo.save(msg);
		String res=contactEmail(contact.getName(),contact.getEmail(),contact.getMessage());
		return "Message processed successfully";
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

	@Override
	public String contactEmail(String name, String email, String message) {
		   SimpleMailMessage mailMessage = new SimpleMailMessage();
		   mailMessage.setTo(mailUsername);
		    mailMessage.setSubject("New Contact Request from " + name);
		    mailMessage.setText(
		        "You have received a new contact request:\n\n" +
		        "Name: " + name + "\n" +
		        "User Email: " + email + "\n\n" +
		        "Message:\n" + message
		    );
		    
		    mailSender.send(mailMessage);
	        
	        return "Message send successfully";
	        
	}
	
	
}
