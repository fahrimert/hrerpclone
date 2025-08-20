package com.hrerp.model.mapper.Dto;

import com.hrerp.model.enums.OfferStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class OfferRequestDTO {

    @NotNull(message = "Candidate Id Cannot Be Null ")
    private  Long candidateId;
    @NotNull(message = "Candidate Id Cannot Be Null ")
    private  Long internalJobId;
    @NotNull(message = "Candidate Id Cannot Be Null ")
    private  Long jobPostingId;



    @Positive
    @NotNull(message = "Proposed Salary Must Be Positive")
    private  Double proposedSalary;

    private String offerExpiryDate;


    private String candidateStartDate;

    @CreatedDate
    private LocalDateTime createdAt;

}
