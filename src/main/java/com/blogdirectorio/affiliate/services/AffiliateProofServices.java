package com.blogdirectorio.affiliate.services;

import com.blogdirectorio.affiliate.dto.AffiliateProofDto;
import java.util.List;

public interface AffiliateProofServices {

	public AffiliateProofDto submitProof(AffiliateProofDto proof);
	public AffiliateProofDto getProofByOrderId(String orderId);
	public AffiliateProofDto verifyProof(String orderId, AffiliateProofDto.Status status);
	public AffiliateProofDto updatePaymentStatus(String orderId, AffiliateProofDto.PaymentStatus paymentStatus);
	public List<AffiliateProofDto> getAllProof();
}
