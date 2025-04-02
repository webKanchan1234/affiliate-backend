package com.blogdirectorio.affiliate.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.blogdirectorio.affiliate.entity.AffiliateProofEntity.Status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AffiliateProofDto {

	private Long id;

    private String orderId; 
    private String userName;
    private String email;
    private String proofImageUrl; 
    private String paymentMethod; 
    
    private String upiId;         // Only for UPI
    private String bankName;      // Only for Bank Transfer
    private String accountNumber; // Only for Bank Transfer
    private String ifscCode;    
    
    private Status status; 
    private PaymentStatus paymentStatus;

    private LocalDate submittedAt;
    private LocalDate verifiedAt;

    public enum Status {
        PENDING, VERIFIED, REJECTED
    }
    
    public enum PaymentStatus {
        PENDING, DONE
    }
}
