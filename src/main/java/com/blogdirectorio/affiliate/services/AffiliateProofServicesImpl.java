package com.blogdirectorio.affiliate.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blogdirectorio.affiliate.dto.AffiliateProofDto;
import com.blogdirectorio.affiliate.dto.AffiliateProofDto.Status;
import com.blogdirectorio.affiliate.entity.AffiliateProofEntity;
import com.blogdirectorio.affiliate.exceptions.ResourceNotFoundException;
import com.blogdirectorio.affiliate.repository.AffiliateProofRepository;

@Service
public class AffiliateProofServicesImpl implements AffiliateProofServices {
	
	@Autowired
	private AffiliateProofRepository affiliateProofRepo;
	
	@Autowired
	private ModelMapper modelMapper;

	
	@Override
	public AffiliateProofDto submitProof(AffiliateProofDto proof) {
		Optional<AffiliateProofEntity> existingProof = this.affiliateProofRepo.findByOrderId(proof.getOrderId());

	    if (existingProof.isPresent()) {
	        throw new RuntimeException("This orderId (" + proof.getOrderId() + ") has already been submitted.");
	    }

	    proof.setStatus(AffiliateProofDto.Status.PENDING);
	proof.setPaymentStatus(AffiliateProofDto.PaymentStatus.PENDING);
	    proof.setSubmittedAt(LocalDate.now());

	    AffiliateProofEntity entity = this.modelMapper.map(proof, AffiliateProofEntity.class);
	    AffiliateProofEntity savedEntity = this.affiliateProofRepo.save(entity);

	    return this.modelMapper.map(savedEntity, AffiliateProofDto.class);
		
	    
	}

	@Override
	public AffiliateProofDto getProofByOrderId(String orderId) {
		AffiliateProofEntity proof=this.affiliateProofRepo.findByOrderId(orderId).orElseThrow(()->new ResourceNotFoundException(orderId, orderId, null));
		
		return this.modelMapper.map(proof, AffiliateProofDto.class);
	}

	@Override
	public AffiliateProofDto verifyProof(String orderId, AffiliateProofDto.Status status) {
		AffiliateProofEntity proofOpt = this.affiliateProofRepo.findByOrderId(orderId)
	            .orElseThrow(() -> new ResourceNotFoundException("Proof not found for orderId: ", orderId,null));

		proofOpt.setStatus(AffiliateProofEntity.Status.valueOf(status.name()));
	    proofOpt.setVerifiedAt(LocalDate.now());

	    AffiliateProofEntity updatedProof = affiliateProofRepo.save(proofOpt);
	    return this.modelMapper.map(updatedProof, AffiliateProofDto.class);
	}
	
	
	
	@Override
	public AffiliateProofDto updatePaymentStatus(String orderId, AffiliateProofDto.PaymentStatus paymentStatus) {
	    AffiliateProofEntity proof = this.affiliateProofRepo.findByOrderId(orderId)
	            .orElseThrow(() -> new ResourceNotFoundException("Proof not found for orderId: ", orderId, null));

	    proof.setPaymentStatus(AffiliateProofEntity.PaymentStatus.valueOf(paymentStatus.name()));
	    AffiliateProofEntity updatedProof = affiliateProofRepo.save(proof);

	    return this.modelMapper.map(updatedProof, AffiliateProofDto.class);
	}

	@Override
	public List<AffiliateProofDto> getAllProof() {
		List<AffiliateProofEntity> lists=this.affiliateProofRepo.findAll();
		List<AffiliateProofDto> proofs=lists.stream().map(li->this.modelMapper.map(li, AffiliateProofDto.class)).collect(Collectors.toList());
		return proofs;
	}
	
	

}
