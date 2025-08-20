package com.hrerp.model;

import com.hrerp.model.enums.OfferStatus;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;

    private  Long candidateId;
    private  Long jobPostingId;
    private Long internalJobId;

    @Enumerated(EnumType.ORDINAL)
    private OfferStatus offerStatus;

    private  Double proposedSalary;

    @Nullable
    private  Double counterOfferSalaryCandidate;
    @Nullable
    private  String counterOfferDemandsCandidate;
    @Nullable
    private  Double counterOfferSalaryInternal;
    @Nullable
    private  String counterOfferDemandsInternal;


    private LocalDateTime offerExpiryDate;


    private LocalDateTime candidateStartDate;

    @CreatedDate
    private LocalDateTime createdAt;

}
