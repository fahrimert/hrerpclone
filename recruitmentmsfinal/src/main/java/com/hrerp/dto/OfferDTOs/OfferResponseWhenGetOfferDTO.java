package com.hrerp.dto.OfferDTOs;


import com.hrerp.model.enums.OfferStatus;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class OfferResponseWhenGetOfferDTO {
    private OfferStatus offerStatus;
    private  Double proposedSalary;
    @Nullable
    private  Double counterOfferSalary;
    @Nullable
    private  String counterOffDemands;
    private LocalDateTime offerExpiryDate;
    private LocalDateTime candidateStartDate;
    }
