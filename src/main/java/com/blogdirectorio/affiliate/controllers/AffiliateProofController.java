package com.blogdirectorio.affiliate.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blogdirectorio.affiliate.dto.AffiliateProofDto;
import com.blogdirectorio.affiliate.services.AffiliateProofServices;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/api/affiliate")
@Tag(name = "Affiliate Proof Controller")
@CrossOrigin
public class AffiliateProofController {

	@Autowired
	private AffiliateProofServices affiliateService;
	
	
	@PostMapping("/submit")
	public ResponseEntity<AffiliateProofDto> submitProof(@RequestBody AffiliateProofDto proof) {
		AffiliateProofDto p=this.affiliateService.submitProof(proof);
		
		return new ResponseEntity<>(p,HttpStatus.CREATED);
	}
	
	
	 @GetMapping("/status/{orderId}")
	 public ResponseEntity<AffiliateProofDto> getProofByOrderId(@PathVariable String orderId) {
		 AffiliateProofDto p=this.affiliateService.getProofByOrderId(orderId);
		 return new ResponseEntity<>(p,HttpStatus.OK);
	 }
	 
	 
	 @PutMapping("/verify/{orderId}")
	 @PreAuthorize("hasRole('ADMIN')")
	 public ResponseEntity<AffiliateProofDto> verifyProof(@PathVariable String orderId, @RequestParam String status) {
//		 if (!status.equals("VERIFIED") && !status.equals("REJECTED")) {
//	            return ResponseEntity.badRequest().build();
//	        } 
		 
		 AffiliateProofDto p=this.affiliateService.verifyProof(orderId, AffiliateProofDto.Status.valueOf(status));
		 return new ResponseEntity<>(p,HttpStatus.OK);
	 }
	 
	 
	 @PutMapping("/update-payment/{orderId}")
	 @PreAuthorize("hasRole('ADMIN')")
	 public ResponseEntity<AffiliateProofDto> updatePaymentStatus(
	         @PathVariable String orderId,
	         @RequestParam String paymentStatus) {

	     AffiliateProofDto updatedProof = affiliateService.updatePaymentStatus(orderId, AffiliateProofDto.PaymentStatus.valueOf(paymentStatus));
	     return new ResponseEntity<>(updatedProof,HttpStatus.OK);
	 }
	 
	 @GetMapping("/")
	 public ResponseEntity<List<AffiliateProofDto>> getAllProofs(){
		 List<AffiliateProofDto> lists=this.affiliateService.getAllProof();
		 return new ResponseEntity<>(lists,HttpStatus.OK);
	 }
	 
}
