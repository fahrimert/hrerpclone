package com.hrerp.candidatems.dto;

import com.hrerp.candidatems.model.ApplicationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationRequestDTO {
    private  Long id;
    private LocalDate applicationDate;
    private String coverLetter;
    private ApplicationStatus applicationStatus;
    @NotNull(message = "Candidate ID must not be null")
    private Long candidateId;
    private  Long jobPostingId;


}
