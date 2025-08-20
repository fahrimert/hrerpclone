package com.hrerp.model.mapper.Dto;

import com.hrerp.dto.CandidateResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class OfferResponseDTO {

    private CandidateResponseDTO candidateResponseDTO;
    private  Double proposedSalary;
    private LocalDateTime offerExpiryDate;
    private LocalDateTime createdAt;

}
