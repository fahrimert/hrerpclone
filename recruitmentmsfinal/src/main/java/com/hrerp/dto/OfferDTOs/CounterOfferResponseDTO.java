package com.hrerp.dto.OfferDTOs;


import com.hrerp.model.enums.OfferStatus;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class    CounterOfferResponseDTO {

    private OfferStatus offerStatus;
    private  Double proposedSalary;
    @Nullable
    private  Double counterOfferSalary;
    @Nullable
    private  String counterOffDemands;
    private  String role;
    private LocalDateTime offerExpiryDate;
    private LocalDateTime candidateStartDate;
}
