package com.blogdirectorio.affiliate.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "affiliate_proofs")
public class AffiliateProofEntity {

	 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderId; // Unique order ID
    private String userName;
    private String email;
    private String proofImageUrl; // URL of uploaded proof
    private String paymentMethod; // PayPal, UPI, Bank Transfer, etc.
    private String upiId;         // Only for UPI
    private String bankName;      // Only for Bank Transfer
    private String accountNumber; // Only for Bank Transfer
    private String ifscCode;     // UPI ID, PayPal email, etc.
    
    @Enumerated(EnumType.STRING)
    private Status status; // PENDING, VERIFIED, REJECTED
    
    @Enumerated(EnumType.STRING)
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
