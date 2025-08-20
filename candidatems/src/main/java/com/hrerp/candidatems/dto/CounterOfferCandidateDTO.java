package com.hrerp.candidatems.dto;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CounterOfferCandidateDTO {
    private OfferStatus offerStatus;
    @Nullable
    private  Double counterOfferSalary;
    @Nullable
    private  String counterOfferDemands;
    private  String role;

}
